package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class LessonsDataSet implements Serializable { // Serializable Important to Hibernate!
    private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "steps")
    private String steps;

    @Column(name = "update_date")
    private String update_date;


    @Column(name = "other")
    private long other;

    //Important to Hibernate!
    @SuppressWarnings("UnusedDeclaration")
    public LessonsDataSet() {
    }

    @SuppressWarnings("UnusedDeclaration")
    public LessonsDataSet(long id, String steps, String update_date, long other) {
        this.setId(id);
        this.setSteps(steps);
        this.setUpdate_date(update_date);
        this.setOther(other);
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    @SuppressWarnings("UnusedDeclaration")
    public long getOther() {
        return other;
    }

    public void setOther(long other) {
        this.other = other;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ",\"steps\": " + steps +
                ", \"update_date\":\"" + update_date +
                "\"}";

    }
}