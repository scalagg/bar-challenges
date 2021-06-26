package com.solexgames.challenges;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solexgames.challenges.challenge.DailyChallenge;
import com.solexgames.challenges.command.ChallengesCommand;
import com.solexgames.challenges.command.DataTrackerCommand;
import com.solexgames.challenges.handler.ChallengeHandler;
import com.solexgames.challenges.handler.DataTrackerHandler;
import com.solexgames.challenges.handler.MongoHandler;
import com.solexgames.challenges.handler.PlayerHandler;
import com.solexgames.challenges.listener.PlayerListener;
import com.solexgames.challenges.listener.tracker.AbstractDataTrackerListener;
import com.solexgames.challenges.task.DailyCheckTask;
import com.solexgames.core.util.BukkitUtil;
import com.solexgames.lib.commons.model.CommonsPlugin;
import lombok.Getter;
import org.bukkit.event.Listener;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Getter
public final class BarChallengesPlugin extends CommonsPlugin {

    @Getter
    private static BarChallengesPlugin instance;

    private MongoHandler mongoHandler;
    private PlayerHandler playerHandler;
    private ChallengeHandler challengeHandler;
    private DataTrackerHandler dataTrackerHandler;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();

        this.mongoHandler = new MongoHandler();
        this.playerHandler = new PlayerHandler();

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
        BukkitUtil.getClassesInPackage(this, "com.solexgames.challenges.listener.tracker.impl")
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
