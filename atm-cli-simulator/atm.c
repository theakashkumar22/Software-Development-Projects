#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
    int accNumber;
    char name[100];
    float balance;
} Account;

#define FILE_NAME "counter.dat"

int getNextAccountNumber() {
    FILE *fp = fopen("counter.dat", "rb+");
    int accNo = 1000;

    if (fp == NULL) {
        // File doesn't exist — create with starting accNo
        fp = fopen("counter.dat", "wb");
        fwrite(&accNo, sizeof(int), 1, fp);
        fclose(fp);
        return accNo;
    }

    fread(&accNo, sizeof(int), 1, fp);
    rewind(fp);
    int next = accNo + 1;
    fwrite(&next, sizeof(int), 1, fp);
    fclose(fp);
    return next;
}

#define FILE_NAME "accounts.dat"

// Function to create a new account
void createAccount() {
    Account acc;
    FILE *fp = fopen(FILE_NAME, "ab");
    if (!fp) {
        printf("Error opening file!\n");
        return;
    }

    printf("Enter name: ");
    scanf(" %[^\n]", acc.name);
    printf("Enter initial deposit amount: ?");
    scanf("%f", &acc.balance);

    acc.accNumber = getNextAccountNumber();  // ensure uniqueness
    fwrite(&acc, sizeof(Account), 1, fp);
    fclose(fp);
    printf("Account created successfully!\nYour Account Number: %d\n", acc.accNumber);
}


// Function to access an account and perform operations
void accessAccount() {
    int accNo;
    printf("Enter your account number: ");
    scanf("%d", &accNo);

    FILE *fp = fopen(FILE_NAME, "rb+");
    if (!fp) {
        printf("No account database found.\n");
        return;
    }

    Account acc;
    int found = 0;
    long pos;

    while (fread(&acc, sizeof(Account), 1, fp)) {
        if (acc.accNumber == accNo) {
            found = 1;
            pos = ftell(fp) - sizeof(Account);
            break;
        }
    }

    if (!found) {
        printf("Account not found.\n");
        fclose(fp);
        return;
    }

    int choice;
    float amount;

    while (1) {
        printf("\nWelcome, %s | Acc No: %d\n", acc.name, acc.accNumber);
        printf("1. Check Balance\n2. Deposit\n3. Withdraw\n4. Exit to Menu\nChoose: ");
        scanf("%d", &choice);
        switch (choice) {
            case 1:
                printf("Balance: ?%.2f\n", acc.balance);
                break;
            case 2:
                printf("Amount to deposit: ?");
                scanf("%f", &amount);
                acc.balance += amount;
                printf("Deposited successfully.\n");
                break;
            case 3:
                printf("Amount to withdraw: ?");
                scanf("%f", &amount);
                if (amount > acc.balance)
                    printf("Insufficient balance!\n");
                else {
                    acc.balance -= amount;
                    printf("Withdrawn successfully.\n");
                }
                break;
            case 4:
                // Save changes before exiting
                fseek(fp, pos, SEEK_SET);
                fwrite(&acc, sizeof(Account), 1, fp);
                fclose(fp);
                return;
            default:
                printf("Invalid choice.\n");
        }
    }
}

// View all accounts and export to CSV
void viewAllAccounts() {
    FILE *fp = fopen(FILE_NAME, "rb");
    if (!fp) {
        printf("No accounts found.\n");
        return;
    }

    FILE *txt = fopen("accounts.csv", "w");
    if (!txt) {
        printf("Error writing to CSV file.\n");
        fclose(fp);
        return;
    }

    Account acc;
    fprintf(txt, "AccountNo,Name,Balance\n");
    printf("\n--- All Accounts ---\n");

    while (fread(&acc, sizeof(Account), 1, fp)) {
        fprintf(txt, "%d,%s,%.2f\n", acc.accNumber, acc.name, acc.balance);
        printf("Acc No: %d | Name: %s | Balance: ?%.2f\n", acc.accNumber, acc.name, acc.balance);
    }

    fclose(fp);
    fclose(txt);
    printf("Account data also written to accounts.csv\n");
}

int main() {
    int choice;
    while (1) {
        printf("\n==== ATM MENU ====\n");
        printf("1. Create Account\n2. Access Account\n3. View All Accounts (Admin)\n4. Exit\nChoose: ");
        scanf("%d", &choice);
        switch (choice) {
            case 1:
                createAccount();
                break;
            case 2:
                accessAccount();
                break;
            case 3:
                viewAllAccounts();
                break;
            case 4:
                printf("Exiting...\n");
                exit(0);
            default:
                printf("Invalid choice.\n");
        }
    }
    return 0;
}

