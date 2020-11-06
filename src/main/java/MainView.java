import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainView {

    private int screenHeight, screenWidth;

    private JFrame frame;
    private JPanel mainPanel, leftPanel, rightTopPanel, rightBottomPanel,
            tasksPanel, doneTasksPanel;
    private JScrollPane tasksScrollPanel, doneTasksScrollPanel;

    private Dimension blockDimensions, tasksBlockDimensions;

    private JLabel appTittleLabel, sortByLabel, sortByWhat, doneLabel,taskCreatedDate;
    private List<JPanel> tasksToDoJPanelsList, tasksDoneJPanelsList;
    private JTextField addTaskTextField, taskNameLabel, taskImportanceLabel, taskReminderDate;
    private JTextArea taskNoteTexrArea;

    public MainView() {

//        Getting screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenHeight = (int) screenSize.getHeight();
        this.screenWidth = (int) screenSize.getWidth();

        this.tasksToDoJPanelsList = new ArrayList<JPanel>();
        this.tasksDoneJPanelsList = new ArrayList<JPanel>();

//        Create frame
        this.frame = new JFrame();
        frame.setSize(new Dimension((screenWidth / 5), (screenHeight / 3)));
        frame.setLocation(((screenWidth / 2) - (screenWidth / 4)), ((screenHeight / 2) - (screenHeight / 4)));
        frame.setLocationRelativeTo(null);

//        Creating planned task sizes
        this.tasksBlockDimensions = new Dimension(frame.getWidth() / 2, (frame.getHeight() / 10)*4);
        this.blockDimensions = new Dimension((int) (tasksBlockDimensions.getWidth()*0.8), ((int) (frame.getHeight() / 10)));

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

// Setting AppTitle on the main panel
        this.appTittleLabel = new JLabel("ToDo");
        appTittleLabel.setFont(new Font("serif", Font.BOLD, 25));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 15, 10, 15);
        c.gridwidth = 6;
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(appTittleLabel, c);


// Creating left panel for components
        leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints lpc = new GridBagConstraints();

        this.sortByLabel = new JLabel("Sort by");
        lpc.fill = GridBagConstraints.HORIZONTAL;
        lpc.gridwidth = 1;
        lpc.gridx = 0;
        lpc.gridy = 0;
        leftPanel.add(sortByLabel, lpc);

        this.sortByWhat = new JLabel("Importance");
        lpc.gridx = 1;
        leftPanel.add(sortByWhat, lpc);

// Crating panel for ToDoTasks along with scroll pane and setting its layout
        this.tasksPanel = new JPanel();
        // Sett layout
        tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));
        // Create scroll pane
        this.tasksScrollPanel = new JScrollPane(tasksPanel);
        // Set preff size of scroll pane
        tasksScrollPanel.setPreferredSize(new Dimension((int)tasksBlockDimensions.getWidth(), (int)tasksBlockDimensions.getHeight()));
        // Set scroll unit
        tasksScrollPanel.getVerticalScrollBar().setUnitIncrement((int) blockDimensions.getHeight());
        // Set when scroll bar is needed
        tasksScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        lpc.gridwidth = 3;
        lpc.gridx = 0;
        lpc.gridy = 1;
        leftPanel.add(tasksScrollPanel, lpc);

//Done tasks label
        this.doneLabel = new JLabel("Done");
        lpc.gridwidth = 1;
        lpc.gridy = 2;
        leftPanel.add(doneLabel, lpc);

//Crating panel for DoneTasks along with scroll pane and setting its layout
        this.doneTasksPanel = new JPanel();
        // Sett layout
        doneTasksPanel.setLayout(new BoxLayout(doneTasksPanel, BoxLayout.Y_AXIS));
        // Create scroll pane
        this.doneTasksScrollPanel = new JScrollPane(doneTasksPanel);
        // Set preff size of scroll pane
        doneTasksScrollPanel.setPreferredSize(new Dimension((int)tasksBlockDimensions.getWidth(), (int)tasksBlockDimensions.getHeight()));
        // Set scroll unit
        doneTasksScrollPanel.getVerticalScrollBar().setUnitIncrement((int) blockDimensions.getHeight());
        // Set when scroll bar is needed
        doneTasksScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        lpc.gridwidth = 3;
        lpc.gridy = 3;
        leftPanel.add(doneTasksScrollPanel, lpc);

