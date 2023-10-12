import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class SynonymV2 {
    String word;
    ArrayList<String> synonyms;
    public SynonymV2(String word) {
        this.word = word;
        this.synonyms = new ArrayList<>();
    }
    public void chooseWord() {
        StringBuilder pageData = new StringBuilder();
        BufferedReader parser = null;
        HttpsURLConnection connection = null;
        try {
            String baseURL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
            URL url = new URL(baseURL + this.word);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                System.out.println("Error: " + connection.getResponseCode() +
                                   " " + connection.getResponseMessage());
            } else {
                parser = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String readWrite;
                while ((readWrite = parser.readLine()) != null) {
                    pageData.append(readWrite + "\n");
                }
                System.out.println(pageData);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (parser != null) {
                try {
                    parser.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void main(String[] args) {
        SynonymV2 synonym = new SynonymV2("hello");
        synonym.chooseWord();
    }
}
