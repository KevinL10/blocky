package model;

import java.util.ArrayList;

/*
Represents the cipher as a sequence of individual Rounds,
where each Round takes in a fixed BLOCK_SIZE and produces
an output of the same size.
 */
public class Cipher {
    private int blockSize;
    private ArrayList<Round> rounds;

    // REQUIRES: blockSize is a multiple of 4
    // MODIFIES: this
    // EFFECTS: constructs a new cipher with no rounds
    // and the given block size
    public Cipher(int blockSize) {
        rounds = new ArrayList<>();
        this.blockSize = blockSize;
    }

    // MODIFIES: this
    // EFFECTS: adds the given round to the list of rounds
    public void addRound(Round round) {
        rounds.add(round);
    }

    // REQUIRES: plaintext should have length blockSize
    // EFFECTS: returns an encrypted byte-array of plaintext
    public Byte[] encryptByteArray(Byte[] plaintext) {
        return null;
    }

    // REQUIRES: ciphertext should have length blockSize
    // EFFECTS: returns a decrypted byte-array of plaintext
    public Byte[] decryptByteArray(Byte[] ciphertext) {
        return null;
    }
}
