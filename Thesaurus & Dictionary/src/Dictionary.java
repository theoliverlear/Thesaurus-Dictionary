import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Dictionary {
    public Dictionary() {

    }// SQL database with definitions synonyms and antonyms of words with pronunciation
    public static void chooseWord() {
        StringBuilder pageData = new StringBuilder();
        try {
            Scanner userInput = new Scanner(System.in);
            System.out.println("Please enter a word.");
            String word = userInput.next().toLowerCase();
            URL url = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/" + word);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader parser = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String readWrite;
            while ((readWrite = parser.readLine()) != null) {
                pageData.append(readWrite + "\n");
                // starts with "definitions":
                // ends with "license":
            }
            System.out.println(pageData);
            parser.close();
            connection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        chooseWord();
    }
}
