/*
 file: TaskManagerUI.java
 Author: Alec Fowler
 Date: 2026-08-14
 Description: My Advanced Task Management app with a table and multiple windows
*/
// Learned a lot from In-class-5 in how the UI is done and applied it in a more organized way in this assignment
// https://github.com/Alec-Fowler/Inclass5/blob/master/src/TaskManagerUI.java

// Imports
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// Main Class
public class TaskManagerUI {

    // Constants
    final static int MAIN_WINDOW_WIDTH = 800;
    final static int MAIN_WINDOW_HEIGHT = 600;
    final static int VIEW_WINDOW_WIDTH = 380;
    final static int VIEW_WINDOW_HEIGHT = 535;
    final static int TASK_WINDOW_WIDTH = 300;
    final static int TASK_WINDOW_HEIGHT = 535;

    // Main task list as specified
    static ArrayList<Task> taskList = new ArrayList<>();

    // The cells shouldn't be editable
    // https://stackoverflow.com/questions/10432385/how-to-make-a-jtable-not-editable-in-java
    static DefaultTableModel tableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    // Creates the table
    static JTable mainTable = new JTable(tableModel);

    // Index for the Task view
    private static int currentIndex;

    // Default Save file position
    public static final File mainfile = new File("./save.json");

    // MAIN WINDOW

