package com.solexgames.challenges.model;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.solexgames.challenges.BarChallengesPlugin;
import com.solexgames.challenges.challenge.DailyChallenge;
import com.solexgames.challenges.tracker.DataTracker;
import com.solexgames.challenges.tracker.impl.KillsDataTracker;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.*;
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
public class ChallengePlayer {

    private Map<DailyChallenge, ChallengeData> dataMap = new HashMap<>();
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
                    if (document == null) {
                        this.setupDefaultDataTrackers();
                        return;
                    }

                    this.dataTrackers = document.getList("data", DataTracker.class);
                    this.lastLogin = document.getLong("lastLogin");
                });
    }

    public void setupDefaultDataTrackers() {
        this.dataTrackers.add(new KillsDataTracker());
    }

    public DataTracker getDataTrackerByClass(Class<? extends DataTracker> trackerClass) {
        return this.dataTrackers.stream()
                .filter(dataTracker -> dataTracker.getClass().equals(trackerClass))
                .findFirst().orElse(null);
    }
}
