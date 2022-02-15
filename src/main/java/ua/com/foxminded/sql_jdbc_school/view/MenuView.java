package ua.com.foxminded.sql_jdbc_school.view;

public interface MenuView<T, E, S, F, M> {
    
    public void showDeleteStudentOrReturnToMenu();
    public void showStudentHasNotBeenDeletedMessage(M studentID);
    public void showStudentHasBeenDeletedMessage(M studentId);
    public void showStudentIdInputMessage();
    public void showStudents(F students);
    public void studentHasBeenAddedMessage();
    public void addStudentOrReturnMainMenuMessage();
    public void confirmingMessage();
    public void showFirstNameInputMessage();
    public void showLastNameInputMessage();
    public void showStudentsOfCourse(S studentCourseList);
    public void showFinalProgramMessage();
    public void showFinalItemMessage();
    public void showCourses(E coursesList);
    public void showStudentsNumberOfGroups(T groupsList);
    public void showStudentsNumberInputMessage();
    public void showCourseIdInputMessage();
    public void showMenuItems();
    public void showWrongInputWarning();
}
