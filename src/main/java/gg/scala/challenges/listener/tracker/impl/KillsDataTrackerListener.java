package gg.scala.challenges.listener.tracker.impl;

import gg.scala.challenges.BarChallengesPlugin;
import gg.scala.challenges.listener.tracker.AbstractDataTrackerListener;
import gg.scala.challenges.model.ChallengePlayer;
import gg.scala.challenges.tracker.DataTracker;
import gg.scala.challenges.tracker.impl.KillsDataTracker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

public class KillsDataTrackerListener extends AbstractDataTrackerListener {

    @EventHandler
    public void onDeathKiller(PlayerDeathEvent event) {
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

    @Override
    public Class<? extends DataTracker> getPairedDataTracker() {
        return KillsDataTracker.class;
    }
}
