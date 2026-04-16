public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private int year;
    private boolean available;

    public Book(String title, String author, String isbn, String genre, int year) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.year = year;
        this.available = true;
    }

    public Book(int id, String title, String author, String isbn, String genre, int year, boolean available) {
        this(title, author, isbn, genre, year);
        this.id = id;
        this.available = available;
    }

    // Getters & Setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public String getGenre() { return genre; }
    public int getYear() { return year; }
    public boolean isAvailable() { return available; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setYear(int year) { this.year = year; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("| %-4d | %-30s | %-20s | %-15s | %-12s | %-6d | %-10s |",
                id, title, author, isbn, genre, year, available ? "Available" : "Borrowed");
    }

    public static String getHeader() {
        return String.format("| %-4s | %-30s | %-20s | %-15s | %-12s | %-6s | %-10s |",
                "ID", "Title", "Author", "ISBN", "Genre", "Year", "Status");
    }

    public static String getDivider() {
        return "-".repeat(115);
    }
}
