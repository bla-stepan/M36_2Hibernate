package stepan.entity;
//создаем отдельный пакет entity в нем создаем этот класс для хранения определенных событий
import java.util.Date;

//в этом классе будем хранить переменные события
public class Event {
    private Long id;
    private String title;
    private Date date;

    //создаем дефолтный конструктор, конструктор с датой и названием, геттеры и сеттеры
    public Event() {
    }

    public Event(String title, Date date) {
        this.title = title;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
