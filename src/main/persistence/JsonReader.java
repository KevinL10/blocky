package persistence;

import model.Cipher;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// referenced JsonSerializationDemo
// Loads corresponding cipher from JSON data in a file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads cipher from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Cipher read() throws IOException {
        return null;
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses cipher from JSON object and returns it
    private Cipher parseCipher(JSONObject jsonObject) {
        return null;
    }

    // MODIFIES: cipher
    // EFFECTS: parses rounds from JSON object and adds them to cipher
    private void parseRounds(Cipher cipher, JSONObject jsonObject) {

    }
}
