#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <time.h>

#define SIZE 3

// Game modes
typedef enum {
    TWO_PLAYER = 1,
    SINGLE_PLAYER = 2
} GameMode;

// AI difficulty levels
typedef enum {
    EASY = 1,
    MEDIUM = 2,
    HARD = 3
} Difficulty;

// Function prototypes
void initializeBoard(char board[SIZE][SIZE]);
void displayBoard(char board[SIZE][SIZE]);
int makeMove(char board[SIZE][SIZE], int position, char player);
int checkWin(char board[SIZE][SIZE]);
int checkTie(char board[SIZE][SIZE]);
void clearScreen();
int getValidInput();
GameMode selectGameMode();
Difficulty selectDifficulty();
int aiMove(char board[SIZE][SIZE], Difficulty diff);
int minimax(char board[SIZE][SIZE], int depth, int isMaximizing);
int findWinningMove(char board[SIZE][SIZE], char player);
int findBlockingMove(char board[SIZE][SIZE], char player);
int getRandomMove(char board[SIZE][SIZE]);
int evaluateBoard(char board[SIZE][SIZE]);

int main() {
    char board[SIZE][SIZE];
    char currentPlayer = 'X';
    int position, gameStatus;
    int moveCount = 0;
    GameMode mode;
    Difficulty difficulty = MEDIUM;
    
    srand(time(NULL)); // Initialize random seed
    
    printf("===========================================\n");
    printf("         WELCOME TO TIC-TAC-TOE!\n");
    printf("===========================================\n");
    
    mode = selectGameMode();
    
    if (mode == SINGLE_PLAYER) {
        difficulty = selectDifficulty();
        printf("\nYou are X, Computer is O\n");
    } else {
        printf("Player 1: X | Player 2: O\n");
    }
    
    printf("Enter positions 1-9 as shown below:\n\n");
    
    // Show position reference
    printf(" 1 | 2 | 3 \n");
    printf("-----------\n");
    printf(" 4 | 5 | 6 \n");
    printf("-----------\n");
    printf(" 7 | 8 | 9 \n\n");
    
    printf("Press Enter to start...");
    getchar();
    
    initializeBoard(board);
    
    while (1) {
        clearScreen();
        displayBoard(board);
        
        if (mode == SINGLE_PLAYER && currentPlayer == 'O') {
            // AI's turn
            printf("\nComputer's turn...\n");
            position = aiMove(board, difficulty);
            printf("Computer chooses position %d\n", position);
            printf("Press Enter to continue...");
            getchar();
        } else {
            // Human player's turn
            if (mode == SINGLE_PLAYER) {
                printf("\nYour turn (X)\n");
            } else {
                printf("\nPlayer %c's turn\n", currentPlayer);
            }
            printf("Enter position (1-9): ");
            position = getValidInput();
        }
        
        if (makeMove(board, position, currentPlayer)) {
            moveCount++;
            
            // Check for win
            gameStatus = checkWin(board);
            if (gameStatus != 0) {
                clearScreen();
                displayBoard(board);
                printf("\n?? GAME OVER! ??\n");
                if (mode == SINGLE_PLAYER) {
                    if (currentPlayer == 'X') {
                        printf("Congratulations! You win!\n");
                    } else {
                        printf("Computer wins! Better luck next time!\n");
                    }
                } else {
                    printf("Player %c wins!\n", currentPlayer);
                }
                break;
            }
            
            // Check for tie
            if (checkTie(board)) {
                clearScreen();
                displayBoard(board);
                printf("\n?? GAME OVER! ??\n");
                printf("It's a tie!\n");
                break;
            }
            
            // Switch players
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        } else {
            if (mode == TWO_PLAYER || currentPlayer == 'X') {
                printf("Invalid move! Position already taken or out of range.\n");
                printf("Press Enter to continue...");
                getchar();
            }
        }
    }
    
    printf("\nThanks for playing!\n");
    return 0;
}

GameMode selectGameMode() {
    int choice;
    printf("Select Game Mode:\n");
    printf("1. Two Player\n");
    printf("2. Single Player (vs Computer)\n");
    printf("Enter your choice (1-2): ");
    
    while (1) {
        if (scanf("%d", &choice) == 1 && (choice == 1 || choice == 2)) {
            while (getchar() != '\n'); // Clear input buffer
            return (GameMode)choice;
        } else {
            while (getchar() != '\n'); // Clear input buffer
            printf("Invalid choice! Please enter 1 or 2: ");
        }
    }
}

