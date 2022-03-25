package ui;

import model.Cipher;
import model.MixKeyRound;
import model.PermutationRound;
import model.SubstitutionRound;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// Class representing the cipher ui
public class CipherUI extends JFrame {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;

    private Cipher cipher;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JPanel buttonPanel;
    private JPanel cipherPanel;

    public CipherUI() {
        super("Cipher Creator");
        initializeFields();
        initializeGraphics();
    }

    // MODIFIES: this
    // EFFECTS:  creates cipher a block size of 2
    private void initializeFields() {
        cipher = new Cipher(2);
    }


    // MODIFIES: this
    // EFFECTS:  draws the Cipher JFrame Window
    private void initializeGraphics() {
        // from https://stackoverflow.com/questions/7050972/layout-manager-preferredsize-java

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        buttonPanel = createButtonPanel();
        cipherPanel = createCipherPanel();
        JMenuBar menuBar = createMenuBar();

        setJMenuBar(menuBar);

        JPanel cipherPanelWrapper = new JPanel();
        cipherPanelWrapper.add(cipherPanel);

        add(buttonPanel, BorderLayout.NORTH);
        add(cipherPanelWrapper, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: creates a new JPanel for the cipher round display
    private JPanel createCipherPanel() {
        JPanel cipherPanel = new JPanel();
        cipherPanel.setLayout(new BoxLayout(cipherPanel, BoxLayout.Y_AXIS));
        cipherPanel.setMinimumSize(new Dimension(100, 200));
        //cipherPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return cipherPanel;
    }

    // MODIFIES: this
    // EFFECTS: adds a new label to the cipher round panel
    private void addRoundLabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(150, 30));
        label.setBorder(BorderFactory.createLineBorder(Color.BLUE));

        // label should be in the center of the panel AND the label's text should be in the center of the label
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(JLabel.CENTER);

        JPanel wrapper = new JPanel();
        wrapper.add(label);
        cipherPanel.add(wrapper);
        validate();
        repaint();
    }


    // ,...
    private JMenuBar createMenuBar() {
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

        // SOURCE: https://stackoverflow.com/questions/45433871/swing-last-
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

        return menuBar;
    }

    // MODIFIES: this
    // EFFECTS: creates and adds a JPanel for encrypt and decrypt buttons
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();

        buttonPanel.add(new JButton(new EncryptMessage()));
        buttonPanel.add(new JButton(new DecryptMessage()));

        return buttonPanel;
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
            // taken from https://stackoverflow.com/questions/6555040/multiple-input-in-joptionpane-showinputdialog
            JTextField message = new JTextField();
            ArrayList<JTextField> keyFields = new ArrayList<>();

            int numOfKeys = cipher.getNumberOfKeyRounds();
            Object[] input = new Object[2 + numOfKeys * 2];
            input[0] = "Message:";
            input[1] = message;
            for (int i = 2; i < 2 + numOfKeys * 2; i += 2) {
                JTextField key = new JTextField();
                input[i] = "Key " + Integer.toString(i) + ":";
                input[i + 1] = key;
                keyFields.add(key);
            }

            ArrayList<Byte[]> keys = new ArrayList<>();
            int option = JOptionPane.showConfirmDialog(null, input,
                    "Encrypt Message", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                for (JTextField next : keyFields) {
                    String[] keyBytes = next.getText().split(" ");
                    keys.add(stringArrayToByteArray(keyBytes));
                }

                String[] messageString = message.getText().split(" ");
                Byte[] messageBytes = stringArrayToByteArray(messageString);
                Byte[] encryptedMessage = cipher.encryptByteArray(messageBytes, keys);
                JOptionPane.showMessageDialog(null, byteArrayToString(encryptedMessage));
            }
        }
    }

    /**
     * Represents action to be taken when user wants to decrypt a message
     */
    private class DecryptMessage extends AbstractAction {

        DecryptMessage() {
            super("Decrypt");
        }


        // TODO: actually make things object-oriented
        @Override
        public void actionPerformed(ActionEvent evt) {
            // taken from https://stackoverflow.com/questions/6555040/multiple-input-in-joptionpane-showinputdialog
            JTextField message = new JTextField();
            ArrayList<JTextField> keyFields = new ArrayList<>();

            int numOfKeys = cipher.getNumberOfKeyRounds();
            Object[] input = new Object[2 + numOfKeys * 2];
            input[0] = "Message:";
            input[1] = message;
            for (int i = 2; i < 2 + numOfKeys * 2; i += 2) {
                JTextField key = new JTextField();
                input[i] = "Key " + Integer.toString(i) + ":";
                input[i + 1] = key;
                keyFields.add(key);
            }

            ArrayList<Byte[]> keys = new ArrayList<>();
            int option = JOptionPane.showConfirmDialog(null, input,
                    "Decrypt Message", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                for (JTextField next : keyFields) {
                    String[] keyBytes = next.getText().split(" ");
                    keys.add(stringArrayToByteArray(keyBytes));
                }

                String[] messageString = message.getText().split(" ");
                Byte[] messageBytes = stringArrayToByteArray(messageString);
                Byte[] encryptedMessage = cipher.decryptByteArray(messageBytes, keys);
                JOptionPane.showMessageDialog(null, byteArrayToString(encryptedMessage));
            }
        }
    }

    public Byte[] stringArrayToByteArray(String[] input) {
        Byte[] output = new Byte[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = Integer.valueOf(input[i]).byteValue();
        }
        return output;
    }

    public String byteArrayToString(Byte[] input) {
        String output = "";
        for (Byte b : input) {
            output += (b & 0xff) + " ";
        }
        return output;
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
            addRoundLabel("Mix Key Round");
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
            int input = JOptionPane.showConfirmDialog(null,
                    "Would you like to randomize the substitution round?");

            if (input == 0) {
                round.fillWithRandomSubstitution();
                addRoundLabel("Substitution Round");
                JOptionPane.showMessageDialog(null, "Added random substitution round");
            } else if (input == 1) {
                int[] mapping = new int[16];
                String[] stringMapping = JOptionPane.showInputDialog(null,
                        "Mapping:").split(" ");
                for (int i = 0; i < 16; i++) {
                    mapping[i] = Integer.parseInt(stringMapping[i]);
                    System.out.println(mapping[i]);
                }
                round.setSubstitutionMapping(mapping);

                addRoundLabel("Substitution Round");
                JOptionPane.showMessageDialog(null, "Added substitution round");
            }
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
            int input = JOptionPane.showConfirmDialog(null,
                    "Would you like to randomize the permutation round?");

            if (input == 0) {
                round.fillWithRandomPermutation();
                addRoundLabel("Permutation Round");
                JOptionPane.showMessageDialog(null, "Added random permutation round");
            } else if (input == 1) {
                int[] mapping = new int[8 * cipher.getBlockSize()];
                String[] stringMapping = JOptionPane.showInputDialog(null,
                        "Mapping:").split(" ");
                for (int i = 0; i < 8 * cipher.getBlockSize(); i++) {
                    mapping[i] = Integer.parseInt(stringMapping[i]);
                    System.out.println(mapping[i]);
                }
                round.setPermutationMapping(mapping);
                addRoundLabel("Permutation Round");
                JOptionPane.showMessageDialog(null, "Added permutation round");
            }
        }
    }
}
