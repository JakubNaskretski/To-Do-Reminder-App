package com;

import com.datechooser.DateChooser;

import javax.swing.*;

public class DatePicker extends JPopupMenu {

    public DatePicker(TasksController tasksController) {
        new DateChooser(tasksController);
    }

}
