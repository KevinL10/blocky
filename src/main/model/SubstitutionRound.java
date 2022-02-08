package model;

/*
PermutationRound represents a round for substituting 4-bit blocks
with a corresponding 4-bit value
 */
public class SubstitutionRound implements Round {

    // REQUIRES: blockSize should be the same as the cipher's block size
    // EFFECTS: constructs a substitution mapping of size 16 (4-bits)
    // initialized with the identity mapping (0 to 0, 1 to 1, etc.)
    public SubstitutionRound(int blockSizeInBytes) {

    }

    @Override
    public Byte[] encryptRound(Byte[] inputBytes) {
        return null;
    }

    @Override
    public Byte[] decryptRound(Byte[] inputBytes) {
        return null;
    }

    // MODIFIES: this
    // EFFECTS: randomizes the substitution mapping
    public void fillWithRandomSubstitution() {

    }

    // getters and setters
    public void setSubstitutionMapping(int[] mapping) {

    }
}
