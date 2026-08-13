import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TaskCalenderUI {
    static ArrayList<Task> taskList = new ArrayList<Task>();
    // https://stackoverflow.com/questions/10432385/how-to-make-a-jtable-not-editable-in-java
    static DefaultTableModel tableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    static JTable mainTable = new JTable(tableModel);


    // https://www.zentut.com/java-swing/how-to-create-menu-in-swing/
    // https://stackoverflow.com/questions/24962793/creating-a-menu-in-java
    // Learning how to make a file menu
    // user Edwin Torres
    public static JMenuBar menuBar() {
        JMenuBar menuBar;
        JMenu fileMenu;
        JMenuItem saveFile;
        JMenuItem loadFile;
        JMenu editMenu;
        JMenuItem newEntry;
        JMenuItem editEntry;
        JMenuItem deleteEntry;


        // File menu
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.getAccessibleContext().setAccessibleDescription("File menu for loading and saving files");
        menuBar.add(fileMenu);

        // Save Option in file menu
        saveFile = new JMenuItem("Save", KeyEvent.VK_S);
        saveFile.getAccessibleContext().setAccessibleDescription("Saves the file");
        fileMenu.add(saveFile);

        // Load Option in file menu
        loadFile = new JMenuItem("Load", KeyEvent.VK_L);
        loadFile.getAccessibleContext().setAccessibleDescription("Loads the file");
        fileMenu.add(loadFile);

        // Edit Menu
        editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        editMenu.getAccessibleContext().setAccessibleDescription("Edit menu for editing and adding tasks");
        menuBar.add(editMenu);

        // add entries
        newEntry = new JMenuItem("New", KeyEvent.VK_N);
        newEntry.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0));
        newEntry.getAccessibleContext().setAccessibleDescription("New menu for editing and adding tasks");
        editMenu.add(newEntry);

        // edit existing entries
        editEntry = new JMenuItem("Edit", KeyEvent.VK_E);
        editEntry.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0));
        editEntry.getAccessibleContext().setAccessibleDescription("Edit menu for editing and adding tasks");
        editMenu.add(editEntry);

        // Delete an entry
        deleteEntry = new JMenuItem("Delete", KeyEvent.VK_DELETE);
        deleteEntry.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        deleteEntry.getAccessibleContext().setAccessibleDescription("Delete menu for editing and adding tasks");
        editMenu.add(deleteEntry);


        loadFile.addActionListener(e -> {
            // Todo Load Function
            JFileChooser filechooser = new JFileChooser();
            int result = filechooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = filechooser.getSelectedFile();
                ObjectMapper mapper = new ObjectMapper();
                try {
                    taskList.clear();

                    taskList = mapper.readValue(file, new TypeReference<ArrayList<Task>>() {});
                } catch (IOException ex) {
                  throw new RuntimeException(ex);
                }
            }
        });


        saveFile.addActionListener(e -> {
            //https://stackoverflow.com/questions/15786129/converting-java-objects-to-json-with-jackson
            ObjectMapper mapper = new ObjectMapper();
            try {
                //Todo Add file chooser functionality
                mapper.writeValue(new File("save.json"), taskList);
            } catch (Exception error) {
                error.printStackTrace();
            }
        });
        newEntry.addActionListener(e -> {
            NewTask();
        });
        editEntry.addActionListener(e -> {
            int selectedRow = mainTable.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = mainTable.convertRowIndexToModel(selectedRow);
                EditTask(modelRow);

            }
        });
        deleteEntry.addActionListener(e -> {
            int selectedRow = mainTable.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = mainTable.convertRowIndexToModel(selectedRow);
                System.out.printf("Selected row: %d\n", modelRow);
                taskList.remove(modelRow);
                  for (int i = 0; i < taskList.size(); i++){
                      taskList.get(i).setID(i);

                  }
                refreshTable();

            }
        });


        return menuBar;
    }

    // https://www.tutorialspoint.com/swingexamples/create_table.htm
    // https://www.tutorialspoint.com/article/java-program-to-append-a-row-to-a-jtable-in-java-swing
    public static void TaskTable() {
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Priority");
        tableModel.addColumn("Status");
        tableModel.addColumn("Date");
        tableModel.addColumn("Description");
    }

    private static void AddTask(Task task) {
        tableModel.addRow(new Object[]{task.getId(), task.getName(), task.getPriority(), task.getStatus(), task.getDate(), task.getDescription()});
    }
    private static void RemoveTask(int index) {
        tableModel.removeRow(index);
    }
    private static void refreshTable() {
        tableModel.setRowCount(0);
        for (Task task : taskList){
            AddTask(task);
        }
    }

    private static void NewTask() {
        JFrame newFrame = new JFrame("Edit Task");
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        newFrame.setSize(280, 535);
        newFrame.setVisible(true);
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        JLabel nameLabel = new JLabel("Name: ");
        inputPanel.add(nameLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        JTextField name = new JTextField(10);
        inputPanel.add(name, gridBagConstraints);
        // Priority
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        JLabel priorityLabel = new JLabel("Priority: ");
        inputPanel.add(priorityLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        JComboBox<Task.Priority> priority = new JComboBox<Task.Priority>(Task.Priority.values());
        inputPanel.add(priority, gridBagConstraints);
        // Status
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        JLabel statusLabel = new JLabel("Status: ");
        inputPanel.add(statusLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        JComboBox<Task.Status> status = new JComboBox<Task.Status>(Task.Status.values());
        inputPanel.add(status, gridBagConstraints);
        // Date
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD): ");
        inputPanel.add(dateLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        JTextField date = new JTextField(10);
        inputPanel.add(date, gridBagConstraints);
        // Description
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        JLabel descriptionLabel = new JLabel("Description: ");
        inputPanel.add(descriptionLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        JTextArea description = new JTextArea(20, 5);
        description.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(description);
        inputPanel.add(scrollPane, gridBagConstraints);
        // IO buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Add");
        JButton cancelButton = new JButton("Edit");
        submitButton.addActionListener(e -> {
            Task task = new Task(taskList.size());
            try {
                task.setName(name.getText());
                task.setPriority((Task.Priority) priority.getSelectedItem());
                task.setStatus((Task.Status) status.getSelectedItem());
                task.setDate(date.getText());
                task.setDescription(description.getText());
                taskList.add(task);
                AddTask(task);
                newFrame.dispose();
            } catch (Exception error) {

                JOptionPane.showMessageDialog(newFrame, "Error: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> newFrame.dispose());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        newFrame.add(inputPanel, BorderLayout.NORTH);
        newFrame.add(buttonPanel, BorderLayout.CENTER);
    }
    private static void EditTask(int id) {
        JFrame editFrame = new JFrame("Edit Task");
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        editFrame.setSize(300, 500);
        editFrame.setVisible(true);
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        JLabel nameLabel = new JLabel("Name: ");
        inputPanel.add(nameLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        JTextField name = new JTextField(10);
        inputPanel.add(name, gridBagConstraints);
        // Priority
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        JLabel priorityLabel = new JLabel("Priority: ");
        inputPanel.add(priorityLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        JComboBox<Task.Priority> priority = new JComboBox<Task.Priority>(Task.Priority.values());
        inputPanel.add(priority, gridBagConstraints);
        // Status
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        JLabel statusLabel = new JLabel("Status: ");
        inputPanel.add(statusLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        JComboBox<Task.Status> status = new JComboBox<Task.Status>(Task.Status.values());
        inputPanel.add(status, gridBagConstraints);
        // Date
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD): ");
        inputPanel.add(dateLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        JTextField date = new JTextField(10);
        inputPanel.add(date, gridBagConstraints);
        // Description
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        JLabel descriptionLabel = new JLabel("Description: ");
        inputPanel.add(descriptionLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        JTextArea description = new JTextArea(20, 10);
        description.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(description);
        inputPanel.add(scrollPane, gridBagConstraints);
        // IO buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Add");
        JButton cancelButton = new JButton("Edit");
        Task task = taskList.get(id);
        name.setText(task.getName());
        priority.setSelectedItem(task.getPriority());
        status.setSelectedItem(task.getStatus());
        date.setText(task.getDate());
        description.setText(task.getDescription());

        submitButton.addActionListener(e -> {
            try {
                task.setName(name.getText());
                task.setPriority((Task.Priority) priority.getSelectedItem());
                task.setStatus((Task.Status) status.getSelectedItem());
                task.setDate(date.getText());
                task.setDescription(description.getText());
                taskList.set(id, task);
                RemoveTask(id);
                AddTask(task);
                editFrame.dispose();
            } catch (Exception error) {

                JOptionPane.showMessageDialog(editFrame, "Error: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> editFrame.dispose());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        editFrame.add(inputPanel, BorderLayout.NORTH);
        editFrame.add(buttonPanel, BorderLayout.CENTER);
    }



    public static void TaskCalenderUI() {
        final JFrame mainFrame = new JFrame("Task Calender App");
        mainFrame.setJMenuBar(menuBar());

        mainFrame.add(new JScrollPane(TaskCalenderUI.mainTable));
        TaskCalenderUI.TaskTable();
        Task test = new Task(0, "Test", "This is a test task", "2026-08-12", Task.Priority.High, Task.Status.Completed);
        TaskCalenderUI.AddTask(test);
        TaskCalenderUI.taskList.add(test);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 500);
        mainFrame.setVisible(true);
    }
}
void main() {
    TaskCalenderUI.TaskCalenderUI();
}

