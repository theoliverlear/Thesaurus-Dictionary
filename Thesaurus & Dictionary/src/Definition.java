import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
public class Definition {
    String word;
    ArrayList<String> definitions;
    public Definition(String word) {
        this.word = word;
        this.definitions = new ArrayList<>();
    }// SQL database with definitions synonyms and antonyms of words with pronunciation
    public void setWord(String word) {
        this.word = word;
    }
    public ArrayList<String> getDefinitions() {
        return this.definitions;
    }
    public void setDefinitions(ArrayList<String> definitions) {
        this.definitions = definitions;
    }
    public void chooseWord() {
        StringBuilder pageData = new StringBuilder();
        ArrayList<String> definitions = new ArrayList<>();
        BufferedReader parser = null;
        HttpURLConnection connection = null;
        try {
            String baseURL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
            URL url = new URL(baseURL + this.word);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("Error: " + connection.getResponseCode() +
                                   " " + connection.getResponseMessage());
            } else {
                parser = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String readWrite;
                while ((readWrite = parser.readLine()) != null) {
                    pageData.append(readWrite + "\n");
                }
                pageData.delete(0, pageData.indexOf("\"definitions\":") + 16);
                String[] definitionSplit = pageData.toString().split("\"definition\":");
                for (String phrase : definitionSplit) {
                    if (!phrase.equals("")) {
                        String formattedPhrase = phrase.substring(0, phrase.indexOf("\",\"synonyms\":"));
                        formattedPhrase = formattedPhrase.replace("\"", "");
                        definitions.add(formattedPhrase);
                    }
                }
            }
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            if (parser != null) {
                try {
                    parser.close();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        this.definitions = definitions;
    } // give it a phrase and it swaps with synonyms
}
