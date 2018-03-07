import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class MongoManager
{
   public static void main(String[] args)
   {
      MongoManager manager = new MongoManager();
      //manager.importCollection();
      //manager.exportCollection();
      //manager.createUserCollection("PaxKing");
   }

   private void importCollection()
   {
      String RESOURCE_FILE_PATH = "./resources/cards.json";
      ArrayList<String> includedSets = new ArrayList<>(Arrays.asList("NAXX", "GVG", "BRM", "KARA", "OG", "CORE", "TGT", "LOE", "EXPERT1"));

      MongoDatabase sampleDB = null;
      MongoClient client = null;
      MongoCollection<Document> collection = null;
      MongoCursor<Document> cursor = null;

      try
      {
         JSONParser parser = new JSONParser();
         JSONArray cardArray = (JSONArray) parser.parse(new FileReader(RESOURCE_FILE_PATH));
         MongoClientURI uri = new MongoClientURI("mongodb://gaben:zidov@ds012168.mlab.com:12168/kidneystone");
         client = new MongoClient(uri);
         sampleDB = client.getDatabase(uri.getDatabase());
         collection = sampleDB.getCollection("cards");


         for (Object object : cardArray)
         {
            JSONObject card = (JSONObject) object;

            // Naxx, GVG, BRM, KARA, OG, CORE, TGT, LOE, EXPERT1

            // Check if card is collectible
            if (card.get("collectible") == null)
            {
               continue;
            }

            // Check if it is a spell or minion
            if (card.get("type") == null)
            {
               continue;
            }
            else
            {
               if (!(card.get("type").toString().equalsIgnoreCase("spell") || card.get("type").toString().equalsIgnoreCase("minion")))
               {
                  continue;
               }
            }

            if (card.get("set") == null)
            {
               continue;
            }
            else
            {
               if (!includedSets.contains(card.get("set").toString()))
               {
                  continue;
               }
            }

            Map<String, Object> retMap = new Gson().fromJson(
                    card.toJSONString(), new TypeToken<HashMap<String, Object>>()
                    {
                    }.getType()

            );

            Document document = new Document(retMap);
            System.out.println("Inserting: " + card.get("name"));
            collection.insertOne(document);


         }

      }
      catch (Exception e)
      {
         System.out.println(e.getMessage());
         System.out.println(e.toString());
         e.printStackTrace();


         if (client != null)
         {
            client.close();
         }
      }

   }

   private void createUserCollection(String userName)
   {

      MongoDatabase sampleDB = null;
      MongoClient client = null;
      MongoCollection<Document> usersCollection = null;
      MongoCollection<Document> cardCollection = null;
      MongoCursor<Document> cursor = null;

      try
      {
         MongoClientURI uri = new MongoClientURI("mongodb://gaben:zidov@ds012168.mlab.com:12168/kidneystone");
         client = new MongoClient(uri);
         sampleDB = client.getDatabase(uri.getDatabase());
         usersCollection = sampleDB.getCollection("users");
         cardCollection = sampleDB.getCollection("cards");

         cursor = cardCollection.find(eq("set", "CORE")).iterator();
         Document userDoc = new Document();
         userDoc.append("userName", userName);
         HashMap<String, Integer> userCards = new HashMap<>();

         while (cursor.hasNext())
         {
            userCards.put(cursor.next().get("id").toString(), 3);
         }
         userDoc.append("cardsOwned", userCards);

         usersCollection.insertOne(userDoc);

      }
      catch (Exception e)
      {
         System.out.println(e.getMessage());
         System.out.println(e.toString());
         e.printStackTrace();


         if (client != null)
         {
            client.close();
         }
      }
   }

   private void exportCollection()
   {

   }

   private void printCardData(JSONObject card)
   {
      // Cost, set, race, artist, health, type, playerClass, attack, name, id, dust, rarity, flavor, text

      System.out.println("Card name: " + card.get("name"));
      System.out.println("\t" + "id: " + card.get("id"));
      System.out.println("\t" + "set: " + card.get("set"));
      System.out.println("\t" + "race: " + card.get("race"));
      System.out.println("\t" + "artist: " + card.get("artist"));
      System.out.println("\t" + "health: " + card.get("health"));
      System.out.println("\t" + "attack: " + card.get("attack"));
      System.out.println("\t" + "type: " + card.get("type"));
      System.out.println("\t" + "playerClass: " + card.get("playerClass"));
      System.out.println("\t" + "rarity: " + card.get("rarity"));
      System.out.println("\t" + "dust: " + card.get("dust"));
      System.out.println("\t" + "text: " + card.get("text"));
      System.out.println("\t" + "flavor: " + card.get("flavor"));
      System.out.println("===========================");

   }
}
