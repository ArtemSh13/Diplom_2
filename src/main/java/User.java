public class User {
    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJsonWithoutEmail() {
        //return "{\"email\": \"" + this.email + "\", \"password\": \"" + this.password + "\", \"name\": \"" + this.name + "\"}";
        return "{\"password\": \"" + this.password + "\", \"name\": \"" + this.name + "\"}";
    }

    public String getJsonWithoutPassword() {
        return "{\"email\": \"" + this.email + "\", \"name\": \"" + this.name + "\"}";
    }

    public String getJsonWithoutName() {
        return "{\"email\": \"" + this.email + "\", \"password\": \"" + this.password + "\"}";
    }
}
