package gg.scala.challenges.command;

import gg.scala.challenges.menu.ChallengeViewMenu;
import net.evilblock.cubed.acf.BaseCommand;
import net.evilblock.cubed.acf.annotation.CommandAlias;
import net.evilblock.cubed.acf.annotation.Default;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */
@CommandAlias("challenge|challenges")
public class ChallengesCommand extends BaseCommand {

    @Default
    public void onDefault(Player player) {
        new ChallengeViewMenu().openMenu(player);
    }
}
