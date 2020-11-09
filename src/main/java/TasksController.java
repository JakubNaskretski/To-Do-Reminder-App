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
import java.util.*;

public class TasksController {

        private MainView mainView;
        private TasksDBConnector tasksDBConnector;
        private TasksController tasksController;

        private Dictionary<Long, Task> tasksToDoDict, tasksDoneDict;
        private Dictionary<Long, JPanel> tasksToDoJPanelDict, tasksDoneJPanelDict;

    public TasksController() {

        this.tasksDBConnector = new TasksDBConnector();
        this.tasksController = this;
        this.tasksToDoDict = new Hashtable<>();
        this.tasksDoneDict = new Hashtable<>();
        this.tasksToDoJPanelDict = new Hashtable<>();
        this.tasksDoneJPanelDict = new Hashtable<>();


        Thread t1 = new Thread(() -> {
            mainView = new MainView();
            });

        Thread t2 = new Thread(() -> {
            if (tasksDBConnector.getToDoTasks() != null) {
                addAllToDoTasksToView();
            }
            if (tasksDBConnector.getDoneTasks() != null) {
                addAllDoneTasksToView();
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
                    addTask(mainView.getAddTaskTextField().getText());
                    mainView.getAddTaskTextField().setText("+Add new task");
                    mainView.getAddTaskTextField().setEditable(false);
                }
            }
        };
// Add action listeners for add task
        mainView.getAddTaskTextField().addMouseListener(addTaskClicked);
        mainView.getAddTaskTextField().addKeyListener(addTaskEnter);
        }

// Add tasksToDo from DB to dict
    public void putToDoTasksToDict() {
        this.tasksToDoDict = new Hashtable<>();
        for (Task taskToDoFromDB : tasksDBConnector.getToDoTasks()) {
            tasksToDoDict.put(taskToDoFromDB.getTaskId(), taskToDoFromDB);
        }
    }

// Add tasks done from DB to dict
    public void putDoneTasksToDict() {
        this.tasksDoneDict = new Hashtable<>();
        for (Task taskDoneFromDB : tasksDBConnector.getDoneTasks()) {
            tasksDoneDict.put(taskDoneFromDB.getTaskId(), taskDoneFromDB);
        }
    }

//    Copies all elements from ToDoTasksDict to JPanel Dict
public void copyToDoTasksFromDictToJPanelDict() {
    this.tasksToDoJPanelDict = new Hashtable<>();
    for (Enumeration i = tasksToDoDict.elements(); i.hasMoreElements();) {
        Task tmp = (Task) i.nextElement();
        tasksToDoJPanelDict.put(tmp.getTaskId(), createToDoTaskPanel(tmp));
        System.out.println("Task ID for test: "+tasksToDoDict.elements().nextElement().getTaskId());
    }
}

//    Copies all elements from DoneTasksDict to JPanel Dict
public void copyDonneTasksFromDictToJPanelDict() {
    this.tasksDoneJPanelDict = new Hashtable<>();
    for (Enumeration i = tasksDoneDict.elements(); i.hasMoreElements();) {
        Task tmp = (Task) i.nextElement();
        tasksDoneJPanelDict.put(tmp.getTaskId(), createDoneTasksPanel(tmp));
        System.out.println("Task ID for test: "+tasksDoneDict.elements().nextElement().getTaskId());
}}


    public void addAllToDoTasksToView() {
        putToDoTasksToDict();
        copyToDoTasksFromDictToJPanelDict();
        mainView.getTasksToDoJPanelsList().clear();
        for (Enumeration i = tasksToDoJPanelDict.elements(); i.hasMoreElements();) {
            mainView.getTasksToDoJPanelsList().add((JPanel) i.nextElement());
        }
        mainView.revaluateToDoList();
    }

    public void addAllDoneTasksToView() {
        putDoneTasksToDict();
        copyDonneTasksFromDictToJPanelDict();
        mainView.getTasksDoneJPanelsList().clear();
        for (Enumeration i = tasksDoneJPanelDict.elements(); i.hasMoreElements(); ) {
            mainView.getTasksDoneJPanelsList().add((JPanel) i.nextElement());
        }
        mainView.revaluateDoneList();
    }

    public JPanel createToDoTaskPanel(Task toDoTask){

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

                        makeTaskDone(toDoTask.getTaskId());

                    }
                }
            });

            c.weightx = 0.1;
            c.gridx = 0;
            c.gridy = 0;
            tmpPanel.add(tmpCheckBox, c);

            JTextField tmpJTextField = new JTextField(toDoTask.getTaskName());

            tmpJTextField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
