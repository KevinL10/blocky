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
            } else if (command.equals("q")) {
                break;
            } else {
                System.out.println("Please select an option from the list above.");
            }
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
        System.out.println("Here are some of the operations you can perform:");
        System.out.println("\t[e]: encrypt a message");
        System.out.println("\t[d]: decrypt a message");
        System.out.println("\t[p]: add a permutation round");
        System.out.println("\t[s]: add a substitution round");
        System.out.println("\t[k]: add a mix-key round");
        System.out.println("\t[v]: view your current cipher");
    }

    // EFFECTS: displays the rounds of the cipher
    private void displayCipherContents() {
        System.out.println("Your cipher is made up of:");
        for (Round round : cipher.getRounds()) {
            if (round instanceof MixKeyRound) {
                System.out.println("\t a mix-key round");
            } else if (round instanceof SubstitutionRound) {
                System.out.println("\t a substitution round");
            } else if (round instanceof PermutationRound) {
                System.out.println("\t a permutation round");
            }
        }
    }

    // EFFECTS: encrypts the user's inputted plaintext with the cipher
    private void handleEncryption() {
        Byte[] plaintext = promptForMessage();
        ArrayList<Byte[]> keys = promptForKeys();
        Byte[] ciphertext = cipher.encryptByteArray(plaintext, keys);
        System.out.println("Your encrypted message is: ");
        for (Byte b : ciphertext) {
            System.out.print(b + " ");
        }
        System.out.println();
    }

    // EFFECTS: decrypts the user's inputted ciphertext with the cipher
    private void handleDecryption() {
        Byte[] ciphertext = promptForMessage();
        ArrayList<Byte[]> keys = promptForKeys();
        Byte[] plaintext = cipher.encryptByteArray(ciphertext, keys);
        System.out.println("Your decrypted message is: ");
        for (Byte b : plaintext) {
            System.out.print(b + " ");
        }
        System.out.println();
    }

    private void handlePermutation() {

    }

    private void handleSubstitution() {

    }

    private void handleKey() {

    }

    // EFFECTS: prompts user for message prior to encryption/decryption
    private Byte[] promptForMessage() {
        int blockSize = cipher.getBlockSize();
        Byte[] plaintext = new Byte[blockSize];
        System.out.println("Please enter your message as " + blockSize + " space-separated integers (bytes):");
        for (int i = 0; i < blockSize; i++) {
            plaintext[i] = input.nextByte();
        }
        return plaintext;
    }

    // EFFECTS: prompts user for keys prior to encryption/decryption
    private ArrayList<Byte[]> promptForKeys() {
        int blockSize = cipher.getBlockSize();
        int numKeyRounds = cipher.getNumberOfKeyRounds();
        ArrayList<Byte[]> keys = new ArrayList<>();
        System.out.println("Please enter each key as " + numKeyRounds + " space-separated integers (bytes):");
        for (int i = 0; i < numKeyRounds; i++) {
            System.out.println("Key " + i + ":");
            Byte[] currentKey = new Byte[blockSize];
            for (int j = 0; j < blockSize; j++) {
                currentKey[j] = input.nextByte();
            }
            keys.add(currentKey);
        }
        return keys;
    }
}
