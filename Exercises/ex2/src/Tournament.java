/**
 * Tournament class for Tic Tac Tow game.
 */
public class Tournament {
    private final Player[] players = new Player[2];
    private final Renderer renderer;
    private final int rounds;

    /**
     * Initialize the tournament.
     *
     * @param rounds   - num of rounds to play.
     * @param renderer - display renderer.
     * @param players  - an array of players.
     */
    public Tournament(int rounds, Renderer renderer, Player[] players) {
        System.arraycopy(players, 0, this.players, 0, players.length);
        this.renderer = renderer;
        this.rounds = rounds;
    }

    /**
     * Run the tournament. in each round the players mark is switch.
     * In the end prints the game result.
     */
    public void playTournament() {
        int[] winsCount = {0, 0, 0};
        for (int i = 0; i < rounds; i++) {
            Game game = new Game(
                    new Player[]{players[i % players.length], players[(i + 1) % players.length]}, renderer);
            Mark winner = game.run();
            switch (winner) {
                case X:
                    winsCount[i % players.length] += 1;
                    break;
                case O:
                    winsCount[(i + 1) % players.length] += 1;
                    break;
                default:
                    winsCount[2] += 1;
                    break;
            }
        }
        System.out.printf("=== player 1: %d | player 2: %d | Draws: %d ===\r",
                winsCount[0],winsCount[1],winsCount[2]);
    }

    /**
     * The main program. initialize and run the tournament.
     */
    public static void main(String[] args) {
        String ERROR_MSG = "Usage: java Tournament [round count] " +
                "[render target: console/none] [player1: human/clever/whatever/snartypamts] " +
                "[player2: human/clever/whatever/snartypamts]";
        if (args.length != 4) {
            System.err.println(ERROR_MSG);
            return;
        }
        int numOfRounds = Integer.parseInt(args[0]);
        RendererFactory rendererFactory = new RendererFactory();
        Renderer renderer = rendererFactory.buildRenderer(args[1]);
        PlayerFactory playerFactory = new PlayerFactory();
        Player player1 = playerFactory.buildPlayer(args[2]);
        Player player2 = playerFactory.buildPlayer(args[3]);
        if ((numOfRounds <= 0) ||(renderer == null)||(player1 == null) || (player2 == null)){
            System.err.println(ERROR_MSG);
            return;
        }
        Tournament tournament = new Tournament(numOfRounds, renderer, new Player[]{player1, player2});
        tournament.playTournament();
    }
}
