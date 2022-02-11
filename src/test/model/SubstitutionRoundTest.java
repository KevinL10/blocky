package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubstitutionRoundTest {
    SubstitutionRound round;

    @BeforeEach
    public void runBefore() {
        round = new SubstitutionRound();
    }

    @Test
    public void testConstructor() {
        int[] mapping = round.getSubstitutionMapping();
        assertEquals(16, mapping.length);
        for (int i = 0; i < 16; i++) {
            assertEquals(i, mapping[i]);
        }
    }

    @Test
    public void testEncryptRound() {
        int[] mapping = {2, 4, 13, 0, 8, 10, 1, 11, 12, 3, 6, 9, 7, 5, 14, 15};
        round.setSubstitutionMapping(mapping);

        Byte[] plaintext = {(byte) 168, (byte) 42};
        Byte[] encrypted = round.encryptRound(plaintext);


        for (int i = 0; i < 2; i++) {
            int upperBits = ((plaintext[i] >> 4) & 0xf);
            int lowerBits = plaintext[i] & 0xf;

            upperBits = mapping[upperBits];
            lowerBits = mapping[lowerBits];

            assertEquals(encrypted[i], (byte) ((upperBits << 4) + lowerBits));
        }
    }

    @Test
    public void testDecryptRound() {
        int[] mapping = {2, 4, 13, 0, 8, 10, 1, 11, 12, 3, 6, 9, 7, 5, 14, 15};
        // mapping such that mapping[inverseMapping[a]] = a;
        int[] inverseMapping = {3, 6, 0, 9, 1, 13, 10, 12, 4, 11, 5, 7, 8, 2, 14, 15};

        round.setSubstitutionMapping(mapping);

        Byte[] ciphertext = {(byte) 168, (byte) 42};
        Byte[] plaintext = round.decryptRound(ciphertext);


        for (int i = 0; i < 2; i++) {
            int upperBits = ((ciphertext[i] >> 4) & 0xf);
            int lowerBits = ciphertext[i] & 0xf;

            upperBits = inverseMapping[upperBits];
            lowerBits = inverseMapping[lowerBits];

            assertEquals(plaintext[i], (byte) ((upperBits << 4) + lowerBits));
        }
    }

    @Test
    public void testRandomSubstitutionNoDuplicates() {
        round.fillWithRandomSubstitution();

        int[] mapping = round.getSubstitutionMapping();
        boolean[] containsN = new boolean[16];

        // check that all numbers from 0 to 15 inclusive appear exactly once
        assertEquals(16, mapping.length);
        for (int i = 0; i < 16; i++) {
            containsN[mapping[i]] = true;
        }

        for(int i = 0; i < 16; i++){
            assertTrue(containsN[i]);
        }
    }

    @Test
    public void testRandomSubstitutionRandomized() {
        round.fillWithRandomSubstitution();
        int[] mapping = round.getSubstitutionMapping();

        // check that the mapping is randomized (different from identity permutation)
        boolean isRandomized = false;
        for (int i = 0; i < 16; i++) {
            if(mapping[i] != i){
                isRandomized = true;
                break;
            }
        }

        assertTrue(isRandomized);
    }
}
