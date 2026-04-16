# 📚 Library Management System
> Java + SQLite console application with full CRUD operations

## Project Structure
```
LibraryManagement/
├── src/
│   ├── LibraryApp.java      # Main application & console menu
│   ├── DatabaseManager.java # SQLite connection & table setup
│   ├── Book.java            # Book model
│   ├── Member.java          # Member model
│   ├── BookDAO.java         # Book CRUD + Borrow/Return operations
│   └── MemberDAO.java       # Member CRUD operations
├── lib/                     # SQLite JDBC driver (auto-downloaded)
├── out/                     # Compiled .class files
├── run.sh                   # Build + run script (Linux/Mac)
└── README.md
```

## Prerequisites
- Java 17+ installed
- Internet connection (first run downloads SQLite JDBC driver)

## How to Run

### Linux / macOS
```bash
chmod +x run.sh
./run.sh
```

### Windows
```cmd
# Download sqlite-jdbc-3.45.1.0.jar from Maven Central into lib/
# Then compile and run:
javac -cp lib/sqlite-jdbc.jar -d out src/*.java
java -cp "out;lib/sqlite-jdbc.jar" LibraryApp
```

## Features

### 📖 Book Management
| Operation | Description               |
|-----------|---------------------------|
| Add       | Add new book with details |
| View All  | List all books + status   |
| View One  | Lookup book by ID         |
| Update    | Edit book details         |
| Delete    | Remove a book             |

### 👤 Member Management
| Operation | Description                 |
|-----------|-----------------------------|
| Register  | Add new library member      |
| View All  | List all members            |
| View One  | Lookup member by ID         |
| Update    | Edit member details         |
| Delete    | Remove a member             |

### 🔄 Borrow / Return
| Operation       | Description                        |
|-----------------|------------------------------------|
| Borrow Book     | Assign a book to a member          |
| Return Book     | Mark a book as returned            |
| View Borrowed   | List active borrowings by member   |

## Database Schema
```sql
books       (id, title, author, isbn, genre, year, available)
members     (id, name, email, phone, joined_date)
borrowings  (id, book_id, member_id, borrow_date, return_date)
```

The database file `library.db` is created automatically in the project root.