Difficulty selectDifficulty() {
    int choice;
    printf("\nSelect Difficulty:\n");
    printf("1. Easy (Random moves)\n");
    printf("2. Medium (Strategic but beatable)\n");
    printf("3. Hard (Nearly unbeatable)\n");
    printf("Enter your choice (1-3): ");
    
    while (1) {
        if (scanf("%d", &choice) == 1 && choice >= 1 && choice <= 3) {
            while (getchar() != '\n'); // Clear input buffer
            return (Difficulty)choice;
        } else {
            while (getchar() != '\n'); // Clear input buffer
            printf("Invalid choice! Please enter 1, 2, or 3: ");
        }
    }
}

int aiMove(char board[SIZE][SIZE], Difficulty diff) {
    int move;
    
    switch (diff) {
        case EASY:
            // 70% random, 30% strategic
            if (rand() % 100 < 70) {
                move = getRandomMove(board);
            } else {
                move = findWinningMove(board, 'O');
                if (move == -1) move = findBlockingMove(board, 'X');
                if (move == -1) move = getRandomMove(board);
            }
            break;
            
        case MEDIUM:
            // Try to win first, then block, then random
            move = findWinningMove(board, 'O');
            if (move == -1) move = findBlockingMove(board, 'X');
            if (move == -1) {
                // 50% chance to play optimally, 50% random
                if (rand() % 2 == 0) {
                    move = getRandomMove(board);
                } else {
                    // Prefer center, then corners, then edges
                    if (board[1][1] == ' ') move = 5;
                    else {
                        int corners[] = {1, 3, 7, 9};
                        for (int i = 0; i < 4; i++) {
                            int pos = corners[i];
                            int row = (pos - 1) / SIZE;
                            int col = (pos - 1) % SIZE;
                            if (board[row][col] == ' ') {
                                move = pos;
                                break;
                            }
                        }
                        if (move == -1) move = getRandomMove(board);
                    }
                }
            }
            break;
            
        case HARD:
            // Use minimax algorithm for optimal play
            {
                int bestScore = -1000;
                int bestMove = -1;
                
                for (int i = 1; i <= 9; i++) {
                    int row = (i - 1) / SIZE;
                    int col = (i - 1) % SIZE;
                    
                    if (board[row][col] == ' ') {
                        board[row][col] = 'O';
                        int score = minimax(board, 0, 0);
                        board[row][col] = ' ';
                        
                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = i;
                        }
                    }
                }
                move = bestMove;
            }
            break;
    }
    
    return move;
}

int minimax(char board[SIZE][SIZE], int depth, int isMaximizing) {
    int score = evaluateBoard(board);
    
    // Base cases
    if (score == 10) return score - depth;  // AI wins
    if (score == -10) return score + depth; // Human wins
    if (checkTie(board)) return 0;          // Tie
    
    if (isMaximizing) {
        int bestScore = -1000;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = 'O';
                    int currentScore = minimax(board, depth + 1, 0);
                    board[i][j] = ' ';
                    bestScore = (currentScore > bestScore) ? currentScore : bestScore;
                }
            }
        }
        return bestScore;
    } else {
        int bestScore = 1000;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = 'X';
                    int currentScore = minimax(board, depth + 1, 1);
                    board[i][j] = ' ';
                    bestScore = (currentScore < bestScore) ? currentScore : bestScore;
                }
            }
        }
        return bestScore;
    }
}

int evaluateBoard(char board[SIZE][SIZE]) {
    // Check all winning conditions
    for (int i = 0; i < SIZE; i++) {
        // Check rows
        if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
            if (board[i][0] == 'O') return 10;
            if (board[i][0] == 'X') return -10;
        }
        
        // Check columns
        if (board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
            if (board[0][i] == 'O') return 10;
            if (board[0][i] == 'X') return -10;
        }
    }
    
    // Check diagonals
    if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
        if (board[0][0] == 'O') return 10;
        if (board[0][0] == 'X') return -10;
    }
    
    if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
        if (board[0][2] == 'O') return 10;
        if (board[0][2] == 'X') return -10;
    }
    
    return 0; // No winner
}

