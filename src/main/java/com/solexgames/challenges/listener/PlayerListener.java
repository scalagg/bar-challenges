package com.solexgames.challenges.listener;

import com.solexgames.challenges.BarChallengesPlugin;
import com.solexgames.challenges.model.ChallengePlayer;
import com.solexgames.challenges.tracker.impl.KillsDataTracker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
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

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        final Player player = event.getEntity().getKiller();

        if (player != null) {
            final ChallengePlayer challengePlayer = BarChallengesPlugin.getInstance().getPlayerHandler().getByPlayer(player);

            if (challengePlayer != null) {
                final KillsDataTracker dataTracker = challengePlayer.getTrackerByClass(KillsDataTracker.class);

                dataTracker.setCurrentKills(dataTracker.getCurrentKills() + 1);
                challengePlayer.recalculateChallenges();
            }
        }
    }
}
