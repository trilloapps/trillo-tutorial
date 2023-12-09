package com.collager.trillo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.collager.trillo.pojo.Result;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import org.json.JSONObject;

import static com.collager.trillo.util.Util.convertToResult;

public class MongoDbApi extends BaseApi {
    private static String mongoDbEndpoint = "/api/v1.1/mongodb";

    public static Result getPage(String url, String databaseName, String collectionName, Integer start, Integer size) {
        Map body = new HashMap();
        body.put("url", url);
        body.put("databaseName", databaseName);
        body.put("collectionName", collectionName);
        body.put("start", start);
        body.put("size", size);
        Object res = HttpRequestUtil.post( mongoDbEndpoint + "/getPage", new JSONObject(body).toString());
        return convertToResult(res);
    }

    public static Result getPage(String url, String databaseName, String collectionName, Integer start) {
        Map body = new HashMap();
        body.put("url", url);
        body.put("databaseName", databaseName);
        body.put("collectionName", collectionName);
        body.put("start", start);
        Object res = HttpRequestUtil.post( mongoDbEndpoint + "/getPage", new JSONObject(body).toString());
        return convertToResult(res);
    }

    public static Result queryDocumentById(String url, String databaseName, String collectionName, String id) {
        Map body = new HashMap();
        body.put("url", url);
        body.put("databaseName", databaseName);
        body.put("collectionName", collectionName);
        body.put("id", id);
        Object res = HttpRequestUtil.post( mongoDbEndpoint + "/queryDocumentById", new JSONObject(body).toString());
        return convertToResult(res);
    }

    public static Result queryPage(String url, String databaseName, String collectionName, BasicDBObject query, Integer start) {
        try (MongoClient mongoClient = MongoClients.create("" + url)) {

            List<Map<String, Object>> rows = new ArrayList<>();
            MongoDatabase database = mongoClient.getDatabase("" + databaseName);

            MongoCollection<Document> collection = database.getCollection("" + collectionName);

            try (MongoCursor<Document> cur = collection.find(query).skip(start).iterator()) {


                while (cur.hasNext()) {
                    Document doc = cur.next();
                    rows.add(doc);

                }
            }
            catch (Exception e) {
                return Result.getFailedResult(e.getMessage());
            }
            return Result.getSuccessResultWithData(rows);

        } catch (Exception e) {
            return Result.getFailedResult(e.getMessage());
        }
    }

    public static Result queryPage(String url, String databaseName, String collectionName, BasicDBObject query, Integer start, Integer size) {
        try (MongoClient mongoClient = MongoClients.create("" + url)) {

            List<Map<String, Object>> rows = new ArrayList<>();
            MongoDatabase database = mongoClient.getDatabase("" + databaseName);

            MongoCollection<Document> collection = database.getCollection("" + collectionName);

            try (MongoCursor<Document> cur = collection.find(query).skip(start).limit(size).iterator()) {


                while (cur.hasNext()) {
                    Document doc = cur.next();
                    rows.add(doc);
                }
            }
            catch (Exception e) {
                return Result.getFailedResult(e.getMessage());
            }
            return Result.getSuccessResultWithData(rows);

        } catch (Exception e) {
            return Result.getFailedResult(e.getMessage());
        }
    }

}
