import javax.persistence.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Date;
import java.util.List;

public class TasksController {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("To-Do-Reminder-App-1");

        private MainView mainView;

    public TasksController() {

        Thread t1 = new Thread(() -> {
            mainView = new MainView();
            });


        Thread t2 = new Thread(() -> {
            if (getTasks() != null) {
                refreshTaskLists();
            }
        });

        t1.start();
        try {
            t1.join(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();

        MouseListener addTaskClicked = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainView.getAddTaskTextField().requestFocus();
                mainView.getAddTaskTextField().setText("");
                mainView.getAddTaskTextField().setEditable(true);
            }
        };

        KeyAdapter addTaskEnter = new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if (e.getKeyCode() == KeyEvent.VK_ENTER && mainView.getAddTaskTextField().isEnabled()){
                    addTask(1, mainView.getAddTaskTextField().getText());
                    mainView.getAddTaskTextField().setText("+Add new task");
                    mainView.getAddTaskTextField().setEditable(false);
                    refreshTaskLists();
                }
            }
        };

        mainView.getAddTaskTextField().addMouseListener(addTaskClicked);
        mainView.getAddTaskTextField().addKeyListener(addTaskEnter);

        }

//      Downloads tasks from DB and refreshes lists in the view
        public void refreshTaskLists(){
            mainView.getTasksList().clear();
            for (Task taskInList : getTasks()) {
                mainView.getTasksList().add(new JTextField(taskInList.getTaskName()));
                mainView.revaluateLists();
            }
        }

    public void createView(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    mainView = new MainView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

//TODO: where to use it
    public static void cloeEntityManager(){
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

    public static List<Task> getTasks() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        // the lowercase c refers to the object
        // :custID is a parameterized query thats value is set below
        String strQuery = "SELECT t FROM Task t WHERE t.id IS NOT NULL";

        // Issue the query and get a matching Customer
        TypedQuery<Task> tq = em.createQuery(strQuery, Task.class);
        List<Task> tasks = null;
        try {
            // Get matching customer object and output
            tasks = tq.getResultList();
            tasks.forEach(task->System.out.println(task.toString()));
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

    public static void deleteTask(int id) {
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

}
