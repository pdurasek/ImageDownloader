import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageDownloader
{
   private final static String RESOURCE_FILE_PATH = "./resources/filteredCards.json";
   private final static String IMAGE_FILE_PATH = "./resources/images.json";
   private final static String OUTPUT_DIRECTORY = "./images/";
   private static int imageCount = 0;
   private static boolean yolo = true;

   public static void main(String[] args)
   {
      try
      {
         JSONParser parser = new JSONParser();
         JSONArray cardArray = (JSONArray) parser.parse(new FileReader(RESOURCE_FILE_PATH));
         JSONArray imageArray = (JSONArray) parser.parse(new FileReader(IMAGE_FILE_PATH));

         for (Object object : cardArray)
         {
            JSONObject card = (JSONObject) object;

            String id = (String) card.get("id");

            if (imageCount < 5 || yolo)
            {
               for(Object object2 : imageArray)
               {
                  JSONObject cardImage = (JSONObject) object2;
                  String imageID = (String) cardImage.get("id");
                  String url = (String) cardImage.get("url");
                  if(id.equals(imageID))
                  {
                     try (InputStream in = new URL(url).openStream())
                     {
                        Files.copy(in, Paths.get(OUTPUT_DIRECTORY + id + ".png"));
                        System.out.println("Downloaded and saved image number " + (imageCount + 1) + ": " + id + ".png");

                        imageCount++;
                     }
                     catch (IOException ioe)
                     {
                        ioe.printStackTrace();
                     }
                  }
               }
            }
         }
      }
      catch (IOException | ParseException e)
      {
         e.printStackTrace();
      }
   }

   public void createJSONFromMLab()
   {
      try
      {
         File file = new File(RESOURCE_FILE_PATH);
         File outputFile = new File("filteredCards.json");
         FileReader reader = new FileReader(file);
         FileWriter writer = new FileWriter(outputFile);
         BufferedReader bufferedReader = new BufferedReader(reader);
         BufferedWriter bufferedWriter = new BufferedWriter(writer);
         String line;
         int count = 1;
         bufferedWriter.write("[");
         while((line = bufferedReader.readLine()) != null)
         {
            bufferedWriter.write(line + ",");
            count++;
         }
         bufferedWriter.write("]");
         bufferedWriter.flush();

         System.out.println("Record count: " + count);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
