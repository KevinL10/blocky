package persistence;

import model.Cipher;
import model.MixKeyRound;
import model.PermutationRound;
import model.SubstitutionRound;
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
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCipher(jsonObject);
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
        int blockSize = jsonObject.getInt("blockSize");
        Cipher cipher = new Cipher(blockSize);
        addRounds(cipher, jsonObject);
        return cipher;
    }

    // MODIFIES: cipher
    // EFFECTS: parses each round from the JSON Object and adds them to the cipher
    private void addRounds(Cipher cipher, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("rounds");
        for (Object json : jsonArray) {
            JSONObject roundJson = (JSONObject) json;
            String type = roundJson.getString("type");

            if (type.equals("MixKey")) {
                addMixKey(cipher, roundJson);
            } else if (type.equals("Permutation")) {
                addPermutation(cipher, roundJson);
            } else if (type.equals("Substitution")) {
                addSubstitution(cipher, roundJson);
            }
        }
    }

    // MODIFIES: cipher
    // EFFECTS: parses MixKeyRound from JSON and adds to cipher's rounds
    private void addMixKey(Cipher cipher, JSONObject jsonObject) {
        MixKeyRound kround = new MixKeyRound(cipher.getBlockSize());
        cipher.addRound(kround);
    }

    // MODIFIES: cipher
    // EFFECTS: parses SubstitutionRound from JSON and adds to cipher's rounds
    private void addSubstitution(Cipher cipher, JSONObject jsonObject) {
        SubstitutionRound sround = new SubstitutionRound(cipher.getBlockSize());
        JSONArray mappingJson = jsonObject.getJSONArray("mapping");
        int[] mapping = new int[mappingJson.length()];
        for (int i = 0; i < mappingJson.length(); i++) {
            mapping[i] = mappingJson.getInt(i);
        }
        sround.setSubstitutionMapping(mapping);
        cipher.addRound(sround);
    }

    // MODIFIES: cipher
    // EFFECTS: parses PermutationRound from JSON and adds to cipher's rounds
    private void addPermutation(Cipher cipher, JSONObject jsonObject) {
        PermutationRound sround = new PermutationRound(cipher.getBlockSize());
        JSONArray mappingJson = jsonObject.getJSONArray("mapping");
        int[] mapping = new int[mappingJson.length()];
        for (int i = 0; i < mappingJson.length(); i++) {
            mapping[i] = mappingJson.getInt(i);
        }
        sround.setPermutationMapping(mapping);
        cipher.addRound(sround);
    }
}
