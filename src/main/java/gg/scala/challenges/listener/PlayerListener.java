package gg.scala.challenges.listener;

import gg.scala.challenges.BarChallengesPlugin;
import gg.scala.challenges.model.ChallengePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

public class PlayerListener implements Listener {

    @EventHandler
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        BarChallengesPlugin.getInstance().getPlayerHandler().insert(event.getUniqueId(), new ChallengePlayer(event.getUniqueId(), event.getName()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final ChallengePlayer challengePlayer = BarChallengesPlugin.getInstance().getPlayerHandler().getByPlayer(event.getPlayer());

        if (challengePlayer != null) {
            challengePlayer.savePlayerData(true);
        }
    }
}
