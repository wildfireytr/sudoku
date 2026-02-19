import java.util.Scanner;
public class Main{
    public static void main(String[] args){
        Board board = new Board();
        Scanner scan=new Scanner(System.in);
        boolean terminal= board.presentMenu();
        board.populate(board.getDifficulty());
        board.refresh(terminal);
        boolean active=true;           //main input processing loop
        while(active){
            active=board.processInput();
        }
        System.out.println("Goodbye!");
    }
}
