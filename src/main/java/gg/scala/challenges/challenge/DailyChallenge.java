package gg.scala.challenges.challenge;

import gg.scala.challenges.reward.ChallengeReward;
import gg.scala.challenges.tracker.DataTracker;

import java.util.List;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

public interface DailyChallenge<T> {

    String getName();

    String getGoal();

    String[] getDescription();

    T getCompletionRequirement();

    List<ChallengeReward> getRewards();

    Class<? extends DataTracker> getTrackerType();

}
