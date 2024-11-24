import acm.program.ConsoleProgram;
import acm.program.*;
import acm.util.*;

import java.awt.*;

public class Hangman extends ConsoleProgram {
    private static final int GUESS_COUNT = 8;

    private static RandomGenerator rgen = RandomGenerator.getInstance();
    private static HangmanLexicon lexicon = new HangmanLexicon();

    private HangmanCanvas canvas;

    public void init() {
        canvas = new HangmanCanvas();
        add(canvas);
    }

    public void run() {
        println("Welcome to Hangman!");
        while (true) {
            canvas.reset();
            beginRound();
        }
    }

    // TODO decompose this >
    private void beginRound() {
        int attemptCount = GUESS_COUNT;
        int idx = rgen.nextInt(0, lexicon.getWordCount() - 1);
        String currentWord = lexicon.getWord(idx);
        String guessedWord = getGuessedWord(currentWord.length());

        // display the word initially
        canvas.displayWord(guessedWord);

        /* for testing */
        System.out.println("Word: " + currentWord);
        /*  ---------- */

        while (attemptCount > 0 && !guessedWord.equals(currentWord)) {
            println("The word now looks like this: " + guessedWord);
            println("You have " + attemptCount + " guesses left.");

            char letter = readChar("Your guess: ");

            if (isCorrectGuess(letter, currentWord)) {
                if guessedWord.contains(letter) continue; // in case the letter is repeated

                guessedWord = updateGuessWord(guessedWord, currentWord, letter);
                canvas.displayWord(guessedWord);
                println("That guess is correct.");
            } else {
                attemptCount--;
                canvas.noteIncorrectGuess(letter);
                println("There are no " + letter + "'s in the word.");
            }
        }

        checkGameState(guessedWord, currentWord);

    }

    // replaces "-" character with the correctly guessed letter
    private String updateGuessWord(String guessedWord, String currentWord, char letter) {
        StringBuilder updatedWord = new StringBuilder(guessedWord);

        int charPos = currentWord.indexOf(letter);
        while (charPos != -1) {
            updatedWord.setCharAt(charPos, letter);
            charPos = currentWord.indexOf(letter, charPos + 1);
        }

        return updatedWord.toString();
    }

    // checks if the current word contains the guessed character
    private Boolean isCorrectGuess(char letter, String word) {
        return word.contains("" + letter) ? true : false;
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

        return ch.toUpperCase().charAt(0);
    }

    // handle the game's ending, display results
    private void checkGameState(String guessedWord, String currentWord) {
        if (guessedWord.equals(currentWord)) {
            println("You guessed the word: " + currentWord);
            println("You win.");
        } else {
            println("The word was: " + currentWord);
            println("You lose.");
        }
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
