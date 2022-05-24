package ua.com.foxminded.sql_jdbc_school.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    
    public String toString(List<String> list) {
        try(Stream<String> stream = list.stream()) {
            return stream.collect(Collectors.joining("\n"));
        }
    }
}
