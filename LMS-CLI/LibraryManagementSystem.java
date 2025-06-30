import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

// Book class to represent books in the library
class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private boolean isAvailable;
    private String borrowedBy;
    private LocalDate borrowDate;
    private LocalDate dueDate;

    public Book(String isbn, String title, String author, String genre) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = true;
        this.borrowedBy = null;
        this.borrowDate = null;
        this.dueDate = null;
    }

    // Getters and setters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public boolean isAvailable() { return isAvailable; }
    public String getBorrowedBy() { return borrowedBy; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }

    public void setAvailable(boolean available) { this.isAvailable = available; }
    public void setBorrowedBy(String borrowedBy) { this.borrowedBy = borrowedBy; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Borrowed by " + borrowedBy;
        return String.format("ISBN: %s | Title: %s | Author: %s | Genre: %s | Status: %s",
                isbn, title, author, genre, status);
    }
}

// Member class to represent library members
class Member implements Serializable {
    private static final long serialVersionUID = 1L;
    private String memberId;
    private String name;
    private String email;
    private String phone;
    private List<String> borrowedBooks;
    private LocalDate membershipDate;

    public Member(String memberId, String name, String email, String phone) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.borrowedBooks = new ArrayList<>();
        this.membershipDate = LocalDate.now();
    }

    // Getters and setters
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public List<String> getBorrowedBooks() { return borrowedBooks; }
    public LocalDate getMembershipDate() { return membershipDate; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }

    public void borrowBook(String isbn) {
        if (!borrowedBooks.contains(isbn)) {
            borrowedBooks.add(isbn);
        }
    }

    public void returnBook(String isbn) {
        borrowedBooks.remove(isbn);
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Email: %s | Phone: %s | Books Borrowed: %d | Member Since: %s",
                memberId, name, email, phone, borrowedBooks.size(), membershipDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
}

// Library class to manage books and members
class Library {
    private Map<String, Book> books;
    private Map<String, Member> members;
    private static final String BOOKS_FILE = "books.dat";
    private static final String MEMBERS_FILE = "members.dat";
    private static final int MAX_BORROW_DAYS = 14;
    private static final int MAX_BOOKS_PER_MEMBER = 5;

    public Library() {
        books = new HashMap<>();
        members = new HashMap<>();
        loadData();
    }

    // Book management methods
    public boolean addBook(String isbn, String title, String author, String genre) {
        if (books.containsKey(isbn)) {
            System.out.println("Book with ISBN " + isbn + " already exists!");
            return false;
        }
        books.put(isbn, new Book(isbn, title, author, genre));
        saveData();
        return true;
    }

    public boolean removeBook(String isbn) {
        if (!books.containsKey(isbn)) {
            System.out.println("Book with ISBN " + isbn + " not found!");
            return false;
        }
        Book book = books.get(isbn);
        if (!book.isAvailable()) {
            System.out.println("Cannot remove book - it is currently borrowed!");
            return false;
        }
        books.remove(isbn);
        saveData();
        return true;
    }

