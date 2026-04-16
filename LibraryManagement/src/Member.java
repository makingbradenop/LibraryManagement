public class Member {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String joinedDate;

    public Member(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Member(int id, String name, String email, String phone, String joinedDate) {
        this(name, email, phone);
        this.id = id;
        this.joinedDate = joinedDate;
    }

    // Getters & Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getJoinedDate() { return joinedDate; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return String.format("| %-4d | %-25s | %-30s | %-15s | %-12s |",
                id, name, email, phone, joinedDate);
    }

    public static String getHeader() {
        return String.format("| %-4s | %-25s | %-30s | %-15s | %-12s |",
                "ID", "Name", "Email", "Phone", "Joined Date");
    }

    public static String getDivider() {
        return "-".repeat(97);
    }
}
