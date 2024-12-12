import java.io.IOException;
import java.io.PrintWriter;

import acm.program.ConsoleProgram;
import acm.util.RandomGenerator;
import acm.util.SwingTimer;
import acm.util.MediaTools;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.awt.event.*;
import java.applet.*;

public class HangmanExt extends ConsoleProgram {
    private static final int GUESS_COUNT = 8;
    private static final int ROUND_TIME = 5;
    private static final int TIME_BETWEEN_ROUNDS = 300;
    private static final String ASSET_PATH = "./assets/";

    private static AudioClip deathSfx = MediaTools.loadAudioClip(ASSET_PATH + "death.wav");
    private static AudioClip winSfx = MediaTools.loadAudioClip(ASSET_PATH + "win.wav");
    private static AudioClip incorrectGuessSfx = MediaTools.loadAudioClip(ASSET_PATH + "incorrect.wav");
    private static AudioClip tickSfx = MediaTools.loadAudioClip(ASSET_PATH + "tick.wav");

    private static RandomGenerator rgen = RandomGenerator.getInstance();
    private static HangmanLexiconExt lexicon = new HangmanLexiconExt();

    private HangmanCanvasExt canvas;
    private ScheduledExecutorService scheduler;
    private int timeLeft;
    private String currentWord;
    private boolean roundRunning = false;
    private int roundNum = 0;

    public void init() {
        canvas = new HangmanCanvasExt();
        add(canvas);
    }

    public void run() {
        println("Welcome to Hangman!");
        while (true) {
            canvas.reset();
            beginRound();
            println();

            pause(TIME_BETWEEN_ROUNDS);
        }
    }

    // runs one round of the game
    private void beginRound() {
        // set initial values before the round
        roundRunning = true;
        timeLeft = ROUND_TIME;
        int attemptCount = GUESS_COUNT;

        roundNum++;
        int idx = rgen.nextInt(0, lexicon.getWordCount() - 1);
        currentWord = lexicon.getWord(idx);
        String guessedWord = getGuessedWord(currentWord.length());

        // for testing
        System.out.println(currentWord);

        initTimer();
        canvas.displayWord(guessedWord);
        canvas.drawRoundCount(roundNum);
        runAttempts(attemptCount, guessedWord);
    }

    // continuously accepts letter input from user and checks if it's correct (and also valid)
    private void runAttempts(int attemptCount, String guessedWord) {
        while (roundRunning && attemptCount > 0 && !guessedWord.equals(currentWord)) {
            println("The word now looks like this: " + guessedWord);
            println("You have " + attemptCount + " guesses left.");
            char letter = Character.toUpperCase(readChar("Your guess: "));

            if (isCorrectGuess(letter)) {
                if (guessedWord.contains("" + letter)) continue; // in case the letter is repeated

                guessedWord = updateGuessWord(guessedWord, letter);
                handleCorrectGuess(guessedWord);
            } else {
                attemptCount--;
                handleIncorrectGuess(letter, attemptCount);
            }
        }
        checkGameState(guessedWord);
    }

    // starts the timer on a seperate thread to limit the player in time
    private void initTimer() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> timerTask(), 0, 1, TimeUnit.SECONDS);
    }

    // task which is to be run in set intervals
    private void timerTask() {
        timeLeft--;
        tickSfx.play();
        canvas.updateTimer(timeLeft, ROUND_TIME);
        if (timeLeft <= 0) {
            roundRunning = false;
            scheduler.shutdown();
            handleGameLoss();
            println("Press enter to play again!!!");
        }
    }

    private void handleCorrectGuess(String guess) {
        canvas.displayWord(guess);
        timeLeft++;  // +1 sec for every correct guess
        println("That guess is correct.");
    }

    // in case the guessed letter was not in the word
    private void handleIncorrectGuess(char letter, int attemptCount) {
        if (!roundRunning) {
            return;
        }

        incorrectGuessSfx.play();
        int wrongGuessNum = GUESS_COUNT - attemptCount;
        canvas.noteIncorrectGuess(letter, wrongGuessNum);

        println("There are no " + letter + "'s in the word.");
    }

    // replaces "-" character with the correctly guessed letter
    private String updateGuessWord(String guessedWord, char letter) {
        StringBuilder updatedWord = new StringBuilder(guessedWord);

        int charPos = currentWord.indexOf(letter);
        while (charPos != -1) {
            updatedWord.setCharAt(charPos, letter);
            charPos = currentWord.indexOf(letter, charPos + 1);
        }

        return updatedWord.toString();
    }

    // checks if the current word contains the guessed character
    private boolean isCorrectGuess(char letter) {
        return currentWord.contains("" + letter) ? true : false;
    }

    // general method for accepting character input
    private char readChar(String prompt) {
        String ch;
        while (true) {
            ch = readLine(prompt);
            if (!roundRunning && ch.equals("")) {   // if user wants to start another round
                return '0';
            }

            if (ch == null || ch.length() != 1) continue;

            if (isInvalidCharInput(ch.charAt(0))) {
                println("Error: invalid input, enter a single letter");
                ch = null;
            } else break; // if the input is valid break out of the loop
        }

        return ch.charAt(0);
    }

    // handle the game's ending, display results
    private void checkGameState(String guessedWord) {
        if (!roundRunning) {  // in case player lost from the timer
            return;
        }

        roundRunning = false;
        if (guessedWord.equals(currentWord)) {
            handleGameWin();
        } else {
            handleGameLoss();
        }

        scheduler.shutdown();
    }

    private void handleGameLoss() {
        deathSfx.play();
        roundNum = 0;
        println("The word was: " + currentWord);
        println("You lose.");
    }

    private void handleGameWin() {
        winSfx.play();
        println("You guessed the word: " + currentWord);
        println("You win.");
    }

    // fill the string with "-" characters
    private String getGuessedWord(int length) {
        String guessedWord = "";
        for (int i = 0; i < length; i++) {
            guessedWord += "-";
        }

        return guessedWord;
    }

    // checks if the character is a letter
    private boolean isInvalidCharInput(char ch) {
        if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) {
            return false;
        }

        return true;
    }
}
