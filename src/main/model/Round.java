package model;

public interface Round {
    // REQUIRES: inputBytes should have length equal to blockSize of the cipher
    // EFFECTS: returns an encrypted bytearray, depending on the Round type
    public Byte[] encryptRound(Byte[] inputBytes);
}
