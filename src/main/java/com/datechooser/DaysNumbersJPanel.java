package com.datechooser;

import com.TasksController;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;

public class DaysNumbersJPanel extends JPanel {

    private ArrayList<JLabel> daysInPrevMonth = new ArrayList(),
            daysInCurrentMonth= new ArrayList(),
            daysInNextMonth= new ArrayList();

    private TasksController tasksController;

    private int previousMonthDaysNo, currentMonthDaysNo, nextMonthDaysNo;
    private int currentMonthFirstDay;
    private int currentYear, currentDay, nextYear, previousYear;
    private int currentMonth, previousMonth, nextMonth;
    private JPanel mainPanel = new JPanel(), daysNumbersJPanel = this;
    private JFrame dateChooserFrame;
    private String[] daysTable = {
            "Sun",
            "Mon",
            "Tue",
            "Wed",
            "Thu",
            "Fri",
            "Sat",
    };



    public DaysNumbersJPanel(int year, int month, JFrame dateChooserFrame, TasksController tasksController) {
        this.dateChooserFrame = dateChooserFrame;
//      Assign current day as currently chosen day;
        this.currentDay = LocalDate.now().getDayOfMonth();

        this.tasksController = tasksController;

        assignValues(year, month);

        fillDaysLists();

        addListenersToCalendarElements();

        paintCalendar();

    }

    public void assignValues(int year, int month){


        this.currentYear = year;
//        Lowering given month due to the arrays numeration
        this.currentMonth = month+1;

        this.currentMonthDaysNo = getDaysOfMonthNumber(currentYear, currentMonth);

        if (currentMonth == 12){

            this.nextMonth = 1;
            this.previousMonth = 11;
            this.previousMonthDaysNo = getDaysOfMonthNumber(currentYear, previousMonth);
            this.nextMonthDaysNo = getDaysOfMonthNumber(currentYear+1, nextMonth);

        } else if (currentMonth == 1){

            this.nextMonth = 2;
            this.previousMonth = 12;
            this.previousMonthDaysNo = getDaysOfMonthNumber(currentYear-1, previousMonth);
            this.nextMonthDaysNo = getDaysOfMonthNumber(currentYear, nextMonth);

        } else {

            this.previousMonth = currentMonth-1;
            this.previousYear = currentYear;
            this.previousMonthDaysNo = getDaysOfMonthNumber(currentYear, previousMonth);
            this.nextMonth = currentMonth+1;
            this.nextYear = currentYear;
            this.nextMonthDaysNo = getDaysOfMonthNumber(nextYear, nextMonth);

        }
            this.currentMonthFirstDay = getDayInWeekNumber(currentYear, currentMonth);
        System.out.println(currentMonthFirstDay);


    }


    public void paintCalendar(){

        remove(mainPanel);

        this.mainPanel = new JPanel(new GridLayout(7, 7, 3, 3));

//      add days into the view
        for (String day : daysTable){
            JLabel tmp = new JLabel(day, SwingConstants.CENTER);
            mainPanel.add(tmp);
        }

        for (JLabel previousMonthDay : daysInPrevMonth){
            mainPanel.add(previousMonthDay);
        }

        for (JLabel currentMonthDay : daysInCurrentMonth){
            mainPanel.add(currentMonthDay);
        }

        for (JLabel nextMonthDay : daysInNextMonth){
            mainPanel.add(nextMonthDay);
        }

        add(mainPanel);

    }

    public void fillDaysLists(){

        int daysToWriteFromPrevMonth = 0;

//        add previous days to the list
        for (int i = previousMonthDaysNo; i > (previousMonthDaysNo-(currentMonthFirstDay-1)); i--){
            JLabel tmp = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            tmp.setForeground(Color.GRAY);
            tmp.setBackground(Color.WHITE);
            daysInPrevMonth.add(tmp);
            daysToWriteFromPrevMonth++;
        }

//        add current month days to the list
        for (int i = 1; i <= currentMonthDaysNo; i++){
            JLabel tmp = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            tmp.setBackground(Color.WHITE);
            daysInCurrentMonth.add(tmp);
            markChosedDay(i, tmp);
        }

//        add next month days to the list
        for (int i = 1; i <= (42-currentMonthDaysNo-(daysToWriteFromPrevMonth)); i++){
            JLabel tmp = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            tmp.setForeground(Color.GRAY);
            tmp.setBackground(Color.WHITE);
            daysInNextMonth.add(tmp);
        }
    }


    public void addListenersToCalendarElements(){
        for (JLabel previousMonthDay : daysInPrevMonth){
            previousMonthDay.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println(previousMonthDay.getText());
                }
            });
        }

        for (JLabel currentMonthDay : daysInCurrentMonth){
            currentMonthDay.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println(currentMonthDay.getText());
                    currentDay = Integer.valueOf(currentMonthDay.getText());
                    markChosedDay(currentDay, currentMonthDay);

//                    assignValues();

                    daysInCurrentMonth.clear();
                    daysInPrevMonth.clear();
                    daysInNextMonth.clear();

                    fillDaysLists();

                    addListenersToCalendarElements();

                    paintCalendar();

//                    Added in case of to do app

                    String tmpDateString=String.valueOf(currentDay)+"/"+String.valueOf(currentMonth)+"/"+String.valueOf(currentYear);
                    tasksController.changeTaskReminderDate(tmpDateString);


                    dateChooserFrame.dispose();

                }
            });
        }

        for (JLabel nextMonthDay : daysInNextMonth){
            nextMonthDay.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println(nextMonthDay.getText());
                }
            });
        }

    }


    public void changeCurrentMonthAdnRepaint(int month, int year){
        currentMonth = month;

        assignValues(month, year);

        daysInCurrentMonth.clear();
        daysInPrevMonth.clear();
        daysInNextMonth.clear();

        fillDaysLists();

        addListenersToCalendarElements();

        paintCalendar();

    }


    private void markChosedDay(int i, JLabel tmp) {
        if (i == currentDay){
            tmp.setForeground(Color.RED);
            tmp.setBorder(new LineBorder(Color.GRAY));
        }
    }


    public int getDaysOfMonthNumber(int year, int month){
        YearMonth yearMonthObject = YearMonth.of(year, month);
        return yearMonthObject.lengthOfMonth();
    }


    public int getDayInWeekNumber(int year, int month){
        LocalDate date = LocalDate.of(year, month, 1);
        return date.get(WeekFields.of(Locale.ENGLISH).dayOfWeek());
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }
}
