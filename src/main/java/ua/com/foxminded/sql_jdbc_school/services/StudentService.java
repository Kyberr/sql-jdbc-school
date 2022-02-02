package ua.com.foxminded.sql_jdbc_school.services;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;

public interface StudentService {
    
    public Integer createStudents() throws ServicesException.StudentCreationFail;
    
    public List<StudentDTO> assignGroup() throws ServicesException.AssignGgoupToStudentsFail;
}
