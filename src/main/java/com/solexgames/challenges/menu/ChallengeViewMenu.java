package com.solexgames.challenges.menu;

import com.solexgames.challenges.BarChallengesPlugin;
import com.solexgames.challenges.challenge.DailyChallenge;
import com.solexgames.challenges.model.ChallengePlayer;
import com.solexgames.challenges.tracker.DataTracker;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.pagination.PaginatedMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

public class ChallengeViewMenu extends PaginatedMenu {

    public ChallengeViewMenu() {
        super(9);
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Challenges » Daily";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();
        final ChallengePlayer challengePlayer = BarChallengesPlugin.getInstance().getPlayerHandler().getByPlayer(player);

        int i = 0;
        for (DailyChallenge<?> dailyChallenge : BarChallengesPlugin.getInstance().getChallengeHandler().getRegisteredChallenges()) {
            buttonMap.put(i++, new ChallengeButton(dailyChallenge, challengePlayer));
        }

        return buttonMap;
    }

    @RequiredArgsConstructor
    private static class ChallengeButton extends Button {

        private final DailyChallenge<?> dailyChallenge;
        private final ChallengePlayer challengePlayer;

        @Override
        public ItemStack getButtonItem(Player player) {
            final List<String> stringList = new ArrayList<>(Arrays.asList(this.dailyChallenge.getDescription()));

            final DataTracker dataTracker = this.challengePlayer.getTrackerByClass(this.dailyChallenge.getTrackerType());
            final boolean hasCompleted = dataTracker.hasCompleted(this.dailyChallenge.getCompletionRequirement());

            stringList.add(" ");
            stringList.add(Color.MAIN_COLOR + ChatColor.BOLD.toString() + "Goal:");
            stringList.add(ChatColor.GRAY + " - " + this.dailyChallenge.getGoal() + " " + dataTracker.getStatusMessage(this.dailyChallenge.getCompletionRequirement()));
            stringList.add(" ");
            stringList.add(Color.MAIN_COLOR + ChatColor.BOLD.toString() + "Rewards:");

            this.dailyChallenge.getRewards().stream()
                    .map(challengeReward -> ChatColor.GRAY + " - " + challengeReward.getDescription())
                    .forEach(stringList::add);

            stringList.add(" ");
            stringList.add(hasCompleted ? ChatColor.GREEN + "[You've finished this challenge today]" : ChatColor.YELLOW + "[You're working on this challenge]");

            return new ItemBuilder(Material.INK_SACK)
                    .setDisplayName(Color.SECONDARY_COLOR + this.dailyChallenge.getName())
                    .setDurability(hasCompleted ? 10 : 8)
                    .addLore(stringList).create();
        }
    }
}
