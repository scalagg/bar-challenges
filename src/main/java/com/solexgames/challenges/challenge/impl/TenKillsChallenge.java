package com.solexgames.challenges.challenge.impl;

import com.solexgames.challenges.challenge.DailyChallenge;
import com.solexgames.challenges.reward.ChallengeReward;
import com.solexgames.challenges.reward.impl.ExperienceReward;

import java.util.Collections;
import java.util.List;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

public class TenKillsChallenge implements DailyChallenge {

    @Override
    public String getName() {
        return "10 Kills";
    }

    @Override
    public String getGoal() {
        return "Get your first 10 kills today on Practice";
    }

    @Override
    public String getDescription() {
        return "Start off the day with getting 10 kills on any ladders, and any queues.";
    }

    @Override
    public List<ChallengeReward> getRewards() {
        return Collections.singletonList(new ExperienceReward(100));
    }
}
