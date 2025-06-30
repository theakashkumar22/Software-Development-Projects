import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

// Book class
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
}

// Member class
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
}

// Library class
class Library implements Serializable {
    private static final long serialVersionUID = 1L;
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
            return false;
        }
        books.put(isbn, new Book(isbn, title, author, genre));
        saveData();
        return true;
    }

    public boolean removeBook(String isbn) {
        if (!books.containsKey(isbn)) {
            return false;
        }
        Book book = books.get(isbn);
        if (!book.isAvailable()) {
            return false;
        }
        books.remove(isbn);
        saveData();
        return true;
    }

    public List<Book> searchBooks(String query) {
        List<Book> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return getAllBooks();
        }
        
        String lowerQuery = query.toLowerCase();
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(lowerQuery) ||
                book.getAuthor().toLowerCase().contains(lowerQuery) ||
                book.getGenre().toLowerCase().contains(lowerQuery) ||
                book.getIsbn().toLowerCase().contains(lowerQuery)) {
                results.add(book);
            }
        }
        return results;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    // Member management methods
    public boolean addMember(String memberId, String name, String email, String phone) {
        if (members.containsKey(memberId)) {
            return false;
        }
        members.put(memberId, new Member(memberId, name, email, phone));
        saveData();
        return true;
    }

    public boolean removeMember(String memberId) {
        if (!members.containsKey(memberId)) {
            return false;
        }
        Member member = members.get(memberId);
        if (!member.getBorrowedBooks().isEmpty()) {
            return false;
        }
        members.remove(memberId);
        saveData();
        return true;
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }

    // Borrowing and returning methods
    public String borrowBook(String isbn, String memberId) {
        if (!books.containsKey(isbn)) {
            return "Book not found!";
        }
        if (!members.containsKey(memberId)) {
            return "Member not found!";
        }

        Book book = books.get(isbn);
        Member member = members.get(memberId);

        if (!book.isAvailable()) {
            return "Book is already borrowed!";
        }

        if (member.getBorrowedBooks().size() >= MAX_BOOKS_PER_MEMBER) {
            return "Member has reached maximum borrowing limit!";
        }

        // Borrow the book
        book.setAvailable(false);
        book.setBorrowedBy(memberId);
        book.setBorrowDate(LocalDate.now());
        book.setDueDate(LocalDate.now().plusDays(MAX_BORROW_DAYS));
        member.borrowBook(isbn);
        
        saveData();
        return "Book borrowed successfully! Due date: " + 
               book.getDueDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public String returnBook(String isbn) {
        if (!books.containsKey(isbn)) {
            return "Book not found!";
        }

        Book book = books.get(isbn);
        if (book.isAvailable()) {
            return "Book is not currently borrowed!";
        }

        String memberId = book.getBorrowedBy();
        Member member = members.get(memberId);

        String result = "Book returned successfully!";
        
        // Check for overdue
        if (LocalDate.now().isAfter(book.getDueDate())) {
            long overdueDays = LocalDate.now().toEpochDay() - book.getDueDate().toEpochDay();
            result += " (Was overdue by " + overdueDays + " days)";
        }

        // Return the book
        book.setAvailable(true);
        book.setBorrowedBy(null);
        book.setBorrowDate(null);
        book.setDueDate(null);
        if (member != null) {
            member.returnBook(isbn);
        }

        saveData();
        return result;
    }

    public List<Book> getBorrowedBooks() {
        List<Book> borrowedBooks = new ArrayList<>();
        for (Book book : books.values()) {
            if (!book.isAvailable()) {
                borrowedBooks.add(book);
            }
        }
        return borrowedBooks;
    }

    public List<Book> getOverdueBooks() {
        List<Book> overdueBooks = new ArrayList<>();
        for (Book book : books.values()) {
            if (!book.isAvailable() && book.getDueDate() != null && 
                LocalDate.now().isAfter(book.getDueDate())) {
                overdueBooks.add(book);
            }
        }
        return overdueBooks;
    }

    public Member getMember(String memberId) {
        return members.get(memberId);
    }

    public Book getBook(String isbn) {
        return books.get(isbn);
    }

    // Data persistence methods
    @SuppressWarnings("unchecked")
    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKS_FILE))) {
            books = (Map<String, Book>) ois.readObject();
        } catch (Exception e) {
            // File doesn't exist or is corrupted, start with empty data
            books = new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MEMBERS_FILE))) {
            members = (Map<String, Member>) ois.readObject();
        } catch (Exception e) {
            // File doesn't exist or is corrupted, start with empty data
            members = new HashMap<>();
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKS_FILE))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.err.println("Error saving books data: " + e.getMessage());
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MEMBERS_FILE))) {
            oos.writeObject(members);
        } catch (IOException e) {
            System.err.println("Error saving members data: " + e.getMessage());
        }
    }

    public Map<String, Integer> getLibraryStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalBooks", books.size());
        stats.put("totalMembers", members.size());
        
        long availableCount = books.values().stream().filter(Book::isAvailable).count();
        stats.put("availableBooks", (int) availableCount);
        stats.put("borrowedBooks", books.size() - (int) availableCount);
        
        long overdueCount = books.values().stream()
            .filter(book -> !book.isAvailable() && book.getDueDate() != null && 
                           LocalDate.now().isAfter(book.getDueDate()))
            .count();
        stats.put("overdueBooks", (int) overdueCount);
        
        return stats;
    }
}

