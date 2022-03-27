/**
 * A clever player for Tic Tac Tow game.
 */
public class CleverPlayer implements Player {

    /**
     * A default constructor.
     */
    public CleverPlayer() {
    }

    /**
     * Play turn. start from the first row, and for every index in the row:
     * if the index is empty puts it's mark there.
     *
     * @param board - the board game.
     * @param mark  - the mark to place.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int row = 0, col = 0;
        while (!board.putMark(mark, row, col)) { // illegal or not empty coordinates.
            if (col < Board.SIZE) {
                col++;
            } else {
                row++;
                col = 0;
            }
        }
    }
}
