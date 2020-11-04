import javax.persistence.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {

    // Create an EntityManagerFactory when you start the application

    public static void main(String[] args) {

        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }


            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        createAndShowGUI();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        public static void createAndShowGUI() throws Exception {
            new MainView();
        }


//        addTask( 1, "testTask1");
//        addTask(1, "testTask2");
//        addTask(1, "testTask3");
//
////        getTask(1L);
////        getTask(2L);
////        getTask(3L);
//
//        getTasks();


    }