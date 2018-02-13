import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageDownloader
{
   public static void main(String[] args)
   {
      JSONParser parser = new JSONParser();
      String RESOURCE_FILE_PATH = "./resources/images.json";
      String OUTPUT_DIRECTORY = "./images/";
      int imageCount = 0;
      boolean yolo = false;

      try
      {
         JSONArray cardArray = (JSONArray) parser.parse(new FileReader(RESOURCE_FILE_PATH));

         for (Object object : cardArray)
         {
            JSONObject card = (JSONObject) object;

            String id = (String) card.get("id");
            String url = (String) card.get("url");

            if (imageCount < 5 || yolo)
            {
               try (InputStream in = new URL(url).openStream())
               {
                  Files.copy(in, Paths.get(OUTPUT_DIRECTORY + id + ".jpg"));
                  System.out.println("Downloaded and saved image number " + (imageCount + 1) + ": " + id + ".jpg");

                  imageCount++;
               }
               catch (IOException ioe)
               {
                  ioe.printStackTrace();
               }
            }
         }
      }
      catch (IOException | ParseException e)
      {
         e.printStackTrace();
      }
   }
}
