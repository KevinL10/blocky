package model;

/*
PermutationRound represents a round for rearranging the bits
according to a certain mapping
 */
public class PermutationRound implements Round {

    // REQUIRES: blockSize should be the same as the cipher's block size
    // EFFECTS: constructs a permutation mapping of size blockSizeInBytes * 8
    // initialized with the identity mapping (0 to 0, 1 to 1, etc.)
    public PermutationRound(int blockSizeInBytes) {

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

    // EFFECTS: return the bit of number at index (from left to right)
    public static int getBitByIndex(Byte number, int index) {
        //return (number >> index) & 1;
        return 0;
    }

    // getters and setters
    // REQUIRES: mapping should have length blockSize * 8
    // EFFECTS: copies the *values* from given mapping into the round's mapping
    public void setPermutationMapping(int[] mapping) {

    }

    public int[] getPermutationMapping() {
        return null;
    }
}
