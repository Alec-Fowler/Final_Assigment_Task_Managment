/*
 file: Task.java
 Author: Alec Fowler
 Date: 2026-08-14
 Description: The Task Class for the advanced task management Application
*/
// Imports
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Task extends Entry{
    // Variables
    private String name;
    private String description;
    private String date;
    private Priority priority;
    private Status status;
    // Enums
    public enum Priority {
        Low,
        Medium,
        High
    }
    // I Opted for an Enum for status rather than a simple Bool I hope that's alright
    public enum Status{
        Not_started,
        In_Progress,
        Completed
    }
    // Constructors
    // https://www.baeldung.com/jackson-deserialization-multi-param-constructor
    // they have to be hinted at JSON properties for the load function to use the constructor
    public Task( @JsonProperty("id") int id,
                 @JsonProperty("name") String name,
                 @JsonProperty("description") String description,
                 @JsonProperty("date") String date,
                 @JsonProperty("priority") Priority priority,
                 @JsonProperty("status") Status status) {


        // Inherit from Entry
        super(id);
        this.name = name;
        this.description = description;
        this.date = date;
        this.priority = priority;
        this.status = status;
    }
    // Basic Constructor for adding things after the fact
    public Task(int id){
      super(id);
    }

    // Name getter and setter
    public String getName(){
        return name;
    }

    public void setName(String name) {
        if (!name.isBlank() && !name.contains(" ")) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name must not have null or blank");
        }
    }

    // Description getter and setter
    public String getDescription(){
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    // Date getter and setter
    public String getDate(){
        return date;
    }

    // Adapted from products assignment
    // https://github.com/Alec-Fowler/A3_Products/blob/master/src/PerishableProduct.java
    public void setDate(String date) {
        try{
            LocalDate inputDate  = LocalDate.parse(date);
            // Has to parsable as a local date does not have match the data type
            if ( inputDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Due date must be in the future");
            }

            this.date = (LocalDate.parse(date)).toString();

        }
        // For parsing errors
        catch(DateTimeParseException e){
            throw new IllegalArgumentException("Date must be formatted correctly");
        }
        // For if they put a past date in
        catch (IllegalArgumentException error) {
            throw error;
        }
        // For all not covered errors
        catch(Exception error){
            // Will need to change Error formating
            throw new IllegalArgumentException("Date is not valid");
        }
    }

    // Status getter and setter for the status enum
    public Status getStatus(){
        return status;
    }
    public void setStatus(Status status){
        this.status = status;
    }

    // Priority getter and setter for the priority enum
    public Priority getPriority(){
        return priority;
    }
    public void setPriority(Priority priority){
        this.priority = priority;
    }

}
