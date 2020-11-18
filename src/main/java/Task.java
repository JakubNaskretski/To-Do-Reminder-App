import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "tasks")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

// Task class

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskid", nullable = false, unique = true)
    private Long taskId;

    @Column(name = "taskName", nullable = false)
    private String taskName;

    @Column(name = "taskNote", nullable = true)
    private String note;

    @Column(name = "taskCreationDate", nullable = false)
    private Date creationDate;

    @Column(name = "taskReminderDate", nullable = true)
    private Date reminderDate;

    @Column(name = "taskImportance", nullable = false)
    private int importance;

    @Column(name = "isTaskDone", nullable = false)
    private int isTaskDone;

    public Task() {
    }

    public Task(int importance, String taskName) {
        this.creationDate = new Date();
        this.importance = importance;
        this.taskName = taskName;
        this.isTaskDone = 0;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", note='" + note + '\'' +
                ", creationDate=" + creationDate +
                ", reminderDate=" + reminderDate +
                ", importance=" + importance +
                ", isTaskDone=" + isTaskDone +
                '}';
    }

    public Long getTaskId() {
        return taskId;
    }

    public int getImportance() {
        return importance;
    }

    public Date getCreationDate() {
        SimpleDateFormat dataFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return creationDate;
    }

    public Collection<?> getReminderDate() {
        SimpleDateFormat dataFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Collection<?> c = new ArrayList<Date>((Collection<? extends Date>) reminderDate);
        return c;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getNote() {
        return note;
    }

    public int getIsTaskDone() {
        return isTaskDone;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setIsTaskDone(int isTaskDone) {
        this.isTaskDone = isTaskDone;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }
}
