import javax.swing.*;
import java.awt.*;

public class MainView {

    private int screenHeight, screenWidth;

    private JFrame frame;
    private JPanel mainPanel;

    private TasksController tasksController;

    public MainView() {

//        Getting screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenHeight = (int) screenSize.getHeight();
        this.screenWidth = (int) screenSize.getWidth();

//        Create controller
        this.tasksController = new TasksController();

//        Create frame
        this.frame = new JFrame();
        addComponentsToThePanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("ToDo");
        frame.setLocationRelativeTo(null);
        frame.setLocation(((screenWidth / 2) - (screenWidth / 4)), ((screenHeight / 2) - (screenHeight / 4)));


//        Display Frame
        frame.pack();
        frame.setVisible(true);
    }

    private void addComponentsToThePanel() {
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
    }
}
