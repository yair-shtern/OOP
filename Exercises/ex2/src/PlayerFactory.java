/**
 * A player factory.
 */
public class PlayerFactory {
    private static final String HUMAN = "human";
    private static final String CLEVER = "clever";
    private static final String WHATEVER = "whatever";
    private static final String SNARTYPAMTS = "snartypamts";

    /**
     * A default constructor.
     */
    public PlayerFactory() {
    }

    /**
     * Build player. gets a string and return Player object.
     *
     * @param type - player type.
     * @return Player object if type is valid input, null otherwise.
     */
    public Player buildPlayer(String type) {
        switch (type) {
            case HUMAN:
                return new HumanPlayer();
            case CLEVER:
                return new CleverPlayer();
            case WHATEVER:
                return new WhateverPlayer();
            case SNARTYPAMTS:
                return new SnartypamtsPlayer();
            default:
                return null;
        }
    }
}
