package ua.com.foxminded.sql_jdbc_school.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    
    public String toStringList(List<String> list) {
        try(Stream<String> stream = list.stream()) {
            return stream.collect(Collectors.joining("\n"));
        }
    }
}
