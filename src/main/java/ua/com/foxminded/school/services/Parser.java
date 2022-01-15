package ua.com.foxminded.school.services;

import java.util.List;
import java.util.stream.Collectors;

public class Parser {
    public String toStringList(List<String> list) {
        return list.stream().collect(Collectors.joining("\n"));
    }
}
