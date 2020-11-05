import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TasksController {

        private MainView mainView;
        private TasksDBConnector tasksDBConnector;

    public TasksController() {

        tasksDBConnector = new TasksDBConnector();

        Thread t1 = new Thread(() -> {
            mainView = new MainView();
            });

        Thread t2 = new Thread(() -> {
            if (tasksDBConnector.getToDoTasks() != null) {
                refreshToDoTaskList();
            }
            if (tasksDBConnector.getDoneTasks() != null) {
                refreshDoneTaskList();
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
                    tasksDBConnector.addTask(1, mainView.getAddTaskTextField().getText());
                    mainView.getAddTaskTextField().setText("+Add new task");
                    mainView.getAddTaskTextField().setEditable(false);
                    refreshToDoTaskList();
                }
            }
        };

        mainView.getAddTaskTextField().addMouseListener(addTaskClicked);
        mainView.getAddTaskTextField().addKeyListener(addTaskEnter);

        }

//      Downloads tasks from DB and refreshes lists in the view
        public void refreshToDoTaskList(){
            mainView.getTasksToDoJPanelsList().clear();

            for (Task taskToDoInList : tasksDBConnector.getToDoTasks()) {
                GridBagLayout gridBagLayout = new GridBagLayout();
                GridBagConstraints c = new GridBagConstraints();

                JPanel tmpPanel = new JPanel();
                tmpPanel.setLayout(gridBagLayout);

                JCheckBox tmpCheckBox = new JCheckBox();
                c.weightx = 0.1;
                c.gridx = 0;
                c.gridy = 0;
                tmpPanel.add(tmpCheckBox, c);

                JTextField tmpJTextField = new JTextField(taskToDoInList.getTaskName());
                tmpJTextField.setBorder(null);
                tmpJTextField.setEditable(false);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 0.8;
                c.gridwidth = 1;
                c.gridx = 1;
                c.gridy = 0;
                tmpPanel.add(tmpJTextField, c);

                mainView.getTasksToDoJPanelsList().add(tmpPanel);
            }
            mainView.revaluateToDoList();
        }

        //      Downloads tasks from DB and refreshes lists in the view
    public void refreshDoneTaskList(){
        mainView.getTasksDoneJPanelsList().clear();

        for (Task tasksDoneInList : tasksDBConnector.getDoneTasks()) {
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            JPanel tmpPanel = new JPanel();
            tmpPanel.setLayout(gridBagLayout);

            JCheckBox tmpCheckBox = new JCheckBox();
            c.weightx = 0.1;
            c.gridx = 0;
            c.gridy = 0;
            tmpPanel.add(tmpCheckBox, c);

            JTextField tmpJTextField = new JTextField(tasksDoneInList.getTaskName());
            tmpJTextField.setBorder(null);
            tmpJTextField.setEditable(false);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.8;
            c.gridwidth = 1;
            c.gridx = 1;
            c.gridy = 0;
            tmpPanel.add(tmpJTextField, c);

            mainView.getTasksDoneJPanelsList().add(tmpPanel);
        }
        mainView.revaluateDoneList();
    }

//        public Void taskDone(){
//
//        }

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



}
