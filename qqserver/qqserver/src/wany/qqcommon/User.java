package wany.qqcommon;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String password;

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;

    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

}