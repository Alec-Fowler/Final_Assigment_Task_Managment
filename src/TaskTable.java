import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class TaskTable {
    public static JTable TaskTable() {
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable maintable = new JTable(tableModel);
        tableModel.addColumn("Name");
        tableModel.addColumn("Priority");
        tableModel.addColumn("Status");
        tableModel.addColumn("Date");
        tableModel.addColumn("Description");


        return maintable;
    }
    public static void refreshTable(ArrayList<Task> list) {


    }
}
