package ru.job4j.grabber.service;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import ru.job4j.grabber.model.Post;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final Logger LOG = Logger.getLogger(HabrCareerParse.class);
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PREFIX = "/vacancies?page=";
    private static final String SUFFIX = "&q=Java%20developer&type=all";

    private static final int NUMBER_OF_PAGES_TO_PARSE = 5;

    @Override
    public List<Post> fetch() {
        var result = new ArrayList<Post>();
        try {
            for (int pageNumber = 1; pageNumber <= NUMBER_OF_PAGES_TO_PARSE; pageNumber++) {
                String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
                var connection = Jsoup.connect(fullLink);
                var document = connection.get();
                var rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    var titleElement = row.select(".vacancy-card__title").first();
                    var dateElement = row.select(".vacancy-card__date").first();
                    var linkElement = titleElement.child(0);
                    String vacancyName = titleElement.text();
                    String vacancyDate = dateElement.child(0).attr("datetime");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[XXX][X]");
                    Long time = Timestamp.valueOf(LocalDateTime.from(formatter.parse(vacancyDate))).getTime();
                    String link = String.format("%s%s", SOURCE_LINK,
                            linkElement.attr("href"));
                    var description = retrieveDescription(link);
                    var post = new Post();
                    post.setTitle(vacancyName);
                    post.setLink(link);
                    post.setDescription(description);
                    post.setTime(time);
                    result.add(post);
                });
            }
        } catch (IOException e) {
            LOG.error("When load page", e);
        }
        return result;
    }

    private String retrieveDescription(String link) {
        String result = "";
        try {
            var connection = Jsoup.connect(link);
            var document = connection.get();
            var row = document.select(".vacancy-description__text");
            result = row.text();
        } catch (IOException e) {
            LOG.error("When retrieve a description", e);
        }
        return result;
    }

    public static void main(String[] args) {
        var parser = new HabrCareerParse();
        parser.fetch().forEach(p -> System.out.printf("%s %s %s%n", p.getTitle(), p.getLink(), new Timestamp(p.getTime())));
    }
}