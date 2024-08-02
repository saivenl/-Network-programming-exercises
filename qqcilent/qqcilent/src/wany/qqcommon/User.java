package wany.qqcommon;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String password;
    public User(){}

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;

    }

    public String getUserId() {

        return userId;
    }
    public String getPasswd() {

        return password;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPasswd(String pwd) {
        this.password = pwd;
    }
}
