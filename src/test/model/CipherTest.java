package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CipherTest {
    Cipher cipher8;
    Cipher cipher16;

    @BeforeEach
    public void runBefore() {
        cipher8 = new Cipher(8);
        cipher16 = new Cipher(16);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, cipher8.getNumberOfRounds());
        assertEquals(0, cipher16.getNumberOfRounds());

        assertEquals(8, cipher8.getBlockSize());
        assertEquals(16, cipher16.getBlockSize());
    }
}