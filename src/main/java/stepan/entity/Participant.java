package stepan.entity;
//у события могут быть участникик и их может быть много, для этого нужна реализация связи один ко многим
//создадим новую сущность участник (класс Paticipant)

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity//данный класс будет пасистентной сущностью
@Table(name = "PARTICIPANT")
public class Participant {
    //создадим три поля
    private Long id;
    private String firstName;
    private String lastName;

    //создадим коннструктор который будет принимать имя и фамилию
    public Participant(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Participant() {
    }

    @Id
    @GeneratedValue(generator = "increment")//в поле ставятся значения из генератора increment
    @GenericGenerator(name = "increment", strategy = "increment")//стратегия метода генератора increment
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
