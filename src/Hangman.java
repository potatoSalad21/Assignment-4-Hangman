import acm.program.ConsoleProgram;
import acm.program.*;
import acm.util.*;

import java.awt.*;

public class Hangman extends ConsoleProgram {
    private static final int GUESS_COUNT = 8;

    private static RandomGenerator rgen = RandomGenerator.getInstance();
    private static HangmanLexicon lexicon = new HangmanLexicon();

    public void run() {
        println("Welcome to Hangman!");
        beginRound();
    }

    private void beginRound() {
        int attemptCount = GUESS_COUNT;
        int idx = rgen.nextInt(0, lexicon.getWordCount() - 1);
        String currentWord = lexicon.getWord(idx);
        String guessedWord = getGuessedWord(currentWord.length());

        System.out.println("Word: " + currentWord);

        while (attemptCount > 0 && !guessedWord.equals(currentWord)) {
            println("The word now looks like this: " + guessedWord);
            println("You have " + attemptCount + " guesses left.");

            char guess = readChar("Your guess: ");

            if (isValidGuess(guess, currentWord)) {
                guessedWord = updateGuessWord(guessedWord, currentWord, guess);
                println("That guess is correct.");
            } else {
                attemptCount--;
                println("There are no " + guess + "'s in the word.");
            }
        }

        if (guessedWord.equals(currentWord)) {
            println("You guessed the word: " + currentWord);
            println("You win.");
        } else {
            println("The word was: " + currentWord);
            println("You lose.");
        }
    }

    // replaces "-" character with the correctly guessed letter
    private String updateGuessWord(String guessedWord, String currentWord, char guess) {
        StringBuilder updatedWord = new StringBuilder(guessedWord);

        int charPos = currentWord.indexOf(guess);
        while (charPos != -1) {
            updatedWord.setCharAt(charPos, guess);
            charPos = currentWord.indexOf(guess, charPos + 1);
        }

        return updatedWord.toString();
    }

    // checks if the current word contains the guessed character
    private Boolean isValidGuess(char guess, String word) {
        return word.contains("" + guess) ? true : false;
    }

    // general method for accepting character input
    private char readChar(String prompt) {
        String ch = null;

        while (ch == null || ch.length() != 1) {
            ch = readLine(prompt);
            if (isInvalidCharInput(ch.charAt(0))) {
                println("Error: invalid input, enter a single letter");
                ch = null;
            }
        }

        System.out.println("WHAT");
        return ch.toUpperCase().charAt(0);
    }

    // checks if the character is a letter
    private Boolean isInvalidCharInput(char ch) {
        if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) {
            return false;
        }

        return true;
    }

    // fill the string with "-" characters
    private String getGuessedWord(int length) {
        String guessedWord = "";
        for (int i = 0; i < length; i++) {
            guessedWord += "-";
        }

        return guessedWord;
    }
}
