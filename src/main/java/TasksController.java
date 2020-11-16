import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class TasksController {

        private MainView mainView;
        private TasksDBConnector tasksDBConnector;
        private TasksController tasksController;

        boolean noteAreaClicked = false;
        Task currentlyChosenTask = null;
        String sorByWhat = "taskId";
        SimpleDateFormat dataFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        private LinkedHashMap<Long, Task> tasksToDoDict, tasksDoneDict;
        private LinkedHashMap<Long, JPanel> tasksToDoJPanelDict, tasksDoneJPanelDict;

    public TasksController() {

        this.tasksDBConnector = new TasksDBConnector();
        this.tasksController = this;
        this.tasksToDoDict = new LinkedHashMap<>();
        this.tasksDoneDict = new LinkedHashMap<>();
        this.tasksToDoJPanelDict = new LinkedHashMap<>();
        this.tasksDoneJPanelDict = new LinkedHashMap<>();


        Thread t1 = new Thread(() -> {
            mainView = new MainView();
            });

        Thread t2 = new Thread(() -> {
            if (tasksDBConnector.getTasks("taskId", 0) != null) {
                addAllToDoTasksToView(sorByWhat, 0);
            }
            if (tasksDBConnector.getTasks("taskId", 1) != null) {
                addAllDoneTasksToView(sorByWhat, 1);
            }
        });

        t1.start();
        try {
            t1.join(900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();

//        =============

//      Creates and adds listeners to labels with info about tasks
        addLabelsListeners();

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
    public void putToDoTasksToDict(String sortBy, int taskType) {
        this.tasksToDoDict = new LinkedHashMap<>();
        for (Task taskToDoFromDB : tasksDBConnector.getTasks(sortBy, taskType)) {
//            System.out.println("task id :"+taskToDoFromDB.getTaskId());
            tasksToDoDict.put(taskToDoFromDB.getTaskId(), taskToDoFromDB);
        }
    }

// Add tasks done from DB to dict
    public void putDoneTasksToDict(String sortBy, int taskType) {
        this.tasksDoneDict = new LinkedHashMap<>();
        for (Task taskDoneFromDB : tasksDBConnector.getTasks(sortBy, taskType)) {
            tasksDoneDict.put(taskDoneFromDB.getTaskId(), taskDoneFromDB);
        }
    }

//    Copies all elements from ToDoTasksDict to JPanel Dict
public void copyToDoTasksFromDictToJPanelDict() {
    this.tasksToDoJPanelDict = new LinkedHashMap<>();
    for (Map.Entry<Long, Task> toDoTask : tasksToDoDict.entrySet())
//    for (Enumeration i = tasksToDoDict.elements(); i.hasMoreElements();) {
//        Task tmp = (Task) i.nextElement();
//        System.out.println("task id :"+tmp.getTaskId());
        tasksToDoJPanelDict.put(toDoTask.getKey(), createToDoTaskPanel(toDoTask.getValue()));
    }

//    Copies all elements from DoneTasksDict to JPanel Dict
public void copyDonneTasksFromDictToJPanelDict() {
    this.tasksDoneJPanelDict = new LinkedHashMap<>();
    for (Map.Entry<Long, Task> doneTask : tasksDoneDict.entrySet())
//    for (Enumeration i = tasksDoneDict.elements(); i.hasMoreElements();) {
//        Task tmp = (Task) i.nextElement();
        tasksDoneJPanelDict.put(doneTask.getKey(), createDoneTasksPanel(doneTask.getValue()));
    }


    public void addAllToDoTasksToView(String sortBy, int taskType) {
        putToDoTasksToDict(sortBy, taskType);
        copyToDoTasksFromDictToJPanelDict();
        mainView.getTasksToDoJPanelsList().clear();
        for (Map.Entry<Long, JPanel> jpanelToDoTask : tasksToDoJPanelDict.entrySet()){
            mainView.getTasksToDoJPanelsList().add(jpanelToDoTask.getValue());
            System.out.println(jpanelToDoTask.getKey());
        }
        mainView.revaluateToDoList();
    }

    public void addAllDoneTasksToView(String sortBy, int taskType) {
        putDoneTasksToDict(sortBy, taskType);
        copyDonneTasksFromDictToJPanelDict();
        mainView.getTasksDoneJPanelsList().clear();
        for (Map.Entry<Long, JPanel> jpanelDoneTask : tasksDoneJPanelDict.entrySet()){
            mainView.getTasksDoneJPanelsList().add(jpanelDoneTask.getValue());
        }
        mainView.revaluateDoneList();
    }

    public JPanel createToDoTaskPanel(Task toDoTask){

            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            JPanel tmpPanel = new JPanel();
            tmpPanel.setLayout(gridBagLayout);

            JCheckBox tmpCheckBox = new JCheckBox();
            tmpCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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
            tmpJTextField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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
            tmpJTextField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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
            addAllToDoTasksToView(sorByWhat, 0);
        }

    public void makeTaskDone(Long taskId){
//                      Change data for this element in DB
        tasksDBConnector.makeTaskDone(taskId);
        addAllToDoTasksToView(sorByWhat, 0);
        addAllDoneTasksToView(sorByWhat, 1);
    }

    public void removeTask(Long taskId){
        tasksDBConnector.deleteTask(taskId);
        addAllToDoTasksToView(sorByWhat, 0);
        addAllDoneTasksToView(sorByWhat, 1);
    }

    public void updateNote(Long taskId, String noteText){
        if (taskId != null) {
            tasksDBConnector.changeTaskNote(taskId, noteText);
            addAllToDoTasksToView(sorByWhat, 0);
            addAllDoneTasksToView(sorByWhat, 1);
        }
    }

    public void changeTaskName(Long taskId, String newName){
        tasksDBConnector.changeTaskName(taskId, newName);
        addAllToDoTasksToView(sorByWhat, 0);
        addAllDoneTasksToView(sorByWhat, 1);
    }

    public void changeTaskImportance(Long taskId, String newImportance){
        try {
            int tmp = 0;
            tmp = Integer.valueOf(newImportance);
            if (tmp >= 1 && tmp <= 10) {
                tasksDBConnector.changeTaskImportance(taskId, tmp);
                addAllToDoTasksToView(sorByWhat, 0);
                addAllDoneTasksToView(sorByWhat, 1);
            }
        } catch (NumberFormatException e) {

        }
    }

    public void addLabelsListeners() {
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


        mainView.getSortByWhat().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    sortTasks();
                    }
            }
        });

        mainView.getTaskNameLabel().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (currentlyChosenTask.getTaskId() != null) {
                    changeTaskName(currentlyChosenTask.getTaskId(), mainView.getTaskNameLabel().getText());
                    mainView.getTaskNameLabel().transferFocus();
                }
            }
        });

        mainView.getTaskImportanceLabel().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (currentlyChosenTask.getTaskId() != null) {
                    changeTaskImportance(currentlyChosenTask.getTaskId(), mainView.getTaskImportanceLabel().getText());
                }
            }
        });

        mainView.getTaskReminderDate().getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                    Date date = (Date) mainView.getTaskReminderDate().getModel().getValue();
                    System.out.println(date);
                    changeReminderDate(currentlyChosenTask.getTaskId(), date);

