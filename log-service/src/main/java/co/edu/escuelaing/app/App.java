package co.edu.escuelaing.app;

import static spark.Spark.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import org.bson.Document;

public class App {

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static void main(String[] args) {
        port(getPort());
        get("hello", (req,res) -> "Hello Docker!");
        post("/api/backend",(req, res)->{
            res.type("application/json");
            System.out.println(req.queryParams("value"));
            return insert(req.queryParams("value"));
        });
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

    private static String findLastTenValues(MongoCollection<Document> collection){
        int index = (int)collection.countDocuments() - 11;
        collection.find(Filters.gt("id", index)).forEach((Consumer<Document>) (Document d) -> System.out.println(d.toJson()));
        return "";
    }

    private static String insert(String a){
        System.out.println(a);
        MongoClient mongoClient = new MongoClient("db");
        System.out.println("mongo creado");
        MongoDatabase db = mongoClient.getDatabase("logservice");
        System.out.println("base creada");
        MongoCollection<Document> collection = db.getCollection("data");
        System.out.println("coleccion creada");
        Document document = new Document();
        System.out.println(document);
        try {
            System.out.println((int)collection.countDocuments());
        } catch(Exception e){
            System.out.println("error: " + e);
        }
        collection.find().forEach((Consumer<Document>) (Document d) -> System.out.println(d.toJson()));
        document.append("id", (int)collection.countDocuments());
        document.append("value", a);
        document.append("date", formatter.format(new Date()));
        System.out.println("Documento: " + document);
        collection.insertOne(document);
        System.out.println("insertado");
        mongoClient.close();
        return findLastTenValues(collection);
    }
}
