import javax.swing.*;

public class RightClickMouseMenu extends JPopupMenu {


    JMenuItem item;
    public RightClickMouseMenu(Task task, TasksController tasksController) {
        item = new JMenuItem("Remove");
        item.addActionListener(e -> {
            TasksDBConnector.deleteTask(task.getTaskId());
            tasksController.refreshToDoTaskList();
            tasksController.refreshDoneTaskList();
        });
        add(item);
    }

}