// Main GUI class
public class LibraryManagementGUI extends JFrame {
    private Library library;
    private JTabbedPane tabbedPane;
    private DefaultTableModel bookTableModel;
    private DefaultTableModel memberTableModel;
    private DefaultTableModel borrowedTableModel;
    private JTable bookTable;
    private JTable memberTable;
    private JTable borrowedTable;

    public LibraryManagementGUI() {
        library = new Library();
        initializeGUI();
        refreshAllTables();
    }

    private void initializeGUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create tabbed pane
        tabbedPane = new JTabbedPane();

        // Add tabs
        tabbedPane.addTab("Books", createBookPanel());
        tabbedPane.addTab("Members", createMemberPanel());
        tabbedPane.addTab("Borrow/Return", createBorrowReturnPanel());
        tabbedPane.addTab("Reports", createReportsPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        JLabel statusLabel = new JLabel("Library Management System Ready");
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);

        setSize(1000, 700);
        setLocationRelativeTo(null);
    }

    private JPanel createBookPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Book form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Add/Remove Books"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField isbnField = new JTextField(15);
        JTextField titleField = new JTextField(15);
        JTextField authorField = new JTextField(15);
        JTextField genreField = new JTextField(15);
        JTextField searchField = new JTextField(15);

        // Add components to form
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        formPanel.add(isbnField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        formPanel.add(authorField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Genre:"), gbc);
        gbc.gridx = 1;
        formPanel.add(genreField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Search:"), gbc);
        gbc.gridx = 1;
        formPanel.add(searchField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Book");
        JButton removeButton = new JButton("Remove Book");
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Table
        String[] bookColumns = {"ISBN", "Title", "Author", "Genre", "Status"};
        bookTableModel = new DefaultTableModel(bookColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(bookTableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Event listeners
        addButton.addActionListener(e -> {
            String isbn = isbnField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();

            if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || genre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (library.addBook(isbn, title, author, genre)) {
                JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearBookForm(isbnField, titleField, authorField, genreField);
                refreshBookTable();
            } else {
                JOptionPane.showMessageDialog(this, "Book with this ISBN already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeButton.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a book to remove!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String isbn = (String) bookTableModel.getValueAt(selectedRow, 0);
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this book?", "Confirm", JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                if (library.removeBook(isbn)) {
                    JOptionPane.showMessageDialog(this, "Book removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshBookTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot remove book - it may be currently borrowed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            List<Book> results = library.searchBooks(query);
            populateBookTable(results);
        });

        refreshButton.addActionListener(e -> refreshBookTable());

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(bookTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMemberPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Member form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Add/Remove Members"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField memberIdField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField phoneField = new JTextField(15);

        // Add components to form
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Member ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(memberIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Member");
        JButton removeButton = new JButton("Remove Member");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Table
        String[] memberColumns = {"Member ID", "Name", "Email", "Phone", "Books Borrowed", "Member Since"};
        memberTableModel = new DefaultTableModel(memberColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        memberTable = new JTable(memberTableModel);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Event listeners
        addButton.addActionListener(e -> {
            String memberId = memberIdField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (memberId.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (library.addMember(memberId, name, email, phone)) {
                JOptionPane.showMessageDialog(this, "Member added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearMemberForm(memberIdField, nameField, emailField, phoneField);
                refreshMemberTable();
            } else {
                JOptionPane.showMessageDialog(this, "Member with this ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeButton.addActionListener(e -> {
            int selectedRow = memberTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a member to remove!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String memberId = (String) memberTableModel.getValueAt(selectedRow, 0);
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this member?", "Confirm", JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                if (library.removeMember(memberId)) {
                    JOptionPane.showMessageDialog(this, "Member removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshMemberTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot remove member - they have borrowed books!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        refreshButton.addActionListener(e -> refreshMemberTable());

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(memberTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBorrowReturnPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Top panel with borrow/return forms
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Borrow panel
        JPanel borrowPanel = new JPanel(new GridBagLayout());
        borrowPanel.setBorder(new TitledBorder("Borrow Book"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField borrowIsbnField = new JTextField(15);
        JTextField borrowMemberIdField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        borrowPanel.add(new JLabel("Book ISBN:"), gbc);
        gbc.gridx = 1;
        borrowPanel.add(borrowIsbnField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        borrowPanel.add(new JLabel("Member ID:"), gbc);
        gbc.gridx = 1;
        borrowPanel.add(borrowMemberIdField, gbc);

        JButton borrowButton = new JButton("Borrow Book");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        borrowPanel.add(borrowButton, gbc);

        // Return panel
        JPanel returnPanel = new JPanel(new GridBagLayout());
        returnPanel.setBorder(new TitledBorder("Return Book"));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);

        JTextField returnIsbnField = new JTextField(15);

        gbc2.gridx = 0; gbc2.gridy = 0;
        returnPanel.add(new JLabel("Book ISBN:"), gbc2);
        gbc2.gridx = 1;
        returnPanel.add(returnIsbnField, gbc2);

        JButton returnButton = new JButton("Return Book");
        gbc2.gridx = 0; gbc2.gridy = 1; gbc2.gridwidth = 2;
        returnPanel.add(returnButton, gbc2);

        topPanel.add(borrowPanel);
        topPanel.add(returnPanel);

        // Borrowed books table
        String[] borrowedColumns = {"ISBN", "Title", "Author", "Borrowed By", "Member Name", "Borrow Date", "Due Date", "Status"};
        borrowedTableModel = new DefaultTableModel(borrowedColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        borrowedTable = new JTable(borrowedTableModel);
        borrowedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Event listeners
        borrowButton.addActionListener(e -> {
            String isbn = borrowIsbnField.getText().trim();
            String memberId = borrowMemberIdField.getText().trim();

            if (isbn.isEmpty() || memberId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String result = library.borrowBook(isbn, memberId);
            if (result.startsWith("Book borrowed successfully")) {
                JOptionPane.showMessageDialog(this, result, "Success", JOptionPane.INFORMATION_MESSAGE);
                borrowIsbnField.setText("");
                borrowMemberIdField.setText("");
                refreshAllTables();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        returnButton.addActionListener(e -> {
            String isbn = returnIsbnField.getText().trim();

            if (isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ISBN is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String result = library.returnBook(isbn);
            if (result.startsWith("Book returned successfully")) {
                JOptionPane.showMessageDialog(this, result, "Success", JOptionPane.INFORMATION_MESSAGE);
                returnIsbnField.setText("");
                refreshAllTables();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(borrowedTable), BorderLayout.CENTER);

        // Refresh button for borrowed books
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton refreshBorrowedButton = new JButton("Refresh Borrowed Books");
        refreshBorrowedButton.addActionListener(e -> refreshBorrowedTable());
        bottomPanel.add(refreshBorrowedButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Stats panel
        JPanel statsPanel = new JPanel(new GridBagLayout());
        statsPanel.setBorder(new TitledBorder("Library Statistics"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel totalBooksLabel = new JLabel("Total Books: 0");
        JLabel totalMembersLabel = new JLabel("Total Members: 0");
        JLabel availableBooksLabel = new JLabel("Available Books: 0");
        JLabel borrowedBooksLabel = new JLabel("Borrowed Books: 0");
        JLabel overdueBooksLabel = new JLabel("Overdue Books: 0");

        gbc.gridx = 0; gbc.gridy = 0;
        statsPanel.add(totalBooksLabel, gbc);
        gbc.gridx = 1;
        statsPanel.add(totalMembersLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        statsPanel.add(availableBooksLabel, gbc);
        gbc.gridx = 1;
        statsPanel.add(borrowedBooksLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        statsPanel.add(overdueBooksLabel, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshStatsButton = new JButton("Refresh Statistics");
        JButton showOverdueButton = new JButton("Show Overdue Books");
        JButton showBorrowedButton = new JButton("Show All Borrowed Books");

        buttonPanel.add(refreshStatsButton);
        buttonPanel.add(showOverdueButton);
        buttonPanel.add(showBorrowedButton);

        // Text area for reports
        JTextArea reportArea = new JTextArea(20, 50);
        reportArea.setEditable(false);
        reportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        // Event listeners
        refreshStatsButton.addActionListener(e -> {
            Map<String, Integer> stats = library.getLibraryStats();
            totalBooksLabel.setText("Total Books: " + stats.get("totalBooks"));
            totalMembersLabel.setText("Total Members: " + stats.get("totalMembers"));
            availableBooksLabel.setText("Available Books: " + stats.get("availableBooks"));
            borrowedBooksLabel.setText("Borrowed Books: " + stats.get("borrowedBooks"));
            overdueBooksLabel.setText("Overdue Books: " + stats.get("overdueBooks"));
        });

        showOverdueButton.addActionListener(e -> {
            List<Book> overdueBooks = library.getOverdueBooks();
            StringBuilder report = new StringBuilder();
            report.append("=== OVERDUE BOOKS REPORT ===\n\n");
            
            if (overdueBooks.isEmpty()) {
                report.append("No overdue books found.\n");
            } else {
                for (Book book : overdueBooks) {
                    Member member = library.getMember(book.getBorrowedBy());
                    long overdueDays = LocalDate.now().toEpochDay() - book.getDueDate().toEpochDay();
                    
                    report.append("ISBN: ").append(book.getIsbn()).append("\n");
                    report.append("Title: ").append(book.getTitle()).append("\n");
                    report.append("Author: ").append(book.getAuthor()).append("\n");
                    report.append("Borrowed by: ").append(member != null ? member.getName() : "Unknown").append(" (").append(book.getBorrowedBy()).append(")\n");
                    report.append("Due Date: ").append(book.getDueDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append("\n");
                    report.append("Days Overdue: ").append(overdueDays).append("\n");
                    report.append("----------------------------------------\n");
                }
            }
            reportArea.setText(report.toString());
        });

        showBorrowedButton.addActionListener(e -> {
            List<Book> borrowedBooks = library.getBorrowedBooks();
            StringBuilder report = new StringBuilder();
            report.append("=== ALL BORROWED BOOKS REPORT ===\n\n");
            
            if (borrowedBooks.isEmpty()) {
                report.append("No books are currently borrowed.\n");
            } else {
                for (Book book : borrowedBooks) {
                    Member member = library.getMember(book.getBorrowedBy());
                    
                    report.append("ISBN: ").append(book.getIsbn()).append("\n");
                    report.append("Title: ").append(book.getTitle()).append("\n");
                    report.append("Author: ").append(book.getAuthor()).append("\n");
                    report.append("Borrowed by: ").append(member != null ? member.getName() : "Unknown").append(" (").append(book.getBorrowedBy()).append(")\n");
                    report.append("Borrow Date: ").append(book.getBorrowDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append("\n");
                    report.append("Due Date: ").append(book.getDueDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append("\n");
                    
                    if (LocalDate.now().isAfter(book.getDueDate())) {
                        long overdueDays = LocalDate.now().toEpochDay() - book.getDueDate().toEpochDay();
                        report.append("Status: OVERDUE (").append(overdueDays).append(" days)\n");
                    } else {
                        report.append("Status: On Time\n");
                    }
                    report.append("----------------------------------------\n");
                }
            }
            reportArea.setText(report.toString());
        });

        panel.add(statsPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(new JScrollPane(reportArea), BorderLayout.SOUTH);

        return panel;
    }

    private void refreshAllTables() {
        refreshBookTable();
        refreshMemberTable();
        refreshBorrowedTable();
    }

    private void refreshBookTable() {
        populateBookTable(library.getAllBooks());
    }

    private void populateBookTable(List<Book> books) {
        bookTableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        for (Book book : books) {
            String status = book.isAvailable() ? "Available" : 
                           "Borrowed" + (book.getDueDate() != null ? " (Due: " + book.getDueDate().format(formatter) + ")" : "");
            
            bookTableModel.addRow(new Object[]{
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                status
            });
        }
    }

    private void refreshMemberTable() {
        memberTableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        for (Member member : library.getAllMembers()) {
            memberTableModel.addRow(new Object[]{
                member.getMemberId(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getBorrowedBooks().size(),
                member.getMembershipDate().format(formatter)
            });
        }
    }

    private void refreshBorrowedTable() {
        borrowedTableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        for (Book book : library.getBorrowedBooks()) {
            Member member = library.getMember(book.getBorrowedBy());
            String memberName = member != null ? member.getName() : "Unknown";
            
            String status = "On Time";
            if (book.getDueDate() != null && LocalDate.now().isAfter(book.getDueDate())) {
                long overdueDays = LocalDate.now().toEpochDay() - book.getDueDate().toEpochDay();
                status = "OVERDUE (" + overdueDays + " days)";
            }
            
            borrowedTableModel.addRow(new Object[]{
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getBorrowedBy(),
                memberName,
                book.getBorrowDate() != null ? book.getBorrowDate().format(formatter) : "N/A",
                book.getDueDate() != null ? book.getDueDate().format(formatter) : "N/A",
                status
            });
        }
    }

    private void clearBookForm(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void clearMemberForm(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel if system look and feel is not available
        }

        SwingUtilities.invokeLater(() -> {
            new LibraryManagementGUI().setVisible(true);
        });
    }
}