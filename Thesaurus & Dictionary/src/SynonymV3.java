import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SynonymV3 {
    String word;
    StringBuilder pageData;
    public SynonymV3(String word) {
        this.word = word;
        this.pageData = new StringBuilder();
        BufferedReader parser = null;
        HttpURLConnection connection = null;
        try {
            String baseURL = "https://www.thesaurus.com/browse/";
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
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Pattern pattern = Pattern.compile("data-linkid=\"y2woe7\".*?>(.*?)</a>");
        Matcher matcher = pattern.matcher(pageData.toString());
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }
    public static void main(String[] args) {
        SynonymV3 synonym = new SynonymV3("hello");
    }
}
