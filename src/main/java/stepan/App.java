package stepan;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import stepan.entity.Event;
import stepan.entity.Participant;
import stepan.entity.Place;

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
                    .addAnnotatedClass(Place.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            //если что-то пошло не так то мы будем нашу сессионфактори просто выключать
            StandardServiceRegistryBuilder.destroy(registry);
        }
//        //теперь можем начинать работу с нашей БД
//        //вызываем сессию
//        Session session = sessionFactory.openSession();
//        //начинаем транзакцию
//        session.beginTransaction();
//        //сохраним одно новое событие (создадим и сохраним класс евент с названием титл и датой
//        session.save(new Event("Скринкаст о Hibernate", new Date()));
//        //получаем сессию и вызываем метод коммита
//        session.getTransaction().commit();
//        //закрываем сессию
//        session.close();
//
//        //попробуем прочитать сохраненное событие
//        //снова вызываем сессию
//        session = sessionFactory.openSession();
//        //начинаем транзакцию
//        session.beginTransaction();
//        //в хибернате есть возможность создать запрос через сессию
//        List result = session.createQuery("from Event").list();//мы хотим вытащить все записи из таблицы event
//        //теперь мы можем пройти по всем событиям которые мы получили
//        for (Event event : (List<Event>) result) {
//            //печатаем результат
//            System.out.println("Event (" + event.getDate() + ") : " + event.getTitle());
//        }
//        session.getTransaction().commit();//делаем коммит
//        session.close();//закрываем сессию
//
//        //у нас уже есть одно событие, сделаем так, чтобы о него были участники
//        //для этого мы должны достать наше событие, проставить в него участников и потом попробовать получить
//        session = sessionFactory.openSession();//вызываем сессию
//        session.beginTransaction();//начинаем транзакцию
//        //получаем наше событие
//        Event event = session.load(Event.class, 1L);
//        //поскольку hbm не умеет сразу сохранять списки, нам предварительно необходимо сохранить всех наших участников
//        List<Participant> participants = getParticipants();
//        for (Participant participant : participants) {
//            session.save(participant);
//        }
//        //укажем наших участников
//        event.setParticipantList(participants);
//        //после сохранения участников мы можем обновить их в БД
//        session.save(event);
//        session.getTransaction().commit();//делаем коммит
//        //предварительно вытаскиваем нашу сущность
//        result = session.createQuery("from Event").list();//мы хотим вытащить все записи из таблицы event
//        //теперь пройдемся по нашему событию только в этот раз мы дополнительно выведем в консоль количество участников
//        for (Event event1 : (List<Event>) result) {
//            //печатаем результат
//            System.out.println("Событие (" + event1.getDate() + ") : " + event1.getTitle() + " участников события = " + event1.getParticipantList().size());
//        }
//        session.close();//закрываем сессию
//
//
//        //используем наш класс плэйс в нашем основном классе
//        //снова открываем сессию
//        session = sessionFactory.openSession();
//        session.beginTransaction();
//        Place place = new Place("Yaroslavl", "M. Proletarscaya", "3");
//        session.save(place);
//        //теперь перейдем в каласс Eventи добавим туда поле с плэйс
//        //теперь мы должны вытащить наше событие из базы
//        event = session.load(Event.class, 1L);
//        //через сеттер устанавливаем место для события
//        event.setPlace(place);
//        //теперь сохраняем
//        session.save(event);
//        session.getTransaction().commit();
//
//        //проходимся по списку и будем выводить дополнительно место
//        result = session.createQuery("from Event").list();
//        for (Event event1 : (List<Event>) result) {
//            //печатаем результат
//            System.out.println("Событие ("
//                    + event1.getDate()
//                    + ") : "
//                    + event1.getTitle()
//                    + " участников события = "
//                    + event1.getParticipantList().size()
//                    + " место события: " + event1.getPlace().getCity()
//            );
//        }
//        session.close();//закрываем сессию
//

        //ПОСЛЕДНИЙ ВИДЕОРОЛИК
        //открываем сессию через фабрику
        Session session = sessionFactory.openSession();
        session.beginTransaction();//начинаем транзакцию
        //сохраняем одно событие
        session.save(new Event("Наше первое событие!", new Date()));
        // теперь вытащим событие из БД с помощью CriteriaAPI
        //первым шагом для получения CriteriaAPI с помощью класса кретриябилдер, который также получается из сессии
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        //у нас будет типизированный запрос (мы заранее знаем, какой тип данных, которые мы хотим получить
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        //поскольку criteriaQuery представляет из себя граф а граф это древовидная структура и по этому мы используем отсылку на класс Rood (изначальная точка нашего запроса)
        Root<Event> query = criteriaQuery.from(Event.class);
        //у query вызываем метод селект и передаем в него наш запрос - мы указали какой витд операции мы хотим выполнить
        criteriaQuery.select(query);
        //снова используем класс Query из пакета хибернате и тут мы сново обращаемся к сесси но только сейчас мы передаем сделаный запрос критериа
        Query<Event> myQuery = session.createQuery(criteriaQuery);
        //теперь мы готовы, выполнить наш запрос
        //указываем список так как мы будем вытаскиват список всех событий
        List<Event> events= myQuery.getResultList();
        System.out.println(events.get(0).getTitle());
        session.getTransaction().commit();
        session.close();

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
