package ua.com.foxminded.sql_jdbc_school.services;

import java.util.List;
import java.util.stream.Collectors;

public class Parser {
    public String toString(List<String> list) {
        return list.stream().collect(Collectors.joining("\n"));
    }
}
