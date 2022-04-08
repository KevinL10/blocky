package model;

import org.json.JSONObject;
import persistence.Writeable;

/*
MixKeyRound represents a round for mixing in the key using XOR on each bit
 */
public class MixKeyRound implements Round {
    private Byte[] key;
    private int blockSize;

    // REQUIRES: blockSize should be the same as the cipher's block size (in bytes)
    // EFFECTS: constructs a bytearray of length blockSize to store the key,
    // initialized with null bytes
    public MixKeyRound(int blockSize) {
        this.blockSize = blockSize;
        key = new Byte[blockSize];
        for (int i = 0; i < blockSize; i++) {
            key[i] = 0;
        }
    }

    @Override
    // REQUIRES: inputBytes should have length equal to blockSize of the cipher
    // EFFECTS: returns an encrypted bytearray by XOR-ing with each bit of the key
    public Byte[] encryptRound(Byte[] inputBytes) {
        Byte[] output = new Byte[blockSize];
        for (int i = 0; i < blockSize; i++) {
            output[i] = (byte) (inputBytes[i] ^ key[i]);
        }
        return output;
    }

    @Override
    // REQUIRES: inputBytes should have length equal to blockSize of the cipher
    // EFFECTS: returns a decrypted bytearray by XOR-ing with each bit of the key
    public Byte[] decryptRound(Byte[] inputBytes) {
        // decryption and encryption are equivalent for XOR
        return encryptRound(inputBytes);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", "MixKey");
        return json;
    }

    @Override
    // EFFECTS: returns true if the compared object is also a MixKeyRound and same block size
    // two MixKeyRounds are equivalent even if their internal keys are different
    public boolean equals(Object o) {
        if (!(o instanceof MixKeyRound)) {
            return false;
        }
        int blockSize = ((MixKeyRound) o).blockSize;
        return blockSize == this.blockSize;
    }

    // getters and setters
    public void setKey(Byte[] key) {
        this.key = key.clone();
    }

    public Byte[] getKey() {
        return key;
    }

    public int getBlockSize() {
        return blockSize;
    }
}
