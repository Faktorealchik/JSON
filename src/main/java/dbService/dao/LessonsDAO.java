package dbService.dao;

import dbService.DBException;
import dbService.dataSets.LessonsDataSet;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class LessonsDAO {

    private final Session session;

    public LessonsDAO(Session session) {
        this.session = session;
    }

    public LessonsDataSet get(long id) throws HibernateException {
        return (LessonsDataSet) session.get(LessonsDataSet.class, id);
    }

    public void insertLesson(long id, String steps, String update_date, long other) throws DBException {
        session.saveOrUpdate(new LessonsDataSet(id, steps, update_date, other));
    }

    public void updateLesson(long id, String steps, String update_date, long other) throws DBException {
        session.update(new LessonsDataSet(id, steps, update_date, other));
    }
}
