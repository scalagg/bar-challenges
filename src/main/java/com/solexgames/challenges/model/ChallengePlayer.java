package com.solexgames.challenges.model;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.solexgames.challenges.BarChallengesPlugin;
import com.solexgames.challenges.challenge.DailyChallenge;
import com.solexgames.challenges.tracker.DataTracker;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Player profile where statistics are saved.
 * <p></p>
 *
 * @author GrowlyX
 * @since 6/12/2021
 */

@Getter
@Setter
public class ChallengePlayer {

    private final Map<DailyChallenge, ChallengeData> dataMap = new HashMap<>();
    private final Set<DataTracker> dataTrackerSet = new HashSet<>();

    private final UUID uuid;
    private final String name;

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

        return document;
    }

    private void loadPlayerData() {
        CompletableFuture.supplyAsync(() -> BarChallengesPlugin.getInstance().getMongoHandler().getPlayerCollection().find(Filters.eq("uuid", this.uuid.toString())).first())
                .thenAccept(document -> {

                });
    }

    public void getDataTracker(String name) {

    }
}
