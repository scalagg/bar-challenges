package com.solexgames.challenges.command;

import com.solexgames.challenges.BarChallengesPlugin;
import com.solexgames.challenges.challenge.DailyChallenge;
import com.solexgames.challenges.model.ChallengePlayer;
import com.solexgames.challenges.tracker.DataTracker;
import com.solexgames.core.command.BaseCommand;
import com.solexgames.core.command.annotation.Command;
import com.solexgames.core.util.Color;
import com.solexgames.core.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@Command(
        label = "datatracker",
        aliases = "tracker",
        permission = "challenges.command.datatracker"
)
public class DataTrackerCommand extends BaseCommand {

    public DataTrackerCommand() {
        super(BarChallengesPlugin.getInstance());
    }

    @Override
    public boolean command(CommandSender commandSender, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(this.ONLY_PLAYERS);
            return false;
        }

        final Player player = (Player) commandSender;

        if (args.length <= 0) {
            player.sendMessage(this.getUsageMessage("status", "<player> <tracker> <challenge>"));
        }

        if (args.length > 0) {
            if (args[0].toLowerCase().equals("status")) {
                if (args.length < 4) {
                    player.sendMessage(this.getUsageMessage("status", "<player> <tracker> <challenge>"));
                }
                if (args.length >= 4) {
                    final Player target = Bukkit.getPlayer(args[1]);

                    if (target != null) {
                        final ChallengePlayer challengePlayer = BarChallengesPlugin.getInstance().getPlayerHandler().getByPlayer(target);
                        final DataTracker dataTracker = challengePlayer.getTrackerByName(args[2]);

                        if (dataTracker != null) {
                            final DailyChallenge<?> dailyChallenge = BarChallengesPlugin.getInstance().getChallengeHandler().getRegisteredChallenges()
                                    .stream().filter(dailyChallenge1 -> dailyChallenge1.getName().equalsIgnoreCase(StringUtil.buildMessage(args, 3))).findFirst().orElse(null);

                            if (dailyChallenge != null) {
                                player.sendMessage(Color.MAIN_COLOR + dailyChallenge.getName() + Color.SECONDARY_COLOR + " is currently " + ChatColor.GREEN + "active" + Color.SECONDARY_COLOR + " and the player has " + (!dataTracker.canAchieve(dailyChallenge.getCompletionRequirement()) ? ChatColor.RED + "not" : ChatColor.GREEN + "already") + Color.SECONDARY_COLOR + " finished the challenge.");
                            } else {
                                player.sendMessage(ChatColor.RED + "Error: No challenge matching " + ChatColor.YELLOW + StringUtil.buildMessage(args, 3) + ChatColor.RED + " found.");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Error: No tracker matching " + ChatColor.YELLOW + args[2] + ChatColor.RED + " found.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Error: No player matching " + ChatColor.YELLOW + args[1] + ChatColor.RED + " found.");
                    }
                }
            } else {
                player.sendMessage(this.getUsageMessage("status", "<player> <tracker> <challenge>"));
            }
        }

        return false;
    }
}
