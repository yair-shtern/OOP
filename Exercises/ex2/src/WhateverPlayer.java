import java.util.Random;

/**
 * A whatever player - a random player for Tic Tac Tow game.
 */
public class WhateverPlayer implements Player {
    private static Random random;

    /**
     * A constructor.
     */
    public WhateverPlayer() {
        random = new Random();
    }

    /**
     * Play turn. choose random coordinates in the board until they are empty, and puts it's mark there.
     *
     * @param board - the board game.
     * @param mark  - the mark to place.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int row = random.nextInt(Board.SIZE);
        int col = random.nextInt(Board.SIZE);
        while (!board.putMark(mark, row, col)) {
            row = random.nextInt(Board.SIZE);
            col = random.nextInt(Board.SIZE);
        }
    }
}
