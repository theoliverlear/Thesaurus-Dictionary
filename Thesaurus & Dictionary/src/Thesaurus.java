import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Thesaurus {
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
    public static void chooseWord() throws IOException {
        Scanner continueInput = new Scanner(System.in);
        do {
            StringBuilder pageData = new StringBuilder();
            StringBuilder pageSynonyms = new StringBuilder();
            StringBuilder pageAntonyms = new StringBuilder();
            StringBuilder dictionaryData = new StringBuilder();

            File file = null;
            try {
                file = new File("C:\\Users\\olive\\GitHub\\Thesaurus-Dictionary\\Thesaurus & Dictionary\\src\\dictionary.txt");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            Scanner fileReader = null;
            try {
                fileReader = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (fileReader.hasNext()) {
                dictionaryData.append(fileReader.next() + "\n");
            }
            BufferedReader parser = null;
            HttpURLConnection connection = null;
            try {
                Scanner userInput = new Scanner(System.in);
                System.out.println("Please enter a word.");
                String word = userInput.next().trim().toLowerCase();
                //URL url = new URL("https://www.thesaurus.com/browse/coffee");
                URL url = new URL("https://www.thesaurus.com/browse/" + word);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                if (connection.getResponseCode() != 200) {
                    System.out.println("Error: " + connection.getResponseCode() + " " + connection.getResponseMessage());
                } else {
                    parser = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String readWrite;
                    while ((readWrite = parser.readLine()) != null) {
                        String[] readWriteNewLine = readWrite.replace("<a", "\n").split("\n");
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
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (parser != null) {
                    parser.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            // css-15bafsg
            ArrayList<String> synonymsArrayList = normalizeData(pageSynonyms);
            synonymsArrayList.forEach(System.out::println);
            System.out.println("Would you like to search for another word? (y/n)");
        } while (continueInput.next().trim().equalsIgnoreCase("y"));
    }
    public static void main(String[] args) throws IOException {
        chooseWord();
    }
}