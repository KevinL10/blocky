package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.util.ArrayList;
import java.util.Collections;

/*
PermutationRound represents a round for rearranging the bits
according to a certain mapping
 */
public class PermutationRound implements Round, Writeable {
    private int[] mapping;
    private int blockSize;

    // REQUIRES: blockSize should be the same as the cipher's block size (in bytes)
    // EFFECTS: constructs a permutation mapping of size blockSize * 8
    // initialized with the identity mapping (0 to 0, 1 to 1, etc.)
    public PermutationRound(int blockSize) {
        this.blockSize = blockSize;
        mapping = new int[blockSize * 8];
        for (int i = 0; i < mapping.length; i++) {
            mapping[i] = i;
        }
    }

    @Override
    // REQUIRES: inputBytes should have length equal to blockSize of the cipher
    // EFFECTS: returns an encrypted bytearray by rearranging all plaintext bits
    // according to the mapping
    public Byte[] encryptRound(Byte[] inputBytes) {
        String originalBits = "";
        int[] permutedBits = new int[blockSize * 8];
        Byte[] output = new Byte[blockSize];

        for (int i = 0; i < blockSize; i++) {
            originalBits += convertByteToBits(inputBytes[i]);
        }
        for (int i = 0; i < originalBits.length(); i++) {
            permutedBits[mapping[i]] = originalBits.charAt(i) - '0';
        }

        // go through each byte, convert to a string
        for (int i = 0; i < blockSize; i++) {
            String currentByte = "";
            for (int j = 0; j < 8; j++) {
                currentByte += permutedBits[8 * i + j];
            }
            output[i] = (byte) Integer.parseInt(currentByte, 2);
        }
        return output;
    }

    @Override
    // REQUIRES: inputBytes should have length equal to blockSize of the cipher
    // EFFECTS: returns a decrypted bytearray by rearranging all plaintext bits
    // according to the inverse mapping
    public Byte[] decryptRound(Byte[] inputBytes) {
        int[] inverseMapping = new int[blockSize * 8];
        for (int i = 0; i < blockSize * 8; i++) {
            inverseMapping[mapping[i]] = i;
        }
        String originalBits = "";
        int[] permutedBits = new int[blockSize * 8];
        Byte[] output = new Byte[blockSize];

        for (int i = 0; i < blockSize; i++) {
            originalBits += convertByteToBits(inputBytes[i]);
        }
        for (int i = 0; i < originalBits.length(); i++) {
            permutedBits[inverseMapping[i]] = originalBits.charAt(i) - '0';
        }

        // go through each byte, convert to a string
        for (int i = 0; i < blockSize; i++) {
            String currentByte = "";
            for (int j = 0; j < 8; j++) {
                currentByte += permutedBits[8 * i + j];
            }
            output[i] = (byte) Integer.parseInt(currentByte, 2);
        }
        return output;
    }

    // MODIFIES: this
    // EFFECTS: randomizes the permutation mapping
    public void fillWithRandomPermutation() {
        ArrayList<Integer> mappingAsList = new ArrayList<>();
        for (int i = 0; i < blockSize * 8; i++) {
            mappingAsList.add(i);
        }
        Collections.shuffle(mappingAsList);
        for (int i = 0; i < blockSize * 8; i++) {
            mapping[i] = mappingAsList.get(i);
        }
    }

    // EFFECTS: return the bit of number at index (from left to right)
    public static int getBitByIndex(Byte number, int index) {
        return (number >> (7 - index)) & 1;
    }

    // EFFECTS: converts the byte to a binary string
    public static String convertByteToBits(Byte b) {
        String output = "";
        for (int i = 7; i >= 0; i--) {
            if (((b >> i) & 1) != 0) {
                output += "1";
            } else {
                output += "0";
            }
        }
        return output;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", "Permutation");
        json.put("mapping", new JSONArray(mapping));
        return json;
    }

    // getters and setters
    // REQUIRES: mapping should have length blockSize * 8
    // EFFECTS: copies the *values* from given mapping into the round's mapping
    public void setPermutationMapping(int[] mapping) {
        this.mapping = mapping.clone();
    }

    public int[] getPermutationMapping() {
        return mapping;
    }
}
