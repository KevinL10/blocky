package ui;

import jdk.nashorn.internal.scripts.JO;
import model.Cipher;
import model.MixKeyRound;
import model.PermutationRound;
import model.SubstitutionRound;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.exceptions.StartMenuException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class CipherUI extends JFrame {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;

    private Cipher cipher;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

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
        //addCipherFrame();

        JPanel j = new JPanel();
        j.add(new JButton("ABCD"));
        add(j, BorderLayout.EAST);
        j.setSize(100, 100);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: creates a JInternalFrame for the cipher
    private void addCipherFrame() {
        JInternalFrame in = new JInternalFrame();
        in.setTitle("cipher frame");

        in.reshape(0, 0, 50, 50);
        in.setVisible(true);
        add(in);
    }

    // MODIFIES: this
    // EFFECTS:  creates cipher a block size of 2
    private void initializeFields() {
        cipher = new Cipher(2);
    }

    // ,...
    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new LoadCipher());
        fileMenu.add(new SaveCipher());
        menuBar.add(fileMenu);

        JMenu addMenu = new JMenu("Add");
        addMenu.add(new AddKeyRound());
        addMenu.add(new AddSubstitutionRound());
        addMenu.add(new AddPermutationRound());
        menuBar.add(addMenu);

        // code taken from https://stackoverflow.com/questions/45433871/swing-last-
        // jmenuitem-occupy-the-rest-of-space-on-jmenubar
        menuBar.add(new JMenuItem(new InfoMenu()) {
            @Override
            public Dimension getMaximumSize() {
                Dimension d1 = super.getPreferredSize();
                Dimension d2 = super.getMaximumSize();
                d2.width = d1.width;
                return d2;
            }
        });

        setJMenuBar(menuBar);
    }

    // MODIFIES: this
    // EFFECTS: creates JPanel for add, encrypt, and decrypt buttons
    private void addButtons() {
        JPanel buttons = new JPanel();

        buttons.add(new JButton(new AddRound()));
        buttons.add(new JButton(new EncryptMessage()));
        buttons.add(new JButton(new DecryptMessage()));

        JLabel label = new JLabel("substitution");
        label.setBounds(150, 100, 50, 50);
        buttons.add(label);
        add(buttons);

        /*
        JPanel panel2 = new JPanel();
        panel2.add(new JButton(new AddRound()));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));


        add(panel2);*/
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

        // EFFECTS....
        @Override
        public void actionPerformed(ActionEvent evt) {
            String filepath = JOptionPane.showInputDialog("Filepath: ");
            jsonWriter = new JsonWriter(filepath);
            jsonReader = new JsonReader(filepath);
            try {
                cipher = jsonReader.read();
                JOptionPane.showMessageDialog(null, "Loaded cipher with block size "
                        + cipher.getBlockSize() + "!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Unable to read from file: "
                        + filepath);
            }
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
            String filepath = JOptionPane.showInputDialog("Filepath: ");
            jsonWriter = new JsonWriter(filepath);
            jsonReader = new JsonReader(filepath);
            try {
                jsonWriter.open();
                jsonWriter.write(cipher);
                jsonWriter.close();
                JOptionPane.showMessageDialog(null, "Saved cipher with block size "
                        + cipher.getBlockSize() + "!");
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Unable to write to file: "
                        + filepath);
            }
        }
    }

    /**
     * Represents action to be taken when user wants to open the help popup
     */
    private class InfoMenu extends AbstractAction {

        InfoMenu() {
            super("Info");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            // image taken from https://www.techtarget.com/searchsecurity/definition/block-cipher
            ImageIcon img = new ImageIcon("./data/block_cipher.png");
            JOptionPane.showMessageDialog(null, "", "Info",
                    JOptionPane.INFORMATION_MESSAGE, img);
        }
    }

    /**
     * Represents action to be taken when user wants to add a key round
     */
    private class AddKeyRound extends AbstractAction {

        AddKeyRound() {
            super("Key Round");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            cipher.addRound(new MixKeyRound(cipher.getBlockSize()));
            JOptionPane.showMessageDialog(null, "Added Mix Key Round");
        }
    }

    /**
     * Represents action to be taken when user wants to add a substitution round
     */
    private class AddSubstitutionRound extends AbstractAction {

        AddSubstitutionRound() {
            super("Substitution Round");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            SubstitutionRound round = new SubstitutionRound(cipher.getBlockSize());
            JOptionPane.showMessageDialog(null, "Added Substitution Round");
        }
    }

    /**
     * Represents action to be taken when user wants to add a permutation round
     */
    private class AddPermutationRound extends AbstractAction {

        AddPermutationRound() {
            super("Permutation Round");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            PermutationRound round = new PermutationRound(cipher.getBlockSize());
            JOptionPane.showMessageDialog(null, "Added Permutation Round");
        }
    }
}
