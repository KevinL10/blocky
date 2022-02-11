package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CipherTest {
    private final int BLOCK_SIZE = 2;
    private final int[] permutationMapping = {2, 4, 13, 0, 8, 10, 1, 11, 12, 3, 6, 9, 7, 5, 14, 15};
    private final int[] substitutionMapping = {9, 13, 8, 12, 1, 0, 5, 15, 6, 14, 7, 2, 4, 10, 3, 11};
    PermutationRound pround;
    SubstitutionRound sround;
    MixKeyRound kround1;
    MixKeyRound kround2;
    Cipher cipher;

    @BeforeEach
    public void runBefore() {
        cipher = new Cipher(BLOCK_SIZE);

        // instantiate the permutation round
        pround = new PermutationRound(BLOCK_SIZE);
        pround.setPermutationMapping(permutationMapping);

        // instantiate the substitution round
        sround = new SubstitutionRound();
        sround.setSubstitutionMapping(substitutionMapping);

        // instantiate the key rounds
        kround1 = new MixKeyRound(BLOCK_SIZE);
        kround2 = new MixKeyRound(BLOCK_SIZE);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, cipher.getNumberOfRounds());
        assertEquals(BLOCK_SIZE, cipher.getBlockSize());
    }

    @Test
    public void testAddRound() {
        cipher.addRound(pround);
        ArrayList<Round> rounds = cipher.getRounds();
        assertEquals(1, cipher.getNumberOfRounds());
        assertEquals(pround, rounds.get(0));
    }

    @Test
    public void testEncryptNoKeyRounds() {
        cipher.addRound(pround);
        cipher.addRound(sround);

        Byte[] plaintext = {(byte) 231, (byte) 85};
        Byte[] ciphertext = cipher.encryptByteArray(plaintext, new ArrayList<>());

        // should be equivalent to encrypting the rounds one-after-another
        Byte[] expectedCiphertext = pround.encryptRound(plaintext);
        expectedCiphertext = sround.encryptRound(expectedCiphertext);

        assertArrayEquals(expectedCiphertext, ciphertext);
    }

    @Test
    public void testEncryptOneKeyRound() {
        cipher.addRound(pround);
        cipher.addRound(kround1);
        cipher.addRound(sround);

        ArrayList<Byte[]> keys = new ArrayList<>();
        Byte[] key1 = {(byte) 113, (byte) 140};
        keys.add(key1);
        kround1.setKey(key1);

        Byte[] plaintext = {(byte) 231, (byte) 85};
        Byte[] ciphertext = cipher.encryptByteArray(plaintext, keys);

        // should be equivalent to encrypting the rounds one-after-another
        Byte[] expectedCiphertext = pround.encryptRound(plaintext);
        expectedCiphertext = kround1.encryptRound(expectedCiphertext);
        expectedCiphertext = sround.encryptRound(expectedCiphertext);
        assertArrayEquals(expectedCiphertext, ciphertext);
    }

    @Test
    public void testEncryptTwoKeyRounds() {
        cipher.addRound(pround);
        cipher.addRound(kround1);
        cipher.addRound(sround);
        cipher.addRound(kround2);

        ArrayList<Byte[]> keys = new ArrayList<>();
        Byte[] key1 = {(byte) 113, (byte) 140};
        Byte[] key2 = {(byte) 31, (byte) 175};
        keys.add(key1);
        keys.add(key2);
        kround1.setKey(key1);
        kround2.setKey(key2);

        Byte[] plaintext = {(byte) 192, (byte) 200};
        Byte[] ciphertext = cipher.encryptByteArray(plaintext, keys);

        // should be equivalent to encrypting the rounds one-after-another
        Byte[] expectedCiphertext = pround.encryptRound(plaintext);
        expectedCiphertext = kround1.encryptRound(expectedCiphertext);
        expectedCiphertext = sround.encryptRound(expectedCiphertext);
        expectedCiphertext = kround2.encryptRound(expectedCiphertext);
        assertArrayEquals(expectedCiphertext, ciphertext);
    }

    @Test
    public void testDecryptNoKeyRounds() {
        // you can use the encrypt decrypt functions from other classes...
        cipher.addRound(pround);
        cipher.addRound(sround);

        Byte[] ciphertext = {(byte) 231, (byte) 85};
        Byte[] plaintext = cipher.decryptByteArray(ciphertext, new ArrayList<>());

        // should be equivalent to encrypting the rounds one-after-another (backwards)
        Byte[] expectedPlaintext = sround.decryptRound(ciphertext);
        expectedPlaintext = pround.decryptRound(expectedPlaintext);

        assertArrayEquals(expectedPlaintext, plaintext);
    }

    @Test
    public void testDecryptOneKeyRound() {
        cipher.addRound(sround);
        cipher.addRound(kround1);
        cipher.addRound(pround);

        ArrayList<Byte[]> keys = new ArrayList<>();
        Byte[] key1 = {(byte) 113, (byte) 140};
        keys.add(key1);
        kround1.setKey(key1);

        Byte[] ciphertext = {(byte) 231, (byte) 85};
        Byte[] plaintext = cipher.decryptByteArray(ciphertext, keys);

        // should be equivalent to encrypting the rounds one-after-another (backwards)
        Byte[] expectedPlaintext = pround.decryptRound(ciphertext);
        expectedPlaintext = kround1.decryptRound(expectedPlaintext);
        expectedPlaintext = sround.decryptRound(expectedPlaintext);

        assertArrayEquals(expectedPlaintext, plaintext);
    }

    @Test
    public void testDecryptTwoKeyRounds() {
        cipher.addRound(pround);
        cipher.addRound(kround1);
        cipher.addRound(sround);
        cipher.addRound(kround2);

        ArrayList<Byte[]> keys = new ArrayList<>();
        Byte[] key1 = {(byte) 113, (byte) 140};
        Byte[] key2 = {(byte) 31, (byte) 175};
        keys.add(key1);
        keys.add(key2);
        kround1.setKey(key1);
        kround2.setKey(key2);

        Byte[] ciphertext = {(byte) 192, (byte) 200};
        Byte[] plaintext = cipher.decryptByteArray(ciphertext, keys);

        // should be equivalent to encrypting the rounds one-after-another (backwards)
        Byte[] expectedPlaintext = kround2.decryptRound(ciphertext);
        expectedPlaintext = sround.decryptRound(expectedPlaintext);
        expectedPlaintext = kround1.decryptRound(expectedPlaintext);
        expectedPlaintext = pround.decryptRound(expectedPlaintext);
        assertArrayEquals(expectedPlaintext, plaintext);
    }

    @Test
    public void testEncryptDecryptSuccessful() {
        cipher.addRound(pround);
        cipher.addRound(kround1);
        cipher.addRound(sround);
        cipher.addRound(kround2);

        ArrayList<Byte[]> keys = new ArrayList<>();
        Byte[] key1 = {(byte) 113, (byte) 140};
        Byte[] key2 = {(byte) 31, (byte) 175};
        keys.add(key1);
        keys.add(key2);
        kround1.setKey(key1);
        kround2.setKey(key2);

        Byte[] plaintext = {(byte) 192, (byte) 200};
        Byte[] ciphertext = cipher.encryptByteArray(plaintext, keys);
        Byte[] plaintext2 = cipher.decryptByteArray(ciphertext, keys);

        assertArrayEquals(plaintext, plaintext2);
    }

    @Test
    public void testGetNumberOfKeyRoundsNone(){
        cipher.addRound(pround);
        cipher.addRound(sround);
        assertEquals(0, cipher.getNumberOfKeyRounds());
    }

    @Test
    public void testGetNumberOfKeyRoundsTwo(){
        cipher.addRound(kround2);
        cipher.addRound(pround);
        cipher.addRound(sround);
        cipher.addRound(kround1);
        assertEquals(2, cipher.getNumberOfKeyRounds());
    }
}