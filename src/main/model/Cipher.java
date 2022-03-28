package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.util.ArrayList;

/*
Represents the cipher as a sequence of individual Rounds, each with size blockSize
 */
public class Cipher implements Writeable {
    private ArrayList<Round> rounds;
    private int blockSize;

    // REQUIRES: blockSize should be positive
    // EFFECTS: constructs an empty cipher with the given blockSize in bytes
    public Cipher(int blockSize) {
        rounds = new ArrayList<>();
        this.blockSize = blockSize;
    }

    // MODIFIES: this
    // EFFECTS: appends the given round to the current cipher and updates the EventLog
    public void addRound(Round round) {
        rounds.add(round);

        if (round instanceof MixKeyRound) {
            EventLog.getInstance().logEvent(new Event("Added Mix Key Round"));
        } else if (round instanceof SubstitutionRound) {
            EventLog.getInstance().logEvent(new Event("Added Substitution Round"));
        } else if (round instanceof PermutationRound) {
            EventLog.getInstance().logEvent(new Event("Added Permutation Round"));
        }
    }

    // REQUIRES: plaintext should have length blockSize
    // keys should have length equal to the # of key rounds
    // each bytearray in keys should have length blockSize
    // MODIFIES: this
    // EFFECTS: returns an encrypted byte-array of plaintext
    public Byte[] encryptByteArray(Byte[] plaintext, ArrayList<Byte[]> keys) {
        int keyIndex = 0;
        Byte[] currentBytes = plaintext.clone();

        // encrypt the current bytes at each round (from beginning to end)
        for (Round round : rounds) {
            if (round instanceof MixKeyRound) {
                ((MixKeyRound) round).setKey(keys.get(keyIndex));
                keyIndex++;
            }
            currentBytes = round.encryptRound(currentBytes);
        }
        EventLog.getInstance().logEvent(new Event("Encrypted Message"));
        return currentBytes;
    }

    // REQUIRES: ciphertext should have length blockSize
    // keys should have length equal to the # of key rounds
    // each bytearray in keys should have length blockSize
    // MODIFIES: this
    // EFFECTS: returns a decrypted byte-array of plaintext
    public Byte[] decryptByteArray(Byte[] ciphertext, ArrayList<Byte[]> keys) {
        int keyIndex = keys.size() - 1;
        Byte[] currentBytes = ciphertext.clone();

        // decrypt the current bytes at each round (from end to beginning)
        for (int i = rounds.size() - 1; i >= 0; i--) {
            Round round = rounds.get(i);
            if (round instanceof MixKeyRound) {
                ((MixKeyRound) round).setKey(keys.get(keyIndex));
                keyIndex--;
            }
            currentBytes = round.decryptRound(currentBytes);
        }
        EventLog.getInstance().logEvent(new Event("Decrypted Message"));
        return currentBytes;
    }

    // EFFECTS: returns the number of key rounds in the cipher
    public int getNumberOfKeyRounds() {
        int keyRoundNum = 0;
        for (Round round : rounds) {
            if (round instanceof MixKeyRound) {
                keyRoundNum++;
            }
        }
        return keyRoundNum;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("blockSize", blockSize);
        json.put("rounds", roundsToJson());
        return json;
    }

    // EFFECTS: returns rounds in this cipher as a JSON array
    public JSONArray roundsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Round r : rounds) {
            jsonArray.put(r.toJson());
        }
        return jsonArray;
    }

    // getters and setters
    public ArrayList<Round> getRounds() {
        return rounds;
    }

    public int getNumberOfRounds() {
        return rounds.size();
    }

    public int getBlockSize() {
        return blockSize;
    }
}
