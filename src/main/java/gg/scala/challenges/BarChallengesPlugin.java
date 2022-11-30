package gg.scala.challenges;

import gg.scala.challenges.command.ChallengesCommand;
import gg.scala.challenges.commons.PlayerCache;
import gg.scala.challenges.commons.impl.BasicPlayerCache;
import gg.scala.challenges.handler.ChallengeHandler;
import gg.scala.challenges.handler.DataTrackerHandler;
import gg.scala.challenges.handler.MongoHandler;
import gg.scala.challenges.model.ChallengePlayer;
import gg.scala.commons.ExtendedScalaPlugin;
import gg.scala.commons.annotations.commands.ManualRegister;
import gg.scala.commons.annotations.container.ContainerEnable;
import gg.scala.commons.command.ScalaCommandManager;
import lombok.Getter;
import net.evilblock.cubed.util.ClassUtils;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

@Getter
public final class BarChallengesPlugin extends ExtendedScalaPlugin {

    @Getter
    private static BarChallengesPlugin instance;

    private MongoHandler mongoHandler;
    private ChallengeHandler challengeHandler;
    private DataTrackerHandler dataTrackerHandler;

    private final PlayerCache<ChallengePlayer> playerHandler = new BasicPlayerCache<>();

    @ContainerEnable
    public void containerEnable() {
        instance = this;

        this.saveDefaultConfig();

        this.mongoHandler = new MongoHandler();
        this.challengeHandler = new ChallengeHandler();
        this.challengeHandler.registerDefaultChallenges();

        this.dataTrackerHandler = new DataTrackerHandler();
        this.dataTrackerHandler.setupDefaultDataTrackerClasses();

        this.setupDefaultChallenges();
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
