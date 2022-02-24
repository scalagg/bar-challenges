package gg.scala.challenges.challenge.impl;

import gg.scala.challenges.challenge.DailyChallenge;
import gg.scala.challenges.reward.ChallengeReward;
import gg.scala.challenges.reward.impl.ExperienceReward;
import gg.scala.challenges.tracker.DataTracker;
import gg.scala.challenges.tracker.impl.KillsDataTracker;
import lombok.var;
import net.evilblock.cubed.util.CC;
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
                CC.SEC + "Start off the day with getting 10",
                CC.SEC + "kills on any ladders and any queues."
        };
    }

    @Override
    public Integer getCompletionRequirement() {
        return 10;
    }

    @Override
    public List<ChallengeReward> getRewards() {
        return Collections.singletonList(
                new ExperienceReward(100)
        );
    }

    @Override
    public Class<? extends DataTracker> getTrackerType() {
        return KillsDataTracker.class;
    }
}
