/*
 file: Entry.java
 Author: Alec Fowler
 Date: 2026-08-14
 Description: Basic Parent Class to demonstrate inheritance was gonna use it for something bigger but scope shrunk
*/
public class Entry {
    private int id;
    // Basic ID only
    public Entry(int id) {
        this.id = id;
    }
    // Setter
    public void setID(int i) {
        id = i;
    }
    // Getter
    public int getId() {
        return id;
    }

}
