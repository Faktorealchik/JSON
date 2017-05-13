package lesson;

import dbService.DBException;
import dbService.DBService;
import dbService.dataSets.LessonsDataSet;

public class LessonsService {
    private final DBService dbService;

    public LessonsService(DBService dbService) {
        this.dbService = dbService;
    }

    public long addLesson(long id, String steps, String update_date, long other) throws DBException {
        return dbService.addUser(id, steps, update_date, other);
    }

    public LessonsDataSet get(long id) throws DBException {
        return dbService.getUser(id);
    }

    public void updateLesson(long id, String steps, String update_date, long other) throws DBException {
        dbService.updateUser(id, steps, update_date, other);
    }
}
