
package ascii_art;

import image.Image;

import java.util.logging.Logger;

/**
 * The main class that drive the all program.
 *
 * @author yair shtern
 */
public class Driver {

    private static final String USAGE_ERROR_MSG = "USAGE: java asciiArt ";
    private static final String INVALID_IMAGE_MSG = "Failed to open image file ";

    /**
     * The main method.
     *
     * @param args - The cmd arguments
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(USAGE_ERROR_MSG);
            return;
        }
        Image img = Image.fromFile(args[0]);
        if (img == null) {
            Logger.getGlobal().severe(INVALID_IMAGE_MSG + args[0]);
            return;
        }
        new Shell(img).run();
    }
}

