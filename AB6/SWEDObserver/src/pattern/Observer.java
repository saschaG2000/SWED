package pattern;

import model.User;

public interface Observer {
    void update(User user, String message);
}