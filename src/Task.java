import java.time.LocalDate;

public class Task extends Entry{
    Priority priority;
    Status status;
    enum Priority {
        Low,
        Medium,
        High
    }
    enum Status{
        Not_started,
        In_Progress,
        Completed
    }
    public Task(String name, String description, LocalDate date, Priority priority, Status status){
        super(name, description, date);
        this.priority = priority;
        this.status = status;
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
