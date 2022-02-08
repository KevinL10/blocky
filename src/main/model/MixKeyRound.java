package model;

/*
MixKeyRound represents a round for mixing in the key by
XOR-ing each key byte with an input byte
 */
public class MixKeyRound implements Round {
    @Override
    public Byte[] encryptRound(Byte[] inputBytes) {
        return null;
    }

    @Override
    public Byte[] decryptRound(Byte[] inputBytes) {
        return null;
    }

    // getters and setters
    public void setKey(Byte[] key){

    }
}
