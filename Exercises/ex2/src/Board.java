
/**
 * A board class for the Tic Tac Toe game.
 *
 * @author Yair Shtern
 */
public class Board {
    public static final int SIZE = 6;
    public static final int WIN_STREAK = 4;
    private final Mark[][] board;
    private boolean gameEnded = false;
    private int numOfBlanks = SIZE * SIZE;
    private Mark winner = Mark.BLANK;

    /**
     * Initialize every index in the board to BLANK.
     */
    public Board() {
        board = new Mark[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = Mark.BLANK;
            }
        }
    }

    /**
     * Puts mark in a given index on the board.
     *
     * @param mark the mark to put.
     * @param row  the row index.
     * @param col  the col index.
     * @return true if operation was successful and false otherwise.
     */
    public boolean putMark(Mark mark, int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return false;
        }
        if (getMark(row, col) != Mark.BLANK) {
            return false;
        }
        board[row][col] = mark;
        if (thereIsAWinner(row, col, mark)) {
            gameEnded = true;
            winner = mark;
        }
        numOfBlanks--;
        return true;

    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game ended and false otherwise.
     */
    public boolean gameEnded() {
        return numOfBlanks == 0 || gameEnded;
    }

    /**
     * Gets the winner of the game.
     *
     * @return an enum {DRAW/X_WIN/O_WIN}.
     */
    public Mark getWinner() {
        return winner;
    }

    /**
     * Gets the mark at a given index in the board.
     *
     * @param row the row index.
     * @param col the col index.
     * @return the mark at the index. BLANK if it's illigal index.
     */
    public Mark getMark(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return Mark.BLANK;
        }
        return board[row][col];
    }

    /**
     * Chacks if there is a winner after the current turn.
     *
     * @param row  the current row index.
     * @param col  the current col index.
     * @param mark the current mark that placed in the given index.
     * @return true if there is a winner and false if there is'nt.
     */
    private boolean thereIsAWinner(int row, int col, Mark mark) {
        return (countMarkInDirection(row, col, 0, 1, mark) +
                countMarkInDirection(row, col, 0, -1, mark) > WIN_STREAK) ||
                (countMarkInDirection(row, col, -1, 0, mark) +
                        countMarkInDirection(row, col, 1, 0, mark) > WIN_STREAK) ||
                (countMarkInDirection(row, col, 1, -1, mark) +
                        countMarkInDirection(row, col, -1, 1, mark) > WIN_STREAK) ||
                (countMarkInDirection(row, col, 1, 1, mark) +
                        countMarkInDirection(row, col, -1, -1, mark) > WIN_STREAK);
    }

    /**
     * Checks the streak of the given mark from the index in a given direction.
     *
     * @param row      the current row.
     * @param col      the current col.
     * @param rowDelta the row direction.
     * @param colDelta the col direction.
     * @param mark     the current mark.
     * @return the streak length.
     */
    private int countMarkInDirection(int row, int col, int rowDelta, int colDelta, Mark mark) {
        int count = 0;
        while (row < SIZE && row >= 0 && col < SIZE && col >= 0 && board[row][col] == mark) {
            count++;
            row += rowDelta;
            col += colDelta;
        }
        return count;
    }
}