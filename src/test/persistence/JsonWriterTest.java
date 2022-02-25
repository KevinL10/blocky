package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// referenced JsonSerializationDemo
public class JsonWriterTest {
    SubstitutionRound sround;
    PermutationRound pround;
    MixKeyRound kround;
    static final int[] sroundMapping =
            {11, 20, 7, 14, 19, 5, 6, 8, 15, 18, 12, 22, 4, 13, 16, 17, 10, 3, 1, 2, 0, 21, 23, 9};
    static final int[] proundMapping =
            {23, 11, 10, 5, 2, 12, 8, 16, 6, 13, 14, 9, 3, 20, 0, 4, 7, 1, 21, 18, 22, 17, 15, 19};

    @BeforeEach
    void runBefore(){
        sround = new SubstitutionRound(3);
        sround.setSubstitutionMapping(sroundMapping);
        pround = new PermutationRound(3);
        pround.setPermutationMapping(proundMapping);
        kround = new MixKeyRound(3);
    }
    @Test
    void testWriterInvalidFile() {
        try {
            Cipher cipher = new Cipher(4);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyCipher() {
        try {
            String filepath = "./data/testWriterEmptyCipher.json";
            Cipher cipher = new Cipher(4);
            JsonWriter writer = new JsonWriter(filepath);
            writer.open();
            writer.write(cipher);
            writer.close();

            JsonReader reader = new JsonReader(filepath);
            cipher = reader.read();
            assertEquals(0, cipher.getNumberOfRounds());
            assertEquals(4, cipher.getBlockSize());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterOneOfEachRound() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterOneOfEachRound.json");
            writer.open();
            writer.write(createOneOfEachRoundCipher());
            writer.close();

            // check that the written rounds in Json are the correct rounds
            JsonReader reader = new JsonReader("./data/testWriterOneOfEachRound.json");
            Cipher cipher = reader.read();
            ArrayList<Round> rounds = cipher.getRounds();
            assertEquals(3, cipher.getBlockSize());
            assertEquals(3, cipher.getNumberOfRounds());
            assertEquals(kround, rounds.get(0));
            assertEquals(sround, rounds.get(1));
            assertEquals(pround, rounds.get(2));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    Cipher createOneOfEachRoundCipher(){
        // create cipher with mixKeyRound, substitutionRound, and permutationRound
        int blockSize = 3;
        Cipher cipher = new Cipher(blockSize);
        cipher.addRound(kround);
        cipher.addRound(sround);
        cipher.addRound(pround);
        return cipher;
    }

    @Test
    void testWriterMultipleOfEachRound(){
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterMultipleOfEachRound.json");
            writer.open();
            writer.write(createMultipleOfEachRoundCipher());
            writer.close();

            // check that the written rounds in Json are the correct rounds
            JsonReader reader = new JsonReader("./data/testWriterMultipleOfEachRound.json");
            Cipher cipher = reader.read();
            ArrayList<Round> rounds = cipher.getRounds();
            assertEquals(3, cipher.getBlockSize());
            assertEquals(5, cipher.getNumberOfRounds());
            assertEquals(kround, rounds.get(0));
            assertEquals(sround, rounds.get(1));
            assertEquals(pround, rounds.get(2));
            assertEquals(sround, rounds.get(3));
            assertEquals(kround, rounds.get(4));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    Cipher createMultipleOfEachRoundCipher(){
        // create cipher with mixKeyRound, substitutionRound, and permutationRound
        int blockSize = 3;
        Cipher cipher = new Cipher(blockSize);
        cipher.addRound(kround);
        cipher.addRound(sround);
        cipher.addRound(pround);
        cipher.addRound(sround);
        cipher.addRound(kround);
        return cipher;
    }
}
