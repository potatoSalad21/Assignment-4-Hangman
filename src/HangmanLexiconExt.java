/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class HangmanLexiconExt {
    ArrayList wordList;

    // lexicon class constructor
    public HangmanLexiconExt() {
        this.wordList = readFile("./ShorterLexicon.txt");
    }

/** Returns the number of words in the lexicon. */
	public int getWordCount() {
        return wordList.size();
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
        return (String) wordList.get(index);
	}

    // returns the array of file lines
    private ArrayList readFile(String path) {
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(path));

            while (true) {
                String line = reader.readLine();
                lines.add(line);

                if (line == null) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return lines;
    }
}
