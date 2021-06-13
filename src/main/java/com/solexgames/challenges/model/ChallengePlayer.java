package com.solexgames.challenges.model;

import com.cryptomorin.xseries.XSound;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.solexgames.challenges.BarChallengesPlugin;
import com.solexgames.challenges.tracker.DataTracker;
import com.solexgames.challenges.util.DateValidationUtil;
import com.solexgames.core.CorePlugin;
import com.solexgames.core.util.Color;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
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
            final JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("className", dataTracker.getClass().getSimpleName());
            jsonObject.addProperty("classData", CorePlugin.GSON.toJson(dataTracker));

            dataTrackers.add(jsonObject.toString());
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

                    final List<String> dataTrackers = document.getList("dataTrackers", String.class);

                    dataTrackers.forEach(dataTracker -> {
                        final JsonObject jsonObject = new JsonParser().parse(dataTracker).getAsJsonObject();
                        final String className = jsonObject.get("className").getAsString();
                        final String classData = jsonObject.get("classData").getAsString();

                        final DataTracker tracker = CorePlugin.GSON.fromJson(classData, BarChallengesPlugin.getInstance().getDataTrackerHandler().getTrackerByClassName(className));

                        this.dataTrackers.add(tracker);
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
                });
    }

    public void recalculateChallenges() {
        BarChallengesPlugin.getInstance().getChallengeHandler().getRegisteredChallenges()
                .forEach(dailyChallenge -> {
                    final DataTracker dataTracker = this.getTrackerByClass(dailyChallenge.getTrackerType());

                    if (dataTracker != null && dataTracker.hasCompleted(dailyChallenge.getCompletionRequirement())) {
                        final Player player = Bukkit.getPlayer(this.uuid);

                        if (player != null) {
                            player.sendMessage(new String[]{
                                    "",
                                    Color.SECONDARY_COLOR + "You've completed today's daily challenge of getting " + Color.MAIN_COLOR + dailyChallenge.getName() + Color.SECONDARY_COLOR + "!",
                                    ChatColor.GRAY + ChatColor.ITALIC.toString() + "You'll receive your rewards shortly!",
                                    ""
                            });

                            XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player.getLocation());

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

    public <T> T getTrackerByClass(Class<? extends DataTracker> clazz) {
        return (T) this.dataTrackers.stream()
                .filter(dataTracker -> clazz.equals(dataTracker.getClass()))
                .findFirst().orElse(null);
    }

    public <T> T getTrackerByName(String name) {
        return (T) this.dataTrackers.stream()
                .filter(dataTracker -> dataTracker.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
