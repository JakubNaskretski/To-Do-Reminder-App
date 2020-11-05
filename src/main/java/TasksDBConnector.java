import javax.persistence.*;
import java.util.Date;
import java.util.List;

public class TasksDBConnector {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("To-Do-Reminder-App-1");

    public TasksDBConnector() {
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
            task.setCreationDate(new Date());
            task.setImportance(importance);
//            task.getSubTasks().add();
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
            System.out.println(task.toString());
        } catch (NoResultException ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static List<Task> getToDoTasks() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        // the lowercase c refers to the object
        // :custID is a parameterized query thats value is set below
        String strQuery = "SELECT t FROM Task t WHERE t.id IS NOT NULL AND t.isTaskDone = 0";

        TypedQuery<Task> tq = em.createQuery(strQuery, Task.class);
        List<Task> tasks = null;
        try {
            // Get matching customer object and output
            tasks = tq.getResultList();
        }
        catch(NoResultException ex) {
            ex.printStackTrace();
        }
        finally {
            em.close();
        }
        return tasks;
    }

    public static List<Task> getDoneTasks() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        // the lowercase c refers to the object
        // :custID is a parameterized query thats value is set below
        String strQuery = "SELECT t FROM Task t WHERE t.id IS NOT NULL AND t.isTaskDone = 1";

        TypedQuery<Task> tq = em.createQuery(strQuery, Task.class);
        List<Task> tasks = null;
        try {
            // Get matching customer object and output
            tasks = tq.getResultList();
        }
        catch(NoResultException ex) {
            ex.printStackTrace();
        }
        finally {
            em.close();
        }
        return tasks;
    }

    public static void changeTaskName(int id, String taskName) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;

        Task task = null;

        try {
            // Get transaction and start
            et = em.getTransaction();
            et.begin();

            // Find customer and make changes
            task = em.find(Task.class, id);
            task.setTaskName(taskName);

            // Save the customer object
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

    public static void makeTaskDone(Long id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;

        Task task = null;

        try {
            // Get transaction and start
            et = em.getTransaction();
            et.begin();

            // Find customer and make changes
            task = em.find(Task.class, id);
            task.setIsTaskDone(1);

            // Save the customer object
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

    public static void deleteTask(Long id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        Task task = null;

        try {
            et = em.getTransaction();
            et.begin();
            task = em.find(Task.class, id);
            em.remove(task);
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

    //TODO: where to use it
    public static void cloeEntityManager(){
        ENTITY_MANAGER_FACTORY.close();
    }

}
