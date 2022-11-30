package gg.scala.challenges.command;

import gg.scala.challenges.menu.ChallengeViewMenu;
import gg.scala.commons.acf.annotation.CommandAlias;
import gg.scala.commons.annotations.commands.AutoRegister;
import gg.scala.commons.command.ScalaCommand;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */
@AutoRegister
public class ChallengesCommand extends ScalaCommand {

    @CommandAlias("challenge|challenges")
    public void onChallenges(Player player) {
        new ChallengeViewMenu().openMenu(player);
    }
}