    // https://www.zentut.com/java-swing/how-to-create-menu-in-swing/
    // https://stackoverflow.com/questions/24962793/creating-a-menu-in-java
    // Learning how to make a file menu
    // user Edwin Torres
    public static JMenuBar menuBar() {
        // JMenu Items
        JMenuBar menuBar;
        JMenu fileMenu;
        JMenuItem saveFile;
        JMenuItem saveAsFile;
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
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S ,KeyEvent.CTRL_DOWN_MASK));
        saveFile.getAccessibleContext().setAccessibleDescription("Saves the file");
        fileMenu.add(saveFile);
        // Save As option for file menu
        saveAsFile = new JMenuItem("Save as",KeyEvent.VK_SUBTRACT);
        saveAsFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S ,KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK));
        saveAsFile.getAccessibleContext().setAccessibleDescription("Saves the file to a directory you select");
        fileMenu.add(saveAsFile);

        // Load Option in file menu
        loadFile = new JMenuItem("Load", KeyEvent.VK_L);
        loadFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L , KeyEvent.CTRL_DOWN_MASK));
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


        // Load Event
        loadFile.addActionListener(_ -> {
            // Load a file
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                ObjectMapper mapper = new ObjectMapper();
                try {
                    taskList = mapper.readValue(file, new TypeReference<>() {
                    });
                    refreshTable();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Save File Event
        saveFile.addActionListener(_ -> {
            //https://stackoverflow.com/questions/15786129/converting-java-objects-to-json-with-jackson
            ObjectMapper mapper = new ObjectMapper();
            try {
                mapper.writeValue(new File("save.json"), taskList);
            } catch (Exception error) {
                error.printStackTrace();
                throw new RuntimeException(error);
            }
        });

        // Save File As event
        saveAsFile.addActionListener(_ -> {
            //https://stackoverflow.com/questions/15786129/converting-java-objects-to-json-with-jackson
            //https://stackoverflow.com/questions/14589386/how-to-save-file-using-jfilechooser-in-java
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    mapper.writeValue(new File(fileChooser.getSelectedFile() + ".json"), taskList);
                } catch (Exception error) {
                    error.printStackTrace();
                    throw new RuntimeException(error);
                }
            }
        });

        // New Entry Event
        newEntry.addActionListener(_ -> {
            newTask();
        });

        // Edit entry Event
        editEntry.addActionListener(_ -> {
            int selectedRow = mainTable.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = mainTable.convertRowIndexToModel(selectedRow);
                editTask(modelRow);

            }
        });

        // Delete Entry event
        deleteEntry.addActionListener(_ -> {
            int selectedRow = mainTable.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = mainTable.convertRowIndexToModel(selectedRow);
                // Debug message to ensure delete worked properly
                //System.out.printf("Selected row: %d\n", modelRow);
                taskList.remove(modelRow);
                  for (int i = 0; i < taskList.size(); i++){
                      taskList.get(i).setID(i);

                  }
                refreshTable();

            }
        });

        // Returns the main page top bar
        return menuBar;
    }


    // https://www.tutorialspoint.com/swingexamples/create_table.htm
    // https://www.tutorialspoint.com/article/java-program-to-append-a-row-to-a-jtable-in-java-swing
    // Creates the table
    public static void taskTable() {
        // Sets up the Table Columns
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Priority");
        tableModel.addColumn("Status");
        tableModel.addColumn("Due Date");
        tableModel.addColumn("Description");


        // Sets up a sorter so you can sort things in the menu without much thought
        // https://docs.oracle.com/en/java/javase/26/docs/api/java.desktop/javax/swing/table/TableRowSorter.html
        mainTable.setAutoCreateRowSorter(true);

        // I don't want you reordering my table, so I disabled it
        //https://stackoverflow.com/questions/17641123/jtable-disable-user-column-dragging
        mainTable.getTableHeader().setReorderingAllowed(false);

        // Listen for double click
        // https://stackoverflow.com/questions/14852719/double-click-listener-on-jtable-in-java
        mainTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                JTable table = ( JTable ) event.getSource();
                Point point = event.getPoint();
                int row = table.rowAtPoint(point);
                if (event.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int modelRow = table.convertRowIndexToModel(row);
                    taskView(modelRow);
                }
            }
        });
    }

    //SECONDARY MENUS

    // Creates the New task menu
    private static void newTask() {
        //  New Task window Setup
        JFrame newFrame = new JFrame("Edit Task");
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        newFrame.setSize(TASK_WINDOW_WIDTH, TASK_WINDOW_HEIGHT);
        // Makes it visable
        newFrame.setVisible(true);
        // Makes it not Resize
        newFrame.setResizable(false);
        // Relative location
        newFrame.setLocationRelativeTo(null);
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
        JComboBox<Task.Priority> priority = new JComboBox<>(Task.Priority.values());
        inputPanel.add(priority, gridBagConstraints);

        // Status
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        JLabel statusLabel = new JLabel("Status: ");
        inputPanel.add(statusLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        JComboBox<Task.Status> status = new JComboBox<>(Task.Status.values());
        inputPanel.add(status, gridBagConstraints);

        // Date
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        JLabel dateLabel = new JLabel("Due Date (YYYY-MM-DD): ");
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

        // Makes it scrollable and text wrap
        description.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(description);
        inputPanel.add(scrollPane, gridBagConstraints);

        // IO buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        // Add button (Submit)
        submitButton.addActionListener(_ -> {
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

        // Cancel Button
        cancelButton.addActionListener(_ -> newFrame.dispose());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        newFrame.add(inputPanel, BorderLayout.NORTH);
        newFrame.add(buttonPanel, BorderLayout.CENTER);
    }

    // Creates the Edit task menu
    private static void editTask(int id) {
        JFrame editFrame = new JFrame("Edit Task");
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        editFrame.setSize(TASK_WINDOW_WIDTH, TASK_WINDOW_HEIGHT);
        editFrame.setVisible(true);
        editFrame.setResizable(false);
        editFrame.setLocationRelativeTo(null);
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
        JComboBox<Task.Priority> priority = new JComboBox<>(Task.Priority.values());
        inputPanel.add(priority, gridBagConstraints);

        // Status
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        JLabel statusLabel = new JLabel("Status: ");
        inputPanel.add(statusLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        JComboBox<Task.Status> status = new JComboBox<>(Task.Status.values());
        inputPanel.add(status, gridBagConstraints);

        // Date
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        JLabel dateLabel = new JLabel("Due Date (YYYY-MM-DD): ");
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
        // Makes it scrollable and text wrapping
        description.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(description);
        inputPanel.add(scrollPane, gridBagConstraints);

        // IO buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Edit");
        JButton cancelButton = new JButton("Cancel");

        Task task = taskList.get(id);
        name.setText(task.getName());
        priority.setSelectedItem(task.getPriority());
        status.setSelectedItem(task.getStatus());
        date.setText(task.getDate());
        description.setText(task.getDescription());

        submitButton.addActionListener(_ -> {
            try {
                task.setName(name.getText());
                task.setPriority((Task.Priority) priority.getSelectedItem());
                task.setStatus((Task.Status) status.getSelectedItem());
                task.setDate(date.getText());
                task.setDescription(description.getText());
                taskList.set(id, task);
                removeTask(id);
                AddTask(task);
                editFrame.dispose();
            } catch (Exception error) {

                throw new RuntimeException(error);
            }
        });

        cancelButton.addActionListener(_ -> editFrame.dispose());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        editFrame.add(inputPanel, BorderLayout.NORTH);
        editFrame.add(buttonPanel, BorderLayout.CENTER);
    }
    // Creates the Task View Menu
    private static void taskView(int id) {
        JFrame viewFrame = new JFrame("View Task");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewFrame.setSize(VIEW_WINDOW_WIDTH, VIEW_WINDOW_HEIGHT);
        viewFrame.setLocationRelativeTo(null);
        viewFrame.setResizable(false);
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        // Buttons
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        JButton backwardButton = new JButton("Backward");
        inputPanel.add(backwardButton, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        JButton forwardButton = new JButton("Forward");
        inputPanel.add(forwardButton, gridBagConstraints);


        // Name
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        JLabel nameLabel = new JLabel("Name: ");
        inputPanel.add(nameLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        JLabel name = new JLabel();
        inputPanel.add(name, gridBagConstraints);
        // Priority
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        JLabel priorityLabel = new JLabel("Priority: ");
        inputPanel.add(priorityLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        JLabel priority = new JLabel();
        inputPanel.add(priority, gridBagConstraints);
        // Status
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        JLabel statusLabel = new JLabel("Status: ");
        inputPanel.add(statusLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        JLabel status = new JLabel();
        inputPanel.add(status, gridBagConstraints);
        // Date
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        JLabel dateLabel = new JLabel(" Due Date (YYYY-MM-DD): ");
        inputPanel.add(dateLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        JLabel date = new JLabel();
        inputPanel.add(date, gridBagConstraints);
        // Description
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        JLabel descriptionLabel = new JLabel("Description: ");
        inputPanel.add(descriptionLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        JTextArea description = new JTextArea(20, 20);
        description.setLineWrap(true);
        description.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(description);
        inputPanel.add(scrollPane, gridBagConstraints);
        // Descriptions look terrible as Labels


        viewFrame.add(inputPanel, BorderLayout.NORTH);
        viewFrame.setVisible(true);
        // Set the current selected task as the labels
        Task task = taskList.get(id);
        name.setText(task.getName());
        priority.setText(task.getPriority().toString());
        status.setText(task.getStatus().toString());
        date.setText(task.getDate());
        description.setText(task.getDescription());


        // Sets default button disabled per the index
        currentIndex = id;
        if (currentIndex + 1 == taskList.size()) {
            forwardButton.setEnabled(false);

        }
        if (currentIndex == 0) {
            backwardButton.setEnabled(false);
        }
        forwardButton.addActionListener(_ -> {
            if (currentIndex + 1 < taskList.size()) {
                currentIndex++;
                Task nextTask = taskList.get(currentIndex);
                name.setText(nextTask.getName());
                priority.setText(nextTask.getPriority().toString());
                status.setText(nextTask.getStatus().toString());
                date.setText(nextTask.getDate());
                description.setText(nextTask.getDescription());
                if (!backwardButton.isEnabled()) {
                    backwardButton.setEnabled(true);
                }
            }
            if (currentIndex + 1 == taskList.size()) {
                forwardButton.setEnabled(false);

            }
        });
        backwardButton.addActionListener(_ -> {
            if (currentIndex - 1 >= 0) {
                currentIndex--;
                Task previousTask = taskList.get(currentIndex);
                name.setText(previousTask.getName());
                priority.setText(previousTask.getPriority().toString());
                status.setText(previousTask.getStatus().toString());
                date.setText(previousTask.getDate());
                description.setText(previousTask.getDescription());
                if (!forwardButton.isEnabled()) {
                    forwardButton.setEnabled(true);
                }
            }
            if (currentIndex == 0) {
                backwardButton.setEnabled(false);
            }
        });
    }

    // HELPER FUNCTIONS

    // Loads Files into arraylist
    private static void loadDefaultFile() {
        if (mainfile.exists()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                taskList = mapper.readValue(mainfile, new TypeReference<ArrayList<Task>>() {});
                refreshTable();
            }
            catch (IOException error) {
                error.printStackTrace();
                throw new RuntimeException(error);
            }
        }
    }

    private static void AddTask(Task task) {
        tableModel.addRow(new Object[]{task.getId(), task.getName(), task.getPriority(), task.getStatus(), task.getDate(), task.getDescription()});
    }

    private static void removeTask(int index) {
        tableModel.removeRow(index);
    }

    private static void refreshTable() {
        tableModel.setRowCount(0);
        for (Task task : taskList){
            AddTask(task);
        }
    }

    // Program Starter
    public static void uiStartup() {

        // Sets title for bar
        final JFrame mainFrame = new JFrame("Task Management App / Todo-List");

        // Sets menu top bar
        mainFrame.setJMenuBar(menuBar());

        // Adds the Table
        mainFrame.add(new JScrollPane(TaskManagerUI.mainTable));

        // Adds my collums and setup for the table
        TaskManagerUI.taskTable();

        // Loads Default File in project
        TaskManagerUI.loadDefaultFile();

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // sets the Size
        mainFrame.setSize(MAIN_WINDOW_WIDTH, MAIN_WINDOW_HEIGHT);
        mainFrame.setVisible(true);
    }
}

// Main Call

public static void main(String[] args) {
    TaskManagerUI.uiStartup();
}




