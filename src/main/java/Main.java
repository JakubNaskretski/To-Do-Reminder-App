import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    // Create an EntityManagerFactory when you start the application
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("To-Do-Reminder-App-1");

    public static void main(String[] args) {
//        addTask(1L, 1, "testTask1");
//        addTask(2L, 1, "testTask2");
//        addTask(3L, 1, "testTask3");
//        addTask( 1, "testTask1");
//        addTask(1, "testTask2");
//        addTask(1, "testTask3");

        getTask(1L);
        getTask(2L);
        getTask(3L);
        ENTITY_MANAGER_FACTORY.close();
    }

    public static void addTask(int importance, String taskName) {
        // The EntityManager class allows operations such as create, read, update, delete
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        // Used to issue transactions on the EntityManager
        EntityTransaction et = null;

        try {
            // Get transaction and start
            et = em.getTransaction();
            et.begin();

            // Create and set values for new task
            Task task = new Task();
//            task.setTaskId(id);
            task.setCreationDate(new Date());
            task.setImportance(importance);
            task.setTaskName(taskName);

            // Save the task object
            em.persist(task);
            et.commit();
        } catch (Exception ex) {
            // If there is an exception rollback changes
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            // Close EntityManager
            em.close();
        }
    }

    public static void getTask(long id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        // the lowercase t refers to the object
        // :taskId is a parameterized query thats value is set below
        String query = "SELECT t FROM Task t WHERE t.id = :taskId";

        // Issue the query and get a matching Task
        TypedQuery<Task> tq = em.createQuery(query, Task.class);
        tq.setParameter("taskId", id);

        Task task = null;
        try {
            // Get matching task object and output
            task = tq.getSingleResult();
            System.out.println(task.getTaskName() + " " + task.getTaskId());
        } catch (NoResultException ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }
}