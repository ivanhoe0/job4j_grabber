package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class HabrCareerDateTimeParserTest {
    @Test
    void when23OfMay() {
        String input = "2025-05-23T18:27:09+03:00";
        LocalDateTime output = new HabrCareerDateTimeParser().parse(input);
        assertThat(output).isEqualTo(LocalDateTime.of(2025, 5, 23, 18, 27, 9));
    }

    @Test
    void when17OfMay() {
        String input = "2025-05-17T00:00:00+03:00";
        LocalDateTime output = new HabrCareerDateTimeParser().parse(input);
        assertThat(output).isEqualTo(LocalDateTime.of(2025, 5, 17, 0, 0, 0));
    }
}