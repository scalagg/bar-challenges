package com.solexgames.challenges.task;

import com.solexgames.challenges.BarChallengesPlugin;
import com.solexgames.challenges.model.ChallengePlayer;
import com.solexgames.challenges.util.DateValidationUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.logging.Logger;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

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

            Logger.getGlobal().info("[Challenges] All online players' data trackers have been reset.");
        }

        this.previousDate = currentDate;
    }
}