    public void searchBooks(String query) {
        List<Book> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(lowerQuery) ||
                book.getAuthor().toLowerCase().contains(lowerQuery) ||
                book.getGenre().toLowerCase().contains(lowerQuery) ||
                book.getIsbn().toLowerCase().contains(lowerQuery)) {
                results.add(book);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No books found matching your search.");
        } else {
            System.out.println("\n=== Search Results ===");
            for (Book book : results) {
                System.out.println(book);
            }
        }
    }

    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        System.out.println("\n=== All Books ===");
        for (Book book : books.values()) {
            System.out.println(book);
        }
    }

    // Member management methods
    public boolean addMember(String memberId, String name, String email, String phone) {
        if (members.containsKey(memberId)) {
            System.out.println("Member with ID " + memberId + " already exists!");
            return false;
        }
        members.put(memberId, new Member(memberId, name, email, phone));
        saveData();
        return true;
    }

    public boolean removeMember(String memberId) {
        if (!members.containsKey(memberId)) {
            System.out.println("Member with ID " + memberId + " not found!");
            return false;
        }
        Member member = members.get(memberId);
        if (!member.getBorrowedBooks().isEmpty()) {
            System.out.println("Cannot remove member - they have borrowed books!");
            return false;
        }
        members.remove(memberId);
        saveData();
        return true;
    }

    public void displayAllMembers() {
        if (members.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }
        System.out.println("\n=== All Members ===");
        for (Member member : members.values()) {
            System.out.println(member);
        }
    }

    // Borrowing and returning methods
    public boolean borrowBook(String isbn, String memberId) {
        if (!books.containsKey(isbn)) {
            System.out.println("Book with ISBN " + isbn + " not found!");
            return false;
        }
        if (!members.containsKey(memberId)) {
            System.out.println("Member with ID " + memberId + " not found!");
            return false;
        }

        Book book = books.get(isbn);
        Member member = members.get(memberId);

        if (!book.isAvailable()) {
            System.out.println("Book is already borrowed!");
            return false;
        }

        if (member.getBorrowedBooks().size() >= MAX_BOOKS_PER_MEMBER) {
            System.out.println("Member has reached maximum borrowing limit!");
            return false;
        }

        // Borrow the book
        book.setAvailable(false);
        book.setBorrowedBy(memberId);
        book.setBorrowDate(LocalDate.now());
        book.setDueDate(LocalDate.now().plusDays(MAX_BORROW_DAYS));
        member.borrowBook(isbn);
        
        saveData();
        System.out.println("Book borrowed successfully! Due date: " + 
                         book.getDueDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        return true;
    }

    public boolean returnBook(String isbn) {
        if (!books.containsKey(isbn)) {
            System.out.println("Book with ISBN " + isbn + " not found!");
            return false;
        }

        Book book = books.get(isbn);
        if (book.isAvailable()) {
            System.out.println("Book is not currently borrowed!");
            return false;
        }

        String memberId = book.getBorrowedBy();
        Member member = members.get(memberId);

        // Check for overdue
        if (LocalDate.now().isAfter(book.getDueDate())) {
            long overdueDays = LocalDate.now().toEpochDay() - book.getDueDate().toEpochDay();
            System.out.println("Book is overdue by " + overdueDays + " days!");
        }

        // Return the book
        book.setAvailable(true);
        book.setBorrowedBy(null);
        book.setBorrowDate(null);
        book.setDueDate(null);
        member.returnBook(isbn);

        saveData();
        System.out.println("Book returned successfully!");
        return true;
    }

    public void displayBorrowedBooks() {
        List<Book> borrowedBooks = new ArrayList<>();
        for (Book book : books.values()) {
            if (!book.isAvailable()) {
                borrowedBooks.add(book);
            }
        }

        if (borrowedBooks.isEmpty()) {
            System.out.println("No books are currently borrowed.");
            return;
        }

        System.out.println("\n=== Currently Borrowed Books ===");
        for (Book book : borrowedBooks) {
            Member member = members.get(book.getBorrowedBy());
            String status = LocalDate.now().isAfter(book.getDueDate()) ? " (OVERDUE)" : "";
            System.out.println(book + " | Borrowed by: " + member.getName() + 
                             " | Due: " + book.getDueDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + status);
        }
    }

    public void displayOverdueBooks() {
        List<Book> overdueBooks = new ArrayList<>();
        for (Book book : books.values()) {
            if (!book.isAvailable() && LocalDate.now().isAfter(book.getDueDate())) {
                overdueBooks.add(book);
            }
        }

        if (overdueBooks.isEmpty()) {
            System.out.println("No overdue books.");
            return;
        }

        System.out.println("\n=== Overdue Books ===");
        for (Book book : overdueBooks) {
            Member member = members.get(book.getBorrowedBy());
            long overdueDays = LocalDate.now().toEpochDay() - book.getDueDate().toEpochDay();
            System.out.println(book + " | Borrowed by: " + member.getName() + 
                             " | Overdue by: " + overdueDays + " days");
        }
    }

    // Data persistence methods
    @SuppressWarnings("unchecked")
    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKS_FILE))) {
            books = (Map<String, Book>) ois.readObject();
        } catch (Exception e) {
            System.out.println("No existing book data found. Starting with empty library.");
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MEMBERS_FILE))) {
            members = (Map<String, Member>) ois.readObject();
        } catch (Exception e) {
            System.out.println("No existing member data found. Starting with empty member list.");
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKS_FILE))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.out.println("Error saving book data: " + e.getMessage());
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MEMBERS_FILE))) {
            oos.writeObject(members);
        } catch (IOException e) {
            System.out.println("Error saving member data: " + e.getMessage());
        }
    }

    public void generateLibraryReport() {
        System.out.println("\n=== Library Report ===");
        System.out.println("Total Books: " + books.size());
        System.out.println("Total Members: " + members.size());
        
        long availableBooks = books.values().stream().filter(Book::isAvailable).count();
        long borrowedBooks = books.size() - availableBooks;
        
        System.out.println("Available Books: " + availableBooks);
        System.out.println("Borrowed Books: " + borrowedBooks);
        
        long overdueBooks = books.values().stream()
            .filter(book -> !book.isAvailable() && LocalDate.now().isAfter(book.getDueDate()))
            .count();
        
        System.out.println("Overdue Books: " + overdueBooks);
    }
}

