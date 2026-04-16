import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private Connection conn;

    public BookDAO(Connection conn) {
        this.conn = conn;
    }

    // CREATE
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, genre, year) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setString(4, book.getGenre());
            ps.setInt(5, book.getYear());
            ps.executeUpdate();
            System.out.println("✅ Book added successfully!");
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                System.err.println("❌ A book with that ISBN already exists.");
            } else {
                System.err.println("❌ Error adding book: " + e.getMessage());
            }
            return false;
        }
    }

    // READ ALL
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("genre"),
                        rs.getInt("year"),
                        rs.getInt("available") == 1
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching books: " + e.getMessage());
        }
        return books;
    }

    // READ ONE
    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("genre"),
                        rs.getInt("year"),
                        rs.getInt("available") == 1
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching book: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateBook(int id, String title, String author, String genre, int year) {
        String sql = "UPDATE books SET title=?, author=?, genre=?, year=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, genre);
            ps.setInt(4, year);
            ps.setInt(5, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Book updated successfully!");
                return true;
            } else {
                System.err.println("❌ No book found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error updating book: " + e.getMessage());
        }
        return false;
    }

    // DELETE
    public boolean deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Book deleted successfully!");
                return true;
            } else {
                System.err.println("❌ No book found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error deleting book: " + e.getMessage());
        }
        return false;
    }

    // BORROW
    public boolean borrowBook(int bookId, int memberId) {
        Book book = getBookById(bookId);
        if (book == null) { System.err.println("❌ Book not found."); return false; }
        if (!book.isAvailable()) { System.err.println("❌ Book is already borrowed."); return false; }

        String updateBook = "UPDATE books SET available=0 WHERE id=?";
        String insertBorrow = "INSERT INTO borrowings (book_id, member_id) VALUES (?, ?)";
        try (PreparedStatement ps1 = conn.prepareStatement(updateBook);
             PreparedStatement ps2 = conn.prepareStatement(insertBorrow)) {
            ps1.setInt(1, bookId);
            ps1.executeUpdate();
            ps2.setInt(1, bookId);
            ps2.setInt(2, memberId);
            ps2.executeUpdate();
            System.out.println("✅ Book borrowed successfully!");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error borrowing book: " + e.getMessage());
        }
        return false;
    }

    // RETURN
    public boolean returnBook(int bookId) {
        Book book = getBookById(bookId);
        if (book == null) { System.err.println("❌ Book not found."); return false; }
        if (book.isAvailable()) { System.err.println("❌ Book is not currently borrowed."); return false; }

        String updateBook = "UPDATE books SET available=1 WHERE id=?";
        String updateBorrow = "UPDATE borrowings SET return_date=DATE('now') WHERE book_id=? AND return_date IS NULL";
        try (PreparedStatement ps1 = conn.prepareStatement(updateBook);
             PreparedStatement ps2 = conn.prepareStatement(updateBorrow)) {
            ps1.setInt(1, bookId);
            ps1.executeUpdate();
            ps2.setInt(1, bookId);
            ps2.executeUpdate();
            System.out.println("✅ Book returned successfully!");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error returning book: " + e.getMessage());
        }
        return false;
    }
}
