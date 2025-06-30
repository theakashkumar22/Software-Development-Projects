# Library Management System

A comprehensive Java Swing-based desktop application for managing library operations including book inventory, member management, and borrowing/returning transactions.

## Features

### 📚 Book Management
- Add new books with ISBN, title, author, and genre
- Remove books from the inventory
- Search books by ISBN, title, author, or genre
- View all books with their availability status
- Prevent removal of currently borrowed books

### 👥 Member Management
- Register new library members with unique IDs
- Store member information (name, email, phone)
- Track membership dates
- Remove members (only if they have no borrowed books)
- View member borrowing history

### 🔄 Borrowing & Returning
- Borrow books with automatic due date calculation (14 days)
- Return books with overdue detection
- Maximum 5 books per member limit
- Track borrowing and return dates
- Real-time status updates

### 📊 Reports & Analytics
- Library statistics dashboard
- Overdue books report with member details
- All borrowed books report
- Real-time data visualization

### 💾 Data Persistence
- Automatic data saving to local files
- Data persistence between application sessions
- Separate storage for books and members data

## Technical Specifications

### Requirements
- **Java Version**: Java 8 or higher
- **GUI Framework**: Java Swing
- **Architecture**: Object-oriented design with MVC pattern
- **Data Storage**: File-based serialization (books.dat, members.dat)

### Key Classes

#### `Book`
- Manages book information and borrowing status
- Tracks ISBN, title, author, genre, and availability
- Handles borrow/return date tracking

#### `Member`
- Stores member personal information
- Manages list of borrowed books
- Tracks membership registration date

#### `Library`
- Core business logic for all library operations
- Handles data persistence and file I/O
- Manages borrowing rules and validations

#### `LibraryManagementGUI`
- Main application window with tabbed interface
- Event handling for all user interactions
- Table management and data display

## Installation & Usage

### Prerequisites
```bash
# Ensure Java is installed
java -version
javac -version
```

### Compilation
```bash
# Compile the Java source file
javac LibraryManagementGUI.java
```

### Running the Application
```bash
# Run the compiled application
java LibraryManagementGUI
```

## User Guide

### Getting Started
1. Launch the application
2. The system will automatically load any existing data
3. Navigate through tabs to access different features

### Adding Books
1. Go to the **Books** tab
2. Fill in all required fields (ISBN, Title, Author, Genre)
3. Click **Add Book**
4. The book will appear in the books table

### Registering Members
1. Navigate to the **Members** tab
2. Enter member details (ID, Name, Email, Phone)
3. Click **Add Member**
4. Member will be added to the system

### Borrowing Books
1. Go to the **Borrow/Return** tab
2. Enter the book ISBN and member ID
3. Click **Borrow Book**
4. System will validate and process the transaction

### Returning Books
1. In the **Borrow/Return** tab
2. Enter the book ISBN
3. Click **Return Book**
4. System will process return and check for overdue status

### Viewing Reports
1. Access the **Reports** tab
2. Click **Refresh Statistics** for current data
3. Use report buttons to generate detailed reports

## Business Rules

### Borrowing Limits
- Maximum **5 books** per member
- **14-day** borrowing period
- No borrowing if member has reached limit

### Book Management
- Each book must have a unique ISBN
- Books cannot be removed if currently borrowed
- All book fields are required

### Member Management
- Each member must have a unique ID
- Members cannot be removed if they have borrowed books
- All member fields are required

## Data Files

The application creates two data files in the application directory:

- **books.dat**: Serialized book data
- **members.dat**: Serialized member data

*Note: These files are automatically created and managed by the application.*

## Error Handling

The application includes comprehensive error handling for:
- Duplicate ISBN/Member ID entries
- Missing required fields
- Invalid borrowing attempts
- File I/O operations
- Data validation errors

## User Interface

### Tabs Overview
- **Books**: Book inventory management
- **Members**: Member registration and management  
- **Borrow/Return**: Transaction processing
- **Reports**: Analytics and reporting

### Features
- Intuitive tabbed interface
- Real-time data updates
- Confirmation dialogs for critical actions
- Status indicators for overdue books
- Search functionality for books

## Future Enhancements

Potential improvements for future versions:
- Database integration (MySQL/PostgreSQL)
- Fine calculation for overdue books
- Email notifications for due dates
- Book reservation system
- Multi-user support with user roles
- Barcode scanning integration
- Advanced reporting with charts

## Troubleshooting

### Common Issues

**Application won't start**
- Ensure Java 8+ is installed
- Check if all class files are compiled
- Verify file permissions

**Data not saving**
- Check write permissions in application directory
- Ensure sufficient disk space

**Books/Members not displaying**
- Try clicking the Refresh button
- Check if data files are corrupted

## License

This project is provided as-is for educational and demonstration purposes.

## Support

For technical support or questions about the Library Management System, please refer to the source code documentation and comments.
