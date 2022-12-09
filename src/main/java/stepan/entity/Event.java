package stepan.entity;
//создаем отдельный пакет entity в нем создаем этот класс для хранения определенных событий
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;

@Table(name = "EVENTS")//добавляем аннотацию таблица
@Entity
//в этом классе будем хранить переменные события
public class Event {
    private Long id;
    private String title;
    private Date date;
    private List<Participant> participantList;//добавляем поле ссылки на список участников

    //создаем дефолтный конструктор, конструктор с датой и названием, геттеры и сеттеры
    public Event() {
    }

    public Event(String title, Date date) {
        this.title = title;
        this.date = date;
    }
    @Id//поле является уникальным идентификатором
    @GeneratedValue(generator = "increment")//в данную колонку вставляются сгенерированные значения
    @GenericGenerator(name = "increment", strategy = "increment")//предоставляет реализацию (использование) генератора
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Column(name = "TITLE")//указываем, что поле является колонкой title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Column(name = "EVENT_DATE")//указываем, что поле я вляется колонкой date
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @OneToMany(targetEntity = Participant.class)//указываем аннотацию один ко многим, т.е. событию могут соотвествовать несколько участников
    public List<Participant> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(List<Participant> participantList) {
        this.participantList = participantList;
    }
}
