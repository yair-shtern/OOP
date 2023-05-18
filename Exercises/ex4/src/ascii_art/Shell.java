package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A shell class that runs the convert image to ascii program.
 *
 * @author Yair Shtern
 */
public class Shell {
    private static final String VISUAL_ARROWS = ">>> ";
    private static final String CHARS = "chars";
    private static final String RENDER = "render";
    private static final String CONSOLE = "console";
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String RES = "res";
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String ALL = "all";
    private static final String SPACE = "space";
    private static final String SPACE_REGEX = "\\s+";
    private static final String WIDTH_SET_MSG = "Width set to ";
    private static final String PARAMETER_MISSING_ERR_MSG =
            "Parameter is missing, please enter valid command";
    private static final String EMPTY_STR = "";
    private static final char EMPTY_CHAR = ' ';
    private static final char FINAL_CHAR = '~';
    private final Set<Character> charSet = new HashSet<>();
    private static final String VALID_COMMAND_PATTERN = "\\s*|\\s*chars\\s*|\\s*render\\s*|\\s*console\\s*|" +
            "\\s*add\\s+([!-~]|[!-~]-[!-~]|space|all)\\s*|" +
            "\\s*remove\\s+([!-~]|[!-~]-[!-~]|space|all)\\s*|\\s*res\\s+(up|down)\\s*";
    private static final String PATTERN_FOR_MISSING_ARG = "\\s*add\\s*|\\s*remove\\s*|\\s*res\\s*";
    private static final String MAX_RESOLUTION_MSG = "You're using the maximal resolution";
    private static final String MIN_RESOLUTION_MSG = "You're using the minimal resolution";
    private static final String CMD_EXIT = "exit";
    private static final String FONT_NAME = "Courier New";
    private static final String OUTPUT_FILENAME = "out.html";
    private static final String INITIAL_CHARS_RANGE = "0-9";
    private static final String ERR_MSG = "Invalid command, try again";
    private final BrightnessImgCharMatcher charMatcher;
    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final int MIN_PIXELS_PER_CHAR = 2;
    private AsciiOutput output;
    private final int minCharsInRow;
    private final int maxCharsInRow;
    private int charsInRow;
    private final Pattern pattern;
    private final Pattern patternForErrors;
    private Matcher matcher;

    /**
     * Constructor
     *
     * @param img - image object to render.
     */
    public Shell(Image img) {
        minCharsInRow = Math.max(1, img.getWidth() / img.getHeight());
        maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow), minCharsInRow);
        charMatcher = new BrightnessImgCharMatcher(img, FONT_NAME);
        output = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME);
        addChars(INITIAL_CHARS_RANGE);

        //create pattern of all the valid commands
        pattern = Pattern.compile(VALID_COMMAND_PATTERN);

        //create pattern for errors
        patternForErrors = Pattern.compile(PATTERN_FOR_MISSING_ARG);
    }

    /**
     * runs the program while the user didn't enter exit
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(VISUAL_ARROWS);
        String cmd = scanner.nextLine().trim();
        String[] words = cmd.split(SPACE_REGEX);
        while (!cmd.equals(CMD_EXIT)) {
            //create Mather from the command and the pattern
            matcher = pattern.matcher(cmd);
            if (!cmd.equals(EMPTY_STR)) {
                if (matcher.matches()) {
                    //valid command
                    handleCmd(cmd, words);
                } else {
                    //invalid command
                    handleError(cmd);
                }
            }
            System.out.print(VISUAL_ARROWS);
            cmd = scanner.nextLine().trim();
            words = cmd.split(SPACE_REGEX);
        }
    }

    /*
     * print error massage when it's invalid parameters
     */
    private void handleError(String cmd) {
        //prints error message
        matcher = patternForErrors.matcher(cmd);
        if (matcher.matches()) {
            //parameter is missing
            System.out.println(PARAMETER_MISSING_ERR_MSG);
        } else {
            //invalid parameter
            System.out.println(ERR_MSG);
        }
    }

    /*
     * handle the command from the user.
     */
    private void handleCmd(String cmd, String[] words) {
        //assume legal input, I handle with it in run
        switch (cmd) {
            case CHARS:
                showChars();
                break;
            case RENDER:
                render();
                break;
            case CONSOLE:
                console();
                break;
            default:
                handleParam(words);
                break;
        }
    }

    /*
     * handle command of length 2
     */
    private void handleParam(String[] words) {
        //assume legal input. (words length is 2)
        String param;
        param = words[1];
        switch (words[0]) {
            case ADD:
                addChars(param);
                break;
            case REMOVE:
                removeChars(param);
                break;
            case RES:
                resChange(param);
                break;
        }
    }

    /*
     * make the command "chars", prints all the chars in the set according their ascii value
     */
    private void showChars() {
        //print the chars in the set in the ascii order
        charSet.stream().sorted().forEach(c -> System.out.print(c + " "));
        System.out.println();
    }

    /*
     * add chars in a given range to the chars set.
     */
    private void addChars(String s) {
        char[] range = parseCharRange(s);
        //adds all the chars in the range
        Stream.iterate(range[0], c -> c <= range[1], c -> (char) ((int) c + 1)).forEach(charSet::add);
    }

    /*
     * remove chars in a given range from the chars set.
     */
    private void removeChars(String s) {
        char[] range = parseCharRange(s);
        //removes all the chars in the range
        Stream.iterate(range[0], c -> c <= range[1], c -> (char) ((int) c + 1)).forEach(charSet::remove);
    }

    /*
     * make the res command, if the result will not exceed the minimum/maximum resolution,
     * multiples the resolution by 2 if the command is "up" and by 1/2 if it's "down".
     */
    private void resChange(String s) {
        switch (s) {
            case UP:
                if (charsInRow < maxCharsInRow) {
                    charsInRow *= 2;
                } else {
                    //we are at the maximum resolution
                    System.out.println(MAX_RESOLUTION_MSG);
                    return;
                }
                break;
            case DOWN:
                if (charsInRow > minCharsInRow) {
                    charsInRow /= 2;
                } else {
                    //we are at the minimum resolution
                    System.out.println(MIN_RESOLUTION_MSG);
                    return;
                }
                break;
        }//res went successfully print the current resolution
        System.out.println(WIDTH_SET_MSG + charsInRow);
    }

    /*
     * "render" command, render the image into the output with the chars from the chars set.
     */
    private void render() {
        if (!charSet.isEmpty()) {
            char[][] chars = charMatcher.chooseChars(charsInRow, charSet.toArray(new Character[0]));
            this.output.output(chars);
        }
    }

    /*
     * "console" command, changes the output to a console output.
     */
    private void console() {
        output = new ConsoleAsciiOutput();
    }

    /*
     * parse and handle command with more than one parameter.
     * we're assuming a valid input in this method
     */
    private static char[] parseCharRange(String param) {
        switch (param) {
            case ALL:
                return new char[]{EMPTY_CHAR, FINAL_CHAR};
            case SPACE:
                return new char[]{EMPTY_CHAR, EMPTY_CHAR};
            default:
                if (param.length() == 1) {
                    //add one character
                    return new char[]{param.charAt(0), param.charAt(0)};
                }
                //add range of characters. we assume valid input.
                return new char[]{(char) Math.min(param.charAt(0), param.charAt(2)),
                        (char) Math.max(param.charAt(0), param.charAt(2))};
        }
    }
}