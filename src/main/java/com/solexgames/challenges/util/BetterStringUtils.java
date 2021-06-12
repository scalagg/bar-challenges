package com.solexgames.challenges.util;

import lombok.experimental.UtilityClass;

/**
 * @author NV6
 * @since 6/12/2021
 */

@UtilityClass
public class BetterStringUtils {

    /**
     * Capitalize a word
     *
     * @param strings the strings to capitalize
     * @return the capitalized string
     */
    public String capitalize(String... strings) {
        final StringBuilder builder = new StringBuilder();

        for (String string : strings) {
            builder
                    .append(Character.toUpperCase(string.toCharArray()[0]))
                    .append(string.substring(1));
        }

        return builder.toString();
    }

    /**
     * Repeat a string an x amount of times
     *
     * @param string the string to repeat
     * @param amount the amount of times the string should be repeated
     * @return the string generated with the provided arguments
     */
    public String repeat(String string, int amount) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < amount; i++) {
            builder.append(string);
        }

        return builder.toString();
    }

    /**
     * Abbreviate a string
     *
     * @param string the string to abbreviate
     * @return the abbreviated string
     */
    public String abbreviate(String string) {
        final String[] split = string.split(" ");
        final StringBuilder toReturn = new StringBuilder();

        for (String current : split) {
            toReturn
                    .append(current.toCharArray()[0])
                    .append(".");
        }

        return toReturn.toString();
    }

    /**
     * Wrap the string to a multi-line string
     *
     * @param string     the string to wrap
     * @param amount     the amount of words per line
     * @param splitRegex the regex to use to split the string
     * @return the wrapped string
     */
    public String[] wrap(String string, int amount, String splitRegex) {
        final String[] split = string.split(splitRegex);
        final String[] toReturn = new String[split.length / amount];

        int counter = 0;
        int counter2 = 0;

        for (String current : split) {
            if (counter2++ == amount) {
                counter++;
                counter2 = 0;
            }

            if (toReturn[counter] == null) {
                toReturn[counter] = current;
            } else {
                toReturn[counter] += current;
            }

            if (counter2 != amount) {
                toReturn[counter] += " ";
            }
        }

        return toReturn;
    }

    /**
     * Wrap the string to a multi-line string
     *
     * @param string the string to wrap
     * @param amount the amount of words per line
     * @return the wrapped string
     */
    public String[] wrap(String string, int amount) {
        return wrap(string, amount, " ");
    }
}
