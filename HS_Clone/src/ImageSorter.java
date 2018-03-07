import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageSorter
{
   public static void main(String[] args)
   {
      JSONParser parser = new JSONParser();
      String RESOURCE_FILE_PATH = "./resources/cards.json";
      String OUTPUT_DIRECTORY = "./imagesSorted/";
      String TARGET_DIRECTORY = "./images";
      int imageCount = 0;

      try
      {
         JSONArray cardArray = (JSONArray) parser.parse(new FileReader(RESOURCE_FILE_PATH));
         File imageDirectory = new File(TARGET_DIRECTORY);
         File[] imageList = imageDirectory.listFiles();

         for (Object object : cardArray)
         {
            JSONObject card = (JSONObject) object;

            String id = (String) card.get("id");

            for (int i = 0; i < imageList.length; i++)
            {
               if (imageList[i].getName().equals(id + ".png"))
               {
                  System.out.println(imageList[i].getName());
                  Files.copy(Paths.get(imageList[i].getPath()), Paths.get(OUTPUT_DIRECTORY + id + ".png"));
                  imageCount++;
                  break;
               }
            }
         }

         System.out.println("Images sorted: " + imageCount);
      }
      catch (IOException | ParseException e)
      {
         e.printStackTrace();
      }
   }
}
