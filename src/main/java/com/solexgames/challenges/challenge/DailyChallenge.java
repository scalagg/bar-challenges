package com.solexgames.challenges.challenge;

import com.solexgames.challenges.reward.ChallengeReward;

import java.util.List;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

public interface DailyChallenge {

    String getName();

    String getGoal();

    String getDescription();

    List<ChallengeReward> getRewards();

}
