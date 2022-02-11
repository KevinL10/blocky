package ui;

import model.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

// referenced the TellerApp class provided by CPSC 210
public class CipherApp {
    private Cipher cipher;
    private Scanner input;

    // EFFECTS: runs the cipher
    public CipherApp() {
        runCipher();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runCipher() {
        String command;
        init();
        askForBlockSize();

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
        } else {
            System.out.println("Please select an option from the list above.");
        }
    }

    // MODIFIES: this
    // EFFECTS: constructs a new scanner for input
    private void init() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // MODIFIES: this
    // EFFECTS: constructs cipher according to specified block size
    private void askForBlockSize() {
        System.out.println("Welcome to Blocky, an interactive playground for building block ciphers!");
        System.out.println("To begin, please choose a block size for your cipher: ");
        int blockSize = input.nextInt();
        cipher = new Cipher(blockSize);
        System.out.println("Successfully constructed a cipher with size " + blockSize + "!");
        System.out.println();
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
