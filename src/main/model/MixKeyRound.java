package model;

import org.json.JSONObject;
import persistence.Writeable;

/*
MixKeyRound represents a round for mixing in the key using XOR on each bit
 */
public class MixKeyRound implements Round, Writeable {
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
        return null;
    }

    // getters and setters
    public void setKey(Byte[] key) {
        this.key = key.clone();
    }

    public Byte[] getKey() {
        return key;
    }
}
