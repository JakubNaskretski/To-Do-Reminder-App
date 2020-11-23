package com.datechooser;

import com.TasksController;
import com.TasksDBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class DateChooser {

    private final JFrame frame;
    private JPanel mainPanel, topPanel, middlePanel, bottomPanel;

    private JButton leftArrow, rightArrow;
    private JLabel monthDisplayLabel;
    private DateChooseButton dateChooseButton;

    private MonthsModel model;
    private DaysNumbersJPanel daysNumersJPanel;
    private String day, month, year;

    private TasksController tasksController;
    private TasksDBConnector dbConnector;


    public DateChooser(TasksController tasksController) {

//        TODO: Temporary>>>
        this.model = new MonthsModel(LocalDate.now().getMonthValue());

        this.tasksController = tasksController;

        this.frame = new JFrame();
        frame.setLocationRelativeTo(null);

        addComponentsToThePanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Choose date");
        frame.setUndecorated(true);

//        Display Frame
        frame.pack();
        frame.setVisible(true);

        addActionListeners();
        System.out.println(monthDisplayLabel.getWidth());
        System.out.println(monthDisplayLabel.getHeight());

    }

    private void addComponentsToThePanel() {
        this.mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints mc = new GridBagConstraints();

        this.topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        this.leftArrow = new JButton("<");
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        topPanel.add(leftArrow, c);

        this.monthDisplayLabel = new JLabel(model.getCurrentMonth(), SwingConstants.CENTER);
        monthDisplayLabel.setPreferredSize(new Dimension(66, 16));
        c.gridwidth = 2;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 0;
        topPanel.add(monthDisplayLabel, c);

        this.dateChooseButton = new DateChooseButton(LocalDate.now().getYear());
        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 3;
        c.gridy = 0;
        topPanel.add(dateChooseButton.getDateChoosePanel(), c);

        this.rightArrow = new JButton(">");
        c.gridx = 4;
        c.gridy = 0;
        topPanel.add(rightArrow, c);

        daysNumersJPanel = new DaysNumbersJPanel(dateChooseButton.getCurrentYear(), model.getCurrentMonthNo(), frame, tasksController);
        this.middlePanel = daysNumersJPanel;

        this.bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2,1));
//        bottomPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        bottomPanel.add(new JLabel("Today is: " + String.valueOf(LocalDate.now()), SwingConstants.LEFT));
        bottomPanel.add(new JLabel(""));



//        mc.anchor=GridBagConstraints.CENTER;
        mc.fill = GridBagConstraints.HORIZONTAL;
        mc.gridy = 0;
        mc.gridx = 0;
        mainPanel.add(topPanel, mc);
        mc.gridy = 1;
        mainPanel.add(Box.createVerticalStrut(5),mc);
        mc.gridy = 2;
        mainPanel.add(middlePanel, mc);
        mc.gridy = 3;
        mainPanel.add(Box.createVerticalStrut(5),mc);
        mc.gridy = 4;
        mainPanel.add(bottomPanel, mc);

        middlePanel.setBackground(Color.WHITE);
        bottomPanel.setBackground(Color.WHITE);


        frame.add(mainPanel);

    }

    public void addActionListeners(){

        rightArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((model.getCurrentMonthNo() + 1) == 12 ){
                    dateChooseButton.setCurrentYear(dateChooseButton.getCurrentYear()+1);
                    dateChooseButton.repaintYear();
                }
                monthDisplayLabel.setText(model.getNextMonth());
                daysNumersJPanel.changeCurrentMonthAdnRepaint(dateChooseButton.getCurrentYear(), model.getCurrentMonthNo());
                frame.repaint();
                setDayMontYear();
            }
        });

        leftArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((model.getCurrentMonthNo() - 1) == -1 ){
                    dateChooseButton.setCurrentYear(dateChooseButton.getCurrentYear()-1);
                    dateChooseButton.repaintYear();
                }
                monthDisplayLabel.setText(model.getPreviousMonth());
                daysNumersJPanel.changeCurrentMonthAdnRepaint(dateChooseButton.getCurrentYear(), model.getCurrentMonthNo());
                frame.repaint();
                setDayMontYear();
            }
        });

    }

    public void setDayMontYear(){
        this.day = String.valueOf(daysNumersJPanel.getCurrentDay());
        this.month = String.valueOf(daysNumersJPanel.getCurrentMonth());
        this.year = String.valueOf(daysNumersJPanel.getCurrentYear());
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }
}
