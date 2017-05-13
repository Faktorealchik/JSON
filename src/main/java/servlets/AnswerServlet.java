package servlets;

import dbService.DBService;
import dbService.DBException;
import dbService.dataSets.LessonsDataSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class AnswerServlet extends HttpServlet implements Runnable {

    private final HttpServletResponse resp;
    private final DBService dbService;
    private boolean isExist = false;
    private LessonsDataSet lessonsDataSet;
    private Set<Integer> lessons;
    private static final Logger logger = LogManager.getLogger(AnswerServlet.class.getName());

    public AnswerServlet(DBService dbService, HttpServletResponse resp, Set<Integer> lessons) {
        this.dbService = dbService;
        this.resp = resp;
        this.lessons = lessons;
    }

    @Override
    public void run() {
        Date date1 = new Date();
        Iterator<Integer> it = lessons.iterator();
        resp.setContentType("text/html;charset=utf-8");

        print(String.format("Id of lessons that you need: %s <br>", lessons.toString()/*req.getParameter("lessons")*/));
        print("--------------------------------------------------------------------------------");

        try {
            while (it.hasNext()) {
                int i = it.next();
                print("<br> answer for id: " + i + "<br><br>");

                Date date = new Date();
                //получаем урок по id, если есть
                lessonsDataSet = dbService.getLesson(i);
                //если такой есть
                if (lessonsDataSet != null) {
                    //если страницы нет, выходим и говорим что такой нет
                    if (lessonsDataSet.getUpdate_date() == null) {
                        print("Sorry, no access/not found");
                        print("<br>" + (double) (new Date().getTime() - date.getTime()) / 1000 + "s");
                        print("<br>--------------------------------------------------------------------------------");
                        date = new Date();
                        resp.flushBuffer();
                        continue;
                        //иначе если прошло меньше суток с последнего обновления, то выставляем кешированное значение
                    } else if (new Date().getTime() - lessonsDataSet.getOther() < 86400000) {//86400000 -- сутки

                        print(lessonsDataSet.toString());
                        print("<br>" + (double) (new Date().getTime() - date.getTime()) / 1000 + "s");
                        print("<br>--------------------------------------------------------------------------------");
                        date = new Date();
                        resp.flushBuffer();
                        continue;
                    }
                    isExist = true;
                } else isExist = false;
                //выводим и сохраняем в бд значения по ид
                initial(i);
                print("<br>" + (double) (new Date().getTime() - date.getTime()) / 1000 + "s");
                print("<br>--------------------------------------------------------------------------------");
                date = new Date();
                resp.flushBuffer();
            }
            print("<br>Total: " + (double) (new Date().getTime() - date1.getTime()) / 1000 + "s");
            print("<br><br> <h1><a href=/index.html>go back </a></h1>");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (DBException | IOException | ParseException e) {
            logger.error("running problem: "+e.getMessage()+ '\n'+e.toString());
            resp.setStatus(400);
        }

    }


    private void initial(int id) throws DBException, ParseException {
        URL url = createUrl("https://stepik.org/api/lessons/" + id);
        String sb = parseUrl(url);
        if (sb.isEmpty()) {
            print("Sorry, no access/not found");
            dbService.addLesson(id, null, null, 0);
            return;
        }
        jsonReader(sb);

    }


    private void jsonReader(String sb) throws ParseException{
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(sb);
        JSONArray steps = (JSONArray) jsonObject.get("lessons");
        JSONObject one = (JSONObject) steps.get(0);
        JSONArray arr = (JSONArray) one.get("steps");


        JSONObject jsonObject1 = new JSONObject();
        try {
            if (isExist) {
                String update = (String) one.get("update_date");
                long id = lessonsDataSet.getId();
                String step = lessonsDataSet.getSteps();
                String update_date = lessonsDataSet.getUpdate_date();
                dbService.updateLesson(id, step, update_date, new Date().getTime());
                jsonObject1.put("id", id);
                jsonObject1.put("steps", step);
                jsonObject1.put("update_date", update_date);


            } else {
                long id = (long) one.get("id");
                String step = arr.toJSONString();
                String update_date = one.get("update_date").toString();
                dbService.addLesson(id, step, update_date, new Date().getTime());
                jsonObject1.put("id", id);
                jsonObject1.put("steps", arr);
                jsonObject1.put("update_date", update_date);

            }
        }
        catch (DBException e){logger.error("DBException: "+e.toString()+" "+e.getMessage());}
        print(jsonObject1.toJSONString());
    }

    //print this
    private void print(Object obj) {
        try {
            resp.getWriter().println(obj);
        } catch (IOException e) {
            logger.error("Problem to print it: "+e.getMessage());
        }
    }

    private String parseUrl(URL url) {
        StringBuilder stringBuilder = new StringBuilder();
        // open connection new URL uses try-with-resources
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
        } catch (IOException e) {
            logger.error("Problem parse it: "+e.getMessage());
        }
        return stringBuilder.toString();
    }

    // create new URL
    private URL createUrl(String link) {
        try {
            return new URL(link);
        } catch (MalformedURLException e) {
            logger.error("No found URL: "+e.getMessage());
            return null;
        }
    }


}
