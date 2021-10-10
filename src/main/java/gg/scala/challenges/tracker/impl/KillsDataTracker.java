package gg.scala.challenges.tracker.impl;

import gg.scala.challenges.tracker.DataTracker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.evilblock.cubed.util.CC;
import org.bukkit.ChatColor;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@Getter
@Setter
@NoArgsConstructor
public class KillsDataTracker implements DataTracker {

    private int currentKills;

    @Override
    public String getName() {
        return "Kills";
    }

    @Override
    public boolean canAchieve(Object data) {
        return data instanceof Integer && this.currentKills >= (Integer) data;
    }

    @Override
    public String getStatusMessage(Object data) {
        return data instanceof Integer ? ChatColor.GRAY + "(" + CC.PRI + this.currentKills + "/" + data + ChatColor.GRAY + ")" : "";
    }
}
