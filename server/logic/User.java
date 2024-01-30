package server.logic;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 * Nuestra clase POJO.
 */
public class User {
    private int id;  //Clave principal
    private String name;
    private String email;
    private String passwd;

    
    public User(String name, String email, String passwd) {
        this.name = name;
        this.email = email;
        this.passwd = passwd;
    }

    public User(int id, String name, String email, String passwd) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwd = passwd;
    }
    public User(String args[]){
        this.name = args[0];
        this.email = args[1];
        this.passwd = args[2];
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPasswd() {
        return passwd;
    }
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", passwd=" + passwd + "]";
    }
    
}
