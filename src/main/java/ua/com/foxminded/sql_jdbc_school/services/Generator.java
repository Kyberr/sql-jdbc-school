package ua.com.foxminded.sql_jdbc_school.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;

public class Generator {
    private static final String HYPHEN = "-";
    private static final int MIN_STUDENTS = 10; 
    private static final int MAX_STUDENTS = 30; 
    private static final int MAX_COURSE_INDEX = 9; 
    private static final int MAX_COURSES_NUMBER = 9; 
    private static final int AMPLITUDE = 20; 
    private static final double ONE_HALF = 0.5; 
    private static final double DOUBLE_PROBABILITY_VALUE = 4.0;
    private static final int INT_PROBABILITY_VALUE = 3;
    private static final int ONE_STUDENT = 1;
    private static final int ZERO_OF_STUDENTS = 0;
    private static final double DOUBLE_AMPL_PROBABILITY = 10.0;
    private static final int INT_AMPL_PROBABILITY = 10;
    private static final int MAX_NO_GROUP_STUDENTS = 11;
    private static final int CROURSE_AMPLITUDE = 3;
    private static final int MIN_NUMBER_OF_CROURSES = 1;
    private static final int ONE = 1;
    
    public List<List<Integer>> getCoursePerStudent(int studentsNumber, int coursesNumber) {
        Map<Integer, Integer> studentsAndNumberOfCourses = new HashMap<>();
        
        for (int i = 0; i < studentsNumber; i++) {
            int numberOfCoursesPerStudent = MIN_NUMBER_OF_CROURSES + new Random().nextInt(CROURSE_AMPLITUDE);
            studentsAndNumberOfCourses.put(i, numberOfCoursesPerStudent);
        }
        
        List<List<Integer>> courseIndexPerStudent = new ArrayList<>();
        
        for (int i = 0; i < studentsNumber; i++) {
            List<Integer> cache = new ArrayList<>();
            
            for (int j = 0; j < studentsAndNumberOfCourses.get(i); j++) {
                int courseIndex = new Random().nextInt(coursesNumber);
                List<Integer> studentAndCourseIndex = new ArrayList<>();
                
                if (!cache.contains(courseIndex)) {
                    cache.add(courseIndex);
                    studentAndCourseIndex.add(i);
                    studentAndCourseIndex.add(courseIndex);
                    courseIndexPerStudent.add(studentAndCourseIndex);
                } else {
                    j -= ONE;
                }
            }
        }
        return courseIndexPerStudent;
    }
            
    public List<Integer> getNumberOfStudentsInGroup(int studentsNumber, int groupsNumber) {
        List<Integer> result = new ArrayList<>();
        int noGroupStudents = new Random().nextInt(MAX_NO_GROUP_STUDENTS); 
        int remainder = studentsNumber - noGroupStudents;

        for (int i = 0; i < groupsNumber; i++) {
            double probability = new Random().nextInt(INT_PROBABILITY_VALUE) / DOUBLE_PROBABILITY_VALUE;
            int zeroProbability = (int) ((probability + ONE_HALF) - (probability - ONE_HALF));
            double amplitudeProbability = new Random().nextInt(INT_AMPL_PROBABILITY) / DOUBLE_AMPL_PROBABILITY;
            int studentsInGroup = (int) (zeroProbability * (MIN_STUDENTS + AMPLITUDE * amplitudeProbability));

            if (studentsInGroup <= remainder && remainder != 0 || studentsInGroup == 0) {
                result.add(studentsInGroup);
                remainder -= studentsInGroup;
            } else if (studentsInGroup > remainder && remainder >= MIN_STUDENTS) {
                result.add(remainder);
                remainder = 0;
            } else if (remainder != 0) {
                for (int j = 0; j < result.size(); j++) {
                    if (result.get(j) < MAX_STUDENTS
                        && result.get(j) >= MIN_STUDENTS 
                        && remainder != 0) {
                        result.set(j, result.get(j) + ONE_STUDENT);
                        remainder -= ONE_STUDENT;

                        if (j == (result.size() - ONE) && remainder > 0) {
                            j = 0;
                        } else if (remainder == 0) {
                            result.add(ZERO_OF_STUDENTS);
                            break;
                        }
                    } 
                }
            } else {
                result.add(ZERO_OF_STUDENTS);
            }
        }
        return result;
    }

    public List<StudentDTO> getStudentData(List<String> firstNames, List<String> lastNames) {
        return Stream.generate(() -> new StudentDTO(firstNames.get(new Random().nextInt(firstNames.size())),
                                                    lastNames.get(new Random().nextInt(lastNames.size()))))
                     .limit(200).collect(Collectors.toList());
    }

    public List<String> getGroupData() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        return Stream.generate(() -> new StringBuilder()
                .append(alphabet.charAt(new Random().nextInt(alphabet.length())))
                .append(alphabet.charAt(new Random().nextInt(alphabet.length()))).append(HYPHEN)
                .append(new Random().nextInt(MAX_COURSE_INDEX)).append(new Random().nextInt(MAX_COURSE_INDEX)).toString())
            .limit(MAX_COURSES_NUMBER).collect(Collectors.toList());
    }
}
