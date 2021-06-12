package com.solexgames.challenges.model;

import com.solexgames.challenges.BarChallengesPlugin;
import com.solexgames.challenges.reward.ChallengeReward;
import com.solexgames.challenges.tracker.DataTracker;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@Getter
@RequiredArgsConstructor
public class ChallengeData {

    private final UUID target;

    private final ChallengeReward reward;
    private final String dataTrackerType;

    public DataTracker<?> getTracker() {
        final Player player = Bukkit.getPlayer(this.target);

        if (player != null) {
            final ChallengePlayer challengePlayer = BarChallengesPlugin.getInstance().getPlayerHandler().getByPlayer(player);

            return challengePlayer.getDataTrackerSet().stream()
                    .filter(dataTracker -> dataTracker.getName().equals(this.dataTrackerType))
                    .findFirst().orElse(null);
        } else {
            return null;
        }
    }
}
