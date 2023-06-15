import java.util.ArrayList;
import java.util.Scanner;

public class Word {
    String word;
    Synonym synonym;
    Definition definitions;


    public Word() {
        this.word = "";
        this.synonym = new Synonym(this.word);
        this.definitions = new Definition(this.word);
    }
    public void printData(ArrayList<String> wordList) {
        wordList.forEach(System.out::println);
    }
    public void chooseWordPrompt() {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Please enter a word.");
        //this.word = Utilities.trimAndLower(userInput.nextLine());
        this.word = userInput.nextLine();
        System.out.println("Would you like to see the synonyms (1), or definitions (2) for " + this.word + "?");
        int menuChoice = userInput.nextInt();
        this.synonym.setWord(this.word);
        this.definitions.setWord(this.word);
        if (menuChoice == 1) {
            this.synonym.chooseWord();
            this.printData(this.synonym.getSynonyms());
        } else if (menuChoice == 2) {
            this.definitions.chooseWord();
            this.printData(this.definitions.getDefinitions());
        } else {
            System.out.println("Invalid choice.");
        }
    }
    public void correctWord() {

    }
}

// Word will have synonyms, antonyms, and definitions

