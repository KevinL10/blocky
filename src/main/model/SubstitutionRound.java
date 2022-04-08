package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
PermutationRound represents a round for substituting 4-bit blocks
with a corresponding 4-bit value
 */
public class SubstitutionRound implements Round {
    private int[] mapping;
    private int blockSize;

    // EFFECTS: constructs a substitution mapping of size 16 (4-bits)
    // initialized with the identity mapping (0 to 0, 1 to 1, etc.)
    public SubstitutionRound(int blockSize) {
        this.blockSize = blockSize;
        mapping = new int[16];
        for (int i = 0; i < 16; i++) {
            mapping[i] = i;
        }
    }

    @Override
    // REQUIRES: inputBytes should have length equal to blockSize of the cipher
    // EFFECTS: returns an encrypted bytearray where all 4-bit blocks
    // are substituted according to the round's mapping
    public Byte[] encryptRound(Byte[] inputBytes) {
        int blockSize = inputBytes.length;
        Byte[] output = new Byte[blockSize];
        for (int i = 0; i < blockSize; i++) {
            int upperBits = ((inputBytes[i] >> 4) & 0xf);
            int lowerBits = inputBytes[i] & 0xf;

            upperBits = mapping[upperBits];
            lowerBits = mapping[lowerBits];

            output[i] = (byte) ((upperBits << 4) + lowerBits);
        }
        return output;
    }

    @Override
    // REQUIRES: inputBytes should have length equal to blockSize of the cipher
    // EFFECTS: returns a decrypted bytearray where all 4-bit blocks
    // are un-substituted according to the round's mapping
    public Byte[] decryptRound(Byte[] inputBytes) {
        int[] inverseMapping = new int[16];
        // find the inverse mapping
        for (int i = 0; i < 16; i++) {
            inverseMapping[mapping[i]] = i;
        }

        int blockSize = inputBytes.length;
        Byte[] output = new Byte[blockSize];
        for (int i = 0; i < blockSize; i++) {
            int upperBits = ((inputBytes[i] >> 4) & 0xf);
            int lowerBits = inputBytes[i] & 0xf;

            upperBits = inverseMapping[upperBits];
            lowerBits = inverseMapping[lowerBits];

            output[i] = (byte) ((upperBits << 4) + lowerBits);
        }
        return output;
    }

    // MODIFIES: this
    // EFFECTS: randomizes the substitution mapping
    public void fillWithRandomSubstitution() {
        ArrayList<Integer> mappingAsList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            mappingAsList.add(i);
        }
        Collections.shuffle(mappingAsList);
        for (int i = 0; i < 16; i++) {
            mapping[i] = mappingAsList.get(i);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", "Substitution");
        json.put("mapping", new JSONArray(mapping));
        return json;
    }

    @Override
    // EFFECTS: returns true if given object is a substitution round with the same mapping and blockSize
    public boolean equals(Object o) {
        if (!(o instanceof SubstitutionRound)) {
            return false;
        }
        SubstitutionRound sround = (SubstitutionRound) o;
        int blockSize = sround.getBlockSize();
        int[] mapping = sround.getSubstitutionMapping();
        return Arrays.equals(mapping, this.mapping) && blockSize == this.blockSize;
    }
    // getters and setters

    // REQUIRES: mapping should have size 16 and uniquely contain the numbers 0-15
    // MODIFIES: this
    // EFFECTS: copies the *values* from given mapping into the round's mapping
    public void setSubstitutionMapping(int[] mapping) {
        this.mapping = mapping.clone();
    }

    public int[] getSubstitutionMapping() {
        return mapping;
    }

    public int getBlockSize() {
        return blockSize;
    }
}
