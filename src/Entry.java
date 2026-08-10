import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class Entry {
    private final String id;
    private String name;
    private String description;
    private LocalDate date;
    // https://www.uuidgenerator.net/dev-corner/java
    // Better then Atomic Numbers
    public Entry(String name, String description, LocalDate date) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.date = date;
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
             this.date = LocalDate.parse(date);
         }
         catch(Exception e){
             // Will need to change Error formating
             System.out.printf("Invalid Date Format please type YYYY-MM-DD: %s \n", e.getMessage());
             System.out.println("Setting Default Expiry Date!");
         }
     }

}
