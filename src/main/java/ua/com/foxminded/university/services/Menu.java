package ua.com.foxminded.university.services;

import java.util.Scanner;

import ua.com.foxminded.university.dao.DAOException;
import ua.com.foxminded.university.services.university.UniversityTableService;

public class Menu {
    private static final String MES_TABLES_CREATION = "The tabels has been created.";
    private static final String ERROR_TABLES_CREATION = "The tables creation failed.";
    
    private TableService postgresTableService;
    
    public Menu (TableService postgresTableService) {
        this.postgresTableService = postgresTableService;
    }
    
    public void boot() throws ServicesException.TableCreationFail {
        
        try {
            postgresTableService.createTables();
            
            
        } catch (ServicesException.TableCreationFail e) {
            throw new ServicesException.TableCreationFail(ERROR_TABLES_CREATION, e);
        }
        
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
