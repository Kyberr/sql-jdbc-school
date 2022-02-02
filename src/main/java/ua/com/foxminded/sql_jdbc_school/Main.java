package ua.com.foxminded.sql_jdbc_school;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.services.CourseService;
import ua.com.foxminded.sql_jdbc_school.services.Generator;
import ua.com.foxminded.sql_jdbc_school.services.GroupService;
import ua.com.foxminded.sql_jdbc_school.services.Parser;
import ua.com.foxminded.sql_jdbc_school.services.Reader;
import ua.com.foxminded.sql_jdbc_school.services.StudentService;
import ua.com.foxminded.sql_jdbc_school.services.TableService;
import ua.com.foxminded.sql_jdbc_school.services.university.UniversityCourseService;
import ua.com.foxminded.sql_jdbc_school.services.university.UniversityGroupService;
import ua.com.foxminded.sql_jdbc_school.services.university.UniversityStudentService;
import ua.com.foxminded.sql_jdbc_school.services.university.UniversityTableService;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    
    

    public static void main(String[] args) {

        Reader reader = new Reader();
        Parser parser = new Parser();
        Generator generator = new Generator();
        TableService<Integer> tableServices = new UniversityTableService(reader, parser);
        StudentService<Integer> studentServices = new UniversityStudentService(reader, generator);
        GroupService<Integer> groupServices = new UniversityGroupService(generator);
        CourseService<Integer> courseServices = new UniversityCourseService(reader);
        
        
        List<Integer> result = generator.generateStudentNumber(200, 10);

        
        int i = 1;
        
        for (Integer element : result) {
            System.out.print(element);
            System.out.println("-" + i++);
        }
        System.out.println("size " + result.size());
        
        /*
        List<Integer> result2 = new ArrayList<>();
        result2.add(0);
        result2.add(0);
        result2.add(0);
        System.out.println(result2.size());
        */
       
        
/*
        try {
            System.out.println(tableServices.creatTables());
            System.out.println(studentServices.createStudents());
            System.out.println(groupServices.createGroups());
            System.out.println(courseServices.createCourses());
        } catch (Exception e) {
            LOGGER.error("Error", e);
        }
        */
    }
}
