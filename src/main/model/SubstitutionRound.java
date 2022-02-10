package model;

/*
PermutationRound represents a round for substituting 4-bit blocks
with a corresponding 4-bit value
 */
public class SubstitutionRound implements Round {

    // EFFECTS: constructs a substitution mapping of size 16 (4-bits)
    // initialized with the identity mapping (0 to 0, 1 to 1, etc.)
    public SubstitutionRound() {

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

    // REQUIRES: mapping should have size 16 and uniquely contain 0-15
    // EFFECTS: copies the *values* from given mapping into the round's mapping
    public void setSubstitutionMapping(int[] mapping) {

    }

    public int[] getMapping() {
        return null;
    }
}
