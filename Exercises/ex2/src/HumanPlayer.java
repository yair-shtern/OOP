
import java.util.Scanner;

/**
 * A player class for the Tic Tac Toe game.
 *
 * @author Yair Shtern
 */
public class HumanPlayer implements Player {
    private static Scanner in;

    /**
     * Initialize the player.
     */
    public HumanPlayer() {
        in = new Scanner(System.in);
    }

    /**
     * Plays a turn. gets an input from the user (until is valid),
     * and put the mark on the board according to the input.
     *
     * @param board the game board.
     * @param mark  the current player mark.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        System.out.print("Player " + mark + ", type coordinates: ");
        int num = in.nextInt();
        while (!board.putMark(mark, (num / 10) - 1, (num % 10) - 1)) {
            System.out.print("Invalid coordinates, type again: ");
            num = in.nextInt();
        }
    }
}