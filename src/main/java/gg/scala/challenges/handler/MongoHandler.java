package gg.scala.challenges.handler;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import gg.scala.lemon.Lemon;
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
        this.client = new MongoClient(
                new MongoClientURI(
                        Lemon.getInstance().getMongoConfig().getUri()
                )
        );
        this.database = this.client.getDatabase("SolexGames");

        this.playerCollection = this.database.getCollection("challenge_players");
    }
}
