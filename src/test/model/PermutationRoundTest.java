package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PermutationRoundTest {
    PermutationRound round;
    private final int BLOCK_SIZE = 2;

    @BeforeEach
    public void runBefore() {
        round = new PermutationRound(2);
    }

    @Test
    public void testConstructor() {
        int[] mapping = round.getPermutationMapping();
        // check that mapping is initialized with identity permutation
        assertEquals(mapping.length, BLOCK_SIZE * 8);
        for (int i = 0; i < mapping.length; i++) {
            assertEquals(i, mapping[i]);
        }
    }

    @Test
    public void testEncryptRound() {
        int[] mapping = {2, 4, 13, 0, 8, 10, 1, 11, 12, 3, 6, 9, 7, 5, 14, 15};
        round.setPermutationMapping(mapping);

        Byte[] plaintext = {(byte) 168, (byte) 230};
        Byte[] encrypted = round.encryptRound(plaintext);
        int[] expectedBits = new int[BLOCK_SIZE * 8];
        int[] permutatedBits = new int[BLOCK_SIZE * 8];

        // Represent the bytes as an array of bits (from left to right)
        for (int i = 0; i < BLOCK_SIZE; i++) {
            for (int j = 0; j < 8; j++) {
                int index = 8 * i + j;
                expectedBits[index] = PermutationRound.getBitByIndex(plaintext[i], j);
            }
        }

        // Permutate the bits according to the mapping
        for (int i = 0; i < BLOCK_SIZE * 8; i++) {
            permutatedBits[mapping[i]] = expectedBits[i];
        }

        // Check that each bit is equal to the encrypted value
        for (int i = 0; i < BLOCK_SIZE; i++) {
            for (int j = 0; j < 8; j++) {
                int index = 8 * i + j;
                int encryptedBit = PermutationRound.getBitByIndex(encrypted[i], j);
                assertEquals(permutatedBits[index], encryptedBit);
            }
        }
    }

    @Test
    public void testGetBitByIndex() {
        // For 210: 11010010
        int[] expectedBits = {1, 1, 0, 1, 0, 0, 1, 0};
        for (int i = 0; i < 8; i++) {
            assertEquals(expectedBits[i], PermutationRound.getBitByIndex((byte) 210, i));
        }
    }
}
