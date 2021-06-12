package com.solexgames.challenges.handler;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.solexgames.core.CorePlugin;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@Getter
@Setter
public class MongoHandler {

    private MongoClient client;
    private MongoDatabase database;

    private MongoCollection<Document> playerCollection;

    public MongoHandler() {
        this.client = new MongoClient(new MongoClientURI(CorePlugin.getInstance().getDatabaseConfig().getString("mongodb.url")));
        this.database = this.client.getDatabase(CorePlugin.getInstance().getDatabaseConfig().getConfiguration().getString("mongodb.database", "SGSoftware"));

        this.playerCollection = this.database.getCollection("challenges");
    }
}
