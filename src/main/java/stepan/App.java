package stepan;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import stepan.entity.Event;
import stepan.entity.Participant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * В JPA есть понятие персистентная (persistence) сущность —
 * сущность, связанная с её данными в БД, которая управляется текущей сессией (соединением).
 */
public class App {
    public static void main(String[] args) {
        //для того,чтобы работать с БД с помощью hibernate  нужно использовать класс сессионфактори
        SessionFactory sessionFactory = null;
        //начинаем собирать нашу сессионфактори с помощью класса StandardServiceRegistry (Стандартный сервисный реестр)
        //с ее помощью хибернет конфигурирует нашу объвертку для работы с БД
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()//вызываем метод конфигурации
                .build();//и метод строительства
        try {
            //обращаемся к вспомогательному классу хибернате для того чтобы собрать нашу сессионфактори
            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(Event.class)//указываем что мы сущность описали не с помощью XML а при помощи аннотированного класса
                    .addAnnotatedClass(Participant.class)//указываем аннотированный класс участника
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            //если что-то пошло не так то мы будем нашу сессионфактори просто выключать
            StandardServiceRegistryBuilder.destroy(registry);
        }
        //теперь можем начинать работу с нашей БД
        //вызываем сессию
        Session session = sessionFactory.openSession();
        //начинаем транзакцию
        session.beginTransaction();
        //сохраним одно новое событие (создадим и сохраним класс евент с названием титл и датой
        session.save(new Event("Скринкаст о Hibernate", new Date()));
        //получаем сессию и вызываем метод коммита
        session.getTransaction().commit();
        //закрываем сессию
        session.close();

        //попробуем прочитать сохраненное событие
        //снова вызываем сессию
        session = sessionFactory.openSession();
        //начинаем транзакцию
        session.beginTransaction();
        //в хибернате есть возможность создать запрос через сессию
        List result = session.createQuery("from Event").list();//мы хотим вытащить все записи из таблицы event
        //теперь мы можем пройти по всем событиям которые мы получили
        for (Event event : (List<Event>) result) {
            //печатаем результат
            System.out.println("Event (" + event.getDate() + ") : " + event.getTitle());
        }
        session.getTransaction().commit();//делаем коммит
        session.close();//закрываем сессию

        //у нас уже есть одно событие, сделаем так, чтобы о него были участники
        //для этого мы должны достать наше событие, проставить в него участников и потом попробовать получить
        session = sessionFactory.openSession();//вызываем сессию
        session.beginTransaction();//начинаем транзакцию
        //получаем наше событие
        Event event = session.load(Event.class, 1L);
        //поскольку hbm не умеет сразу сохранять списки, нам предварительно необходимо сохранить всех наших участников
        List<Participant> participants = getParticipants();
        for (Participant participant : participants) {
            session.save(participant);
        }
        //укажем наших участников
        event.setParticipantList(participants);
        //после сохранения участников мы можем обновить их в БД
        session.save(event);
        session.getTransaction().commit();//делаем коммит
        //предварительно вытаскиваем нашу сущность
        result = session.createQuery("from Event").list();//мы хотим вытащить все записи из таблицы event
        //теперь пройдемся по нашему событию только в этот раз мы дополнительно выведем в консоль количество участников
        for (Event event1 : (List<Event>) result) {
            //печатаем результат
            System.out.println("Событие (" + event1.getDate() + ") : " + event1.getTitle() + " участников события = " + event1.getParticipantList().size());
        }
        session.close();//закрываем сессию
        //закрываем сессионфактори предварительно проверить что она не nullв а то может выкинуть исключение
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    //создадим утилитный метод, который будет нам возвращать список участников participant
    public static List<Participant> getParticipants() {
        //будем возвразать массив как список наших участников
        return Arrays.asList(
                //с помощью конструктора создадим несколько участников
                new Participant("Stepan", "Blandinskiy"),
                new Participant("Fedor", "Loshenkov"),
                new Participant("Ivan", "Ivanov"),
                new Participant("Anna", "Fedotova")
        );
    }
}
