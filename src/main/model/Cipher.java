package model;

import java.util.ArrayList;

/*
Represents the cipher as a sequence of individual Rounds,
where each Round takes in a fixed BLOCK_SIZE and produces
an output of the same size.
 */
public class Cipher {

    // REQUIRES: blockSize is a multiple of 8
    // EFFECTS: constructs an empty cipher with the given blockSize in bits
    public Cipher(int blockSizeInBits) {
    }

    // MODIFIES: this
    // EFFECTS: adds the given round to the list of rounds
    public void addRound(Round round) {

    }

    // REQUIRES: plaintext should have length blockSizeInBits/8
    // and key should have length equal to the # of key rounds
    // and each Byte[] should have length blockSizeInBits/8
    // EFFECTS: returns an encrypted byte-array of plaintext
    public Byte[] encryptByteArray(Byte[] plaintext, ArrayList<Byte[]> key) {
        return null;
    }

    // REQUIRES: ciphertext should have length blockSizeInBits/8
    // and key should have length equal to the # of key rounds
    // and each Byte[] should have length blockSizeInBits/8
    // EFFECTS: returns a decrypted byte-array of plaintext
    public Byte[] decryptByteArray(Byte[] ciphertext, ArrayList<Byte[]> key) {
        return null;
    }
}
