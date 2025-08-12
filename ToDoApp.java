import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
public class ToDoApp {
    private JFrame frame;
    private DefaultListModel<Task> listModel;
    private JList<Task> taskList;
    private JTextField inputField;
    private JTextField searchField;
    private JButton addBtn, deleteBtn, toggleDoneBtn, clearDoneBtn, saveBtn, loadBtn;
    private List<Task> allTasks = new ArrayList<>();
    public ToDoApp() {
        initUI();
        loadTasksOnStart();
    }
    private void initUI() {
        frame = new JFrame("ToDo App - Java Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 520);
        frame.setLocationRelativeTo(null);
        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        inputField = new JTextField();
        addBtn = new JButton("Add Task");
        topPanel.add(inputField, BorderLayout.CENTER);
        topPanel.add(addBtn, BorderLayout.EAST);
        JPanel searchPanel = new JPanel(new BorderLayout(6,6));
        searchField = new JTextField();
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setCellRenderer(new TaskCellRenderer());
        JScrollPane scroll = new JScrollPane(taskList);
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(0,1,6,6));
        deleteBtn = new JButton("Delete");
        toggleDoneBtn = new JButton("Toggle Done");
        clearDoneBtn = new JButton("Clear Completed");
        saveBtn = new JButton("Save");
        loadBtn = new JButton("Load");
        rightPanel.add(toggleDoneBtn);
        rightPanel.add(deleteBtn);
        rightPanel.add(clearDoneBtn);
        rightPanel.add(saveBtn);
        rightPanel.add(loadBtn);
        JPanel footer = new JPanel(new BorderLayout());
        footer.add(new JLabel("Tip: Press Enter to add. Double-click to toggle done."), BorderLayout.WEST);
        frame.getContentPane().setLayout(new BorderLayout(10,10));
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(searchPanel, BorderLayout.AFTER_LAST_LINE);
        frame.add(scroll, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);
        frame.add(footer, BorderLayout.SOUTH);
        addBtn.addActionListener(e -> addTaskFromInput());
        inputField.addActionListener(e -> addTaskFromInput());
        deleteBtn.addActionListener(e -> deleteSelectedTask());
        toggleDoneBtn.addActionListener(e -> toggleSelectedTaskDone());
        clearDoneBtn.addActionListener(e -> clearCompleted());
        saveBtn.addActionListener(e -> saveTasks());
        loadBtn.addActionListener(e -> loadTasks());
        taskList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    toggleSelectedTaskDone();
                }
            }
        });
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });
        frame.getRootPane().setDefaultButton(addBtn);
        InputMap im = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = frame.getRootPane().getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
        am.put("delete", new AbstractAction() { public void actionPerformed(ActionEvent e) { deleteSelectedTask(); } });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "save");
        am.put("save", new AbstractAction() { public void actionPerformed(ActionEvent e) { saveTasks(); } });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK), "load");
        am.put("load", new AbstractAction() { public void actionPerformed(ActionEvent e) { loadTasks(); } });
        frame.setVisible(true);
    }
    private void addTaskFromInput() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Task cannot be empty.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Task t = new Task(text);
        allTasks.add(0, t); // newest on top
        refreshList();
        inputField.setText("");
    }
    private void deleteSelectedTask() {
        int idx = taskList.getSelectedIndex();
        if (idx == -1) return;
        Task selected = listModel.get(idx);
        int confirm = JOptionPane.showConfirmDialog(frame, "Delete selected task?\n" + selected.getText(), "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            allTasks.remove(selected);
            refreshList();
        }
    }
    private void toggleSelectedTaskDone() {
        int idx = taskList.getSelectedIndex();
        if (idx == -1) return;
        Task selected = listModel.get(idx);
        selected.setDone(!selected.isDone());
        refreshList();
    }
    private void clearCompleted() {
        allTasks = allTasks.stream().filter(t -> !t.isDone()).collect(Collectors.toList());
        refreshList();
    }
    private void saveTasks() {
        try {
            TaskStorage.save(new ArrayList<>(allTasks));
            JOptionPane.showMessageDialog(frame, "Tasks saved successfully.", "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error saving tasks:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadTasks() {
        try {
            List<Task> loaded = TaskStorage.load();
            allTasks = new ArrayList<>(loaded);
            refreshList();
            JOptionPane.showMessageDialog(frame, "Tasks loaded (" + allTasks.size() + ").", "Loaded", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error loading tasks:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadTasksOnStart() {
        try {
            List<Task> loaded = TaskStorage.load();
            allTasks = new ArrayList<>(loaded);
            refreshList();
        } catch (Exception ex) {
        }
    }
    private void filter() {
        String q = searchField.getText().trim().toLowerCase();
        refreshList(q);
    }
    private void refreshList() { refreshList(searchField.getText().trim().toLowerCase()); }
    private void refreshList(String filter) {
        listModel.clear();
        for (Task t : allTasks) {
            if (filter == null || filter.isEmpty() || t.getText().toLowerCase().contains(filter)) {
                listModel.addElement(t);
            }
        }
    }
    private static class TaskCellRenderer extends JLabel implements ListCellRenderer<Task> {
        public Component getListCellRendererComponent(JList<? extends Task> list, Task value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value == null) {
                setText("");
                return this;
            }
            String text = value.getText();
            if (value.isDone()) {
                setText("<html><span style='color:gray;'><strike>" + escapeHtml(text) + "</strike></span></html>");
            } else {
                setText("<html>" + escapeHtml(text) + "</html>");
            }
            setOpaque(true);
            if (isSelected) {
                setBackground(new Color(0xDDEEFF));
            } else {
                setBackground(Color.WHITE);
            }
            setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
            return this;
        }
        private static String escapeHtml(String s) {
            return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoApp());
    }
}