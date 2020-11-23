package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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


//      Creates thread for MainView()
        Thread t1 = new Thread(() -> {
            mainView = new MainView();
            });

//      Creates thread for db connector
        Thread t2 = new Thread(() -> {
            if (tasksDBConnector.getTasks("taskId", 0) != null) {
                addAllToDoTasksToView(sorByWhat, 0);
            }
            if (tasksDBConnector.getTasks("taskId", 1) != null) {
                addAllDoneTasksToView(sorByWhat, 1);
            }
        });


//      Start MainView thread and makes it wait until db connector is established
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


// Declares action listeners for add task button
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

// Loads tasksToDo from DB to dict
    public void putToDoTasksToDict(String sortBy, int taskType) {
        this.tasksToDoDict = new LinkedHashMap<>();
        for (Task taskToDoFromDB : tasksDBConnector.getTasks(sortBy, taskType))
            tasksToDoDict.put(taskToDoFromDB.getTaskId(), taskToDoFromDB);
    }

//  Loads tasks done from DB to dict
    public void putDoneTasksToDict(String sortBy, int taskType) {
        this.tasksDoneDict = new LinkedHashMap<>();
        for (Task taskDoneFromDB : tasksDBConnector.getTasks(sortBy, taskType))
            tasksDoneDict.put(taskDoneFromDB.getTaskId(), taskDoneFromDB);
    }

//  Copies all elements from ToDoTasksDict to JPanel Dict
    public void copyToDoTasksFromDictToJPanelDict() {
        this.tasksToDoJPanelDict = new LinkedHashMap<>();
        for (Map.Entry<Long, Task> toDoTask : tasksToDoDict.entrySet())
            tasksToDoJPanelDict.put(toDoTask.getKey(), createToDoTaskPanel(toDoTask.getValue()));
    }

//  Copies all elements from DoneTasksDict to JPanel Dict
    public void copyDoneTasksFromDictToJPanelDict() {
        this.tasksDoneJPanelDict = new LinkedHashMap<>();
        for (Map.Entry<Long, Task> doneTask : tasksDoneDict.entrySet())
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
        copyDoneTasksFromDictToJPanelDict();
        mainView.getTasksDoneJPanelsList().clear();
        for (Map.Entry<Long, JPanel> jpanelDoneTask : tasksDoneJPanelDict.entrySet()){
            mainView.getTasksDoneJPanelsList().add(jpanelDoneTask.getValue());
        }
        mainView.revaluateDoneList();
    }

//  Creates layout for tasks panel
//  with style formatting and listeners
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
                        RightClickMouseMenu rightClickMouseMenu = new RightClickMouseMenu(toDoTask, tasksController);
                        rightClickMouseMenu.show(e.getComponent(), e.getX(), e.getY());
                    } else if (e.getButton() == MouseEvent.BUTTON1) {
                        currentlyChosenTask = toDoTask;
                        displayLabels();
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


//  Creates layout for tasks panel
//  with style formatting and listeners
    public JPanel createDoneTasksPanel(Task doneTask){
//      Downloads DoneTasks from DB via connector, sets layout checkbox and text, adds task to tasksList to display them

//      Creates custom font type for Strikethrough text
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
//      Add task to the DB and in the  view
        tasksDBConnector.addTask(1, taskName);
//      Clears ToDoDict and adds task there
//      Adds all elements from dict to JPanel
//      For each JPanel element displays it with design
        addAllToDoTasksToView(sorByWhat, 0);
    }

//  Marks taskId element as done in DB and in the  view
    public void makeTaskDone(Long taskId){
        tasksDBConnector.makeTaskDone(taskId);
        addAllToDoTasksToView(sorByWhat, 0);
        addAllDoneTasksToView(sorByWhat, 1);
    }

//  Removes taskId element from DB and in the  view
    public void removeTask(Long taskId){
        tasksDBConnector.deleteTask(taskId);
        addAllToDoTasksToView(sorByWhat, 0);
        addAllDoneTasksToView(sorByWhat, 1);
    }

//  Updates taskId element note in DB and in the view
    public void updateNote(Long taskId, String noteText){
        if (taskId != null) {
            tasksDBConnector.changeTaskNote(taskId, noteText);
            addAllToDoTasksToView(sorByWhat, 0);
            addAllDoneTasksToView(sorByWhat, 1);
        }
    }

//  Changes taskId element name in DB and in the view
    public void changeTaskName(Long taskId, String newName){
        tasksDBConnector.changeTaskName(taskId, newName);
        addAllToDoTasksToView(sorByWhat, 0);
        addAllDoneTasksToView(sorByWhat, 1);
    }

//  Changes taskId element importance level in DB and in the view
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

//  Changes taskId element reminder date in DB and in the view
    public void changeTaskReminderDate(String date){
        try {
            Date tmpDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            tasksDBConnector.changeReminderDate(currentlyChosenTask.getTaskId(), tmpDate);

            addAllToDoTasksToView(sorByWhat, 0);
            addAllDoneTasksToView(sorByWhat, 1);

            displayLabels();

        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

    }

//  Adds listeners to the view labels
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

        mainView.getTaskReminderDate().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                    new DatePicker(tasksController);

//                mainView.getTaskReminderDate().setText(currentlyChosenTask.getReminderDate());
            }
        });
    }


        public void displayLabels(){
            mainView.getTaskNameLabel().setText(currentlyChosenTask.getTaskName());
            mainView.getTaskImportanceLabel().setText(String.valueOf(currentlyChosenTask.getImportance()));
//            if (currentlyChosenTask.getReminderDate() != null) {
            mainView.getTaskReminderDate().setText(currentlyChosenTask.getReminderDate());
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

    public Task getCurrentlyChosenTask() {
        return currentlyChosenTask;
    }
}
