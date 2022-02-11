package model;

/*
MixKeyRound represents a round for mixing in the key by
XOR-ing each key byte with an input byte
 */
public class MixKeyRound implements Round {
    private Byte[] key;
    private int blockSize;

    // REQUIRES: blockSize should be the same as the cipher's block size (in bytes)
    // EFFECTS: constructs a round for mixing in the key (initialized with null bytes)
    // and has size blockSize
    public MixKeyRound(int blockSize) {
        this.blockSize = blockSize;
        key = new Byte[blockSize];
        for (int i = 0; i < blockSize; i++) {
            key[i] = 0;
        }
    }


    @Override
    public Byte[] encryptRound(Byte[] inputBytes) {
        Byte[] output = new Byte[blockSize];
        for (int i = 0; i < blockSize; i++) {
            output[i] = (byte) (inputBytes[i] ^ key[i]);
        }
        return output;
    }

    @Override
    public Byte[] decryptRound(Byte[] inputBytes) {
        // decryption and encryption are equivalent for XOR
        return encryptRound(inputBytes);
    }

    // getters and setters
    public void setKey(Byte[] key) {
        this.key = key.clone();
    }

    public Byte[] getKey() {
        return key;
    }
}
