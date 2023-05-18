package ascii_art;

import java.util.HashMap;
import java.util.HashSet;

/**
 * A class for solutions of the algo questions in ex4 in OOP.
 *
 * @author Yair Shtern
 */
public class Algorithms {
    private static final int SMALL_A_ASCII_VAL = 97;

    /**
     * Gets an array of n+1 integers in the range [1-n] with only one number that can be duplicate
     * and find it.
     *
     * @param numList - an array of integers.
     * @return the duplicated number.
     */
    public static int findDuplicate(int[] numList) {
        int firstVal = numList[0];
        int secondVal = numList[numList[0]];
        //first passage for finding the "cycle" in the array
        while (firstVal != secondVal) {
            firstVal = numList[firstVal];
            secondVal = numList[numList[secondVal]];
        }
        firstVal = 0;
        //second passage for finding the actual value of the duplicate number
        while (firstVal != secondVal) {
            firstVal = numList[firstVal];
            secondVal = numList[secondVal];
        }
        return firstVal;
    }


    /**
     * Gets an array of strings and return the number of different morse code that create from the strings
     * without repetitions.
     *
     * @param words - an array of strings (words) in lower case.
     * @return - the number of different morse code that create from the words.
     */
    public static int uniqueMorseRepresentations(String[] words) {
        //map the a-z to its morse code
        HashMap<Character, String> morseCharacters = new HashMap<>();
        String[] arr = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---",
                "-.- ", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-",
                "...-", ".--", "-..-", "-.--", "--.."};
        for (int i = 0; i < arr.length; i++) {
            morseCharacters.put((char) (i + SMALL_A_ASCII_VAL), arr[i]);
        }
        //keep in set all the unique morse codes
        HashSet<String> UniqueMorseCodes = new HashSet<>();
        for (String word : words) {
            String s = "";
            //for each word find its morse code
            for (int i = 0; i < word.length(); i++) {
                s += morseCharacters.get(word.charAt(i));
            }
            //add it to the sat
            UniqueMorseCodes.add(s);
        }
        //return the number unique morse codes
        return UniqueMorseCodes.size();
    }
}
