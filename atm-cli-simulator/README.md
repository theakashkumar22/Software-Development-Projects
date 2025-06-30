# ATM Banking System

A simple console-based ATM banking system implemented in C that allows users to create accounts, perform banking operations, and manage account data.

## Features

- **Account Creation**: Create new bank accounts with unique account numbers
- **Account Access**: Access existing accounts using account numbers
- **Banking Operations**:
  - Check account balance
  - Deposit money
  - Withdraw money (with insufficient funds protection)
- **Account Management**: View all accounts with admin functionality
- **Data Persistence**: Account data stored in binary files
- **CSV Export**: Export account data to CSV format for external use

## How It Works

### Account Number Generation
The system automatically generates unique account numbers starting from 1000. Account numbers are tracked in a separate counter file (`counter.dat`) to ensure uniqueness across sessions.

### Data Storage
- **accounts.dat**: Binary file storing all account information
- **counter.dat**: Binary file tracking the next available account number
- **accounts.csv**: Generated CSV file containing account data (created when viewing all accounts)

## Getting Started

### Prerequisites
- GCC compiler or any C compiler
- Terminal/Command prompt access

### Compilation
```bash
gcc -o atm atm.c
```

### Running the Program
```bash
./atm
```

## Usage

### Main Menu Options

1. **Create Account**
   - Enter your full name
   - Enter initial deposit amount
   - System generates unique account number automatically

2. **Access Account**
   - Enter your account number
   - Access your personal banking menu with options to:
     - Check balance
     - Deposit money
     - Withdraw money
     - Exit to main menu

3. **View All Accounts (Admin)**
   - Displays all account information
   - Automatically exports data to `accounts.csv`

4. **Exit**
   - Safely closes the application

### Sample Usage Flow

```
==== ATM MENU ====
1. Create Account
2. Access Account  
3. View All Accounts (Admin)
4. Exit
Choose: 1

Enter name: John Doe
Enter initial deposit amount: ?1000
Account created successfully!
Your Account Number: 1000
```

## File Structure

```
atm.c           # Main source code
accounts.dat    # Binary file storing account data
counter.dat     # Binary file storing account number counter
accounts.csv    # CSV export of account data (generated)
```

## Data Structure

The program uses a simple `Account` structure:

```c
typedef struct {
    int accNumber;     // Unique account number
    char name[100];    // Account holder name
    float balance;     // Account balance
} Account;
```

## Security Features

- **Input Validation**: Prevents withdrawal of amounts exceeding account balance
- **File Handling**: Proper file opening/closing with error checking
- **Data Integrity**: Binary file format ensures data consistency

## Limitations

- No password protection (account number serves as identifier)
- Single-user system (no concurrent access handling)
- Basic error handling
- Fixed maximum name length (100 characters)
- No transaction history tracking

## Future Enhancements

- Add password/PIN protection
- Implement transaction history
- Add account deletion functionality
- Include transfer between accounts
- Add input validation and error handling improvements
- Implement proper user authentication system

## Technical Notes

- Uses binary file I/O for efficient data storage
- Implements file positioning for in-place updates
- Automatic account number generation prevents duplicates
- CSV export functionality for data portability

## Troubleshooting

**"Error opening file!"**: Ensure you have write permissions in the current directory

**"No account database found"**: This appears when no accounts have been created yet

**"Account not found"**: Verify the account number entered is correct

## License

This is a educational project and is free to use and modify.
