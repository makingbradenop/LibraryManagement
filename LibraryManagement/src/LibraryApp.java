import java.util.List;
import java.util.Scanner;

public class LibraryApp {
    private static DatabaseManager db;
    private static BookDAO bookDAO;
    private static MemberDAO memberDAO;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        db = new DatabaseManager();
        bookDAO = new BookDAO(db.getConnection());
        memberDAO = new MemberDAO(db.getConnection());

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║     📚 Library Management System     ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> bookMenu();
                case 2 -> memberMenu();
                case 3 -> borrowingMenu();
                case 0 -> running = false;
                default -> System.out.println("⚠️  Invalid choice. Try again.");
            }
        }

        db.close();
        System.out.println("\n👋 Goodbye!");
    }

    // ───────── MENUS ─────────

    static void printMainMenu() {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("       MAIN MENU");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  1. 📖 Book Management");
        System.out.println("  2. 👤 Member Management");
        System.out.println("  3. 🔄 Borrow / Return");
        System.out.println("  0. ❌ Exit");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    static void bookMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n── Book Management ──");
            System.out.println("  1. Add Book");
            System.out.println("  2. View All Books");
            System.out.println("  3. View Book by ID");
            System.out.println("  4. Update Book");
            System.out.println("  5. Delete Book");
            System.out.println("  0. Back");
            int choice = readInt("Choice: ");
            switch (choice) {
                case 1 -> addBook();
                case 2 -> viewAllBooks();
                case 3 -> viewBookById();
                case 4 -> updateBook();
                case 5 -> deleteBook();
                case 0 -> back = true;
                default -> System.out.println("⚠️  Invalid choice.");
            }
        }
    }

    static void memberMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n── Member Management ──");
            System.out.println("  1. Register Member");
            System.out.println("  2. View All Members");
            System.out.println("  3. View Member by ID");
            System.out.println("  4. Update Member");
            System.out.println("  5. Delete Member");
            System.out.println("  0. Back");
            int choice = readInt("Choice: ");
            switch (choice) {
                case 1 -> addMember();
                case 2 -> viewAllMembers();
                case 3 -> viewMemberById();
                case 4 -> updateMember();
                case 5 -> deleteMember();
                case 0 -> back = true;
                default -> System.out.println("⚠️  Invalid choice.");
            }
        }
    }

    static void borrowingMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n── Borrow / Return ──");
            System.out.println("  1. Borrow a Book");
            System.out.println("  2. Return a Book");
            System.out.println("  3. View Member's Borrowed Books");
            System.out.println("  0. Back");
            int choice = readInt("Choice: ");
            switch (choice) {
                case 1 -> borrowBook();
                case 2 -> returnBook();
                case 3 -> viewBorrowedBooks();
                case 0 -> back = true;
                default -> System.out.println("⚠️  Invalid choice.");
            }
        }
    }

    // ───────── BOOK OPERATIONS ─────────

    static void addBook() {
        System.out.println("\n── Add New Book ──");
        String title  = readString("Title: ");
        String author = readString("Author: ");
        String isbn   = readString("ISBN: ");
        String genre  = readString("Genre: ");
        int year      = readInt("Publication Year: ");
        bookDAO.addBook(new Book(title, author, isbn, genre, year));
    }

    static void viewAllBooks() {
        List<Book> books = bookDAO.getAllBooks();
        System.out.println("\n── All Books ──");
        if (books.isEmpty()) { System.out.println("  No books found."); return; }
        System.out.println(Book.getDivider());
        System.out.println(Book.getHeader());
        System.out.println(Book.getDivider());
        books.forEach(System.out::println);
        System.out.println(Book.getDivider());
        System.out.println("Total: " + books.size() + " book(s)");
    }

    static void viewBookById() {
        int id = readInt("Enter Book ID: ");
        Book book = bookDAO.getBookById(id);
        if (book == null) { System.err.println("❌ Book not found."); return; }
        System.out.println(Book.getDivider());
        System.out.println(Book.getHeader());
        System.out.println(Book.getDivider());
        System.out.println(book);
        System.out.println(Book.getDivider());
    }

    static void updateBook() {
        int id = readInt("Enter Book ID to update: ");
        Book existing = bookDAO.getBookById(id);
        if (existing == null) { System.err.println("❌ Book not found."); return; }
        System.out.println("Current: " + existing.getTitle() + " by " + existing.getAuthor());
        System.out.println("(Press Enter to keep current value)");
        String title  = readStringOrDefault("New Title: ", existing.getTitle());
        String author = readStringOrDefault("New Author: ", existing.getAuthor());
        String genre  = readStringOrDefault("New Genre: ", existing.getGenre());
        int year      = readIntOrDefault("New Year: ", existing.getYear());
        bookDAO.updateBook(id, title, author, genre, year);
    }

    static void deleteBook() {
        int id = readInt("Enter Book ID to delete: ");
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("yes")) bookDAO.deleteBook(id);
        else System.out.println("Cancelled.");
    }

    // ───────── MEMBER OPERATIONS ─────────

    static void addMember() {
        System.out.println("\n── Register New Member ──");
        String name  = readString("Name: ");
        String email = readString("Email: ");
        String phone = readString("Phone: ");
        memberDAO.addMember(new Member(name, email, phone));
    }

    static void viewAllMembers() {
        List<Member> members = memberDAO.getAllMembers();
        System.out.println("\n── All Members ──");
        if (members.isEmpty()) { System.out.println("  No members found."); return; }
        System.out.println(Member.getDivider());
        System.out.println(Member.getHeader());
        System.out.println(Member.getDivider());
        members.forEach(System.out::println);
        System.out.println(Member.getDivider());
        System.out.println("Total: " + members.size() + " member(s)");
    }

    static void viewMemberById() {
        int id = readInt("Enter Member ID: ");
        Member member = memberDAO.getMemberById(id);
        if (member == null) { System.err.println("❌ Member not found."); return; }
        System.out.println(Member.getDivider());
        System.out.println(Member.getHeader());
        System.out.println(Member.getDivider());
        System.out.println(member);
        System.out.println(Member.getDivider());
    }

    static void updateMember() {
        int id = readInt("Enter Member ID to update: ");
        Member existing = memberDAO.getMemberById(id);
        if (existing == null) { System.err.println("❌ Member not found."); return; }
        System.out.println("(Press Enter to keep current value)");
        String name  = readStringOrDefault("New Name: ", existing.getName());
        String email = readStringOrDefault("New Email: ", existing.getEmail());
        String phone = readStringOrDefault("New Phone: ", existing.getPhone());
        memberDAO.updateMember(id, name, email, phone);
    }

    static void deleteMember() {
        int id = readInt("Enter Member ID to delete: ");
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("yes")) memberDAO.deleteMember(id);
        else System.out.println("Cancelled.");
    }

    // ───────── BORROWING OPERATIONS ─────────

    static void borrowBook() {
        viewAllBooks();
        int bookId = readInt("Enter Book ID to borrow: ");
        viewAllMembers();
        int memberId = readInt("Enter Member ID: ");
        bookDAO.borrowBook(bookId, memberId);
    }

    static void returnBook() {
        int bookId = readInt("Enter Book ID to return: ");
        bookDAO.returnBook(bookId);
    }

    static void viewBorrowedBooks() {
        int memberId = readInt("Enter Member ID: ");
        Member member = memberDAO.getMemberById(memberId);
        if (member == null) { System.err.println("❌ Member not found."); return; }
        System.out.println("Member: " + member.getName());
        memberDAO.showBorrowedBooks(memberId);
    }

    // ───────── HELPERS ─────────

    static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    static String readStringOrDefault(String prompt, String defaultVal) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultVal : input;
    }

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Please enter a valid number.");
            }
        }
    }

    static int readIntOrDefault(String prompt, int defaultVal) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return defaultVal;
        try { return Integer.parseInt(input); }
        catch (NumberFormatException e) { return defaultVal; }
    }
}
