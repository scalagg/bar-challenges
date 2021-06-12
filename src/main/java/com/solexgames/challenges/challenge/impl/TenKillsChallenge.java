package com.solexgames.challenges.challenge.impl;

import com.solexgames.challenges.challenge.DailyChallenge;
import com.solexgames.challenges.reward.ChallengeReward;
import com.solexgames.challenges.reward.impl.ExperienceReward;
import com.solexgames.challenges.tracker.DataTracker;
import com.solexgames.challenges.tracker.impl.KillsDataTracker;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.List;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

public class TenKillsChallenge implements DailyChallenge<Integer> {

    @Override
    public String getName() {
        return "10 Kills";
    }

    @Override
    public String getGoal() {
        return "Get your first 10 kills today";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                ChatColor.GRAY + "Start off the day with getting 10",
                ChatColor.GRAY + "kills on any ladders and any queues."
        };
    }

    @Override
    public Integer getRequirementToComplete() {
        return 10;
    }

    @Override
    public List<ChallengeReward> getRewards() {
        return Collections.singletonList(new ExperienceReward(100));
    }

    @Override
    public Class<? extends DataTracker> getTrackerType() {
        return KillsDataTracker.class;
    }
}
