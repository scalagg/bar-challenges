package gg.scala.challenges.model;

import com.cryptomorin.xseries.XSound;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import gg.scala.challenges.BarChallengesConstants;
import gg.scala.challenges.BarChallengesPlugin;
import gg.scala.challenges.challenge.DailyChallenge;
import gg.scala.challenges.tracker.DataTracker;
import gg.scala.challenges.util.DateValidationUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import net.evilblock.cubed.serializers.Serializers;
import net.evilblock.cubed.util.CC;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@Getter
@SuppressWarnings("all")
public class ChallengePlayer {

    private List<String> completedChallenges = new ArrayList<>();
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
            jsonObject.addProperty("classData", Serializers.getGson().toJson(dataTracker));

            dataTrackers.add(jsonObject.toString());
        });

        document.put("dataTrackers", dataTrackers);
        document.put("completedChallenges", this.completedChallenges);

        document.put("lastLogin", System.currentTimeMillis());

        return document;
    }

    public CompletableFuture<Void> loadPlayerData() {
        return CompletableFuture
            .supplyAsync(() -> BarChallengesPlugin.getInstance().getMongoHandler().getPlayerCollection().find(Filters.eq("uuid", this.uuid.toString())).first())
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

                        final DataTracker tracker = Serializers.getGson().fromJson(classData, BarChallengesPlugin.getInstance().getDataTrackerHandler().getTrackerByClassName(className));

                        this.dataTrackers.add(tracker);
                    });

                    this.lastLogin = document.getLong("lastLogin");
                    this.completedChallenges = document.getList("completedChallenges", String.class);

                    if (this.completedChallenges == null) {
                        this.completedChallenges = new ArrayList<>();
                    }

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

                    if (dataTracker != null && dataTracker.canAchieve(dailyChallenge.getCompletionRequirement()) && !this.hasCompleted(dailyChallenge)) {
                        final Player player = Bukkit.getPlayer(this.uuid);

                        if (player != null) {
                            player.sendMessage(new String[]{
                                    BarChallengesConstants.CHALLENGE_PREFIX + CC.SEC + "You've completed the " + CC.PRI + dailyChallenge.getName() + CC.SEC + " challenge!",
                                    BarChallengesConstants.CHALLENGE_PREFIX + ChatColor.GRAY + "Rewards for this challenge completion will be given shortly.",
                            });

                            this.completedChallenges.add(dailyChallenge.getName());

                            XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player.getLocation());
                            dailyChallenge.getRewards().forEach(challengeReward -> challengeReward.giveTo(player));
                        }
                    }
                });
    }

    public boolean hasCompleted(DailyChallenge<?> dailyChallenge) {
        return this.completedChallenges.contains(dailyChallenge.getName());
    }

    public void resetAndAddDefaultDataTrackers() {
        this.dataTrackers.clear();
        this.completedChallenges.clear();

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

    @SneakyThrows
    public void registerTrackerByClass(Class<?> clazz) {
        this.dataTrackers.add((DataTracker) clazz.newInstance());
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
