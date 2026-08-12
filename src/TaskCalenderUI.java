import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
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
        newEntry.getAccessibleContext().setAccessibleDescription("New menu for editing and adding tasks");
        editMenu.add(newEntry);

        // edit existing entries
        editEntry = new JMenuItem("Edit", KeyEvent.VK_E);
        editEntry.getAccessibleContext().setAccessibleDescription("Edit menu for editing and adding tasks");
        editMenu.add(editEntry);

        // Delete an entry
        deleteEntry = new JMenuItem("Delete", KeyEvent.VK_D);
        deleteEntry.getAccessibleContext().setAccessibleDescription("Delete menu for editing and adding tasks");
        editMenu.add(deleteEntry);


        saveFile.addActionListener(e -> {
            // TODO: Save to File code
            //https://stackoverflow.com/questions/15786129/converting-java-objects-to-json-with-jackson
            ObjectMapper mapper = new ObjectMapper();
            try {
                mapper.writeValue(new File("save.json"), taskList);
            }
            catch (Exception error)
            {
                error.printStackTrace();
            }
        });
        deleteEntry.addActionListener(e -> {
            int selectedRow = mainTable.getSelectedRow();
            if (selectedRow != -1){
                int  modelRow = mainTable.convertRowIndexToModel(selectedRow);
                System.out.printf("Selected row: %d\n", modelRow);
                taskList.remove(modelRow);
                tableModel.removeRow(mainTable.getSelectedRow());

            }
        });


        return  menuBar;
    }
    //https://youtu.be/OsgX1grOJZA?si=-wGdsgSHya89-xgw
    public static void deleteAction(ActionEvent e){
        if (mainTable.getSelectedRow() != -1){
            int row  = mainTable.convertColumnIndexToModel(mainTable.getSelectedRow());
            Object value = mainTable.getValueAt(row, 0);
            System.out.printf((String) value);
        }
    }

    // https://www.tutorialspoint.com/swingexamples/create_table.htm
    // https://www.tutorialspoint.com/article/java-program-to-append-a-row-to-a-jtable-in-java-swing
    public static void TaskTable(){
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Priority");
        tableModel.addColumn("Status");
        tableModel.addColumn("Date");
        tableModel.addColumn("Description");
    }
    public static void AddTask( Task task) {
        tableModel.addRow( new Object[] {task.getId(), task.getName(), task.getPriority(),task.getStatus(), task.getDate(), task.getDescription() });
    }
    public static void EditTask() {
        JFrame editFrame = new JFrame("Edit Task");
        editFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        editFrame.setSize(200,400);
        editFrame.setVisible(true);


    }
    public static void DeleteTask( Task task) {

    }
    public static void TaskCalenderUI(){
        final JFrame mainFrame = new JFrame("Task Calender App");
        mainFrame.setJMenuBar(menuBar());

        mainFrame.add( new JScrollPane(mainTable));
        TaskTable();
        Task test = new Task( 0, "Test", "This is a test task" , "2026-08-12",Task.Priority.High, Task.Status.Completed );
        AddTask( test );
        taskList.add(test);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(500, 500);
        mainFrame.setVisible(true);
    }

}

