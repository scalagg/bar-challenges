package com.solexgames.challenges;

import com.solexgames.challenges.handler.ChallengeHandler;
import com.solexgames.challenges.handler.MongoHandler;
import com.solexgames.challenges.handler.PlayerHandler;
import com.solexgames.challenges.task.DailyCheckTask;
import com.solexgames.lib.commons.model.CommonsPlugin;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public final class BarChallengesPlugin extends CommonsPlugin {

    @Getter
    private static BarChallengesPlugin instance;

    private MongoHandler mongoHandler;
    private PlayerHandler playerHandler;
    private ChallengeHandler challengeHandler;

    @Override
    public void onEnable() {
        instance = this;

        this.mongoHandler = new MongoHandler();
        this.playerHandler = new PlayerHandler();
        this.challengeHandler = new ChallengeHandler();

        new DailyCheckTask()
                .runTaskTimerAsynchronously(this, 0L, TimeUnit.MINUTES.toMillis(5L));
    }
}
