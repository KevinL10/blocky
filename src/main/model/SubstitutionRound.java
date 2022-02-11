package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
PermutationRound represents a round for substituting 4-bit blocks
with a corresponding 4-bit value
 */
public class SubstitutionRound implements Round {
    private int[] mapping;

    // EFFECTS: constructs a substitution mapping of size 16 (4-bits)
    // initialized with the identity mapping (0 to 0, 1 to 1, etc.)
    public SubstitutionRound() {
        mapping = new int[16];
        for (int i = 0; i < 16; i++) {
            mapping[i] = i;
        }
    }

    @Override
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

    // getters and setters

    // REQUIRES: mapping should have size 16 and uniquely contain 0-15
    // EFFECTS: copies the *values* from given mapping into the round's mapping
    public void setSubstitutionMapping(int[] mapping) {
        this.mapping = mapping.clone();
    }

    public int[] getSubstitutionMapping() {
        return mapping;
    }
}
