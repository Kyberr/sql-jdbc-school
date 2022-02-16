package ua.com.foxminded.sql_jdbc_school.view;

public interface MenuView<T, E, S, F, M> {
    
    public void addStudentToCourseOrReturnMenu();
    public void studentHasNotBeenAddedToCourse();
    public void studentHasBeenAddedToCourse();
    public void deleteStudentOrReturnMenu();
    public void studentHasNotBeenDeleted(M studentID);
    public void studentHasBeenDeleted(M studentId);
    public void enterStudentId();
    public void showStudents(F students);
    public void studentHasBeenAddedToDatabase();
    public void addStudentToDatabaseOrReturnMenu();
    public void addStudentYesOrNo();
    public void enterFirstName();
    public void enterLastName();
    public void showStudentsOfCourse(S studentCourseList);
    public void executionHasBeenStopped();
    public void returnMenuOrExit();
    public void showCourses(E coursesList);
    public void showNumberOfStudentsInGroups(T groupsList);
    public void enterNumberOfStudents();
    public void enterCourseId();
    public void showMenuItems();
    public void showIncorrectInputWarning();
}
