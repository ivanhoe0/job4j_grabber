package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerDateTimeParser implements DateTimeParser {

    @Override
    public LocalDateTime parse(String parse) {
        var formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return LocalDateTime.from(formatter.parse(parse));
    }

}