package stepan;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import stepan.entity.Event;

import java.util.Date;
import java.util.List;

/**
 * Hello world!
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
        //закрываем сессионфактори предварительно проверить что она не nullв а то может выкинуть исключение
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
