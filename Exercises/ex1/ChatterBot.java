import java.util.*;

/**
 * Base file for the ChatterBot exercise.
 * The bot's replyTo method receives a statement.
 * If it starts with the constant REQUEST_PREFIX, the bot returns
 * whatever is after this prefix. Otherwise, it returns one of
 * a few possible replies as supplied to it via its constructor.
 * In this case, it may also include the statement after
 * the selected reply (coin toss).
 *
 * @author Dan Nirel
 */

class ChatterBot {
    static final String REQUEST_PREFIX = "say ";
    static final String REQUESTED_PHRASE_PLACEHOLDER = "<phrase>";
    static final String ILLEGAL_REQUEST_PLACEHOLDER = "<request>";
    String name;
    Random rand = new Random();
    String[] repliesToLegalRequest;
    String[] repliesToIllegalRequest;

    // constructor gets a string name and two arrays of optional answers.
    ChatterBot(String name, String[] repliesToLegalRequest,
               String[] repliesToIllegalRequest) {
        this.name = name;
        this.repliesToIllegalRequest = new String[repliesToIllegalRequest.length];
        for (int i = 0; i < repliesToIllegalRequest.length; i = i + 1) {
            this.repliesToIllegalRequest[i] = repliesToIllegalRequest[i];
        }
        this.repliesToLegalRequest = new String[repliesToLegalRequest.length];
        for (int i = 0; i < repliesToLegalRequest.length; i = i + 1) {
            this.repliesToLegalRequest[i] = repliesToLegalRequest[i];
        }
    }

    // reply to statement.
    String replyTo(String statement) {
        if (statement.startsWith(REQUEST_PREFIX)) {
            return respondToLegalRequest(statement);
        }
        return respondToIllegalRequest(statement);
    }

    // if statement start with "say", reply sum answer.
    String respondToLegalRequest(String statement) {
        int randomIndex = rand.nextInt(repliesToLegalRequest.length);
        String responsePattern = repliesToLegalRequest[randomIndex];
        String reply = responsePattern.replaceAll(REQUESTED_PHRASE_PLACEHOLDER, statement);
        return reply;
    }

    // statement not start with "say".
    String respondToIllegalRequest(String statement) {
        int randomIndex = rand.nextInt(repliesToIllegalRequest.length);
        String responsePattern = repliesToIllegalRequest[randomIndex];
        String reply = responsePattern.replaceAll(ILLEGAL_REQUEST_PLACEHOLDER, statement);
        if (rand.nextBoolean()) {
            reply = reply + statement;
        }
        return reply;
    }

    // returns the name of the current bot.
    String getName() {
        return name;
    }
}