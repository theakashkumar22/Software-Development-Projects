# Tic-Tac-Toe Game (C)

A classic two-player console-based Tic-Tac-Toe game implemented in C with clean UI and robust game logic.

## Features

- **Two-Player Gameplay**: Players take turns as X and O
- **Intuitive Interface**: Clear board display with position numbering (1-9)
- **Win Detection**: Automatically detects wins across rows, columns, and diagonals
- **Tie Detection**: Recognizes when the board is full with no winner
- **Input Validation**: Handles invalid inputs and occupied positions gracefully
- **Cross-Platform**: Works on Windows, Linux, and macOS
- **Clean UI**: Screen clearing and formatted output for better user experience

## Game Rules

1. The game is played on a 3x3 grid
2. Player 1 is X, Player 2 is O
3. Players take turns placing their mark in empty positions
4. First player to get 3 marks in a row (horizontally, vertically, or diagonally) wins
5. If all 9 positions are filled without a winner, the game is a tie

## Position Layout

```
 1 | 2 | 3 
-----------
 4 | 5 | 6 
-----------
 7 | 8 | 9 
```

Players enter numbers 1-9 to place their marks in the corresponding positions.

## How to Compile and Run

### Prerequisites
- GCC compiler (or any C compiler)
- Terminal/Command prompt

### Compilation
```bash
gcc -o tictactoe tictactoe.c
```

### Running the Game
```bash
./tictactoe
```

On Windows:
```cmd
tictactoe.exe
```

## Code Structure

### Main Functions

- **`main()`**: Game loop and main program flow
- **`initializeBoard()`**: Sets up empty 3x3 game board
- **`displayBoard()`**: Renders the current board state
- **`makeMove()`**: Processes player moves and validates positions
- **`checkWin()`**: Checks all winning conditions
- **`checkTie()`**: Determines if the game is a tie
- **`clearScreen()`**: Cross-platform screen clearing
- **`getValidInput()`**: Robust input validation and error handling

### Key Implementation Details

1. **Board Representation**: 2D character array (`char board[3][3]`)
2. **Position Mapping**: Converts user input (1-9) to array indices
3. **Win Conditions**: Checks 3 rows, 3 columns, and 2 diagonals
4. **Input Validation**: Handles non-numeric input and out-of-range values
5. **Memory Management**: Static allocation for simplicity and efficiency

## Example Gameplay

```
===========================================
         WELCOME TO TIC-TAC-TOE!
===========================================
Player 1: X | Player 2: O

   Current Board:
   ===============
   X |   | O 
   -----------
     | X |   
   -----------
     | O | X 

Player O's turn
Enter position (1-9): 4
```

## Technical Features

- **Robust Input Handling**: Clears input buffer to prevent issues
- **Cross-Platform Compatibility**: Uses preprocessor directives for different OS
- **Memory Efficient**: Uses stack allocation for game state
- **Modular Design**: Separate functions for each game operation
- **Error Prevention**: Validates all user inputs before processing

## Possible Enhancements

- Add single-player mode with AI opponent
- Implement difficulty levels
- Add game statistics tracking
- Create a GUI version
- Add sound effects
- Implement network multiplayer

## Author

This Tic-Tac-Toe game demonstrates fundamental C programming concepts including:
- 2D arrays and memory management
- Function decomposition and modular programming
- User input validation and error handling
- Game state management
- Cross-platform development techniques

## License

This project is open source and available under the MIT License.
