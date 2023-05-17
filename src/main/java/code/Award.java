package code;
import java.time.LocalDate;

public class Award {

    private LocalDate time;
    private String name;
    private String tag;
    private String description;

    public Award() {
    }

    public Award(LocalDate time, String name, String tag, String description) {
        this.time = time;
        this.name = name;
        this.tag = tag;
        this.description = description;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}