#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

#define SIZE 3

// Function prototypes
void initializeBoard(char board[SIZE][SIZE]);
void displayBoard(char board[SIZE][SIZE]);
int makeMove(char board[SIZE][SIZE], int position, char player);
int checkWin(char board[SIZE][SIZE]);
int checkTie(char board[SIZE][SIZE]);
void clearScreen();
int getValidInput();

int main() {
    char board[SIZE][SIZE];
    char currentPlayer = 'X';
    int position, gameStatus;
    int moveCount = 0;
    
    printf("===========================================\n");
    printf("         WELCOME TO TIC-TAC-TOE!\n");
    printf("===========================================\n");
    printf("Player 1: X | Player 2: O\n");
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
        
        printf("\nPlayer %c's turn\n", currentPlayer);
        printf("Enter position (1-9): ");
        
        position = getValidInput();
        
        if (makeMove(board, position, currentPlayer)) {
            moveCount++;
            
            // Check for win
            gameStatus = checkWin(board);
            if (gameStatus != 0) {
                clearScreen();
                displayBoard(board);
                printf("\n?? GAME OVER! ??\n");
                printf("Player %c wins!\n", currentPlayer);
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
            printf("Invalid move! Position already taken or out of range.\n");
            printf("Press Enter to continue...");
            getchar();
        }
    }
    
    printf("\nThanks for playing!\n");
    return 0;
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
