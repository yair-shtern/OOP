
/**
 * A Tic Tac Toe game class.
 *
 * @author Yair Shtern
 */
public class Game {
    private final Player[] players = new Player[2];
    private final Mark[] marks = {Mark.X, Mark.O};
    private final Renderer renderer;

    /**
     * Initialize the game with two players.
     *
     * @param players an array of players.
     * @param renderer renderer object the display the board.
     */
    public Game(Player[] players, Renderer renderer) {
        System.arraycopy(players, 0, this.players, 0, players.length);
        this.renderer = renderer;
    }

    /**
     * Runs the game.
     *
     * @return an enum of the winning player.
     */
    public Mark run() {
        Board board = new Board();
        renderer.renderBoard(board);
        for (int i = 0; !board.gameEnded(); i++){
            players[i % players.length].playTurn(board,marks[i % players.length]);
            renderer.renderBoard(board);
        }
        renderer.renderBoard(board);
        return board.getWinner();
    }
}
