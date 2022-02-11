package model;

import java.util.ArrayList;

/*
Represents the cipher as a sequence of individual Rounds,
where each Round takes in a fixed BLOCK_SIZE and produces
an output of the same size.
 */
public class Cipher {
    private ArrayList<Round> rounds;
    private int blockSize;

    // EFFECTS: constructs an empty cipher with the given blockSize in bytes
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
    // and key should have length equal to the # of key rounds
    // and each Byte[] should have length blockSize
    // EFFECTS: returns an encrypted byte-array of plaintext
    public Byte[] encryptByteArray(Byte[] plaintext, ArrayList<Byte[]> keys) {
        int keyIndex = 0;
        Byte[] currentBytes = plaintext.clone();
        for (Round round : rounds) {
            if (round instanceof MixKeyRound) {
                ((MixKeyRound) round).setKey(keys.get(keyIndex));
                keyIndex += 1;
            }

            currentBytes = round.encryptRound(currentBytes);
        }
        return currentBytes;
    }

    // REQUIRES: ciphertext should have length blockSize
    // and key should have length equal to the # of key rounds
    // and each Byte[] should have length blockSize
    // EFFECTS: returns a decrypted byte-array of plaintext
    public Byte[] decryptByteArray(Byte[] ciphertext, ArrayList<Byte[]> keys) {
        int keyIndex = 0;
        Byte[] currentBytes = ciphertext.clone();
        for (Round round : rounds) {
            if (round instanceof MixKeyRound) {
                ((MixKeyRound) round).setKey(keys.get(keyIndex));
                keyIndex += 1;
            }

            currentBytes = round.decryptRound(currentBytes);
        }
        return currentBytes;
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