int findWinningMove(char board[SIZE][SIZE], char player) {
    for (int i = 1; i <= 9; i++) {
        int row = (i - 1) / SIZE;
        int col = (i - 1) % SIZE;
        
        if (board[row][col] == ' ') {
            board[row][col] = player;
            if (checkWin(board)) {
                board[row][col] = ' ';
                return i;
            }
            board[row][col] = ' ';
        }
    }
    return -1; // No winning move found
}

int findBlockingMove(char board[SIZE][SIZE], char opponent) {
    return findWinningMove(board, opponent);
}

int getRandomMove(char board[SIZE][SIZE]) {
    int availableMoves[9];
    int count = 0;
    
    for (int i = 1; i <= 9; i++) {
        int row = (i - 1) / SIZE;
        int col = (i - 1) % SIZE;
        
        if (board[row][col] == ' ') {
            availableMoves[count++] = i;
        }
    }
    
    if (count == 0) return -1;
    return availableMoves[rand() % count];
}

void initializeBoard(char board[SIZE][SIZE]) {
    int i, j;
    for (i = 0; i < SIZE; i++) {
        for (j = 0; j < SIZE; j++) {
            board[i][j] = ' ';
        }
    }
}

void displayBoard(char board[SIZE][SIZE]) {
    int i, j;
    printf("\n   Current Board:\n");
    printf("   ===============\n");
    
    for (i = 0; i < SIZE; i++) {
        printf("   ");
        for (j = 0; j < SIZE; j++) {
            printf(" %c ", board[i][j]);
            if (j < SIZE - 1) printf("|");
        }
        printf("\n");
        if (i < SIZE - 1) {
            printf("   -----------\n");
        }
    }
    printf("\n");
}

int makeMove(char board[SIZE][SIZE], int position, char player) {
    // Convert position (1-9) to array indices
    if (position < 1 || position > 9) {
        return 0; // Invalid position
    }
    
    int row = (position - 1) / SIZE;
    int col = (position - 1) % SIZE;
    
    // Check if position is empty
    if (board[row][col] == ' ') {
        board[row][col] = player;
        return 1; // Success
    }
    
    return 0; // Position already taken
}

int checkWin(char board[SIZE][SIZE]) {
    int i, j;
    
    /* Check rows */
    for (i = 0; i < SIZE; i++) {
        if (board[i][0] != ' ' && 
            board[i][0] == board[i][1] && 
            board[i][1] == board[i][2]) {
            return 1;
        }
    }
    
    /* Check columns */
    for (j = 0; j < SIZE; j++) {
        if (board[0][j] != ' ' && 
            board[0][j] == board[1][j] && 
            board[1][j] == board[2][j]) {
            return 1;
        }
    }
    
    /* Check diagonals */
    if (board[0][0] != ' ' && 
        board[0][0] == board[1][1] && 
        board[1][1] == board[2][2]) {
        return 1;
    }
    
    if (board[0][2] != ' ' && 
        board[0][2] == board[1][1] && 
        board[1][1] == board[2][0]) {
        return 1;
    }
    
    return 0; /* No win */
}

int checkTie(char board[SIZE][SIZE]) {
    int i, j;
    for (i = 0; i < SIZE; i++) {
        for (j = 0; j < SIZE; j++) {
            if (board[i][j] == ' ') {
                return 0; /* Empty space found, game continues */
            }
        }
    }
    return 1; /* Board is full, it's a tie */
}

void clearScreen() {
    #ifdef _WIN32
        system("cls");
    #else
        system("clear");
    #endif
}

int getValidInput() {
    int input;
    char c;
    
    while (1) {
        if (scanf("%d", &input) == 1) {
            // Clear any remaining characters in the input buffer
            while ((c = getchar()) != '\n' && c != EOF);
            
            if (input >= 1 && input <= 9) {
                return input;
            } else {
                printf("Please enter a number between 1 and 9: ");
            }
        } else {
            // Clear invalid input
            while ((c = getchar()) != '\n' && c != EOF);
            printf("Invalid input! Please enter a number between 1 and 9: ");
        }
    }
}
