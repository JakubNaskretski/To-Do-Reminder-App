import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {

// Task class

    @Id
    @Column(name = "taskid", nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

//    @Transient
//    private List<String> subTasks;

    @Column(name = "taskCreationDate", nullable = false)
    private Date creationDate;

    @Column(name = "taskReminderDate", nullable = true)
    private Date reminderDate;

    @Column(name = "taskImportance", nullable = false)
    private int importance;

    @Column(name = "taskNote", nullable = true)
    private String note;

    @Column(name = "taskName", nullable = false)
    private String taskName;

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
//                ", subTasks=" + subTasks +
                ", note='" + note + '\'' +
                ", taskName='" + taskName + '\'' +
                '}';
    }

    public Long getTaskId() {
        return taskId;
    }

//    public List<String> getSubTasks() {
//        return subTasks;
//    }

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
