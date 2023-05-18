/**
 * A snartypamts player for Tic Tac Tow game.
 */
public class SnartypamtsPlayer implements Player {

    /**
     * A default constructor.
     */
    public SnartypamtsPlayer() {
    }

    /**
     * Play turn. start from the second col top down, if the index is empty puts it's mark there.
     * (if it's not found a free place on the right side (from second col forward) return to the first col).
     *
     * @param board - the board game.
     * @param mark  - the mark to place.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int row = 0, col = 1;
        while (!board.putMark(mark, row, col)) { // illegal or not empty coordinates.
            if (row < Board.SIZE) {
                row++;
                continue;
            }
            // row = board size.
            if (col < Board.SIZE) {
                col++;
            } else { // if col = board size return to the first col.
                col = 0;
            }
            row = 0;
        }
    }
}
