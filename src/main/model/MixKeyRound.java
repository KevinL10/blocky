package model;

/*
MixKeyRound represents a round for mixing in the key by
XOR-ing each key byte with an input byte
 */
public class MixKeyRound implements Round {

    // REQUIRES: blockSize should be the same as the cipher's block size
    // EFFECTS: constructs a round for mixing in the key;
    // and key is initialized with null bytes
    public MixKeyRound(int blockSizeInBytes) {

    }


    @Override
    public Byte[] encryptRound(Byte[] inputBytes) {
        return null;
    }

    @Override
    public Byte[] decryptRound(Byte[] inputBytes) {
        return null;
    }

    // getters and setters
    public void setKey(Byte[] key) {

    }

    public Byte[] getKey() {
        return null;
    }
}
