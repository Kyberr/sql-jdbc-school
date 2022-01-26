package ua.com.foxminded.university.services;

import ua.com.foxminded.university.services.ServicesException.TableCreationFail;

public interface TableService {
    
    public int createTables() throws TableCreationFail;
}
