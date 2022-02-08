package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CipherTest {
    Cipher cipher1;
    Cipher cipher2;

    @BeforeEach
    public void runBefore() {
        cipher1 = new Cipher(1);
        cipher2 = new Cipher(2);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, cipher1.getNumberOfRounds());
        assertEquals(0, cipher2.getNumberOfRounds());

        assertEquals(1, cipher1.getBlockSize());
        assertEquals(2, cipher2.getBlockSize());
    }
}