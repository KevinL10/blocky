package model;

/*
PermutationRound represents a round for rearranging the bits
according to a certain mapping
 */
public class PermutationRound implements Round {

    // REQUIRES: blockSize should be the same as the cipher's block size
    // MODIFIES: this
    // EFFECTS: constructs a permutation mapping of size blockSize
    // initialized with the identity mapping (0 to 0, 1 to 1, etc.)
    public PermutationRound(int blockSize) {
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
    // EFFECTS: randomizes the permutation mapping
    public void fillWithRandomPermutation() {

    }

    // getters and setters
    public void setPermutationMapping(int[] mapping) {

    }
}
