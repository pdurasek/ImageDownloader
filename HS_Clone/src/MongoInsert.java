import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class MongoInsert
{
   public static void main(String[] args)
   {
      String RESOURCE_FILE_PATH = "./resources/cards.json";
      int count = 0;

      MongoDatabase sampleDB = null;
      MongoClient client = null;
      MongoCollection<Document> collection = null;
      MongoCursor<Document> cursor = null;

      try
      {

         JSONParser parser = new JSONParser();
         JSONArray cardArray = (JSONArray) parser.parse(new FileReader(RESOURCE_FILE_PATH));
         client = new MongoClient("mongodb://gaben:zidov@ds012168.mlab.com:12168/kidneystone");
         sampleDB = client.getDatabase("kidneystone");
         collection = sampleDB.getCollection("cards");

         for (Object object : cardArray)
         {
            JSONObject card = (JSONObject) object;

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
            //{"cost":3,"set":"BRM","race":"BEAST","artist":"Hideaki Takamura","health":5,"dbfId":2311,"type":"MINION","playerClass":"DRUID","attack":2,"name":"Druid of the Flame","id":"BRM_010t2","dust":[40,400,5,50],"rarity":"COMMON"}
            // Cost, set, race, artist, health, type, playerClass, attack, name, id, dust, rarity, flavor, text

            //Document cardUnit = new Document()

            count++;

            if(count > 10)
            {
               break;
            }
         }



         //collection.insertMany(cardArray);

         MongoCursor<String> mc = client.listDatabaseNames().iterator();

         try
         {



         }
         catch (MongoException e)
         {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
            e.printStackTrace();
         }
         finally
         {
            mc.close();
         }

         sampleDB = client.getDatabase("SampleSocial");

         String dbName = sampleDB.getName();


         collection = sampleDB.getCollection("Tweets");

      }
      catch (Exception e)
      {
         System.out.println(e.getMessage());
         System.out.println(e.toString());
         e.printStackTrace();


         client.close();
      }
   }
}
