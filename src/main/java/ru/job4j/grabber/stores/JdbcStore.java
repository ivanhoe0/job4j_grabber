package ru.job4j.grabber.stores;

import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcStore implements Store {
    private final Connection connection;

    public JdbcStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Post post) {
        try (var statement = connection.prepareStatement("INSERT INTO post(name, text, link, created) VAlUES(?, ?, ?, ?) ON CONFLICT DO NOTHING",
                Statement.RETURN_GENERATED_KEYS
                )) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, new Timestamp(post.getTime()));
            statement.execute();
            try (var set = statement.getGeneratedKeys()) {
                if (set.next()) {
                    post.setId(set.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        var result = new ArrayList<Post>();
        try (var statement = connection.prepareStatement("SELECT * FROM post")) {
            try (var set = statement.executeQuery()) {
                while (set.next()) {
                    result.add(
                            makePost(set)
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Optional<Post> findById(Long id) {
        Optional<Post> result = Optional.empty();
        try (var statement = connection.prepareStatement("SELECT * FROM post WHERE ID = ?")) {
            statement.setLong(1, id);
            try (var set = statement.getResultSet()) {
                if (set.next()) {
                    result = Optional.of(makePost(set));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Post makePost(ResultSet set) throws SQLException {
     return new Post(
             set.getLong(1),
             set.getString(2),
             set.getString(3),
             set.getString(4),
             set.getTimestamp(5).getTime());
    }
}