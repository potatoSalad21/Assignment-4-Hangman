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

    // draw all structures
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
            wordLabel.setFont(new Font("Arial", Font.PLAIN, 20));

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
            wrongGuessLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            add(wrongGuessLabel, WORD_X_OFFSET, getHeight() / 2 + SCAFFOLD_HEIGHT + WORD_GAP_HEIGHT);
        }

        String prevText = wrongGuessLabel.getLabel();
        wrongGuessLabel.setLabel(prevText + letter);

        int wrongGuessNum = wrongGuessLabel.getLabel().length();
        drawBodyPart(wrongGuessNum);
	}

    // draw a body part according to the number of wrong guesses
    private void drawBodyPart(int num) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        switch (num) {
        case 1:
            drawHead(centerX, centerY);
            break;
        case 2:
            drawBody(centerX, centerY);
            break;
        case 3:
            drawLeftHand(centerX, centerY);
            break;
        case 4:
            drawRightHand(centerX, centerY);
            break;
        case 5:
            drawLeftLeg();
            break;
        case 6:
            drawRightLeg();
            break;
        case 7:
            drawLeftFoot();
            break;
        case 8:
            drawRightFoot();
            break;
        }
    }

    /*
     *  ~~ DRAW METHODS FOR THE STRUCTURES ~~
     */
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

    /*
     *  ~~ DRAW METHODS FOR THE BODY PARTS ~~
     */
    private void drawHead(int centerX, int centerY) {
        int headDiameter = 2 * HEAD_RADIUS;

        int headX = centerX - HEAD_RADIUS;
        int headY = centerY - ARM_OFFSET_FROM_HEAD - headDiameter;

        GOval head = new GOval(headX, headY, headDiameter, headDiameter);
        add(head);
    }

    private void drawBody(int centerX, int centerY) {
        int startY = centerY - ARM_OFFSET_FROM_HEAD;
        int endY = startY + BODY_LENGTH;

        GLine body = new GLine(centerX, startY, centerX, endY);
        add(body);
    }

    private void drawLeftHand(int centerX, int centerY) {
        int upperArmStartX = centerX - UPPER_ARM_LENGTH;

        GLine upperArm = new GLine(upperArmStartX, centerY, centerX, centerY);
        GLine lowerArm = new GLine(upperArmStartX, centerY, upperArmStartX, centerY + LOWER_ARM_LENGTH);

        add(upperArm);
        add(lowerArm);
    }

    private void drawRightHand(int centerX, int centerY) {
        int upperArmEndX = centerX + UPPER_ARM_LENGTH;

        GLine upperArm = new GLine(centerX, centerY, upperArmEndX, centerY);
        GLine lowerArm = new GLine(upperArmEndX, centerY, upperArmEndX, centerY + LOWER_ARM_LENGTH);

        add(upperArm);
        add(lowerArm);
    }

    private void drawLeftLeg() {

    }

    private void drawRightLeg() {

    }

    private void drawLeftFoot() {

    }

    private void drawRightFoot() {

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
