package com.solexgames.challenges.challenge;

import com.solexgames.challenges.reward.ChallengeReward;
import com.solexgames.challenges.tracker.DataTracker;

import java.util.List;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

public interface DailyChallenge<K> {

    String getName();

    String getGoal();

    String[] getDescription();

    K getRequirementToComplete();

    List<ChallengeReward> getRewards();

    Class<? extends DataTracker> getTrackerType();

}
