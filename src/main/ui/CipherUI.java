package ui;

import model.Cipher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class CipherUI extends JFrame {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;

    private Cipher cipher;

    public CipherUI() {
        super("Cipher Creator");
        initializeFields();
        initializeGraphics();
    }

    // MODIFIES: this
    // EFFECTS:  draws the Cipher JFrame Window
    private void initializeGraphics() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));

        addButtons();
        addMenuBar();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS:  creates cipher a block size of 2
    private void initializeFields() {
        cipher = new Cipher(2);
    }

    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new LoadCipher());
        fileMenu.add(new SaveCipher());
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    // MODIFIES: this
    // EFFECTS: creates JPanel for add, encrypt, and decrypt buttons
    private void addButtons() {
        JPanel buttons = new JPanel();

        buttons.add(new JButton(new AddRound()));
        buttons.add(new JButton(new EncryptMessage()));
        buttons.add(new JButton(new DecryptMessage()));

        add(buttons);
    }

    /**
     * Represents action to be taken when user wants to add a new round
     * to the cipher.
     */
    private class AddRound extends AbstractAction {

        AddRound() {
            super("Add Round");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JOptionPane.showMessageDialog(null, "Adding round...");
        }
    }

    /**
     * Represents action to be taken when user wants to encrypt a message
     */
    private class EncryptMessage extends AbstractAction {

        EncryptMessage() {
            super("Encrypt");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JOptionPane.showMessageDialog(null, "Encrypting message...");
        }
    }

    /**
     * Represents action to be taken when user wants to decrypt a message
     */
    private class DecryptMessage extends AbstractAction {

        DecryptMessage() {
            super("Decrypt");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JOptionPane.showMessageDialog(null, "Decrypting message...");
        }
    }

    /**
     * Represents action to be taken when user wants to load a cipher from a filepath
     */
    private class LoadCipher extends AbstractAction {

        LoadCipher() {
            super("Load");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JOptionPane.showMessageDialog(null, "Loading cipher...");
        }
    }


    /**
     * Represents action to be taken when user wants to save a message to a filepath
     */
    private class SaveCipher extends AbstractAction {

        SaveCipher() {
            super("Save");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JOptionPane.showMessageDialog(null, "Saving cipher...");
        }
    }
}
