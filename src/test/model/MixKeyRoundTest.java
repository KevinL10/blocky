package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MixKeyRoundTest {
    MixKeyRound round;
    private final int BLOCK_SIZE = 2;

    @BeforeEach
    public void runBefore() {
        round = new MixKeyRound(BLOCK_SIZE);
    }

    @Test
    public void testConstructor() {
        // check that initial key contains BLOCK_SIZE null bytes
        Byte[] initialKey = round.getKey();
        assertEquals(BLOCK_SIZE, initialKey.length);
        for (int i = 0; i < BLOCK_SIZE; i++) {
            assertEquals(0, (int) initialKey[i]);
        }
    }

    @Test
    public void testEncryptRound() {
        Byte[] testKey = {(byte) 230, (byte) 125};
        round.setKey(testKey);

        Byte[] plaintext = {(byte) 54, (byte) 179};
        Byte[] encrypted = round.encryptRound(plaintext);

        assertEquals((int) encrypted[0], testKey[0] ^ plaintext[0]);
        assertEquals((int) encrypted[1], testKey[1] ^ plaintext[1]);
    }

    @Test
    public void testDecryptRound() {
        Byte[] testKey = {(byte) 184, (byte) 6};
        round.setKey(testKey);

        Byte[] encrypted = {(byte) 56, (byte) 199};
        Byte[] plaintext = round.encryptRound(encrypted);

        assertEquals((int) plaintext[0], testKey[0] ^ encrypted[0]);
        assertEquals((int) plaintext[1], testKey[1] ^ encrypted[1]);
    }
}
