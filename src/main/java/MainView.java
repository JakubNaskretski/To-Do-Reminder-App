import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainView {

    private int screenHeight, screenWidth;

    private JFrame frame;
    private JPanel mainPanel, leftPanel, rightTopPanel, rightBottomPanel, tasksPanel, doneTasksPanel;
    private JScrollPane tasksScrollPanel, doneTasksScrollPanel;

    private Dimension blockDimensions, tasksBlockDimensions, taskDimensions;


    private TasksController tasksController;

    private JLabel appTittleLabel, sortByLabel, sortByWhat, doneLabel, taskNameLabel, taskImportanceLabel, taskReminderDate, taskCreatedDate;
    private List<JTextField> tasksList, doneTasksList;
    private JTextField task1, doneTask1, addTaskTextField;
    private JTextArea taskNoteTexrArea;

    public MainView() {

//        Getting screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenHeight = (int) screenSize.getHeight();
        this.screenWidth = (int) screenSize.getWidth();

//        Create controller
//        this.tasksController = new TasksController();

        this.tasksList = new ArrayList<>();
        this.doneTasksList = new ArrayList<JTextField>();

//        Create frame
        this.frame = new JFrame();
        frame.setSize(new Dimension((screenWidth / 5), (screenHeight / 3)));

        frame.setLocationRelativeTo(null);
        frame.setLocation(((screenWidth / 2) - (screenWidth / 4)), ((screenHeight / 2) - (screenHeight / 4)));

        this.tasksBlockDimensions = new Dimension(frame.getWidth() / 2, (frame.getHeight() / 10)*4);
        this.blockDimensions = new Dimension((int) (tasksBlockDimensions.getWidth()*0.8), ((int) (frame.getHeight() / 10)));
//        this.blockDimensions = new Dimension(screenWidth / 18, screenHeight / 15);
//        this.tasksBlockDimensions = new Dimension(screenWidth / 17, screenHeight / 9);

        addComponentsToThePanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("ToDo");


//        Display Frame
        frame.pack();
        frame.setVisible(true);
    }

    private void addComponentsToThePanel() {

        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

//Tittle
        this.appTittleLabel = new JLabel("ToDo");
        appTittleLabel.setFont(new Font("serif", Font.BOLD, 25));
//        appTittleLabel.setPreferredSize(blockDimensions);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 15, 10, 15);
        c.gridwidth = 6;
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(appTittleLabel, c);

        leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints lpc = new GridBagConstraints();


        //Tittle
        this.sortByLabel = new JLabel("Sort by");
//        sortByLabel.setPreferredSize(blockDimensions);
        lpc.fill = GridBagConstraints.HORIZONTAL;
        lpc.gridwidth = 1;
        lpc.gridx = 0;
        lpc.gridy = 0;
        leftPanel.add(sortByLabel, lpc);

        //Tittle
        this.sortByWhat = new JLabel("Importance");
        lpc.gridx = 1;
        leftPanel.add(sortByWhat, lpc);

//Creates panel for tasks sets and sets its layout
//      TODO: how to add each task
        this.tasksPanel = new JPanel();
        tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.PAGE_AXIS));
//        tasksPanel.setPreferredSize(new Dimension(100,50));
        tasksPanel.setBackground(Color.gray);
//
//        this.task1 = new JTextField("Task 1");
//        tasksList.add(task1);

////        TestTasks
//        JTextField testTextArea1 = new JTextField("Task 2");
//        tasksList.add(testTextArea1);
//
////        TestTasks
//        JTextField testTextArea2 = new JTextField("Task 3");
//        tasksList.add(testTextArea2);
//
//
////        TestTasks
//        JTextField testTextArea3 = new JTextField("Task 4");
//        tasksList.add(testTextArea3);
//
////        TestTasks
//        JTextField testTextArea4 = new JTextField("Task 5");
//        tasksList.add(testTextArea4);
//
////        TestTasks
//        JTextField testTextArea5 = new JTextField("Task 6");
//        tasksList.add(testTextArea5);
//
////        TestTasks
//        JTextField testTextArea6 = new JTextField("Task 7");
//        tasksList.add(testTextArea6);


//Iterates over list and add all tasks to the panel
        for (JTextField taskInList : tasksList) {
            tasksPanel.add(taskInList);
            taskInList.setEditable(false);
            taskInList.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createRaisedBevelBorder(), "",
                    TitledBorder.CENTER,
                    TitledBorder.TOP));
            taskInList.setPreferredSize(blockDimensions);
        }

        this.tasksScrollPanel = new JScrollPane(tasksPanel);
        tasksScrollPanel.setPreferredSize(new Dimension((int)tasksBlockDimensions.getWidth(), (int)tasksBlockDimensions.getHeight()));
        System.out.println(tasksBlockDimensions.getHeight()/4);
        tasksScrollPanel.getVerticalScrollBar().setUnitIncrement((int) blockDimensions.getHeight());

        tasksScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        lpc.gridwidth = 3;
        lpc.gridx = 0;
        lpc.gridy = 1;
        leftPanel.add(tasksScrollPanel, lpc);

//Done tasks
        this.doneLabel = new JLabel("Done");
        lpc.gridwidth = 1;
        lpc.gridy = 2;
        leftPanel.add(doneLabel, lpc);

//      TODO: how to add each task

        this.doneTasksPanel = new JPanel();
        doneTasksPanel.setLayout(new BoxLayout(doneTasksPanel, BoxLayout.PAGE_AXIS));
        doneTasksPanel.setBackground(null);
