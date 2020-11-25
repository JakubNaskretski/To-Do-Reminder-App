package com.datechooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DateChooseButton extends JPanel {

    private JLabel numberLabel = new JLabel("", SwingConstants.CENTER);
    private JButton decrease = new JButton("-");
    private JButton increase = new JButton("+");
    private int currentYear;
    private JPanel dateChoosePanel;

    public DateChooseButton(int takenYear) {
        this.dateChoosePanel = this;
        this.currentYear = takenYear;
        setLayout(new GridLayout(1, 3));
        numberLabel.setText(String.valueOf(currentYear));
        add(decrease);
        add(numberLabel);
        add(increase);

        increase.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentYear++;
                numberLabel.setText("" + currentYear);
            }
        });
        decrease.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentYear--;
                numberLabel.setText("" + currentYear);
            }
        });
    }

    public JPanel getDateChoosePanel() {
        return dateChoosePanel;
    }

    public JLabel getNumberLabel() {
        return numberLabel;
    }

    public JButton getDecrease() {
        return decrease;
    }

    public JButton getIncrease() {
        return increase;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setNumberLabel(JLabel numberLabel) {
        this.numberLabel = numberLabel;
    }

    public void setDecrease(JButton decrease) {
        this.decrease = decrease;
    }

    public void setIncrease(JButton increase) {
        this.increase = increase;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public void repaintYear(){
        numberLabel.setText("" + currentYear);
    }

}