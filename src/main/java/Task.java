import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public Task() {
    }

    public Task(int importance, String taskName) {
        this.creationDate = new Date();
        this.importance = importance;
        this.taskName = taskName;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", creationDate=" + creationDate +
                ", reminderDate=" + reminderDate +
                ", taskId=" + taskId +
                ", importance=" + importance +
                ", note='" + note + '\'' +
                ", taskName='" + taskName + '\'' +
                '}';
    }

    public Long getTaskId() {
        return taskId;
    }

    public int getImportance() {
        return importance;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getNote() {
        return note;
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

}
