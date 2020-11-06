import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

public class TasksController {

        private MainView mainView;
        private TasksDBConnector tasksDBConnector;
        private TasksController tasksController;
        

    public TasksController() {

        this.tasksDBConnector = new TasksDBConnector();
        this.tasksController = this;

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

// Action listeners for add task
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
// Add action listeners for add task
        mainView.getAddTaskTextField().addMouseListener(addTaskClicked);
        mainView.getAddTaskTextField().addKeyListener(addTaskEnter);

        }

// Downloads ToDoTasks from DB via connector, sets layout checkbox and text, adds task to tasksList to display them
// Makes action listener for task marked as done
        public void refreshToDoTaskList(){
//        Clears list of tasks so there will be no duplicates
            mainView.getTasksToDoJPanelsList().clear();

            for (Task taskToDoInList : tasksDBConnector.getToDoTasks()) {
                GridBagLayout gridBagLayout = new GridBagLayout();
                GridBagConstraints c = new GridBagConstraints();

                JPanel tmpPanel = new JPanel();
                tmpPanel.setLayout(gridBagLayout);

                JCheckBox tmpCheckBox = new JCheckBox();

// If checkbox marked calls makeTaskDone
                    tmpCheckBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JCheckBox cb = (JCheckBox) e.getSource();
                            if (cb.isSelected()) {

                                makeTaskDone(taskToDoInList.getTaskId());

                            }
                        }
                    });

                c.weightx = 0.1;
                c.gridx = 0;
                c.gridy = 0;
                tmpPanel.add(tmpCheckBox, c);

                JTextField tmpJTextField = new JTextField(taskToDoInList.getTaskName());

                    tmpJTextField.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (e.getButton() == MouseEvent.BUTTON3) {
//                            TODO: is it good idea to call it recursively?
                                RightClickMouseMenu rightClickMouseMenu = new RightClickMouseMenu(taskToDoInList, tasksController);
                                rightClickMouseMenu.show(e.getComponent(), e.getX(), e.getY());
                            } else if (e.getButton() == MouseEvent.BUTTON1) {
                                mainView.getTaskNameLabel().setText(taskToDoInList.getTaskName());
                                mainView.getTaskImportanceLabel().setText(String.valueOf(taskToDoInList.getImportance()));
                                mainView.getTaskReminderDate().setText(String.valueOf(taskToDoInList.getReminderDate()));
                                mainView.getTaskNoteTexrArea().setText(taskToDoInList.getNote());
//                            TODO: make this not change size of frame
//                            mainView.getTaskCreatedDate().setText(String.valueOf(taskToDoInList.getCreationDate()));
                            }
                        }
                    });

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

// Downloads DoneTasks from DB via connector, sets layout checkbox and text, adds task to tasksList to display them
    public void refreshDoneTaskList(){
//        Clears list of tasks so there will be no duplicates
        mainView.getTasksDoneJPanelsList().clear();

//        Creates custom font type for Strikethrough text
        Font font = new Font("arial", Font.PLAIN, 12);
        Map fontAttr = font.getAttributes();
        fontAttr.put (TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        Font myFont = new Font(fontAttr);

        for (Task tasksDoneInList : tasksDBConnector.getDoneTasks()) {
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            JPanel tmpPanel = new JPanel();
            tmpPanel.setLayout(gridBagLayout);

            JTextField tmpJTextField = new JTextField(tasksDoneInList.getTaskName());

//          Make strikethrough text
            tmpJTextField.setFont(myFont);

                tmpJTextField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON3) {
//                        TODO: is it good idea to call it recursively?
                            RightClickMouseMenu rightClickMouseMenu = new RightClickMouseMenu(tasksDoneInList, tasksController);
                            rightClickMouseMenu.show(e.getComponent(), e.getX(), e.getY());
//                            refreshDoneTaskList();
                            mainView.revaluateDoneList();
                        } else if (e.getButton() == MouseEvent.BUTTON1) {
                            mainView.getTaskNameLabel().setText(tasksDoneInList.getTaskName());
                            mainView.getTaskImportanceLabel().setText(String.valueOf(tasksDoneInList.getImportance()));
                            mainView.getTaskReminderDate().setText(String.valueOf(tasksDoneInList.getReminderDate()));
                            mainView.getTaskNoteTexrArea().setText(tasksDoneInList.getNote());
//                            TODO: make this not change size of frame
//                        mainView.getTaskCreatedDate().setText(String.valueOf(tasksDoneInList.getCreationDate()));
                        }
                    }
                });

