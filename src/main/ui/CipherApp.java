package ui;

import model.*;

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
        String command = null;

        init();
        askForBlockSize();

        while (true) {
            displayOptions();
            command = input.next();
            if (command.equals("e")) {
                handleMessage("e");
            } else if (command.equals("d")) {
                handleMessage("d");
            } else if (command.equals("p")) {
                handlePermutation();
            } else if (command == "s") {
                handleSubstitution();
            } else if (command == "k") {
                handleKey();
            } else if (command.equals("v")) {
                displayCipherContents();
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
}
