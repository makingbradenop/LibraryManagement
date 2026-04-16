import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    private Connection conn;

    public MemberDAO(Connection conn) {
        this.conn = conn;
    }

    // CREATE
    public boolean addMember(Member member) {
        String sql = "INSERT INTO members (name, email, phone) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.executeUpdate();
            System.out.println("✅ Member registered successfully!");
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                System.err.println("❌ A member with that email already exists.");
            } else {
                System.err.println("❌ Error adding member: " + e.getMessage());
            }
            return false;
        }
    }

    // READ ALL
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("joined_date")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching members: " + e.getMessage());
        }
        return members;
    }

    // READ ONE
    public Member getMemberById(int id) {
        String sql = "SELECT * FROM members WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Member(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("joined_date")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching member: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateMember(int id, String name, String email, String phone) {
        String sql = "UPDATE members SET name=?, email=?, phone=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setInt(4, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Member updated successfully!");
                return true;
            } else {
                System.err.println("❌ No member found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error updating member: " + e.getMessage());
        }
        return false;
    }

    // DELETE
    public boolean deleteMember(int id) {
        String sql = "DELETE FROM members WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Member deleted successfully!");
                return true;
            } else {
                System.err.println("❌ No member found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error deleting member: " + e.getMessage());
        }
        return false;
    }

    // GET BORROWED BOOKS BY MEMBER
    public void showBorrowedBooks(int memberId) {
        String sql = """
            SELECT b.title, b.author, br.borrow_date
            FROM borrowings br
            JOIN books b ON br.book_id = b.id
            WHERE br.member_id = ? AND br.return_date IS NULL
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            System.out.println("\n📚 Currently Borrowed Books:");
            System.out.println("-".repeat(65));
            System.out.printf("| %-30s | %-20s | %-10s |\n", "Title", "Author", "Borrow Date");
            System.out.println("-".repeat(65));
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("| %-30s | %-20s | %-10s |\n",
                        rs.getString("title"), rs.getString("author"), rs.getString("borrow_date"));
            }
            if (!found) System.out.println("  No books currently borrowed.");
            System.out.println("-".repeat(65));
        } catch (SQLException e) {
            System.err.println("❌ Error fetching borrowed books: " + e.getMessage());
        }
    }
}
