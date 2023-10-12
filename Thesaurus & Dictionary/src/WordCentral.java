import java.util.ArrayList;
import java.util.Scanner;

public class WordCentral {
    ArrayList<Word> wordList;
    public WordCentral() {
        this.wordList = new ArrayList<>();
    }
    public void beginWordCentral() {
        do {
            Word word = new Word();
            word.chooseWordPrompt();
            this.wordList.add(word);
        } while (this.continueWordCentral());
    }
    public boolean continueWordCentral() {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Would you like to search for another word? (y/n)");
        String continueChoice = Utilities.trimAndLower(userInput.next());
        if (continueChoice.equals("y")) {
            return true;
        } else {
            return false;
        }
    }
    public void printWords() {

    }
    public static void main(String[] args) {
        WordCentral wordCentral = new WordCentral();
        wordCentral.beginWordCentral();
    }
}