//                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
//
//
//                    System.out.println(date);
//                    System.out.println(date);

            }
        });

    }


        public void displayLabels(){
            mainView.getTaskNameLabel().setText(currentlyChosenTask.getTaskName());
            mainView.getTaskImportanceLabel().setText(String.valueOf(currentlyChosenTask.getImportance()));
//            if (currentlyChosenTask.getReminderDate() != null) {
//                mainView.getTaskReminderDate().getModel().setValue(currentlyChosenTask.getReminderDate());
//            }
            mainView.getTaskNoteTexrArea().setText(currentlyChosenTask.getNote());
            mainView.getTaskCreatedDate().setText(dataFormatter.format(currentlyChosenTask.getCreationDate()));
            addAllToDoTasksToView(sorByWhat, 0);
            addAllDoneTasksToView(sorByWhat, 1);
        }

        public void changeReminderDate(Long taskId, Date reminderDate){
            tasksDBConnector.changeReminderDate(taskId, reminderDate);
            addAllToDoTasksToView(sorByWhat, 0);
            addAllDoneTasksToView(sorByWhat, 1);
        }

        public void sortTasks() {
            switch (sorByWhat) {
                case "taskId":
                    mainView.getSortByWhat().setText("Name");
                    sorByWhat = "taskName";
                    addAllToDoTasksToView(sorByWhat, 0);
                    addAllDoneTasksToView(sorByWhat, 1);
                    break;
                case "taskName":
                    mainView.getSortByWhat().setText("Importance");
                    sorByWhat = "taskImportance";
                    addAllToDoTasksToView(sorByWhat, 0);
                    addAllDoneTasksToView(sorByWhat, 1);
                    break;
                case "taskImportance":
                    mainView.getSortByWhat().setText("Id");
                    sorByWhat = "taskId";
                    addAllToDoTasksToView(sorByWhat, 0);
                    addAllDoneTasksToView(sorByWhat, 1);
                    break;
            }
        }


    public void undoneTask(Long taskId) {
        tasksDBConnector.undoneTask(taskId);
        addAllToDoTasksToView(sorByWhat, 0);
        addAllDoneTasksToView(sorByWhat, 1);
    }


    public MainView getMainView() {
        return mainView;
    }

}
