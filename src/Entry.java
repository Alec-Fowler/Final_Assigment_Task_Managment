import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class Entry {
    private final int id;
    private String name;
    private String description;
    private String date;
    // https://www.uuidgenerator.net/dev-corner/java
    // Better than Atomic Numbers
    public Entry(int id , String name, String description, String date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
    }
    public int getId() {
        return id;
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

}
