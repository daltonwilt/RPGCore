package com.outcast.rpgcore.db;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

public class DatabaseService {

    private static ConnectionString connectionString;
    private static MongoClient client;

    public DatabaseService() {}

    public void init() {
        System.out.println("Starting database...");
        System.out.println("");

        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

        connectionString = new ConnectionString("mongodb+srv://Hexacorx:Aer432Kol7833@outcast-data.tenmjmf.mongodb.net/?retryWrites=true&w=majority");
        client = MongoClients.create(connectionString);

        // calling methods to mongoClient
//        create();
    }

    // create
    private static void create(List<Document> documents, String collectionName, String databaseName) {
        MongoCollection<Document> characters = client.getDatabase("rpg-class").getCollection("character");

//        Document data = new Document("Class", "Mage")
//                .append("Level", 24)
//                .append("Experience", 427)
//                .append("Profession", "Alchemist");

        // characters.insertOne(data);
        characters.insertMany(documents);
    }

    // read
    private static void read(MongoClient client) {
        List<Document> documents = client.listDatabases().into(new ArrayList<>());
        documents.forEach(document -> System.out.println(document.toJson()));
    }

    // update
    private static void update(List<Document> documents, String collectionName, String databaseName) {
        MongoCollection<Document> characters = client.getDatabase("rpg-class").getCollection("character");

        Bson filter = eq("", 100);
        Bson updateOperations = set("comment", "You should learn mongo");

        filter = and(eq("student_id", 10002d), eq("class_id", 10d));


        characters.updateMany(filter, updateOperations);
    }

    // delete
    private static void delete(MongoClient client) {
        List<Document> documents = client.listDatabases().into(new ArrayList<>());
        documents.forEach(document -> System.out.println(document.toJson()));
    }

}
