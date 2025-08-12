import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private String text;
    private boolean done;
    private LocalDateTime createdAt;
    public Task(String text) {
        this.text = text;
        this.done = false;
        this.createdAt = LocalDateTime.now();
    }
    public String getText() { return text; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setText(String text) { this.text = text; }
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("%s  (%s)%s", text, createdAt.format(fmt), done ? " [DONE]" : "");
    }
}