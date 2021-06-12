package com.solexgames.challenges.model;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.solexgames.challenges.BarChallengesPlugin;
import com.solexgames.challenges.tracker.DataTracker;
import com.solexgames.challenges.tracker.impl.KillsDataTracker;
import com.solexgames.core.util.Color;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Player profile where data is saved.
 * <p></p>
 *
 * @author GrowlyX
 * @since 6/12/2021
 */

@Getter
@Setter
@SuppressWarnings("all")
public class ChallengePlayer {

    private List<DataTracker> dataTrackers = new ArrayList<>();

    private final UUID uuid;
    private final String name;

    private long lastLogin;

    public ChallengePlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        this.loadPlayerData();
    }

    public void savePlayerData(boolean remove) {
        CompletableFuture.runAsync(() -> BarChallengesPlugin.getInstance().getMongoHandler().getPlayerCollection().replaceOne(Filters.eq("uuid", this.uuid.toString()), this.getDocument(), new ReplaceOptions().upsert(true)));

        if (remove) {
            BarChallengesPlugin.getInstance().getPlayerHandler().getPlayerTypeMap().remove(this.getUuid());
        }
    }

    public Document getDocument() {
        final Document document = new Document("_id", this.uuid);

        document.put("uuid", this.uuid.toString());
        document.put("name", this.name);

        document.put("data", this.dataTrackers);
        document.put("lastLogin", System.currentTimeMillis());

        return document;
    }

    private void loadPlayerData() {
        CompletableFuture.supplyAsync(() -> BarChallengesPlugin.getInstance().getMongoHandler().getPlayerCollection().find(Filters.eq("uuid", this.uuid.toString())).first())
                .thenAccept(document -> {
                    try {
                        if (document == null) {
                            this.setupDefaultDataTrackers();
                            return;
                        }

                        this.dataTrackers = document.getList("data", DataTracker.class);
                        this.lastLogin = document.getLong("lastLogin");

                        if (this.dataTrackers.isEmpty()) {
                            this.setupDefaultDataTrackers();
                        }

                        final Date lastLogonDate = new Date(this.lastLogin);

                        if (lastLogonDate.before(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1L)))) {
                            this.setupDefaultDataTrackers();
                        }
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                });
    }

    public void recalculateChallenges() {
        BarChallengesPlugin.getInstance().getChallengeHandler().getRegisteredChallenges()
                .forEach(dailyChallenge -> {
                    final DataTracker dataTracker = this.getTrackerByClass(dailyChallenge.getTrackerType());

                    if (dataTracker != null && dataTracker.hasCompleted(dailyChallenge.getRequirementToComplete())) {
                        final Player player = Bukkit.getPlayer(this.uuid);

                        if (player != null) {
                            player.sendMessage(new String[]{
                                    Color.SECONDARY_COLOR + "You've completed today's daily challenge of getting " + Color.MAIN_COLOR + dailyChallenge.getName() + Color.SECONDARY_COLOR + "!",
                                    ChatColor.GRAY + ChatColor.ITALIC.toString() + "We will assign you your rewards shortly!"
                            });

                            dailyChallenge.getRewards().forEach(challengeReward -> challengeReward.giveTo(player));
                        }
                    }
                });
    }

    public void setupDefaultDataTrackers() {
        this.dataTrackers.clear();
        this.dataTrackers.add(new KillsDataTracker());
    }

    public <T extends DataTracker> T getTrackerByClass(Class<T> clazz) {
        return (T) this.dataTrackers.stream()
                .filter(dataTracker -> dataTracker.getClass().equals(clazz))
                .findFirst().orElse(null);
    }
}
