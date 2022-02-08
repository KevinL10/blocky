package model;

public interface Round {
    // REQUIRES: inputBytes should have length equal to blockSizeInBits/8 of the cipher
    // EFFECTS: returns an encrypted bytearray, depending on the Round type
    public Byte[] encryptRound(Byte[] plaintextBytes);

    // REQUIRES: inputBytes should have length equal to blockSizeInBits/8 of the cipher
    // EFFECTS: returns a decrypted bytearray, depending on the Round type
    public Byte[] decryptRound(Byte[] ciphertextBytes);
}
