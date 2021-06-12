package com.solexgames.challenges.menu;

import com.solexgames.challenges.BarChallengesPlugin;
import com.solexgames.challenges.challenge.DailyChallenge;
import com.solexgames.core.util.builder.ItemBuilder;
import com.solexgames.core.util.external.Button;
import com.solexgames.core.util.external.pagination.PaginatedMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

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

        int i = 0;
        for (DailyChallenge dailyChallenge : BarChallengesPlugin.getInstance().getChallengeHandler().getRegisteredChallenges()) {
            buttonMap.put(i++, new ChallengeButton(dailyChallenge));
        }

        return buttonMap;
    }

    @RequiredArgsConstructor
    private static class ChallengeButton extends Button {

        private final DailyChallenge dailyChallenge;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.INK_SACK)
                    .create();
        }
    }
}
