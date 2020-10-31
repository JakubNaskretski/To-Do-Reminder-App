import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Task {

// Task class

    private LocalDateTime creationDate, reminderDate;
    private DateTimeFormatter dateTimeFormatter;
    private int taskNumber, importance;
    private ArrayList<Task> subTasks;
    private String note, taskName;

    public Task( int taskNumber, int importance, String taskName) {
        this.creationDate = LocalDateTime.now();
        this.dateTimeFormatter = DateTimeFormatter .ofPattern("dd-MM-yyyy HH:mm:ss");
        creationDate.format(dateTimeFormatter);
        this.taskNumber = taskNumber;
        this.importance = importance;
        this.taskName = taskName;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getReminderDate() {
        return reminderDate;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public int getImportance() {
        return importance;
    }

    public ArrayList<Task> getSubTasks() {
        return subTasks;
    }

    public String getNote() {
        return note;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setReminderDate(LocalDateTime reminderDate) {
        this.reminderDate = reminderDate;
    }

    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public void setSubTasks(ArrayList<Task> subTasks) {
        this.subTasks = subTasks;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
