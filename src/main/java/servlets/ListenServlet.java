package servlets;

import lesson.LessonsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ListenServlet extends HttpServlet {
    private final LessonsService lessonsService;
    private static final Logger logger = LogManager.getLogger(ListenServlet.class.getName());

    public ListenServlet(LessonsService lessonsService) {
        this.lessonsService = lessonsService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] lessons = req.getParameter("lessons").trim().split(" ");
        logger.info("lessons: " + Arrays.toString(lessons));

        Set<Integer> setOfLessons = new TreeSet<>();
        try {
            for (String id : lessons) {
                setOfLessons.add(Integer.parseInt(id));
            }
            Thread thread = new Thread(new AnswerServlet(lessonsService, resp, setOfLessons));
            thread.run();
            thread.join();
        } catch (NumberFormatException | InterruptedException e) {
            logger.error("Denied " + e.getMessage());
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println("Access Denied, please give all correct");
            resp.getWriter().println("<br><br> <h1><a href=/index.html>go back </a></h1>");

        }
    }
}
