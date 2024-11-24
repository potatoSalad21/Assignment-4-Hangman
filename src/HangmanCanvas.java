/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;
import java.awt.*;

public class HangmanCanvas extends GCanvas {
    private GLabel wordLabel;
    private GLabel wrongGuessLabel;

/** Resets the display so that only the scaffold appears */
	public void reset() {
        removeAll();
        drawStructure();
	}

    private void drawStructure() {
        int startingX = getWidth() / 2 - BEAM_LENGTH;
        int startingY = getHeight() / 2 - ARM_OFFSET_FROM_HEAD - 2 * HEAD_RADIUS - ROPE_LENGTH;

        drawScaffold(startingX, startingY);
        drawBeam(startingX, startingY);
        drawRope(startingY);
    }

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
	public void displayWord(String word) {
        int labelY = getHeight() / 2 + SCAFFOLD_HEIGHT;

        if (wordLabel == null) {
            wordLabel = new GLabel(word);
            wordLabel.setFont(new Font("Serif-25", Font.BOLD, 20));

            add(wordLabel, WORD_X_OFFSET, labelY);
        } else {
            wordLabel.setLabel(word);
        }
	}

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter) {
        if (wrongGuessLabel == null) {
            wrongGuessLabel = new GLabel("");
            wrongGuessLabel.setFont(new Font("Serif-25", Font.BOLD, 16));

            add(wrongGuessLabel, WORD_X_OFFSET, getHeight() / 2 + SCAFFOLD_HEIGHT + WORD_GAP_HEIGHT);
        }

        String prevText = wrongGuessLabel.getLabel();
        wrongGuessLabel.setLabel(prevText + letter);
	}

    private void drawScaffold(int startingX, int startingY) {
        GLine scaffold = new GLine(startingX, startingY, startingX, startingY + SCAFFOLD_HEIGHT);
        add(scaffold);
    }

    private void drawBeam(int startingX, int startingY) {
        GLine beam = new GLine(startingX, startingY, startingX + BEAM_LENGTH, startingY);
        add(beam);
    }

    private void drawRope(int startingY) {
        int centerX = getWidth() / 2;
        GLine rope = new GLine(centerX, startingY, centerX, startingY + ROPE_LENGTH);
        add(rope);
    }

/* Constants for the simple version of the picture (in pixels) */
    private static final int WORD_X_OFFSET = 50;
    private static final int WORD_GAP_HEIGHT = 20;

	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;

}
