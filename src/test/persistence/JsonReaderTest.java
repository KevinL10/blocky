package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// referenced JsonSerializationDemo
public class JsonReaderTest extends JsonTest {
    static final int[] sroundMapping = {5, 9, 6, 8, 1, 2, 15, 11, 13, 0, 12, 7, 10, 3, 14, 4};
    static final int[] proundMapping = {2, 4, 13, 0, 8, 10, 1, 11, 12, 3, 6, 9, 7, 5, 14, 15};

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Cipher cipher = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyCipher() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyCipher.json");
        try {
            Cipher cipher = reader.read();
            assertEquals(4, cipher.getBlockSize());
            assertEquals(0, cipher.getNumberOfRounds());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderMixKeyRoundCipher() {
        JsonReader reader = new JsonReader("./data/testReaderMixKeyRoundCipher.json");
        try {
            Cipher cipher = reader.read();
            assertEquals(4, cipher.getBlockSize());
            assertEquals(1, cipher.getNumberOfRounds());

            // expect only a mix key round
            ArrayList<Round> rounds = cipher.getRounds();
            MixKeyRound kround = new MixKeyRound(4);
            assertEquals(kround, rounds.get(0));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderSubstitutionRoundCipher() {
        JsonReader reader = new JsonReader("./data/testReaderSubstitutionRoundCipher.json");
        try {
            Cipher cipher = reader.read();
            assertEquals(4, cipher.getBlockSize());
            assertEquals(1, cipher.getNumberOfRounds());

            // expect only a substitution round
            ArrayList<Round> rounds = cipher.getRounds();

            SubstitutionRound sround = new SubstitutionRound(4);
            sround.setSubstitutionMapping(sroundMapping);

            assertEquals(sround, rounds.get(0));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderPermutationRoundCipher() {
        JsonReader reader = new JsonReader("./data/testReaderPermutationRoundCipher.json");
        try {
            Cipher cipher = reader.read();
            assertEquals(2, cipher.getBlockSize());
            assertEquals(1, cipher.getNumberOfRounds());

            // expect only a substitution round
            ArrayList<Round> rounds = cipher.getRounds();

            PermutationRound pround = new PermutationRound(2);
            pround.setPermutationMapping(proundMapping);

            assertEquals(pround, rounds.get(0));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderThreeRoundCipher() {
        JsonReader reader = new JsonReader("./data/testReaderThreeRoundCipher.json");
        try {
            Cipher cipher = reader.read();
            assertEquals(2, cipher.getBlockSize());
            assertEquals(3, cipher.getNumberOfRounds());

            // expect only a substitution round
            ArrayList<Round> rounds = cipher.getRounds();

            PermutationRound pround = new PermutationRound(2);
            pround.setPermutationMapping(proundMapping);

            MixKeyRound kround = new MixKeyRound(2);

            SubstitutionRound sround = new SubstitutionRound(2);
            sround.setSubstitutionMapping(sroundMapping);

            assertEquals(pround, rounds.get(0));
            assertEquals(kround, rounds.get(1));
            assertEquals(sround, rounds.get(2));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }


    @Test
    void testReaderMultipleOfEachRound() {
        JsonReader reader = new JsonReader("./data/testReaderMultipleOfEachRound  .json");
        try {
            Cipher cipher = reader.read();
            assertEquals(2, cipher.getBlockSize());
            assertEquals(5, cipher.getNumberOfRounds());

            // expect only a substitution round
            ArrayList<Round> rounds = cipher.getRounds();

            PermutationRound pround = new PermutationRound(2);
            pround.setPermutationMapping(proundMapping);

            MixKeyRound kround = new MixKeyRound(2);

            SubstitutionRound sround = new SubstitutionRound(2);
            sround.setSubstitutionMapping(sroundMapping);

            MixKeyRound kround2 = new MixKeyRound(2);

            SubstitutionRound sround2 = new SubstitutionRound(2);
            int[] smapping2 = {11, 4, 9, 7, 15, 12, 13, 8, 14, 2, 3, 1, 5, 10, 6, 0};
            sround2.setSubstitutionMapping(smapping2);

            assertEquals(pround, rounds.get(0));
            assertEquals(kround, rounds.get(1));
            assertEquals(sround, rounds.get(2));
            assertEquals(kround2, rounds.get(3));
            assertEquals(sround2, rounds.get(4));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