//                            TODO: is it good idea to call it recursively?
                        RightClickMouseMenu rightClickMouseMenu = new RightClickMouseMenu(toDoTask, tasksController);
                        rightClickMouseMenu.show(e.getComponent(), e.getX(), e.getY());
                    } else if (e.getButton() == MouseEvent.BUTTON1) {
                        mainView.getTaskNameLabel().setText(toDoTask.getTaskName());
                        mainView.getTaskImportanceLabel().setText(String.valueOf(toDoTask.getImportance()));
                        mainView.getTaskReminderDate().setText(String.valueOf(toDoTask.getReminderDate()));
                        mainView.getTaskNoteTexrArea().setText(toDoTask.getNote());
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

            return tmpPanel;
        }

        public JPanel createDoneTasksPanel(Task doneTask){
// Downloads DoneTasks from DB via connector, sets layout checkbox and text, adds task to tasksList to display them
//        Clears list of tasks so there will be no duplicates

//        Creates custom font type for Strikethrough text
        Font font = new Font("arial", Font.PLAIN, 12);
        Map fontAttr = font.getAttributes();
        fontAttr.put (TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        Font myFont = new Font(fontAttr);

            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            JPanel tmpPanel = new JPanel();
            tmpPanel.setLayout(gridBagLayout);

            JTextField tmpJTextField = new JTextField(doneTask.getTaskName());

//          Make strikethrough text
            tmpJTextField.setFont(myFont);

            tmpJTextField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
//                        TODO: is it good idea to call it recursively?
                        RightClickMouseMenu rightClickMouseMenu = new RightClickMouseMenu(doneTask, tasksController);
                        rightClickMouseMenu.show(e.getComponent(), e.getX(), e.getY());
                        mainView.revaluateDoneList();
                    } else if (e.getButton() == MouseEvent.BUTTON1) {
                        mainView.getTaskNameLabel().setText(doneTask.getTaskName());
                        mainView.getTaskImportanceLabel().setText(String.valueOf(doneTask.getImportance()));
                        mainView.getTaskReminderDate().setText(String.valueOf(doneTask.getReminderDate()));
                        mainView.getTaskNoteTexrArea().setText(doneTask.getNote());
//                            TODO: make this not change size of frame
//                        mainView.getTaskCreatedDate().setText(String.valueOf(tasksDoneInList.getCreationDate()));
                    }
                }
            });

//  Task values changers
            mainView.getTaskNameLabel().addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    if (!mainView.getTaskNameLabel().getText().equals(doneTask.getTaskName())) {
                        tasksDBConnector.changeTaskName(doneTask.getTaskId(), mainView.getTaskNameLabel().getText());
//                            refreshDoneTaskList();
                        mainView.revaluateDoneList();
                    }
                }
            });
            mainView.getTaskImportanceLabel().addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
//                    TODO: Limit range of importances
                    if (Integer.valueOf(mainView.getTaskImportanceLabel().getText()).equals(doneTask.getImportance())) {
                        try {
                            tasksDBConnector.changeTaskImportance(doneTask.getTaskId(), Integer.valueOf(mainView.getTaskImportanceLabel().getText()));
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
                    if (!mainView.getTaskNoteTexrArea().getText().equals(doneTask.getNote())) {
                        tasksDBConnector.changeTaskNote(doneTask.getTaskId(), mainView.getTaskNoteTexrArea().getText());
//                            refreshDoneTaskList();
                        mainView.revaluateDoneList();
                        System.out.println("Changed");
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if (!mainView.getTaskNoteTexrArea().getText().equals(doneTask.getNote())) {
                        tasksDBConnector.changeTaskNote(doneTask.getTaskId(), mainView.getTaskNoteTexrArea().getText());
//                            refreshDoneTaskList();
                        mainView.revaluateDoneList();
                        System.out.println("Changed");
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    if (!mainView.getTaskNoteTexrArea().getText().equals(doneTask.getNote())) {
                        tasksDBConnector.changeTaskNote(doneTask.getTaskId(), mainView.getTaskNoteTexrArea().getText());
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

            return tmpPanel;
        }


        public void addTask(String taskName){
//        Add task to the DB
            tasksDBConnector.addTask(1, taskName);
//            Clears ToDoDict and adds task there
//            Adds all elements from dict to JPanel
//            For each JPanel element displays it with design
            addAllToDoTasksToView();
        }

    public void makeTaskDone(Long taskId){
//                      Change data for this element in DB
        tasksDBConnector.makeTaskDone(taskId);
        addAllToDoTasksToView();
        addAllDoneTasksToView();
    }

    public void removeTask(Long taskId){
        tasksDBConnector.deleteTask(taskId);
        addAllToDoTasksToView();
        addAllDoneTasksToView();
    }

    public void changeTaskName(Long taskId, String newName){

    }

}
