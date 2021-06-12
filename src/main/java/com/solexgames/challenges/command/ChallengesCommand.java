package com.solexgames.challenges.command;

import com.solexgames.challenges.BarChallengesPlugin;
import com.solexgames.challenges.menu.ChallengeViewMenu;
import com.solexgames.core.command.BaseCommand;
import com.solexgames.core.command.annotation.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@Command(
        label = "challenges",
        aliases = "challenge",
        hidden = false
)
public class ChallengesCommand extends BaseCommand {

    public ChallengesCommand() {
        super(BarChallengesPlugin.getInstance());
    }

    @Override
    public boolean command(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(this.ONLY_PLAYERS);
            return false;
        }

        final Player player = (Player) commandSender;

        new ChallengeViewMenu().openMenu(player);

        return false;
    }
}
