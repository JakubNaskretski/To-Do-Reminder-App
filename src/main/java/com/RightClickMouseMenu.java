package com;

import javax.swing.*;

public class RightClickMouseMenu extends JPopupMenu {


    JMenuItem item;
    public RightClickMouseMenu(Task task, TasksController tasksController, int taskType) {
        item = new JMenuItem("Remove");
        item.addActionListener(e -> {
            tasksController.removeTask(task.getTaskId());
        });
        add(item);

        if (taskType == 1) {

            item = new JMenuItem("Undone");
            item.addActionListener(e -> {
                tasksController.undoneTask(task.getTaskId());
            });
            add(item);
        }
    }

}
