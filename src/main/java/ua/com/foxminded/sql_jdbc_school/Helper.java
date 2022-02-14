package ua.com.foxminded.sql_jdbc_school;

import java.util.Scanner;

public class Helper {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while(scanner.hasNextLine()) {
            String one = scanner.nextLine();
            System.out.println(one);
            
            if (one.equals("exit")) {
                System.out.println("happy exit");
                System.exit(0);
            } else if (one.equals("")) {
                System.out.println("exit one one");
                break;
            }
                
        }
        
        System.out.println("end 111111");
        
        
        
        
    }
}
