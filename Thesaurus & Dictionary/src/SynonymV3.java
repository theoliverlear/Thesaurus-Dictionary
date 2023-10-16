import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SynonymV3 {
    String word;
    StringBuilder pageData;
    ArrayList<String> rawSynonyms;
    ArrayList<String> synonyms;
    public SynonymV3(String word) {
        this.word = word;
        this.pageData = new StringBuilder();
        this.rawSynonyms = new ArrayList<>();
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
            this.rawSynonyms.add(matcher.group(1));
        }
        this.normalizeData();
    }
    public void normalizeData() {
        HashSet<String> listHashSet = new HashSet<>();
        for (String rawSynonym : rawSynonyms) {
            StringBuilder rawSynonymFormatted = new StringBuilder(rawSynonym);
            String[] characterCodes = new String[]{"%20", "%27", "%2F", "&#x27;"};
            for (String characterCode : characterCodes) {
                String codeToChar = switch (characterCode) {
                    case "%20" -> " ";
                    case "%27" -> "'";
                    case "%2F" -> "/";
                    case "&#x27;" -> "\'";
                    default -> "";
                };
                while (rawSynonymFormatted.toString().contains(characterCode)) {
                    rawSynonymFormatted.insert(rawSynonym.indexOf(characterCode), codeToChar);
                    rawSynonymFormatted.delete(rawSynonym.indexOf(characterCode) + 1, rawSynonymFormatted.indexOf(characterCode)
                                                                                  + characterCode.length());
                }
            }
            listHashSet.add(rawSynonymFormatted.toString());
        }
        this.synonyms = new ArrayList<>(listHashSet);
    }
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        System.out.print("Enter a word: ");
        String word = userInput.nextLine().trim().toLowerCase();
        SynonymV3 synonym = new SynonymV3(word);
        System.out.println("---Unformatted Synonyms---");
        synonym.rawSynonyms.forEach(System.out::println);
        System.out.println("---Formatted Synonyms---");
        synonym.synonyms.forEach(System.out::println);
    }
}
