import java.io.Serializable;

public class userinfo implements Serializable {
    int id;
    String organization,username,password;
    public userinfo(int id,String organization, String username,String password) {
        this.id=id;
        this.organization=organization;
        this.username=username;
        this.password=password;
    }
}
