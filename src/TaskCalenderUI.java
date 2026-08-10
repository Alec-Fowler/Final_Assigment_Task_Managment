import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TaskCalenderUI {
    ArrayList taskList = new ArrayList<Task>();


    // https://www.zentut.com/java-swing/how-to-create-menu-in-swing/
    // https://stackoverflow.com/questions/24962793/creating-a-menu-in-java
    // Learning how to make a file menu
    // user Edwin Torres
    public static JMenuBar menuBar() {
        JMenuBar menuBar;
        JMenu fileMenu;
        JMenuItem saveFile;
        JMenuItem loadFile;
        JMenuItem newEntry;



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
        return menuBar;



    }

    // https://www.tutorialspoint.com/swingexamples/create_table.htm
    // https://www.tutorialspoint.com/article/java-program-to-append-a-row-to-a-jtable-in-java-swing
    public static JTable TaskTable(){
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable maintable = new JTable(tableModel);
        tableModel.addColumn("Name");
        tableModel.addColumn("Priority");
        tableModel.addColumn("Status");
        tableModel.addColumn("Date");
        tableModel.addColumn("Description");





        return maintable;
    }
    public static void TaskCalenderUI(){
        final JFrame mainFrame = new JFrame("Task Calender App");
        mainFrame.setJMenuBar(menuBar());
        mainFrame.add( new JScrollPane(TaskTable()));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(500, 500);
        mainFrame.setVisible(true);
    }

}

