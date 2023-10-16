import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Synonym {
    String word;
    ArrayList<String> synonyms;
    ArrayList<String> rawSynonyms;
    public Synonym(String word) {
        this.word = word;
        this.synonyms = new ArrayList<>();
    }
    public void setWord(String word) {
        this.word = word;
    }
    public ArrayList<String> getSynonyms() {
        return this.synonyms;
    }
    public void setSynonyms(ArrayList<String> synonyms) {
        this.synonyms = synonyms;
    }

    public static ArrayList<String> normalizeData(StringBuilder list) {
        HashSet<String> listHashSet = new HashSet<>();
        String[] listArray = list.toString().split("\n");
        for (String singlePhrase: listArray) {
            StringBuilder singlePhraseFormat = new StringBuilder(singlePhrase);
            if (singlePhraseFormat.toString().contains("?")) {
                singlePhraseFormat.delete(singlePhraseFormat.indexOf("?"), singlePhraseFormat.length());
            }
            String[] characterCodes = new String[]{"%20", "%27", "%2F"};
            for (String characterCode : characterCodes) {
                String codeToChar = "";
                if (characterCode.equals("%20")) {
                    codeToChar = " ";
                } else if (characterCode.equals("%27")) {
                    codeToChar = "'";
                } else if (characterCode.equals("%2F")) {
                    codeToChar = "/";
                }
                while (singlePhraseFormat.toString().contains(characterCode)) {
                    singlePhraseFormat.insert(singlePhraseFormat.indexOf(characterCode), codeToChar);
                    singlePhraseFormat.delete(singlePhraseFormat.indexOf(characterCode), singlePhraseFormat.indexOf(characterCode) + 3);
                }
            }
            listHashSet.add(singlePhraseFormat.toString());
        }
        ArrayList<String> asArrayList = new ArrayList<>(listHashSet);
        asArrayList.sort(Comparator.naturalOrder());
        return asArrayList;
    }
    public void chooseWord() {
        StringBuilder pageData = new StringBuilder();
        StringBuilder pageSynonyms = new StringBuilder();
        StringBuilder pageAntonyms = new StringBuilder();
        StringBuilder dictionaryData = new StringBuilder();
        File file = null;
        try {
            file = new File("C:\\Users\\olive\\GitHub\\Thesaurus-Dictionary\\Thesaurus & Dictionary\\src\\dictionary.txt");
        } catch (NullPointerException err) {
            err.printStackTrace();
        }
        Scanner fileReader = null;
        try {
            fileReader = new Scanner(file);
        } catch (FileNotFoundException err) {
            err.printStackTrace();
        }
        if (fileReader != null) {
            while (fileReader.hasNext()) {
                dictionaryData.append(fileReader.next() + "\n");
            }
        }
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
                    String[] readWriteNewLine = readWrite.replace("<a",
                                                              "\n").split("\n");
                    String tokenKey = "/browse/";
                    for (String newReadWrite : readWriteNewLine) {
                        if (newReadWrite.contains(tokenKey)) {
                            if (newReadWrite.contains("css-1kg1yv8")
                                    || newReadWrite.contains("css-1gyuw4i")
                                    || newReadWrite.contains("css-fu3u5j")
                                    || newReadWrite.contains("css-1n6g4vv")) {
                                int keyLengthAdjustment = newReadWrite.indexOf(tokenKey) + tokenKey.length();
                                pageSynonyms.append(newReadWrite.substring(keyLengthAdjustment, newReadWrite.indexOf("\"", keyLengthAdjustment)) + "\n");
                            }
                        }
                    }
                    pageData.append(readWrite + "\n");
                }
                System.out.println(pageSynonyms);
                ArrayList<String> synonymsArrayList = normalizeData(pageSynonyms);
                this.synonyms = synonymsArrayList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (parser != null) {
                try {
                    parser.close();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
            if (fileReader != null) {
                fileReader.close();
            }
            if (connection != null) {
                connection.disconnect();
            }

        }
        // css-15bafsg

    }
}