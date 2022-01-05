/*class Main {
    public static void main(String[] args) {
        User user1 = new User();
        user1.setFirstName("хуй");
        user1.setLastName(null);
        System.out.println(user1.getFullName());
    }
} */
class User {
    private String firstName;
    private String lastName;

    public User() {
        this.firstName = "";
        this.lastName = "";
    }

    public void setFirstName(String firstName) {
        if (firstName != null){
            this.firstName = firstName;
        }
    }

    public void setLastName(String lastName) {
        // write your code here
        if (lastName != null){
            this.lastName = lastName;
        }
    }

    public String getFullName() {
        if (firstName == "" && lastName == "") {
            return "Unknown";
        } else if (firstName == "") {
            return lastName;
        } else if (lastName == ""){
            return firstName;
        } else {
            return firstName + " " + lastName;
        }
    }
}