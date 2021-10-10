package gg.scala.challenges;

import gg.scala.challenges.command.ChallengesCommand;
import gg.scala.challenges.commons.PlayerCache;
import gg.scala.challenges.commons.impl.BasicPlayerCache;
import gg.scala.challenges.handler.ChallengeHandler;
import gg.scala.challenges.handler.DataTrackerHandler;
import gg.scala.challenges.handler.MongoHandler;
import gg.scala.challenges.listener.PlayerListener;
import gg.scala.challenges.model.ChallengePlayer;
import gg.scala.challenges.task.DailyCheckTask;
import lombok.Getter;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import net.evilblock.cubed.util.ClassUtils;
import org.bukkit.event.Listener;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Getter
public final class BarChallengesPlugin extends ExtendedJavaPlugin {

    @Getter
    private static BarChallengesPlugin instance;

    private MongoHandler mongoHandler;
    private ChallengeHandler challengeHandler;
    private DataTrackerHandler dataTrackerHandler;

    private final PlayerCache<ChallengePlayer> playerHandler = new BasicPlayerCache<>();

    @Override
    public void enable() {
        instance = this;

        this.saveDefaultConfig();

        this.mongoHandler = new MongoHandler();
        this.challengeHandler = new ChallengeHandler();
        this.challengeHandler.registerDefaultChallenges();

        this.dataTrackerHandler = new DataTrackerHandler();
        this.dataTrackerHandler.setupDefaultDataTrackerClasses();

        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        this.setupDefaultChallenges();

        new ChallengesCommand();
        new DataTrackerCommand();

        new DailyCheckTask()
                .runTaskTimerAsynchronously(this, 0L, TimeUnit.MINUTES.toMillis(1L));
    }

    private void setupDefaultChallenges() {
        ClassUtils.getClassesInPackage(this, "gg.scala.challenges.listener.tracker.impl")
                .forEach(aClass -> {
                    try {
                        final Object object = aClass.newInstance();

                        if (object instanceof Listener) {
                            this.getServer().getPluginManager().registerEvents((Listener) aClass.newInstance(), this);
                        }
                    } catch (Exception exception) {
                        Logger.getGlobal().info("[Challenges] Couldn't register the listener " + aClass.getSimpleName() + ".");
                    }
                });
    }
}
