public class Entry {
    private int id;
    // https://www.uuidgenerator.net/dev-corner/java
    // Better than Atomic Numbers
    public Entry(int id) {
        this.id = id;
    }
    public void setID(int i) {
        id = i;
    }
    public int getId() {
        return id;
    }

}
