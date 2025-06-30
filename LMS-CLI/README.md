# Library Management System

A comprehensive console-based Library Management System built in Java that allows librarians to efficiently manage books, members, and borrowing operations with persistent data storage.

## Features

### Book Management
- **Add Books**: Register new books with ISBN, title, author, and genre
- **Remove Books**: Delete books from the library (only if not currently borrowed)
- **Search Books**: Find books by title, author, genre, or ISBN
- **View All Books**: Display complete book inventory with availability status

### Member Management
- **Register Members**: Add new library members with contact information
- **Remove Members**: Delete member accounts (only if no books are borrowed)
- **View All Members**: Display all registered members with their borrowing history

### Borrowing System
- **Borrow Books**: Check out books to members with automatic due date calculation
- **Return Books**: Process book returns with overdue detection
- **Borrowing Limits**: Maximum 5 books per member, 14-day borrowing period
- **Overdue Tracking**: Automatic calculation and display of overdue books

### Reporting & Analytics
- **Library Report**: Generate comprehensive statistics about books and members
- **Borrowed Books**: View all currently borrowed books with due dates
- **Overdue Books**: Track and display overdue items with member details

### Data Persistence
- **Automatic Saving**: All data is automatically saved to disk
- **Data Recovery**: System loads existing data on startup
- **File-based Storage**: Uses serialization for reliable data storage

## System Requirements

- **Java**: JDK 8 or higher
- **Operating System**: Windows, macOS, or Linux
- **Memory**: Minimum 256 MB RAM
- **Storage**: 10 MB available disk space

## Installation & Setup

1. **Clone or Download**: Get the source code
   ```bash
   git clone <repository-url>
   cd library-management-system
   ```

2. **Compile the Code**:
   ```bash
   javac LibraryManagementSystem.java
   ```

3. **Run the Application**:
   ```bash
   java LibraryManagementSystem
   ```

## Usage Guide

### Starting the System
When you run the application, you'll see the main menu with numbered options:

```
=== Main Menu ===
1. Add Book
2. Remove Book
3. Search Books
4. Display All Books
5. Add Member
6. Remove Member
7. Display All Members
8. Borrow Book
9. Return Book
10. Display Borrowed Books
11. Display Overdue Books
12. Generate Library Report
0. Exit
```

### Adding a Book
1. Select option `1` from the main menu
2. Enter the required information:
   - **ISBN**: Unique identifier for the book
   - **Title**: Book title
   - **Author**: Author name
   - **Genre**: Book category (Fiction, Non-fiction, etc.)

### Registering a Member
1. Select option `5` from the main menu
2. Provide member details:
   - **Member ID**: Unique identifier
   - **Name**: Full name
   - **Email**: Contact email
   - **Phone**: Phone number

### Borrowing a Book
1. Select option `8` from the main menu
2. Enter:
   - **ISBN**: Book identifier
   - **Member ID**: Borrower's ID
3. The system will automatically set a 14-day due date

### Returning a Book
1. Select option `9` from the main menu
2. Enter the **ISBN** of the book being returned
3. System will check for overdue status and process the return

## Business Rules

### Borrowing Limits
- **Maximum Books per Member**: 5 books
- **Borrowing Period**: 14 days
- **Overdue Policy**: Books returned after due date are marked as overdue

### Data Validation
- **Unique ISBNs**: No duplicate books allowed
- **Unique Member IDs**: No duplicate member registrations
- **Required Fields**: All fields must be filled when adding books/members

### Operational Constraints
- Books cannot be removed if currently borrowed
- Members cannot be removed if they have borrowed books
- Only available books can be borrowed

## File Structure

```
library-management-system/
├── LibraryManagementSystem.java    # Main application file
├── books.dat                       # Book data storage (auto-generated)
├── members.dat                     # Member data storage (auto-generated)
└── README.md                       # This file
```

## Data Storage

The system uses Java serialization to store data in binary files:

- **books.dat**: Contains all book information and borrowing status
- **members.dat**: Stores member information and borrowing history

These files are automatically created when you first add books or members, and are updated whenever changes are made.

## Error Handling

The system includes comprehensive error handling for:
- **Invalid Input**: Non-numeric menu choices, empty fields
- **Data Conflicts**: Duplicate ISBNs, non-existent records
- **File Operations**: Missing or corrupted data files
- **Business Logic**: Borrowing limit violations, overdue conditions

## Sample Workflow

1. **Initial Setup**:
   - Add some books to the library
   - Register library members

2. **Daily Operations**:
   - Members borrow books
   - Process book returns
   - Search for available books

3. **Administrative Tasks**:
   - Generate library reports
   - Check overdue books
   - Manage member accounts

## Technical Details

### Classes Overview

- **Book**: Represents individual books with borrowing status
- **Member**: Represents library members with borrowing history
- **Library**: Core business logic and data management
- **LibraryManagementSystem**: Main class with user interface

### Key Features Implementation

- **Serialization**: Used for data persistence
- **Collections**: HashMap for efficient data retrieval
- **Date Handling**: LocalDate for due date calculations
- **Stream API**: For filtering and reporting operations

## Troubleshooting

### Common Issues

**Problem**: "No existing book/member data found" message
**Solution**: This is normal on first run - the system will create data files as you add content

**Problem**: Application won't start
**Solution**: Ensure Java is properly installed and JAVA_HOME is set

**Problem**: Data not saving
**Solution**: Check write permissions in the application directory

### Recovery Options

If data files become corrupted:
1. Stop the application
2. Delete `books.dat` and `members.dat` files
3. Restart the application (will start with empty database)
4. Re-enter your data

## Future Enhancements

Potential improvements for future versions:
- **GUI Interface**: Replace console with graphical interface
- **Database Integration**: MySQL or PostgreSQL support
- **Advanced Search**: Multiple criteria and sorting options
- **Fine System**: Automated fine calculation for overdue books
- **Backup/Restore**: Data backup and recovery features
- **Multi-user Support**: Concurrent access capabilities

## Contributing

To contribute to this project:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This is a educational project and is free to use and modify.

## Support

For questions or issues:
- Review this README for common solutions
- Check the troubleshooting section
- Create an issue in the project repository

---

**Version**: 1.0  
**Last Updated**: June 2025  
**Compatible with**: Java 8+
