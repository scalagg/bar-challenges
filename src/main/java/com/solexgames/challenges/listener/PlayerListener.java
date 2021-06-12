package com.solexgames.challenges.listener;

import com.solexgames.challenges.BarChallengesPlugin;
import com.solexgames.challenges.model.ChallengePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final ChallengePlayer challengePlayer = new ChallengePlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());

        BarChallengesPlugin.getInstance().getPlayerHandler().insert(challengePlayer.getUuid(), challengePlayer);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final ChallengePlayer challengePlayer = BarChallengesPlugin.getInstance().getPlayerHandler().getByPlayer(event.getPlayer());

        if (challengePlayer != null) {
            challengePlayer.savePlayerData(true);
        }
    }
}
