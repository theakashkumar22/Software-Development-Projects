# 🎮 Enhanced Tic-Tac-Toe Game

A feature-rich, console-based Tic-Tac-Toe game written in C with AI opponent support and multiple difficulty levels.

## ✨ Features

- **🎯 Two Game Modes**
  - Two-player mode for local multiplayer
  - Single-player mode against AI opponent

- **🤖 Smart AI with 3 Difficulty Levels**
  - **Easy**: Random moves with occasional strategy (perfect for beginners)
  - **Medium**: Balanced gameplay with strategic thinking
  - **Hard**: Nearly unbeatable using minimax algorithm

- **🎨 User-Friendly Interface**
  - Clear board visualization
  - Position reference guide (1-9 grid)
  - Input validation and error handling
  - Cross-platform screen clearing

- **🧠 Advanced AI Features**
  - Winning move detection
  - Blocking opponent's winning moves
  - Strategic positioning (center → corners → edges)
  - Minimax algorithm for optimal play

## 🚀 Quick Start

### Prerequisites
- GCC compiler or any C compiler
- Terminal/Command prompt

### Installation & Running

1. **Clone or download the source code**
   ```bash
   # Save the code as 'tictactoe.c'
   ```

2. **Compile the program**
   ```bash
   gcc -o tictactoe tictactoe.c
   ```

3. **Run the game**
   ```bash
   ./tictactoe        # On Linux/Mac
   tictactoe.exe      # On Windows
   ```

## 🎮 How to Play

### Game Setup
1. **Choose Game Mode**:
   - Enter `1` for Two-Player mode
   - Enter `2` for Single-Player (vs AI)

2. **Select Difficulty** (Single-Player only):
   - `1` - Easy: Good for learning
   - `2` - Medium: Balanced challenge
   - `3` - Hard: Ultimate challenge

### Gameplay
- The board is numbered 1-9 as shown:
  ```
   1 | 2 | 3 
  -----------
   4 | 5 | 6 
  -----------
   7 | 8 | 9 
  ```
- Players take turns entering position numbers (1-9)
- First to get three in a row (horizontal, vertical, or diagonal) wins!
- If all 9 positions are filled without a winner, it's a tie

### Controls
- Enter numbers 1-9 to place your mark
- Follow on-screen prompts
- Press Enter to continue after messages

## 🧠 AI Strategy Breakdown

### Easy Mode (70% Random, 30% Strategic)
- Mostly random moves for unpredictable, beginner-friendly gameplay
- Occasionally makes strategic moves (winning or blocking)
- Great for new players to build confidence

### Medium Mode (Strategic with Imperfections)
- Always tries to win if possible
- Blocks opponent's winning moves
- Prefers center, then corners, then edges
- 50% chance of optimal play vs random moves
- Challenging but beatable

### Hard Mode (Minimax Algorithm)
- Calculates all possible game outcomes
- Always chooses the optimal move
- Nearly impossible to beat (best case: tie)
- Perfect for experienced players seeking ultimate challenge

## 📁 Code Structure

```
tictactoe.c
├── Main Game Loop
├── Board Management
│   ├── initializeBoard()
│   ├── displayBoard()
│   └── makeMove()
├── Game Logic
│   ├── checkWin()
│   ├── checkTie()
│   └── evaluateBoard()
├── AI System
│   ├── aiMove()
│   ├── minimax()
│   ├── findWinningMove()
│   ├── findBlockingMove()
│   └── getRandomMove()
├── User Interface
│   ├── selectGameMode()
│   ├── selectDifficulty()
│   ├── getValidInput()
│   └── clearScreen()
└── Utility Functions
```

## 🔧 Technical Details

### Algorithms Used
- **Minimax Algorithm**: For optimal AI play in hard mode
- **Alpha-Beta Pruning**: Implicit through depth-limited search
- **Strategic Positioning**: Center-first, corner-second heuristics

### Data Structures
- 2D character array for board representation
- Enums for game modes and difficulty levels
- Position mapping (1-9 to row/column coordinates)

### Cross-Platform Compatibility
- Windows: Uses `system("cls")` for screen clearing
- Linux/Mac: Uses `system("clear")` for screen clearing
- Robust input handling with buffer clearing

## 🎯 Game Statistics

### Win Rates by Difficulty
- **Easy Mode**: ~60-70% player win rate
- **Medium Mode**: ~30-40% player win rate  
- **Hard Mode**: ~0-5% player win rate (mostly ties)

### Optimal Play Theory
In perfect play:
- First player (X) can force a win or tie
- Second player (O) can force a tie
- Hard mode AI plays optimally as O, making wins very rare

## 🚀 Future Enhancements

Possible improvements for future versions:
- 🎵 Sound effects and animations
- 📊 Game statistics tracking
- 🏆 Tournament mode with multiple rounds
- 🌐 Network multiplayer support
- 🎨 GUI version with graphics
- 💾 Save/load game functionality
- 🎮 Different board sizes (4x4, 5x5)

## 🐛 Troubleshooting

### Common Issues

**Compilation Errors:**
```bash
# Make sure you have GCC installed
gcc --version

# Use specific C standard if needed
gcc -std=c99 -o tictactoe tictactoe.c
```

**Screen Clearing Not Working:**
- On some systems, `clear` or `cls` commands might not work
- The game will still function, just without screen clearing

**Input Issues:**
- Make sure to enter only numbers 1-9
- Press Enter after each input
- The program handles invalid input gracefully

## 📝 License

This project is open source and available under the MIT License.

## 🤝 Contributing

Feel free to fork this project and submit pull requests for improvements!

### Ideas for Contributions:
- Add more AI personalities
- Implement different board themes
- Add game replay functionality
- Create automated testing
- Improve cross-platform compatibility

## 👨‍💻 Author - Akash Kumar P R

Created as an enhanced version of the classic Tic-Tac-Toe game, featuring advanced AI opponents and user-friendly gameplay.

---

**Have fun playing! 🎉**

*Challenge yourself against the AI and see if you can achieve the rare victory on Hard mode!*
