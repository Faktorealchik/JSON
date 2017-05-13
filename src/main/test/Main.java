import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Александр on 03.05.2017.
 */
public class Main {
    public static void main(String[] args) throws Exception {
//        String path = new File("").getAbsolutePath();
//        for (int i = 15; i < 1000; i++) {
//            File file = new File(path+"/files/"+i+".txt");
//            FileWriter writer = new FileWriter(file);
//            writer.write("");
//        }

        BufferedReader bufferedReader = new BufferedReader(new FileReader("task.txt"));
        String s =bufferedReader.readLine().trim();
        String[] id = s.split(" ");


        HashSet<Integer> ids = new HashSet<>();
        for (int i = 0; i < id.length; i++) {
            ids.add(Integer.parseInt(id[i]));
        }
        Iterator<Integer> it = ids.iterator();
        System.out.println(String.format("Id of lessons that you need: %s ",s));
        System.out.println("\n--------------------------------------------------------------------------------");
        Date date = new Date();
        Date date1 = new Date();
        while(it.hasNext()){
            int a = it.next();
            System.out.println("answer for id: "+a);
            initial(a);
            System.out.println((double)(new Date().getTime()-date.getTime())/1000+"s");
            System.out.println("\n--------------------------------------------------------------------------------");
            date = new Date();
        }
        System.out.println(("\nTotal: "+(double)(new Date().getTime()-date1.getTime())/1000+"s"));

    }

    private static void initial(int id){

        try {
            String sb;
            BufferedReader file;
            file = createFile("files/" + id + ".txt");
            sb = parseUrl(file);
            if (sb.isEmpty()) {
                System.out.println("Sorry, no access");
                return;
            }
            jsonReader(sb);
            file.close();

        }
        catch (Exception e){
                System.out.println("Sorry, no access");
//            e.printStackTrace();
            return;
        }

//        System.out.println(sb);
    }


    private static void jsonReader(String sb) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(sb);
        JSONArray steps = (JSONArray) jsonObject.get("lessons");
        JSONObject one = (JSONObject) steps.get(0);
        JSONArray arr = (JSONArray) one.get("steps");
//        print("id: "+one.get("id"));
//        print("<br><br>steps:");
//        print(arr);
//        print("<br><br>update date: "+one.get("update_date"));

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id", one.get("id"));
        jsonObject1.put("steps", arr);
        jsonObject1.put("update_date",one.get("update_date"));
        System.out.println(jsonObject1.toJSONString());

    }

    private static String parseUrl(BufferedReader url) {
        StringBuilder stringBuilder = new StringBuilder();
        // open connection new URL uses try-with-resources
        try {
            String inputLine;
            while ((inputLine = url.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
        return stringBuilder.toString();
    }

    // create new URL
    private static BufferedReader createFile(String link) {
        try {
            return new BufferedReader(new FileReader(link));
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            return null;
        }
    }
}
