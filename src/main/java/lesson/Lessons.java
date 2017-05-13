package lesson;

public class Lessons {
    private final long id;
    private final String steps;
    private final String update_date;
    private final String other;

    public Lessons(long id, String steps, String update_date, String other) {
        this.id = id;
        this.steps = steps;
        this.update_date = update_date;
        this.other = other;
    }

    public long getId() {
        return id;
    }

    public String getSteps() {
        return steps;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public String getOther() {
        return other;
    }
}
