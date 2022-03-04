package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.exceptions.StartMenuException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

// referenced the TellerApp class provided by CPSC 210
public class CipherApp {
    private Cipher cipher;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // MODIFIES: this
    // EFFECTS: constructs a new scanner and runs the cipher
    public CipherApp() {
        // by default, construct a cipher with blockSize 2
        cipher = new Cipher(2);
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        runCipher();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runCipher() {
        displayStartMenu();
        // keep prompting for input until user constructs a valid cipher
        while (true) {
            try {
                handleStart();
                break;
            } catch (StartMenuException e) {
                System.out.println("Please choose an option again:");
            }
        }

        String command;
        while (true) {
            displayOptions();
            command = input.next();
            if (command.equals("q")) {
                break;
            }
            handleCommand(command);
            System.out.println();
        }
    }

    // MODIFIES: this
    // EFFECTS: allows the user to load or create a new cipher
    private void handleStart() throws StartMenuException {
        String command = input.next();
        if (command.equals("l")) {
            loadCipher();
        } else if (command.equals("c")) {
            promptBlockSize();
        } else {
            throw new StartMenuException();
        }
    }

    // MODIFIES: this
    // EFFECTS: performs the operation the user specifies
    private void handleCommand(String command) {
        if (command.equals("e")) {
            handleEncryption();
        } else if (command.equals("d")) {
            handleDecryption();
        } else if (command.equals("p")) {
            handlePermutation();
        } else if (command.equals("s")) {
            handleSubstitution();
        } else if (command.equals("k")) {
            handleKey();
        } else if (command.equals("v")) {
            displayCipherContents();
        } else if (command.equals("w")) {
            saveCipher();
        } else {
            System.out.println("Please select an option from the list above.");
        }
    }

    // EFFECTS: presents a start menu where user can load or create a new cipher
    private void displayStartMenu() {
        System.out.println("Welcome to Blocky, an interactive playground for building block ciphers!");
        System.out.println("To begin, please choose an option: ");
        System.out.println("\t[l]: load an existing cipher");
        System.out.println("\t[c]: create a new cipher");
    }

    // EFFECTS: displays a list of options for the user
    private void displayOptions() {
        System.out.println("Valid Operations:");
        System.out.println("\t[e]: encrypt a message");
        System.out.println("\t[d]: decrypt a message");
        System.out.println("\t[p]: add a permutation round");
        System.out.println("\t[s]: add a substitution round");
        System.out.println("\t[k]: add a mix-key round");
        System.out.println("\t[v]: view your current cipher");
        System.out.println("\t[w]: save your current cipher");
        System.out.println("\t[q]: quit");
    }

    // EFFECTS: displays the rounds of the cipher
    private void displayCipherContents() {
        System.out.println("Your cipher is made up of:");
        System.out.println("-------------------------------------");
        for (Round round : cipher.getRounds()) {
            if (round instanceof MixKeyRound) {
                System.out.println("|            mix-key round          |");
            } else if (round instanceof SubstitutionRound) {
                System.out.println("|         substitution round        |");
            } else if (round instanceof PermutationRound) {
                System.out.println("|          permutation round        |");
            }
            System.out.println("-------------------------------------");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads the cipher from the given source
    private void loadCipher() throws StartMenuException {
        System.out.println("Please enter the filepath to your cipher:");
        String source = input.next();
        jsonWriter = new JsonWriter(source);
        jsonReader = new JsonReader(source);
        try {
            cipher = jsonReader.read();
            System.out.println("Loaded cipher with block size " + cipher.getBlockSize() + "!");
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + source);
            throw new StartMenuException();
        }
    }

    // MODIFIES: this
    // EFFECTS: saves the cipher to the given destination
    private void saveCipher() {
        System.out.println("Please enter the destination filepath:");
        String destination = input.next();
        jsonWriter = new JsonWriter(destination);
        jsonReader = new JsonReader(destination);
        try {
            jsonWriter.open();
            jsonWriter.write(cipher);
            jsonWriter.close();
            System.out.println("Saved cipher with block size " + cipher.getBlockSize() + "!");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + destination);
        }
    }

    // MODIFIES: this
    // EFFECTS: constructs a cipher with the given blockSize
    private void promptBlockSize() {
        System.out.println("Please choose a block size for your cipher:");
        int blockSize = input.nextInt();
        cipher = new Cipher(blockSize);
        System.out.println("Successfully constructed a cipher with size " + blockSize + "!");
        System.out.println();
    }

    // MODIFIES: this
    // EFFECTS: encrypts the user's inputted plaintext with the cipher
    private void handleEncryption() {
        Byte[] plaintext = promptForMessage();
        ArrayList<Byte[]> keys = promptForKeys();
        Byte[] ciphertext = cipher.encryptByteArray(plaintext, keys);
        System.out.println("Your encrypted message is: ");
        printByteArray(ciphertext);
    }

    // MODIFIES: this
    // EFFECTS: decrypts the user's inputted ciphertext with the cipher
    private void handleDecryption() {
        Byte[] ciphertext = promptForMessage();
        ArrayList<Byte[]> keys = promptForKeys();
        Byte[] plaintext = cipher.decryptByteArray(ciphertext, keys);
        System.out.println("Your decrypted message is: ");
        printByteArray(plaintext);
    }

    // MODIFIES: this
    // EFFECTS: adds a permutation round to the cipher with the specified mapping
    private void handlePermutation() {
        int blockSize = cipher.getBlockSize();
        PermutationRound round = new PermutationRound(cipher.getBlockSize());
        int[] mapping = new int[blockSize * 8];
        System.out.println("Choose an option for your permutation mapping");
        System.out.println("\t[r]: randomized permutation");
        System.out.println("\t[c]: custom permutation");
        String choice = input.next();
        if (choice.equals("c")) {
            System.out.println("Enter your permutation mapping as "
                    + 8 * blockSize + " line-separated integers (bytes)");
            for (int i = 0; i < blockSize * 8; i++) {
                mapping[i] = (byte) input.nextInt();
            }
            round.setPermutationMapping(mapping);
        } else if (choice.equals("r")) {
            round.fillWithRandomPermutation();
        } else {
            System.out.println("Sorry, that is not a valid choice.");
            return;
        }
        cipher.addRound(round);
        System.out.println("Permutation round successfully added!");
    }

    // MODIFIES: this
    // EFFECTS: add a substitution round to the cipher with the specified mapping
    private void handleSubstitution() {
        int blockSize = cipher.getBlockSize();
        SubstitutionRound round = new SubstitutionRound(blockSize);
        int[] mapping = new int[blockSize * 8];
        System.out.println("Choose an option for your substitution mapping");
        System.out.println("\t[r]: randomized substitution");
        System.out.println("\t[c]: custom substitution");
        String choice = input.next();
        if (choice.equals("c")) {
            System.out.println("Enter your substitution mapping as 16 line-separated integers (bytes)");
            for (int i = 0; i < blockSize * 8; i++) {
                mapping[i] = (byte) input.nextInt();
            }
            round.setSubstitutionMapping(mapping);
        } else if (choice.equals("r")) {
            round.fillWithRandomSubstitution();
        } else {
            System.out.println("Sorry, that is not a valid choice.");
            return;
        }
        cipher.addRound(round);
        System.out.println("Substitution round successfully added!");
    }

    // MODIFIES: this
    // EFFECTS: adds a mix key round to the cipher
    private void handleKey() {
        int blockSize = cipher.getBlockSize();
        MixKeyRound round = new MixKeyRound(blockSize);
        cipher.addRound(round);
        System.out.println("Mix key round successfully added!");
    }

    // EFFECTS: prompts user for message prior to encryption/decryption
    private Byte[] promptForMessage() {
        int blockSize = cipher.getBlockSize();
        Byte[] plaintext = new Byte[blockSize];
        System.out.println("Please enter your message as " + blockSize + " line-separated integers (bytes):");
        for (int i = 0; i < blockSize; i++) {
            plaintext[i] = (byte) input.nextInt();
        }
        return plaintext;
    }

    // EFFECTS: prompts user for keys prior to encryption/decryption
    private ArrayList<Byte[]> promptForKeys() {
        int blockSize = cipher.getBlockSize();
        int numKeyRounds = cipher.getNumberOfKeyRounds();
        ArrayList<Byte[]> keys = new ArrayList<>();
        System.out.println("Please enter each key as " + blockSize + " line-separated integers (bytes):");
        for (int i = 0; i < numKeyRounds; i++) {
            System.out.println("Key " + i + ":");
            Byte[] currentKey = new Byte[blockSize];
            for (int j = 0; j < blockSize; j++) {
                currentKey[j] = (byte) input.nextInt();
            }
            keys.add(currentKey);
        }
        return keys;
    }

    // EFFECTS: prints out the bytes in the bytearray as space-separated, unsigned values
    private void printByteArray(Byte[] bytes) {
        for (Byte b : bytes) {
            System.out.print((b & 0xff) + " ");
        }
        System.out.println();
    }
}