// Main class with CLI interface
public class LibraryManagementSystem {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Welcome to Library Management System ===");
        
        while (true) {
            displayMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1: addBookMenu(); break;
                case 2: removeBookMenu(); break;
                case 3: searchBooksMenu(); break;
                case 4: library.displayAllBooks(); break;
                case 5: addMemberMenu(); break;
                case 6: removeMemberMenu(); break;
                case 7: library.displayAllMembers(); break;
                case 8: borrowBookMenu(); break;
                case 9: returnBookMenu(); break;
                case 10: library.displayBorrowedBooks(); break;
                case 11: library.displayOverdueBooks(); break;
                case 12: library.generateLibraryReport(); break;
                case 0: 
                    System.out.println("Thank you for using Library Management System!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Add Book");
        System.out.println("2. Remove Book");
        System.out.println("3. Search Books");
        System.out.println("4. Display All Books");
        System.out.println("5. Add Member");
        System.out.println("6. Remove Member");
        System.out.println("7. Display All Members");
        System.out.println("8. Borrow Book");
        System.out.println("9. Return Book");
        System.out.println("10. Display Borrowed Books");
        System.out.println("11. Display Overdue Books");
        System.out.println("12. Generate Library Report");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getChoice() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // Clear the buffer
            return -1;
        }
    }

    private static void addBookMenu() {
        scanner.nextLine(); // Clear the buffer
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine().trim();
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine().trim();

        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || genre.isEmpty()) {
            System.out.println("All fields are required!");
            return;
        }

        if (library.addBook(isbn, title, author, genre)) {
            System.out.println("Book added successfully!");
        }
    }

    private static void removeBookMenu() {
        scanner.nextLine(); // Clear the buffer
        System.out.print("Enter ISBN of book to remove: ");
        String isbn = scanner.nextLine().trim();

        if (isbn.isEmpty()) {
            System.out.println("ISBN is required!");
            return;
        }

        if (library.removeBook(isbn)) {
            System.out.println("Book removed successfully!");
        }
    }

    private static void searchBooksMenu() {
        scanner.nextLine(); // Clear the buffer
        System.out.print("Enter search query (Title/Author/Genre/ISBN): ");
        String query = scanner.nextLine().trim();

        if (query.isEmpty()) {
            System.out.println("Search query cannot be empty!");
            return;
        }

        library.searchBooks(query);
    }

    private static void addMemberMenu() {
        scanner.nextLine(); // Clear the buffer
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine().trim();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine().trim();

        if (memberId.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            System.out.println("All fields are required!");
            return;
        }

        if (library.addMember(memberId, name, email, phone)) {
            System.out.println("Member added successfully!");
        }
    }

    private static void removeMemberMenu() {
        scanner.nextLine(); // Clear the buffer
        System.out.print("Enter Member ID to remove: ");
        String memberId = scanner.nextLine().trim();

        if (memberId.isEmpty()) {
            System.out.println("Member ID is required!");
            return;
        }

        if (library.removeMember(memberId)) {
            System.out.println("Member removed successfully!");
        }
    }

    private static void borrowBookMenu() {
        scanner.nextLine(); // Clear the buffer
        System.out.print("Enter ISBN of book to borrow: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine().trim();

        if (isbn.isEmpty() || memberId.isEmpty()) {
            System.out.println("Both ISBN and Member ID are required!");
            return;
        }

        library.borrowBook(isbn, memberId);
    }

    private static void returnBookMenu() {
        scanner.nextLine(); // Clear the buffer
        System.out.print("Enter ISBN of book to return: ");
        String isbn = scanner.nextLine().trim();

        if (isbn.isEmpty()) {
            System.out.println("ISBN is required!");
            return;
        }

        library.returnBook(isbn);
    }
}