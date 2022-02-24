package gg.scala.challenges.reward.impl;

import gg.scala.challenges.reward.ChallengeReward;

import gg.scala.grape.GrapeSpigotPlugin;
import gg.scala.grape.player.impl.PersistentGrapePlayer;
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
        return this.amount + " Coins";
    }

    @Override
    public String getDescription() {
        return "Receive " + this.amount + " coins";
    }

    @Override
    public void giveTo(Player player) {
        final PersistentGrapePlayer grapePlayer = GrapeSpigotPlugin
                .getInstance().getPlayerHandler().getByPlayer(player);

        grapePlayer.setCoins(
                grapePlayer.getCoins() + this.amount
        );
    }
}
