package be.sam.application.Database;

public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int rights;

    public User() { }

    public User(String firstName, String lastName, String email, String password, int rights) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.rights = rights;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRights() {return rights; }

    public void setRights(int rights) {this.rights = rights; }


    public String toString() {
        String rights;
        if(this.getRights() == 0) rights = "R";
        else if(this.getRights() == 1) rights = "R&W";
        else rights = "SU";
        return  "ID : "+this.getId()+ "\n"+
                "Name : "+this.getFirstName()+" "+this.getLastName()+"\n"
                + "Email : "+this.getEmail() +"\n"
                +"Rights : " +rights+
                "\n\n";
    }




}
