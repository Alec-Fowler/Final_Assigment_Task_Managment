import java.time.LocalDate;

public class Task extends Entry{
    private String name;
    private String description;
    private String date;
    private Priority priority;
    private Status status;
    public enum Priority {
        Low,
        Medium,
        High
    }
    public enum Status{
        Not_started,
        In_Progress,
        Completed
    }
    public Task(int id, String name, String description, String date, Priority priority, Status status){
        super(id);
        this.name = name;
        this.description = description;
        this.date = date;
        this.priority = priority;
        this.status = status;
    }
    public Task(int id){
      super(id);
    }
    public String getName(){
        return name;
    }
    public void setName(String name) {
        if (!name.isBlank() && !name.contains(" ")) {
            this.name = name;
        } else {
            throw new IllegalArgumentException();
        }
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    // Adapted from products assignment
    // https://github.com/Alec-Fowler/A3_Products/blob/master/src/PerishableProduct.java
    public void setDate(String date) {
        try{
            // Has to parsable as a local date does not have match the data type
            this.date = (LocalDate.parse(date)).toString();
        }
        catch(Exception e){
            // Will need to change Error formating
            System.out.printf("Invalid Date Format please type YYYY-MM-DD: %s \n", e.getMessage());
            System.out.println("Setting Default Expiry Date!");
        }
    }
    public String getDate(){
        return date;
    }
    public Status getStatus(){
        return status;
    }
    public void setStatus(Status status){
        this.status = status;
    }
    public Priority getPriority(){
        return priority;
    }
    public void setPriority(Priority priority){
        this.priority = priority;
    }

}
