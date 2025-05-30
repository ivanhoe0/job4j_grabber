package ru.job4j;

import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.*;
import ru.job4j.grabber.stores.JdbcStore;
import ru.job4j.grabber.stores.MemStore;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

public class Main {
    public static void main(String[] args) {
        var config = new Config();
        config.load("application.properties");
        var store = new JdbcStore(config.initConnection());
        var dateTime = new HabrCareerDateTimeParser();
        var parser = new HabrCareerParse(dateTime);
        parser.fetch().forEach(store::save);
        var scheduler = new SchedulerManager();
        scheduler.init();
        scheduler.load(
                Integer.parseInt(config.get("rabbit.interval")),
                SuperJobGrab.class,
                store);
        new Web(store).start(Integer.parseInt(config.get("server.port")));
    }
}