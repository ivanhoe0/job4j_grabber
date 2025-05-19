package ru.job4j.model;

import java.util.Objects;

public class Post {
    private Long id;
    private String title;
    private String link;
    private String description;
    private Long time;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(link, post.link) && Objects.equals(time, post.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link, time);
    }
}
