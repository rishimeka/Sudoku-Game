//Author: @Rishi Meka

import java.lang.*;
import java.util.*;

public class gameOfSudoku {
    static char[][] board;
    public static void main(String[] args) {
        playGame();
    }
    private static void generateBoard(int level){
        Random random = new Random();
        // Fills all the diagonal matrices in the sudoku board
        fillDiagonal();
        // Fills in the rest of the board using recursion
        fillBoard();
        // Empty's out the solution matrix to create the game board given a level (percent to clear)
        for(int i = 0; i < level; i++){
            int row;
            int col;
            do{
                row = random.nextInt(9);
                col = random.nextInt(9);
            } while(board[row][col] == '.');
            board[row][col] = '.';
        }
    }
    private static void fillDiagonal(){
        Random random = new Random();
        // Creates an empty board
        board = new char[][]{{'.', '.', '.', '.', '.', '.', '.', '.', '.'}
                , {'.', '.', '.', '.', '.', '.', '.', '.', '.'}
                , {'.', '.', '.', '.', '.', '.', '.', '.', '.'}
                , {'.', '.', '.', '.', '.', '.', '.', '.', '.'}
                , {'.', '.', '.', '.', '.', '.', '.', '.', '.'}
                , {'.', '.', '.', '.', '.', '.', '.', '.', '.'}
                , {'.', '.', '.', '.', '.', '.', '.', '.', '.'}
                , {'.', '.', '.', '.', '.', '.', '.', '.', '.'}
                , {'.', '.', '.', '.', '.', '.', '.', '.', '.'}};
        // Fills in the diagonal matrices of the game board with random numbers
        for (int i = 0; i < 9; i+=3) {
            String s = "";
            for(int row = i; row < i + 3; row++){
                for (int col = i; col < i + 3 ; col++) {
                    int value;
                    do{
                        value = random.nextInt(9) + 1;
                    } while (s.contains(""+(""+value).charAt(0)));
                    s += value + "";
                    board[row][col] = ("" + value).charAt(0);
                }
            }
        }
    }
    private static boolean fillBoard(){
        // Checks if the board at row, col is empty, and if so trys to fill in that spot
        for(int row = 0; row < 9; row++){
            for (int col = 0; col < 9; col++) {
                if(board[row][col] == '.') {
                    for(int i = 1; i <= 9; i++){
                        // Fills in the board at row, col with a number between 1 and 9, and validates the placement
                        board[row][col] = ("" + i).charAt(0);
                        if(validate(board)){
                            // If valid placement, call fillBoard() to fill in the rest of the board recursively
                            if(fillBoard())
                                return true;
                        }
                        // If at some point the placement is invalid, it is reset and backtracked
                        else
                            board[row][col] = '.';
                    }
                    return false;
                }
            }
        }
        // If the nested for loop executes completely, then we return true: The board is completely filled and legal
        return true;
    }
    private static char[][] getBoard(){
        // Create a copy of the board
        char[][] newBoard = new char[9][9];
        for(int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                newBoard[row][col] = board[row][col];
            }
        }
        return newBoard;
    }
    private static void playGame(){
        Scanner input = new Scanner(System.in);
        int row, col, level;
        boolean validMove = true;
        System.out.println("Welcome to a game of Sudoku!");
        // Setting the difficulty
        do{
            System.out.print("Select your difficulty level!\n1. Easy\n2.Medium\n3.Hard\nLevel (1-3): ");
            level = input.nextInt();
            if(level < 0 || level > 3)
                System.out.println("Invalid Selection. Please try again.");
        }while(level < 0 || level > 3);
        switch (level){
            // Changing how filled the board will be based on the difficulty
            case 1:
                generateBoard((int)(81*0.55));
                break;
            case 2:
                generateBoard((int)(81*0.65));
                break;
            case 3:
                generateBoard((int)(81*0.75));
                break;
        }
        // Creating a copy of the original board
        char[][] originalBoard = getBoard();
        System.out.println("This is the original board: ");
        // While the board is empty
        while(!isFull(board)){
            printBoard(board);
            // While the current move is invalid:
            do {
                // Get the row and col choice
                System.out.println("Which position would you like to play in?");
                System.out.print("Row: ");
                row = input.nextInt();
                System.out.print("Col: ");
                col = input.nextInt();
                // If the row and col are out of bounce, ask the user to try again
                if(((row < 0 || row > 8) || (col < 0 || col > 8))) {
                    System.out.println("Invalid Row or Col number. Please try again.");
                    validMove = false;
                }
                // If the chosen spot is filled in the original board, ask the user to try again
                else if(originalBoard[row][col] != '.') {
                    System.out.println("This value cannot be changed. Please try again.");
                    validMove = false;
                }
                else{
                    // If the row and col choice is valid, ask the user what number they want to place in the chosen spot
                    String value;
                    do {
                        System.out.println("What number would you like to place at Row " + row + " and Col " + col + "?");
                        System.out.print("Value (0-9 or \".\"): ");
                        value = input.next();
                        // If the input's is too large or (if the char is not a number or a period) ask user to try again
                        if(value.length() > 1 || (!Character.isDigit(value.charAt(0)) && value.charAt(0) != '.')) {
                            System.out.println("Please enter a value between 0 and 9 or a period.");
                            validMove = false;
                        }
                        // If the chosen number will result in an invalid move
                        else if(value.charAt(0) != '.' && !validate(board, row, col, value.charAt(0))){
                            System.out.println("This number cannot be placed here. Please try again.");
                            validMove = false;
                        }
                        // If the move is valid, then make the placement final
                        else{
                            board[row][col] = value.charAt(0);
                            validMove = true;
                        }
                    } while (!validMove); // Check if a valid move was made
                }
            } while (!validMove); // Check if a valid move was made
        }
        // The game is complete
        System.out.println("   Game completed, thank you for playing!  ");
        System.out.println("*-----*-----*-----*-----*-----*-----*-----*");
    }
    private static boolean validate(char[][] board) {
        // Check if each matrix within the board is valid
        for(int i = 0; i < board.length; i += 3){
            for(int j = 0; j < board.length; j += 3){
                if(!validateSquare(board, i, j))
                    return false;
            }
        }
        // Check if all the rows and columns are valid
        for(int i = 0; i < board.length; i++){
            if(!validateHorizontal(board[i]))
                return false;
            if(!validateVertical(board, i))
                return false;
        }
        return true;
    }
    private static boolean validate(char[][] gameBoard, int row, int col, char newValue){
        // Overloaded method to check if a specific placement is valid before finalizing the move
        char[][] board = getBoard();
        board[row][col] = newValue;
        return validate(board);
    }
    private static boolean validateSquare(char[][] board, int rowStartIndex, int colStartIndex){
        // Collecting all the values in the matrix to check if there is a duplicate
        String s = "";
        for(int i = 0, row = rowStartIndex; i < 3; row++, i++){
            for(int j = 0, col = colStartIndex; j < 3; col++, j++){
                char c = board[row][col];
                if(c != '.'){
                    if(!s.contains(c + ""))
                        s += c + "";
                    else
                        return false;
                }
            }
        }
        return true;
    }
    private static boolean validateHorizontal(char[] row){
        // Checking if the given row is valid
        for(int i = 0; i < row.length; i++){
            for(int j = i + 1; j < row.length; j++){
                if(row[i] == row[j] && row[i] != '.')
                    return false;
            }
        }
        return true;
    }
    private static boolean validateVertical(char[][] board, int colNum){
        // Checking if the given col is valid
        for(int i = 0; i < board.length; i++){
            for(int j = i + 1; j < board.length; j++){
                if(board[i][colNum] == board[j][colNum] && board[i][colNum] != '.')
                    return false;
            }
        }
        return true;
    }
    private static void printBoard(char[][] board){
        // Printing the board with indecencies to help the player identify the rows and cols easily
        System.out.println("    0 1 2    3 4 5    6 7 8");
        System.out.println("    - - -    - - -    - - -");
        for (int row = 0; row < board.length; row++) {
            System.out.print(row + " | ");
            for (int col = 0; col < board.length; col++) {
                System.out.print(board[row][col] + " ");
                if(col == 2 || col == 5)
                    System.out.print(" | ");
            }
            if(row == 2 || row == 5)
                System.out.println("\n    - - -    - - -    - - -");
            else
                System.out.println();
        }
        System.out.println("    - - -    - - -    - - -");
    }
    private static boolean isFull(char[][] board) {
        // Checking if all the spots in the board are filled
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                if(board[row][col] == '.')
                    return false;
            }
        }
        return true;
    }
}