//Add task field
        this.addTaskTextField = new JTextField("+ Add new task");
        addTaskTextField.setPreferredSize(blockDimensions);
        addTaskTextField.setEditable(false);
        lpc.insets = new Insets(10, 0, 10, 0);
        lpc.gridy = 4;
        leftPanel.add(addTaskTextField, lpc);


// Creating right top panel for components
        rightTopPanel = new JPanel(new GridBagLayout());
        GridBagConstraints rtpc = new GridBagConstraints();
//        rightTopPanel.setPreferredSize( new Dimension(frame.getWidth()/3, (int) (frame.getHeight()*0.8)));

        this.taskNameLabel = new JTextField("Name");
        taskNameLabel.setBackground(null);
        rtpc.fill = GridBagConstraints.HORIZONTAL;
        rtpc.gridwidth = 1;
        rtpc.gridy = 0;
        rightTopPanel.add(taskNameLabel, rtpc);

        this.taskImportanceLabel = new JTextField("Importance");
        taskImportanceLabel.setBackground(null);
        rtpc.gridy = 1;
        rightTopPanel.add(taskImportanceLabel, rtpc);

        this.taskReminderDate = new JTextField("Reminder date");
        taskReminderDate.setBackground(null);
        rtpc.gridy = 2;
        rightTopPanel.add(taskReminderDate, rtpc);

        this.taskNoteTexrArea = new JTextArea("Task note Area", 2,6);
        taskNoteTexrArea.setBackground(null);
        rtpc.gridy = 3;
        rightTopPanel.add(taskNoteTexrArea, rtpc);


// Creating right bottom panel for components
        rightBottomPanel = new JPanel();
        rightBottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints rbpc = new GridBagConstraints();
//Created
        this.taskCreatedDate = new JLabel("Created date");
        rbpc.fill = GridBagConstraints.HORIZONTAL;
        rbpc.gridwidth = 1;
        rbpc.gridx = 0;
        rbpc.gridy = 0;
        rightBottomPanel.add(taskCreatedDate, rbpc);


// Adding left, top right, bottom right panels to the main panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridheight = 5;
        c.insets = new Insets(5, 15, 10, 10);
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(leftPanel, c);

        c.insets = new Insets(5, 10, 10, 15);
        c.weightx = 0.5;
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

// Repaints container with ToDOTasks
    public void revaluateToDoList(){
        tasksPanel.removeAll();
        for (JPanel taskJPanel : tasksToDoJPanelsList) {
            taskJPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createRaisedBevelBorder(), "",
                    TitledBorder.CENTER,
                    TitledBorder.TOP));
            tasksPanel.add(taskJPanel);
            taskJPanel.setPreferredSize(blockDimensions);
        }

        frame.revalidate();
        frame.repaint();

    }

// Repaints container with DoneTasks
    public void revaluateDoneList(){
        doneTasksPanel.removeAll();
        for (JPanel doneTaskInList : tasksDoneJPanelsList) {
            doneTaskInList.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createRaisedBevelBorder(), "",
                    TitledBorder.CENTER,
                    TitledBorder.TOP));
            doneTasksPanel.add(doneTaskInList);
            doneTaskInList.setPreferredSize(blockDimensions);
        }

        frame.revalidate();
        frame.repaint();

    }

    public JLabel getSortByWhat() {
        return sortByWhat;
    }

    public JTextArea getTaskNoteTexrArea() {
        return taskNoteTexrArea;
    }

    public JTextField getTaskNameLabel() {
        return taskNameLabel;
    }

    public JTextField getTaskImportanceLabel() {
        return taskImportanceLabel;
    }

    public JTextField getTaskReminderDate() {
        return taskReminderDate;
    }

    public JLabel getTaskCreatedDate() {
        return taskCreatedDate;
    }

    public List<JPanel> getTasksToDoJPanelsList() {
        return tasksToDoJPanelsList;
    }

    public List<JPanel> getTasksDoneJPanelsList() {
        return tasksDoneJPanelsList;
    }

    public JTextField getAddTaskTextField() {
        return addTaskTextField;
    }

    public Dimension getBlockDimensions() {
        return blockDimensions;
    }

}