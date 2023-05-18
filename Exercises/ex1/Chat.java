/**
 * The Chat program implements a simple chat between 2 bots.
 */

import java.util.Scanner;

class Chat {
    // main method
    public static void main(String[] args) {
        String[] bot1ReplyToIlligal = {"what ", "say I should say ", "say again, what do you mean? "};
        String[] bot2ReplyToIlligal = {"whaaat? ", "what is <request> ",
                "say what? <request>? what is <request> ", "again. <request>? "};
        String[] botReplyToLigal = {
                "You want me to say <phrase>, do you? alright: <phrase>",
                "say <phrase>? okay: <phrase> ", "okay: <phrase>", "I'm saying: <phrase>"};
        ChatterBot[] bots = {
                new ChatterBot("Yair", botReplyToLigal, bot1ReplyToIlligal),
                new ChatterBot("David", botReplyToLigal, bot2ReplyToIlligal)
        };
        String statement = "hello";
        System.out.print(bots[bots.length - 1].getName() + ": " + statement);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        for (int i = 0; ; i++) {
            statement = bots[i % bots.length].replyTo(statement);
            System.out.print(bots[i % bots.length].getName() + ": " + statement);
            scanner.nextLine();
        }

    }
}