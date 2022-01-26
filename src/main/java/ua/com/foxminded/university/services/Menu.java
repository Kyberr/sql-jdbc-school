package ua.com.foxminded.university.services;

import java.util.Scanner;
import ua.com.foxminded.university.services.postgres.PostgresTableService;

public class Menu {
    private static final String MES_TABLES_CREATION = "The tabels has been created.";
    
    public void boot(PostgresTableService services) {
        
        int intInput = toInt(scan());
        
        switch (intInput) {
        case 1:
            System.out.println("Enter the number of students.");
            
            
            
        
        }
        
        
        
                
    }
    
    public int toInt (String input) {
        int intInput = 0;
        
        try {
            intInput = Integer.valueOf(input);
            
        } catch (NumberFormatException e) {
            System.out.println("The input must be a number.");
        }
        return intInput;
    }
    
    
    
    public String scan() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextLine();
        }
    }
}