//
//        this.doneTask1 = new JTextField("Task 1");
//        doneTasksList.add(doneTask1);
//
////        TestTasks
//        JTextField doneTestTextArea1 = new JTextField("Task 2");
//        doneTasksList.add(doneTestTextArea1);
//
////        TestTasks
//        JTextField doneTestTextArea2 = new JTextField("Task 3");
//        doneTasksList.add(doneTestTextArea2);
//
//
////        TestTasks
//        JTextField doneTestTextArea3 = new JTextField("Task 4");
//        doneTasksList.add(doneTestTextArea3);


//Iterates over list and add all tasks to the panel
        for (JTextField doneTaskInList : doneTasksList) {
            doneTasksPanel.add(doneTaskInList);
            doneTaskInList.setEditable(false);
            doneTaskInList.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createRaisedBevelBorder(), "",
                    TitledBorder.CENTER,
                    TitledBorder.TOP));
            doneTaskInList.setPreferredSize(blockDimensions);
        }

        this.doneTasksScrollPanel = new JScrollPane(doneTasksPanel);
        doneTasksPanel.setPreferredSize(tasksBlockDimensions);
        doneTasksScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        lpc.gridwidth = 3;
        lpc.gridy = 3;
        leftPanel.add(doneTasksScrollPanel, lpc);

//Addtask
        this.addTaskTextField = new JTextField("+ Add new task");
        addTaskTextField.setPreferredSize(blockDimensions);
        addTaskTextField.setEditable(false);
        lpc.insets = new Insets(10, 0, 10, 0);
        lpc.gridy = 4;
        leftPanel.add(addTaskTextField, lpc);

        rightTopPanel = new JPanel(new GridBagLayout());
        GridBagConstraints rtpc = new GridBagConstraints();

//TaskName
        this.taskNameLabel = new JLabel("Name");
        rtpc.fill = GridBagConstraints.HORIZONTAL;
        rtpc.gridwidth = 1;
        rtpc.gridy = 0;
        rightTopPanel.add(taskNameLabel, rtpc);

//TaskName
        this.taskImportanceLabel = new JLabel("Importance");
        rtpc.gridy = 1;
        rightTopPanel.add(taskImportanceLabel, rtpc);

//Reminder
        this.taskReminderDate = new JLabel("Reminder date");
        rtpc.gridy = 2;
        rightTopPanel.add(taskReminderDate, rtpc);

//TaskNote
        this.taskNoteTexrArea = new JTextArea("Task note Area");
        rtpc.gridy = 3;
        rightTopPanel.add(taskNoteTexrArea, rtpc);


        rightBottomPanel = new JPanel();
        rightBottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints rbpc = new GridBagConstraints();

//Created
        this.taskCreatedDate = new JLabel("Created date");
        rbpc.fill = GridBagConstraints.HORIZONTAL;
//        rbpc.anchor = GridBagConstraints.LAST_LINE_END;
//        rbpc.insets = new Insets(0  , 0, 10, 0);
        rbpc.gridwidth = 1;
        rbpc.gridx = 0;
        rbpc.gridy = 0;
        rightBottomPanel.add(taskCreatedDate, rbpc);


//        mainPanel = new JPanel();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
//        c.gridheight = 9;
        c.gridheight = 5;
        c.insets = new Insets(5, 15, 10, 10);
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(leftPanel, c);

        c.insets = new Insets(5, 10, 10, 15);
        c.gridheight = 4;
        c.gridx = 4;
        c.gridy = 1;
        mainPanel.add(rightTopPanel, c);

// TODO: Align bottom
        c.insets = new Insets(5, 10, 10, 15);
        c.gridheight = 1;
        c.fill = GridBagConstraints.VERTICAL;
        c.gridheight = GridBagConstraints.LINE_END;
        c.gridx = 4;
        c.gridy = 5;
        mainPanel.add(rightBottomPanel, c);

        frame.add(mainPanel);
    }

    public void repaintFrame(){
        frame.revalidate();
        frame.repaint();
    }

    public void revaluateLists(){
        for (JTextField taskInList : tasksList) {
            tasksPanel.add(taskInList);
            taskInList.setEditable(false);
            taskInList.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createRaisedBevelBorder(), "",
                    TitledBorder.CENTER,
                    TitledBorder.TOP));
            taskInList.setPreferredSize(blockDimensions);
        }

        for (JTextField doneTaskInList : doneTasksList) {
            doneTasksPanel.add(doneTaskInList);
            doneTaskInList.setEditable(false);
            doneTaskInList.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createRaisedBevelBorder(), "",
                    TitledBorder.CENTER,
                    TitledBorder.TOP));
            doneTaskInList.setPreferredSize(blockDimensions);
        }

        frame.revalidate();
        frame.repaint();

    }

    public JLabel getSortByWhat() {
        return sortByWhat;
    }

    public JLabel getTaskNameLabel() {
        return taskNameLabel;
    }

    public JLabel getTaskImportanceLabel() {
        return taskImportanceLabel;
    }

    public JLabel getTaskReminderDate() {
        return taskReminderDate;
    }

    public JLabel getTaskCreatedDate() {
        return taskCreatedDate;
    }

    public List<JTextField> getTasksList() {
        return tasksList;
    }

    public List<JTextField> getDoneTasksList() {
        return doneTasksList;
    }

    public JTextField getAddTaskTextField() {
        return addTaskTextField;
    }
}