//  Task values changers
                mainView.getTaskNameLabel().addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        if (!mainView.getTaskNameLabel().getText().equals(tasksDoneInList.getTaskName())) {
                            tasksDBConnector.changeTaskName(tasksDoneInList.getTaskId(), mainView.getTaskNameLabel().getText());
//                            refreshDoneTaskList();
                            mainView.revaluateDoneList();
                        }
                    }
                });
                mainView.getTaskImportanceLabel().addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
//                    TODO: Limit range of importances
                        if (Integer.valueOf(mainView.getTaskImportanceLabel().getText()).equals(tasksDoneInList.getImportance())) {
                            try {
                                tasksDBConnector.changeTaskImportance(tasksDoneInList.getTaskId(), Integer.valueOf(mainView.getTaskImportanceLabel().getText()));
//                                refreshDoneTaskList();
                                mainView.revaluateDoneList();
                            } catch (NumberFormatException nfe) {
                                System.out.println("Wrong valye have been passed as an importance");
                            }
                        }
                    }
                });
//            mainView.getTaskReminderDate().addFocusListener(new FocusAdapter() {
//                @Override
//                public void focusLost(FocusEvent e) {
//                    if (!mainView.getTaskNameLabel().equals(tasksDoneInList.getTaskName())){
//                        tasksDBConnector.changeTaskName(tasksDoneInList.getTaskId(), mainView.getTaskNameLabel().getText());
//                        refreshDoneTasksWithoutListeners();
//                    }
//                }
//            });
//            mainView.getTaskNoteTexrArea().addFocusListener(new FocusAdapter() {
//                @Override
//                public void focusLost(FocusEvent e) {
//                    if (!mainView.getTaskNoteTexrArea().getText().equals(tasksDoneInList.getNote())){
//                        tasksDBConnector.changeTaskNote(tasksDoneInList.getTaskId(), mainView.getTaskNoteTexrArea().getText());
//                        refreshDoneTaskList();
//                    }
//                }
//            });
//            mainView.getTaskNoteTexrArea().addPropertyChangeListener(new PropertyChangeListener() {
//                @Override
//                public void propertyChange(PropertyChangeEvent evt) {
//                    if (!mainView.getTaskNoteTexrArea().getText().equals(tasksDoneInList.getNote())){
//                        tasksDBConnector.changeTaskNote(tasksDoneInList.getTaskId(), mainView.getTaskNoteTexrArea().getText());
//                        refreshDoneTaskList();
//                        System.out.println("Changed");
//                    }
//                }
//            });

                mainView.getTaskNoteTexrArea().getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        if (!mainView.getTaskNoteTexrArea().getText().equals(tasksDoneInList.getNote())) {
                            tasksDBConnector.changeTaskNote(tasksDoneInList.getTaskId(), mainView.getTaskNoteTexrArea().getText());
//                            refreshDoneTaskList();
                            mainView.revaluateDoneList();
                            System.out.println("Changed");
                        }
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        if (!mainView.getTaskNoteTexrArea().getText().equals(tasksDoneInList.getNote())) {
                            tasksDBConnector.changeTaskNote(tasksDoneInList.getTaskId(), mainView.getTaskNoteTexrArea().getText());
//                            refreshDoneTaskList();
                            mainView.revaluateDoneList();
                            System.out.println("Changed");
                        }
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        if (!mainView.getTaskNoteTexrArea().getText().equals(tasksDoneInList.getNote())) {
                            tasksDBConnector.changeTaskNote(tasksDoneInList.getTaskId(), mainView.getTaskNoteTexrArea().getText());
//                            refreshDoneTaskList();
                            mainView.revaluateDoneList();
                            System.out.println("Changed");
                        }
                    }
                });

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

    public void refreshDoneTasksWithoutListeners(){

        //        Clears list of tasks so there will be no duplicates
        mainView.getTasksDoneJPanelsList().clear();

//        Creates custom font type for Strikethrough text
        Font font = new Font("arial", Font.PLAIN, 12);
        Map fontAttr = font.getAttributes();
        fontAttr.put (TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        Font myFont = new Font(fontAttr);

        for (Task tasksDoneInList : tasksDBConnector.getDoneTasks()) {
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            JPanel tmpPanel = new JPanel();
            tmpPanel.setLayout(gridBagLayout);

            JTextField tmpJTextField = new JTextField(tasksDoneInList.getTaskName());

//          Make strikethrough text
            tmpJTextField.setFont(myFont);

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

    public void makeTaskDone(Long taskId){
        tasksDBConnector.makeTaskDone(taskId);
        refreshToDoTaskList();
        refreshDoneTaskList();
    }

}
