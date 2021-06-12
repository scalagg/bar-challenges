package com.solexgames.challenges.reward;

import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

public interface ChallengeReward {

    String getName();

    String getDescription();

    void giveTo(Player player);

}
