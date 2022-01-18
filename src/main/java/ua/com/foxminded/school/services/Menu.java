package ua.com.foxminded.school.services;

import java.util.Scanner;

public class Menu {
    
    public void boot(SchoolServices services) {
        
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
