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

        boolean noteAreaClicked = false;
        Task currentlyChosenTask = null;

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
            t1.join(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();

//        =============

//      Creates and adds listeners to labels with info about tasks
        addLabelListeners();

        mainView.getFrame().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainView.getFrame().requestFocus();
            }
        });


//        ===========


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

//          Sets info about notes in view or removes task
            tmpJTextField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
//                            TODO: is it good idea to call it recursively?
                        RightClickMouseMenu rightClickMouseMenu = new RightClickMouseMenu(toDoTask, tasksController);
                        rightClickMouseMenu.show(e.getComponent(), e.getX(), e.getY());
                    } else if (e.getButton() == MouseEvent.BUTTON1) {
                        currentlyChosenTask = toDoTask;
                        displayLabels();
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

//          Sets info about notes in view or removes task
            tmpJTextField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
//                        TODO: is it good idea to call it recursively?
                        RightClickMouseMenu rightClickMouseMenu = new RightClickMouseMenu(doneTask, tasksController);
                        rightClickMouseMenu.show(e.getComponent(), e.getX(), e.getY());
                    } else if (e.getButton() == MouseEvent.BUTTON1) {
                        currentlyChosenTask = doneTask;
                        displayLabels();

//                            TODO: make this not change size of frame
//                        mainView.getTaskCreatedDate().setText(String.valueOf(tasksDoneInList.getCreationDate()));
                    }
                }
            });
//
//            mainView.getTaskNoteTexrArea().addKeyListener(new KeyAdapter() {
//                @Override
//                public void keyPressed(KeyEvent e) {
//                    if (e.getKeyCode() == KeyEvent.VK_ENTER && mainView.getTaskNoteTexrArea().isEnabled()) {
//                        updateNote(doneTask.getTaskId(), mainView.getTaskNoteTexrArea().getText());
//                    }
//                }
//            });


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

    public void updateNote(Long taskId, String noteText){
        if (taskId != null) {
            tasksDBConnector.changeTaskNote(taskId, noteText);
            addAllToDoTasksToView();
            addAllDoneTasksToView();
        }
    }

    public void changeTaskName(Long taskId, String newName){
        tasksDBConnector.changeTaskName(taskId, newName);
        addAllToDoTasksToView();
        addAllDoneTasksToView();
    }

    public void changeTaskImportance(Long taskId, String newImportance){
        try {
            int tmp = Integer.valueOf(newImportance);
            if (tmp >= 1 && tmp <= 10) {
                tasksDBConnector.changeTaskImportance(taskId, tmp);
                addAllToDoTasksToView();
                addAllDoneTasksToView();
            }
        } catch (NumberFormatException e) {

        }
    }

    public void addLabelListeners() {
        DeferredDocumentListener noteListener = new DeferredDocumentListener(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentlyChosenTask != null) {
                    updateNote(currentlyChosenTask.getTaskId(), mainView.getTaskNoteTexrArea().getText());
                }
            }
        }, true);

        mainView.getTaskNoteTexrArea().getDocument().addDocumentListener(noteListener);
        mainView.getTaskNoteTexrArea().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                noteListener.start();
            }

            @Override
            public void focusLost(FocusEvent e) {
                noteListener.stop();
            }
        });

        mainView.getTaskNameLabel().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                changeTaskName(currentlyChosenTask.getTaskId(), mainView.getTaskNameLabel().getText());
                mainView.getTaskNameLabel().transferFocus();
            }
        });

        mainView.getTaskImportanceLabel().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                changeTaskImportance(currentlyChosenTask.getTaskId(), mainView.getTaskImportanceLabel().getText());
            }
        });
    }

        public void displayLabels(){
            mainView.getTaskNameLabel().setText(currentlyChosenTask.getTaskName());
            mainView.getTaskImportanceLabel().setText(String.valueOf(currentlyChosenTask.getImportance()));
            mainView.getTaskReminderDate().setText(String.valueOf(currentlyChosenTask.getReminderDate()));
            mainView.getTaskNoteTexrArea().setText(currentlyChosenTask.getNote());
            mainView.getTaskCreatedDate().setText(String.valueOf(currentlyChosenTask.getCreationDate()));
            addAllToDoTasksToView();
            addAllDoneTasksToView();
        }


}
