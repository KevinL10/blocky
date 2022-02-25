package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PermutationRoundTest {
    PermutationRound round;
    private final int BLOCK_SIZE = 2;
    private final int[] mapping1 = {2, 4, 13, 0, 8, 10, 1, 11, 12, 3, 6, 9, 7, 5, 14, 15};
    private final int[] inverseMapping1 = {3, 6, 0, 9, 1, 13, 10, 12, 4, 11, 5, 7, 8, 2, 14, 15};

    @BeforeEach
    public void runBefore() {
        round = new PermutationRound(2);
    }

    @Test
    public void testConstructor() {
        int[] mapping = round.getPermutationMapping();
        assertEquals(BLOCK_SIZE, round.getBlockSize());
        // check that mapping is initialized with identity permutation
        assertEquals(mapping.length, BLOCK_SIZE * 8);
        for (int i = 0; i < mapping.length; i++) {
            assertEquals(i, mapping[i]);
        }
    }

    @Test
    public void testEncryptRound() {
        round.setPermutationMapping(mapping1);

        Byte[] plaintext = {(byte) 168, (byte) 230};
        Byte[] encrypted = round.encryptRound(plaintext);
        int[] expectedBits = new int[BLOCK_SIZE * 8];
        int[] permutatedBits = new int[BLOCK_SIZE * 8];

        for (int i = 0; i < BLOCK_SIZE; i++) {
            for (int j = 0; j < 8; j++) {
                expectedBits[8 * i + j] = PermutationRound.getBitByIndex(plaintext[i], j);
            }
        }
        for (int i = 0; i < BLOCK_SIZE * 8; i++) {
            permutatedBits[mapping1[i]] = expectedBits[i];
        }
        for (int i = 0; i < BLOCK_SIZE; i++) {
            for (int j = 0; j < 8; j++) {
                int encryptedBit = PermutationRound.getBitByIndex(encrypted[i], j);
                assertEquals(permutatedBits[8 * i + j], encryptedBit);
            }
        }
    }

    @Test
    public void testDecryptRound() {
        round.setPermutationMapping(mapping1);

        Byte[] ciphertext = {(byte) 168, (byte) 230};
        Byte[] plaintext = round.decryptRound(ciphertext);
        int[] expectedBits = new int[BLOCK_SIZE * 8];
        int[] permutatedBits = new int[BLOCK_SIZE * 8];

        // Represent the bytes as an array of bits (from left to right)
        for (int i = 0; i < BLOCK_SIZE; i++) {
            for (int j = 0; j < 8; j++) {
                expectedBits[8 * i + j] = PermutationRound.getBitByIndex(ciphertext[i], j);
            }
        }
        for (int i = 0; i < BLOCK_SIZE * 8; i++) {
            permutatedBits[inverseMapping1[i]] = expectedBits[i];
        }
        // Check that each bit is equal to the encrypted value
        for (int i = 0; i < BLOCK_SIZE; i++) {
            for (int j = 0; j < 8; j++) {
                int decryptedBit = PermutationRound.getBitByIndex(plaintext[i], j);
                assertEquals(permutatedBits[8 * i + j], decryptedBit);
            }
        }
    }

    @Test
    public void testRandomPermutationRandomized() {
        round.fillWithRandomPermutation();
        int[] mapping = round.getPermutationMapping();

        // check that the mapping is randomized (different from identity permutation)
        boolean isRandomized = false;
        for (int i = 0; i < BLOCK_SIZE * 8; i++) {
            if (mapping[i] != i) {
                isRandomized = true;
                break;
            }
        }

        assertTrue(isRandomized);
    }

    @Test
    public void testGetBitByIndex() {
        // For 210: 11010010
        int[] expectedBits = {1, 1, 0, 1, 0, 0, 1, 0};
        for (int i = 0; i < 8; i++) {
            assertEquals(expectedBits[i], PermutationRound.getBitByIndex((byte) 210, i));
        }
    }

    @Test
    public void testConvertByteToBits() {
        assertEquals("11010010", PermutationRound.convertByteToBits((byte) 210));
        assertEquals("01000001", PermutationRound.convertByteToBits((byte) 65));
    }

    @Test
    public void testEqualsDifferentObject() {
        SubstitutionRound round2 = new SubstitutionRound(BLOCK_SIZE);
        assertNotEquals(round, round2);
    }

    @Test
    public void testEqualsDifferentMapping() {
        PermutationRound round2 = new PermutationRound(BLOCK_SIZE);
        round.setPermutationMapping(mapping1);
        round2.setPermutationMapping(inverseMapping1);
        assertNotEquals(round, round2);
    }

    @Test
    public void testEqualsDifferentBlocksize() {
        PermutationRound round2 = new PermutationRound(BLOCK_SIZE - 1);
        round.setPermutationMapping(mapping1);
        round2.setPermutationMapping(mapping1);
        assertNotEquals(round, round2);
    }

    @Test
    public void testEqualsSameMapping() {
        PermutationRound round2 = new PermutationRound(BLOCK_SIZE);
        round.setPermutationMapping(mapping1);
        round2.setPermutationMapping(mapping1);
        assertEquals(round, round2);
    }
}
