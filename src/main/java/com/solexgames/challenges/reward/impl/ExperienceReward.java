package com.solexgames.challenges.reward.impl;

import com.solexgames.challenges.reward.ChallengeReward;
import com.solexgames.core.CorePlugin;
import com.solexgames.core.player.PotPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@RequiredArgsConstructor
public class ExperienceReward implements ChallengeReward {

    private final int amount;

    @Override
    public String getName() {
        return this.amount + " Experience";
    }

    @Override
    public String getDescription() {
        return "Receive " + this.amount + " experience";
    }

    @Override
    public void giveTo(Player player) {
        final PotPlayer potPlayer = CorePlugin.getInstance().getPlayerManager().getPlayer(player);

        potPlayer.setExperience(potPlayer.getExperience() + this.amount);
    }
}
