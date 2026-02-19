import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Board {
    int[][] board;
    int[][] solution;
    boolean[][] editable;
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m"; // user-entered
    public static final String BLUE  = "\u001B[34m"; // given clues
    public static final String RED = "\u001B[31m"; // wrong xxx
    public Board() {
        board = new int[9][9];
        solution=new int[9][9];
        editable = new boolean[9][9];
    }
    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public void sleep(int mils){
        try {
            Thread.sleep(mils);
        } catch (InterruptedException e) {
            return;
        }
    }
    Scanner scan=new Scanner(System.in);
    boolean menu=true;
    boolean terminal=false;
    public boolean presentMenu(){
        System.out.println("-------JS Ver 1.8-------");
        System.out.println("Welcome!");
        while(menu){
            System.out.println("1. Config");
            System.out.println("2. Play game");
            System.out.println("3. Credits");
            System.out.println("4. How to play");
            System.out.println("Please enter a number.");
            String cmd=scan.nextLine();
            switch (cmd) {
                case "1" -> {
                    System.out.println("Does your terminal support ANSI?");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    System.out.println("3. What?");
                    cmd = scan.nextLine();
                    switch (cmd) {
                        case "1" -> {
                            terminal = true;
                            System.out.println("Changes saved.");
                            sleep(1000);
                        }
                        case "2" -> {
                            System.out.println("Changes saved.");
                            sleep(1000);
                        }
                        case "3" -> {
                            //check ANSI
                            System.out.println(BLUE + "Blue" + RESET);
                            sleep(500);
                            System.out.println("What do you see?");
                            System.out.println("1. A jumble of random characters");
                            System.out.println("2. Blue blue");
                            String x = scan.nextLine();
                            switch (x) {
                                case "1":
                                    System.out.println("ANSI unsupported :(");
                                    System.out.println("Changes saved.");
                                    sleep(1000);
                                    break;
                                case "2":
                                    System.out.println("ANSI supported!");
                                    terminal = true;
                                    System.out.println("Changes saved.");
                                    sleep(1000);
                                    break;
                            }
                        }
                        default -> System.out.println("Input not recognized.");
                    }
                }
                case "2" -> menu = false;
                case "3" -> {
                    System.out.println("credits: me");
                    sleep(1000);
                }
                case "4" -> {
                    System.out.println("You will be presented with a 9x9 grid partially full of numbers.");
                    System.out.println("The objective of Sudoku is to fill the grid with numbers 1-9 such that");
                    System.out.println("there are no repeat numbers in each row, column, and 3x3 box on the grid.");
                    System.out.println("You can enter <check>, <hint> or <quit> at any point during the game to");
                    System.out.println("check the board for completion, get a hint, or quit the game.");
                    System.out.println("Good luck!");
                    sleep(1000);
                    System.out.println("Press enter to continue.");
                    scan.nextLine();
                }
                default -> System.out.println("Input not recognized.");
            }
        }
        return terminal;
    }
    public int getDifficulty(){
        System.out.println("Select difficulty:");
        System.out.println("1. Easy");
        System.out.println("2. Moderate");
        System.out.println("3. Hard");
        System.out.println("Please enter a number.");
        return switch (scan.nextLine()){
            case "1" -> 1;
            case "2" -> 2;
            case "3" -> 3;
            default -> 0;
        };
    }
    public void refresh(boolean fancy){
        StringBuilder sb=new StringBuilder();
        System.out.println("   0   1   2   3   4   5   6   7   8");
        System.out.println(" _____________________________________");
        for(int r=0;r<9;r++){
            sb.append(" | ");
            for(int c=0;c<9;c++){
                if(board[r][c] == 0){
                    sb.append(" ");
                }
                else if(editable[r][c]){
                    // user-entered -> green
                    if(fancy) sb.append(GREEN).append(board[r][c]).append(RESET);
                    else{
                        sb.setLength(sb.length()-1);
                        sb.append("?").append(board[r][c]);
                    }
                }
                else{
                    // given -> blue
                    //sb.append(BLUE).append(board[r][c]).append(RESET);
                    sb.append(board[r][c]);
                }

                if(c == 2 || c == 5 || c == 8) sb.append(" | ");
                else sb.append(" : ");
            }
            if(r==3||r==6) System.out.println(" -------------------------------------");
            sb.append(" ").append(r);
            System.out.println(sb);
            sb.setLength(0);
        }
        System.out.println(" -------------------------------------");
    }
    public void populate(int diff){
        //populate board with a valid solution
        fillBoard();
        //store the solution for hints(deep copy--solution=board WILL NOT WORK!!)
        for (int r = 0; r < 9; r++) {
            solution[r] = Arrays.copyOf(board[r], 9);
        }
        //remove certain cells
        for(int r=0;r<9;r++){
            for(int c=0;c<9;c++){
                if(diff==1){
                    if(Math.random()<0.1){
                        board[r][c]=0;
                        editable[r][c]=true;
                    }
                }
                else if(diff==2){
                    if(Math.random()<0.5){
                        board[r][c]=0;
                        editable[r][c]=true;
                    }
                }
                else if(diff==3){
                    if(Math.random()<0.8){
                        board[r][c]=0;
                        editable[r][c]=true;
                    }
                }
            }
        }
    }
    //recursive solving algorithm
    public boolean fillBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {               // find the first empty cell
                    int[] numbers = {1,2,3,4,5,6,7,8,9};
                    shuffle(numbers);                     // randomize numbers
                    for (int num : numbers) {
                        if (isValid(row, col, num)) {     // check if number can go here
                            board[row][col] = num;        // place the number
                            if (fillBoard()) {            // recurse to fill the next cell
                                return true;              // if successful, keep going
                            }
                            board[row][col] = 0;          // backtrack: remove number and try next
                        }
                    }
                    return false;                         // if no number fits, trigger backtracking
                }
            }
        }
        return true;                                      // board is completely filled, stop recursion
    }
    //helper--Fischer Yates shuffle
    private void shuffle(int[] a){
        int n=a.length;
        Random rand=new Random();
        for(int i=n-1;i>0;i--){
            int j=rand.nextInt(i+1);
            int temp=a[i];
            a[i]=a[j];
            a[j]=temp;
        }
    }
    //helper--checks if solution is valid for row, col, box
    private boolean isValid(int r, int c, int val){
        for(int i=0;i<9;i++) if(board[i][c]==val) return false;
        for(int i=0;i<9;i++) if(board[r][i]==val) return false;
        int boxRow=(r/3)*3;
        int boxCol=(c/3)*3;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(board[boxRow+i][boxCol+j]==val) return false;
            }
        }
        return true;
    }
    public boolean setCell(int r, int c, int value){
        if(locked(r,c)){
            System.out.println("Specified cell is a given clue. Please try a different one.");
            return false;
        }
        else{
            board[r][c]=value;
            return true;
        }
    }
    //helper--doesn't allow alterations if cell is given
    private boolean locked(int r,int c){return !editable[r][c];}
    public boolean check() {
        for (int row=0;row<9;row++) {
            for (int col=0;col<9;col++) {
                int val=board[row][col];
                if(board[row][col]==0) return false;
                else if(val!=0) {
                    board[row][col] = 0;     // temporarily clear
                    if (!isValid(row,col,val)) {
                        board[row][col]=val; // restore
                        return false;
                    }
                    board[row][col]=val;     // restore
                }
            }
        }
        return true;
    }
    public void hint(int row,int col){
        try{
            System.out.println("Correct value: "+solution[row][col]);
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Faulty input. Please make sure you entered the coordinates correctly.");
        }
    }
    public void checkPrint(boolean fancy){
        StringBuilder sb=new StringBuilder();
        System.out.println("   0   1   2   3   4   5   6   7   8");
        System.out.println(" _____________________________________");
        for(int r=0;r<9;r++){
            sb.append(" | ");
            for(int c=0;c<9;c++){
                if(board[r][c] == 0) {
                    sb.append(" ");
                }
                if(!editable[r][c]){
                    sb.append(board[r][c]);
                }
                if(board[r][c]!=solution[r][c]&&board[r][c]!=0){
                    if(fancy) sb.append(RED).append(board[r][c]).append(RESET);
                    else{
                        sb.setLength(sb.length()-1);
                        sb.append("X").append(board[r][c]);
                    }
                }
                if(editable[r][c]&&board[r][c]==solution[r][c]){
                    sb.append(GREEN).append(board[r][c]).append(RESET);
                }
                if(c == 2 || c == 5 || c == 8) sb.append(" | ");
                else sb.append(" : ");
            }
            if(r==3||r==6) System.out.println(" -------------------------------------");
            sb.append(" ").append(r);
            System.out.println(sb);
            sb.setLength(0);
        }
        System.out.println(" -------------------------------------");
    }
    long startTime = System.currentTimeMillis();
    public boolean processInput(){
        System.out.println("Enter coordinates and value(three unseparated digits, ie. XYV):");
        String input=scan.nextLine();
        switch (input) {
            case "quit" -> {
                return false;
            }
            case "hint" -> {
                System.out.println("Enter coordinates:");
                input = scan.nextLine();
                hint(input.charAt(1) - '0', input.charAt(0) - '0');
                return true;
            }
            case "check" -> {
                if (check()) {
                    System.out.println("You beat the game. Great job!");
                    long endTime = System.currentTimeMillis();
                    long elapsed = endTime - startTime;
                    long totalSeconds = elapsed / 1000;
                    long minutes = totalSeconds / 60;
                    long seconds = totalSeconds % 60;
                    System.out.println("You took: " + minutes + " minutes and " + seconds + " seconds.");
                    return false;
                } else {
                    checkPrint(terminal);
                    System.out.println("Not quite!");
                    return true;
                }
            }
            default -> {
                try {
                    int c = input.charAt(0) - '0';
                    int r = input.charAt(1) - '0';
                    int val = input.charAt(2) - '0';
                    if (setCell(r, c, val)) {
                        clear();
                        refresh(terminal);
                        System.out.println("Updated successfully!");
                    }
                } catch (ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException e) {
                    System.out.println("Faulty input. Please formulate coordinates correctly. Ex, x 4 y 3 val 6: 436");
                }
                return true;
            }
        }
    }
}