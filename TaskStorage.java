import java.io.*;
import java.util.List;
import java.util.ArrayList;
public class TaskStorage {
    private static final String FILENAME = "tasks.dat";
    public static void save(List<Task> tasks) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            out.writeObject(new ArrayList<>(tasks));
        }
    }
    public static List<Task> load() throws IOException, ClassNotFoundException {
        File f = new File(FILENAME);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (List<Task>) in.readObject();
        }
    }
}