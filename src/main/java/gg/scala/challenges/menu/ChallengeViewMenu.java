package gg.scala.challenges.menu;

import gg.scala.challenges.BarChallengesPlugin;
import gg.scala.challenges.challenge.DailyChallenge;
import gg.scala.challenges.model.ChallengePlayer;
import gg.scala.challenges.tracker.DataTracker;
import lombok.RequiredArgsConstructor;
import net.evilblock.cubed.menu.Button;
import net.evilblock.cubed.menu.pagination.PaginatedMenu;
import net.evilblock.cubed.util.CC;
import net.evilblock.cubed.util.bukkit.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */
public class ChallengeViewMenu extends PaginatedMenu {

    {
        setPlaceholdBorders(true);
    }

    @NotNull
    @Override
    public List<Integer> getAllPagesButtonSlots() {
        return Arrays.asList(10, 11, 12, 13, 14, 15, 16);
    }

    @Override
    public int size(@NotNull Map<Integer, ? extends Button> buttons) {
        return 27;
    }

    @NotNull
    @Override
    public String getPrePaginatedTitle(@NotNull Player player) {
        return "Challenges » Daily";
    }

    @NotNull
    @Override
    public Map<Integer, Button> getAllPagesButtons(@NotNull Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();
        final ChallengePlayer challengePlayer = BarChallengesPlugin.getInstance().getPlayerHandler().getByPlayer(player);

        BarChallengesPlugin.getInstance().getChallengeHandler().getRegisteredChallenges().forEach(dailyChallenge -> {
            buttonMap.put(buttonMap.size(), new ChallengeButton(dailyChallenge, challengePlayer));
        });

        return buttonMap;
    }

    @RequiredArgsConstructor
    private static class ChallengeButton extends Button {

        private final DailyChallenge<?> dailyChallenge;
        private final ChallengePlayer challengePlayer;

        @NotNull
        @Override
        public ItemStack getButtonItem(@NotNull Player player) {
            final List<String> stringList = new ArrayList<>(Arrays.asList(this.dailyChallenge.getDescription()));

            final DataTracker dataTracker = this.challengePlayer.getTrackerByClass(this.dailyChallenge.getTrackerType());
            final boolean hasCompleted = this.challengePlayer.hasCompleted(this.dailyChallenge);

            stringList.add(" ");
            stringList.add(CC.PRI + "Goal:");
            stringList.add(ChatColor.WHITE + " - " + this.dailyChallenge.getGoal() + " " + CC.GRAY + dataTracker.getStatusMessage(this.dailyChallenge.getCompletionRequirement()));
            stringList.add(" ");
            stringList.add(CC.PRI + "Rewards:");

            this.dailyChallenge.getRewards().stream()
                    .map(challengeReward -> ChatColor.WHITE + " - " + challengeReward.getDescription())
                    .forEach(stringList::add);

            stringList.add(" ");
            stringList.add(hasCompleted ? ChatColor.LIGHT_PURPLE + "You've completed this challenge." : ChatColor.GREEN + "You're working on this challenge.");

            return new ItemBuilder(Material.INK_SACK)
                    .name(CC.B_GOLD + this.dailyChallenge.getName())
                    .data((short) (hasCompleted ? 10 : 8))
                    .setLore(stringList).build();
        }
    }
}
