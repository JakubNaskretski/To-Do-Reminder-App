package com;

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

    public static Task getTask(long id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        // the lowercase t refers to the object
        // :taskId is a parameterized query thats value is set below
        String query = "SELECT t FROM Task t WHERE t.id = :taskId";

        // Issue the query and get a matching com.Task
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
        return task;
    }


//    com.Task sort by tasId, taskName, taskImportance
//    com.Task type type 0 - to do, 1 - done, 2 - all
    public static String generateGetTasksStringQuery(String sortBy, int taskType) {
        if (taskType == 3) {
            return "SELECT t FROM com.Task t WHERE t.id IS NOT NULL "+"ORDER BY "+ sortBy;
        }
        System.out.println("Querry string "+ "SELECT t FROM com.Task t WHERE t.id IS NOT NULL AND t.isTaskDone = " + taskType + " ORDER BY " + sortBy);
        return "SELECT t FROM com.Task t WHERE t.id IS NOT NULL AND t.isTaskDone = " + taskType + " ORDER BY " + sortBy;
    }

    public static List<Task> getTasks(String sortBy, int taskType) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        // the lowercase c refers to the object
        // :custID is a parameterized query thats value is set below
        String strQuery = generateGetTasksStringQuery(sortBy, taskType);

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




//    public static List<com.Task> getDoneTasks() {
//        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
//
//        // the lowercase c refers to the object
//        // :custID is a parameterized query thats value is set below
//        String strQuery = "SELECT t FROM com.Task t WHERE t.id IS NOT NULL AND t.isTaskDone = 1";
//
//        TypedQuery<com.Task> tq = em.createQuery(strQuery, com.Task.class);
//        List<com.Task> tasks = null;
//        try {
//            // Get matching customer object and output
//            tasks = tq.getResultList();
//        }
//        catch(NoResultException ex) {
//            ex.printStackTrace();
//        }
//        finally {
//            em.close();
//        }
//        return tasks;
//    }

    public static void changeTaskName(Long id, String taskName) {
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

    public static void changeTaskImportance(Long id, int Importance) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;

        Task task = null;

        try {
            // Get transaction and start
            et = em.getTransaction();
            et.begin();

            // Find customer and make changes
            task = em.find(Task.class, id);
            task.setImportance(Importance);

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

    public static void changeTaskNote(Long id, String note) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;

        Task task = null;

        try {
            // Get transaction and start
            et = em.getTransaction();
            et.begin();

            // Find customer and make changes
            task = em.find(Task.class, id);
            task.setNote(note);

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

    public void undoneTask(Long taskId) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;

        Task task = null;

        try {
            // Get transaction and start
            et = em.getTransaction();
            et.begin();

            // Find customer and make changes
            task = em.find(Task.class, taskId);
            task.setIsTaskDone(0);

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

    public void changeReminderDate(Long taskId, Date reminderDate) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;

        Task task = null;

        try {
            // Get transaction and start
            et = em.getTransaction();
            et.begin();

            // Find customer and make changes
            task = em.find(Task.class, taskId);
            task.setReminderDate(reminderDate);

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

}
