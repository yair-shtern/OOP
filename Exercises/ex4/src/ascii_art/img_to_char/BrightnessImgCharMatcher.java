package ascii_art.img_to_char;

import image.Image;

import java.awt.*;
import java.util.HashMap;

/**
 * A class that gets an image and can match a tow dimensional array of chars with similar brightness.
 *
 * @author Yair Shtern
 */
public class BrightnessImgCharMatcher {
    private final HashMap<Image, Double> cache = new HashMap<>();
    private static final int DEFAULT_NUM_OF_PIXELS = 16;
    private static final double RED_PART_IN_IMAGE = 0.2126;
    private static final double GREEN_PART_IN_IMAGE = 0.7152;
    private static final double BLUE_PART_IN_IMAGE = 0.0722;
    private static final double WHITE_VALUE = 255;
    private final Image img;
    private final String fontName;

    /**
     * Constructor
     *
     * @param img      - The image object to read.
     * @param fontName - A specific font of Characters.
     */
    public BrightnessImgCharMatcher(Image img, String fontName) {
        this.img = img;
        this.fontName = fontName;
    }

    /**
     * The main method of the class, convert th image to a tow dimensional array of chars (that given as a
     * parameter) with similar brightness.
     *
     * @param numCharsInRow - The num chars in a row in the result array.
     * @param charSet       - Character array to choose from.
     * @return - A tow dimensional array of chars.
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        if (charSet.length == 0) {
            return new char[0][0];
        }
        double[] charsBrightness = getBrightness(charSet);
        int pixels = img.getWidth() / numCharsInRow;
        return convertImageToAscii(charSet, charsBrightness, pixels);
    }

    //gets an array of chars and of brightness and returns a dimensional array of chars from the chars given
    //that every char is the closes to the brightness of the sub image in the same index on th image.
    private char[][] convertImageToAscii(Character[] charSet, double[] CharsBrightness, int pixels) {
        char[][] asciiArt = new char[img.getHeight() / pixels][img.getWidth() / pixels];
        int row = 0, col = 0;
        //match for every sub image a char with similar brightness
        for (Image subImage : img.squareSubImagesOfSize(pixels)) {
            int indexCloseToAverage = getIndexCloseToAverage(CharsBrightness, subImage);
            asciiArt[row][col] = charSet[indexCloseToAverage];
            //update indexes
            if (col >= img.getWidth() / pixels - 1) {
                col = 0;
                row++;
            } else {
                col++;
            }
        }
        return asciiArt;
    }

    //return the closest index to the average brightness of the image
    private int getIndexCloseToAverage(double[] charsBrightness, Image image) {
        //gets the average brightness of the image
        Double imageAverageBrightness = cache.get(image);
        if (imageAverageBrightness == null) {
            //if image brightness not in the map
            imageAverageBrightness = findImageAverageBrightness(image);
            //update the image brightness in the map
            cache.put(image, imageAverageBrightness);
        }
        int indexCloseToAverage = 0;
        double valueCloseToAverage = 1; //max distance is 1
        //find the closest index
        for (int i = 0; i < charsBrightness.length; i++) {
            if (Math.abs(imageAverageBrightness - charsBrightness[i]) <= valueCloseToAverage) {
                indexCloseToAverage = i;
                valueCloseToAverage = Math.abs(imageAverageBrightness - charsBrightness[i]);
            }
        }
        return indexCloseToAverage;
    }

    //gets an array of chars and returns an array that maps the brightness every item in the given array.
    private double[] getBrightness(Character[] charSet) {
        double[] brightness = new double[charSet.length];
        for (int i = 0; i < charSet.length; i++) {
            brightness[i] = getBrightnessForChar(charSet[i]);
        }
        return newCharsBrightness(brightness, findMax(brightness), findMin(brightness));
    }


    //private method that gets a char and returns it brightness
    private double getBrightnessForChar(char c) {
        boolean[][] brightness = CharRenderer.getImg(c, DEFAULT_NUM_OF_PIXELS, fontName);
        double counter = 0;
        //sum all the true val
        for (boolean[] booleans : brightness) {
            for (boolean val : booleans) {
                if (val) {
                    counter++;
                }
            }
        }
        //divide by the num of pixels
        return counter / (DEFAULT_NUM_OF_PIXELS * DEFAULT_NUM_OF_PIXELS);
    }

    //normalize the values of the given array
    private double[] newCharsBrightness(double[] brightness, double maxVal, double minVal) {
        if (maxVal == minVal) {
            return new double[brightness.length];
        }
        for (int i = 0; i < brightness.length; i++) {
            //a formula for normalize the values
            brightness[i] = (brightness[i] - minVal) / (maxVal - minVal);
        }
        return brightness;
    }

    //find the average brightness of image
    private double findImageAverageBrightness(Image img) {
        double sum = 0;
        for (Color pixel : img.pixels()) {
            //a formula to calculate the grey value from a colored pixel
            sum += (pixel.getRed() * RED_PART_IN_IMAGE +
                    pixel.getGreen() * GREEN_PART_IN_IMAGE +
                    pixel.getBlue() * BLUE_PART_IN_IMAGE) / WHITE_VALUE;
        }
        return sum / (img.getWidth() * img.getHeight());
    }

    //find min value in array
    private double findMin(double[] brightness) {
        double min = 1;
        for (double value : brightness) {
            if (value <= min) {
                min = value;
            }
        }
        return min;
    }

    //find max value in array
    private double findMax(double[] brightness) {
        double max = 0;
        for (double value : brightness) {
            if (value >= max) {
                max = value;
            }
        }
        return max;
    }
}
