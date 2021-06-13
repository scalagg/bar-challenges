package com.solexgames.challenges.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.solexgames.challenges.BarChallengesPlugin;
import com.solexgames.challenges.tracker.DataTracker;
import com.solexgames.challenges.util.DateValidationUtil;
import com.solexgames.core.util.Color;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
            BarChallengesPlugin.getInstance().getPlayerHandler().getPlayerTypeMap().remove(this.uuid);
        }
    }

    public Document getDocument() {
        final Document document = new Document("_id", this.uuid);

        document.put("uuid", this.uuid.toString());
        document.put("name", this.name);

        final List<String> dataTrackers = new ArrayList<>();

        this.dataTrackers.forEach(dataTracker -> {
            dataTrackers.add(BarChallengesPlugin.GSON.toJson(dataTracker));
        });

        document.put("dataTrackers", dataTrackers);
        document.put("lastLogin", System.currentTimeMillis());

        return document;
    }

    private void loadPlayerData() {
        CompletableFuture.supplyAsync(() -> BarChallengesPlugin.getInstance().getMongoHandler().getPlayerCollection().find(Filters.eq("uuid", this.uuid.toString())).first())
                .thenAccept(document -> {
                    if (document == null) {
                        this.resetAndAddDefaultDataTrackers();
                        return;
                    }

                    try {
                        final List<String> dataTrackers = document.getList("dataTrackers", String.class);

                        dataTrackers.forEach(dataTracker -> {
                            final JsonObject jsonObject = new JsonParser().parse(dataTracker).getAsJsonObject();
                            final String className = jsonObject.get("className").getAsString();
                            final String classData = jsonObject.get("classData").getAsString();

                            this.dataTrackers.add(BarChallengesPlugin.GSON.fromJson(classData, this.getTrackerByName(className)));
                        });

                        this.lastLogin = document.getLong("lastLogin");

                        if (this.dataTrackers.isEmpty()) {
                            this.resetAndAddDefaultDataTrackers();
                        }

                        final Date lastLogonDate = new Date(this.lastLogin);
                        final Date currentDate = new Date();

                        if (!DateValidationUtil.isSameDay(lastLogonDate, currentDate)) {
                            this.resetAndAddDefaultDataTrackers();
                        }

                        this.checkForNewDataTrackers();
                    } catch (Exception e) {
                        e.printStackTrace();
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
                                    ChatColor.GRAY + ChatColor.ITALIC.toString() + "Your rewards will be distributed shortly."
                            });

                            dailyChallenge.getRewards().forEach(challengeReward -> challengeReward.giveTo(player));
                        }
                    }
                });
    }

    public void resetAndAddDefaultDataTrackers() {
        this.dataTrackers.clear();

        BarChallengesPlugin.getInstance().getDataTrackerHandler().getDataTrackerClasses()
                .forEach(this::registerTrackerByClass);
    }

    public void checkForNewDataTrackers() {
        BarChallengesPlugin.getInstance().getDataTrackerHandler().getDataTrackerClasses()
                .forEach(aClass -> {
                    final DataTracker dataTracker = this.dataTrackers.stream()
                            .filter(tracker -> tracker.getClass().equals(aClass))
                            .findFirst().orElse(null);

                    if (dataTracker == null) {
                        this.registerTrackerByClass(aClass);
                    }
                });
    }

    public void registerTrackerByClass(Class<?> clazz) {
        try {
            this.dataTrackers.add((DataTracker) clazz.newInstance());
        } catch (Exception exception) {
            // it should never throw an exception
            exception.printStackTrace();
        }
    }

    public <T extends DataTracker> T getTrackerByClass(Class<T> clazz) {
        return (T) this.dataTrackers.stream()
                .filter(dataTracker -> dataTracker.getClass().equals(clazz))
                .findFirst().orElse(null);
    }

    public <T extends DataTracker> T getTrackerByName(String name) {
        return (T) this.dataTrackers.stream()
                .filter(dataTracker -> dataTracker.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
