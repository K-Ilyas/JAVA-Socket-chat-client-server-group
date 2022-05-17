import java.io.Serializable;
import java.lang.Comparable;

public class UserInformation implements Comparable<UserInformation>, Serializable {

    private String pseudo = "";
    private String password = "";

    public UserInformation(String pseudo, String password) {

        this.pseudo = pseudo;
        this.password = password;

    }

    public int compareTo(UserInformation o) {

        if (o.getClass().equals(UserInformation.class)) {
            UserInformation cd = (UserInformation) o;
            return this.pseudo.compareTo(cd.getPassword()) & this.password.compareTo(cd.getPassword());

        }
        return -1;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPesudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
