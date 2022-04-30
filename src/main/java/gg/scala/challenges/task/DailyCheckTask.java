package gg.scala.challenges.task;

import com.mongodb.Block;
import gg.scala.challenges.BarChallengesPlugin;
import gg.scala.challenges.model.ChallengePlayer;
import gg.scala.challenges.util.DateValidationUtil;
import gg.scala.commons.annotations.runnables.Repeating;
import me.lucko.helper.promise.ThreadContext;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */
@Repeating(value = 20L * 60L, context = ThreadContext.ASYNC)
public class DailyCheckTask extends BukkitRunnable {

    private Date previousDate;

    @Override
    public void run() {
        final Date currentDate = new Date();
        final boolean isSameDay = DateValidationUtil.isSameDay(this.previousDate == null ? currentDate : this.previousDate, currentDate);

        if (!isSameDay) {
            Bukkit.getOnlinePlayers().stream()
                    .map(BarChallengesPlugin.getInstance().getPlayerHandler()::getByPlayer)
                    .forEach(ChallengePlayer::resetAndAddDefaultDataTrackers);

            BarChallengesPlugin.getInstance().getMongoHandler().getPlayerCollection().find().forEach((Block<? super Document>) document -> {
                final ChallengePlayer challengePlayer = new ChallengePlayer(UUID.fromString(document.getString("uuid")), document.getString("name"));

                Bukkit.getScheduler().runTaskLaterAsynchronously(BarChallengesPlugin.getInstance(), () -> {
                    challengePlayer.savePlayerData(false);
                }, 10L);
            });

            Logger.getGlobal().info("[Challenges] All online players' data trackers have been reset.");
        }

        this.previousDate = currentDate;
    }
}
