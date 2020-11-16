import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Properties;

public class DatePickerClickMenu extends JPopupMenu {

    private JDatePickerImpl datePicker;

    public DatePickerClickMenu(Task task, TasksController tasksController) {
        this.datePicker = createDatePicker();
        add(datePicker);
        System.out.println(datePicker.getModel().getValue());
//        tasksController.getMainView().getTaskReminderDate().setText(datePicker.getModel().getValue());
//        tasksController.changeReminderDate(task.getTaskId(), datePicker.getModel().getDay());
    }

    public JDatePickerImpl createDatePicker(){
        UtilDateModel model = new UtilDateModel();
//model.setDate(20,04,2014);
// Need this...
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
// Don't know about the formatter, but there it is...
//        return datePanel;
        return new JDatePickerImpl(datePanel, new DatePicker());
    }

    public JDatePickerImpl getDatePicker() {
        return datePicker;
    }
}
