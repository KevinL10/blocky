package ui;

import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// Class representing the cipher ui
// Referenced CPSC 210 DrawingPlayer and AlarmSystem projects for Java Swing
public class CipherUI extends JFrame {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;

    private Cipher cipher;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JPanel buttonPanel;
    private JPanel cipherPanel;

    // MODIFIES: this
    // EFFECTS: initializes the UI fields and graphics
    public CipherUI() {
        super("Cipher Creator");
        initializeFields();
        initializeGraphics();
    }

    // MODIFIES: this
    // EFFECTS:  creates a cipher with block size 2
    private void initializeFields() {
        cipher = new Cipher(2);
    }

    // MODIFIES: this
    // EFFECTS: adds the button panel, cipher round panel, and the menu bar to the main JFrame
    private void initializeGraphics() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        buttonPanel = createButtonPanel();
        cipherPanel = createCipherPanel();

        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        JPanel cipherPanelWrapper = new JPanel();
        cipherPanelWrapper.add(cipherPanel);

        add(buttonPanel, BorderLayout.NORTH);
        add(cipherPanelWrapper, BorderLayout.CENTER);

        // SOURCE: https://stackoverflow.com/questions/16372241/run-function-on-jframe-close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                EventLog eventLog = EventLog.getInstance();
                for (Event next : eventLog) {
                    System.out.println(next + "\n");
                }
                dispose();
                System.exit(0);
            }
        });
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        setVisible(true);
    }

    // EFFECTS: creates a new JPanel for the cipher round display
    // SOURCE: https://stackoverflow.com/questions/7050972/layout-manager-preferredsize-java
    private JPanel createCipherPanel() {
        JPanel cipherPanel = new JPanel();
        cipherPanel.setLayout(new BoxLayout(cipherPanel, BoxLayout.Y_AXIS));
        cipherPanel.setMinimumSize(new Dimension(100, 200));
        return cipherPanel;
    }

    // MODIFIES: this
    // EFFECTS: adds a new label to the cipher round panel and redisplay the panel
    private void addRoundLabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(150, 30));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // label should be in the center of the panel AND the label's text should be in the center of the label
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(JLabel.CENTER);

        JPanel wrapper = new JPanel();
        wrapper.add(label);
        cipherPanel.add(wrapper);
        validate();
        repaint();
    }

    // EFFECTS: creates a menu bar consisting of "File", "Add", and "Info" options
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new LoadCipher());
        fileMenu.add(new SaveCipher());
        fileMenu.add(new NewCipher());
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
    // EFFECTS: creates a JPanel for the encrypt and decrypt buttons
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
        // MODIFIES: this
        // EFFECTS: displays a UI pane for user to enter their message and keys,
        // and encrypts the message accordingly
        public void actionPerformed(ActionEvent evt) {
            // SOURCE: https://stackoverflow.com/questions/6555040/multiple-input-in-joptionpane-showinputdialog
            JTextField message = new JTextField();
            ArrayList<JTextField> keyFields = new ArrayList<>();
            Object[] input = createMessageObject(message, keyFields);

            int option = JOptionPane.showConfirmDialog(null, input,
                    "Encrypt Message", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                ArrayList<Byte[]> keys = new ArrayList<>();
                Byte[] messageToEncrypt;

                getKeysFromFields(keyFields, keys);
                messageToEncrypt = stringArrayToByteArray(message.getText().split(" "));

                Byte[] encryptedMessage = cipher.encryptByteArray(messageToEncrypt, keys);
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

        @Override
        // MODIFIES: this
        // EFFECTS: displays a UI pane for user to enter their message and keys,
        // and decrypts the message accordingly
        public void actionPerformed(ActionEvent evt) {
            // taken from https://stackoverflow.com/questions/6555040/multiple-input-in-joptionpane-showinputdialog
            JTextField message = new JTextField();
            ArrayList<JTextField> keyFields = new ArrayList<>();

            Object[] input = createMessageObject(message, keyFields);

            int option = JOptionPane.showConfirmDialog(null, input,
                    "Decrypt Message", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                ArrayList<Byte[]> keys = new ArrayList<>();
                Byte[] messageToEncrypt;

                getKeysFromFields(keyFields, keys);
                messageToEncrypt = stringArrayToByteArray(message.getText().split(" "));

                Byte[] decryptedMessage = cipher.decryptByteArray(messageToEncrypt, keys);
                JOptionPane.showMessageDialog(null, byteArrayToString(decryptedMessage));
            }
        }
    }

    // MODIFIES: keys
    // EFFECTS: adds each JTextField in keyFields to keys as a byte array
    private void getKeysFromFields(ArrayList<JTextField> keyFields, ArrayList<Byte[]> keys) {
        for (JTextField next : keyFields) {
            String[] keyBytes = next.getText().split(" ");
            keys.add(stringArrayToByteArray(keyBytes));
        }
    }

    // EFFECTS: converts each string in the input array to a byte value
    public Byte[] stringArrayToByteArray(String[] input) {
        Byte[] output = new Byte[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = Integer.valueOf(input[i]).byteValue();
        }
        return output;
    }

    // MODIFIES: message, keyFields
    // EFFECTS: constructs an object for encryption/decryption that asks for a message and list of keys
    private Object[] createMessageObject(JTextField message, ArrayList<JTextField> keyFields) {
        int numOfKeys = cipher.getNumberOfKeyRounds();
        Object[] input = new Object[2 + numOfKeys * 2];
        input[0] = "Message:";
        input[1] = message;
        for (int i = 1; i <= numOfKeys; i++) {
            JTextField key = new JTextField();
            input[2 * i] = "Key " + i + ":";
            input[2 * i + 1] = key;
            keyFields.add(key);
        }
        return input;
    }

    /**
     * Represents action to be taken when user wants to load a cipher from a filepath
     */
    private class LoadCipher extends AbstractAction {

        LoadCipher() {
            super("Load");
        }

        @Override
        // MODIFIES: this
        // EFFECTS: loads the cipher in the specified filepath that the user inputs in a pop-up pane
        public void actionPerformed(ActionEvent evt) {
            String filepath = JOptionPane.showInputDialog("Filepath: ");
            jsonWriter = new JsonWriter(filepath);
            jsonReader = new JsonReader(filepath);
            try {
                cipher = jsonReader.read();
                redisplayRounds();
                JOptionPane.showMessageDialog(null, "Loaded cipher with block size "
                        + cipher.getBlockSize() + "!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Unable to read from file: "
                        + filepath);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: clears the current cipher panel and redisplay the cipher rounds
    public void redisplayRounds() {
        cipherPanel.removeAll();
        for (Round r : cipher.getRounds()) {
            if (r instanceof MixKeyRound) {
                addRoundLabel("Mix Key Round");
            } else if (r instanceof SubstitutionRound) {
                addRoundLabel("Substitution Round");
            } else if (r instanceof PermutationRound) {
                addRoundLabel("Permutation Round");
            }
        }
        revalidate();
        repaint();
    }

    /**
     * Represents action to be taken when user wants to save a message to a filepath
     */
    private class SaveCipher extends AbstractAction {

        SaveCipher() {
            super("Save");
        }

        @Override
        // MODIFIES: this
        // EFFECTS: saves the cipher to the specified filepath that the user inputs in a pop-up pane
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
     * Represents action to be taken when user wants to create a new cipher with a specific blocksize
     */
    private class NewCipher extends AbstractAction {

        NewCipher() {
            super("New");
        }

        @Override
        // MODIFIES: this
        // EFFECTS: creates a new cipher with a specified block size that the user inputs in a pop-up pane
        public void actionPerformed(ActionEvent evt) {
            try {
                int blockSize = Integer.parseInt(JOptionPane.showInputDialog("Block size:"));
                cipher = new Cipher(blockSize);
                JOptionPane.showMessageDialog(null, "Created new cipher with block size "
                        + blockSize);
                redisplayRounds();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "An error occurred."
                        + " Please input a single integer");
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
        // EFFECTS: displays an info image for block ciphers
        // SOURCE: https://www.techtarget.com/searchsecurity/definition/block-cipher
        public void actionPerformed(ActionEvent evt) {
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
        // MODIFIES: this
        // EFFECTS: prompts the user to add a mix key round to the cipher
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
        // MODIFIES: this
        // EFFECTS: prompts the user to add a substitution round to the cipher
        public void actionPerformed(ActionEvent evt) {
            SubstitutionRound round = new SubstitutionRound(cipher.getBlockSize());
            int input = JOptionPane.showConfirmDialog(null,
                    "Would you like to randomize the substitution round?");

            if (input == 0) {
                round.fillWithRandomSubstitution();
                addRoundLabel("Substitution Round");
                cipher.addRound(round);
                JOptionPane.showMessageDialog(null, "Added random substitution round");
            } else if (input == 1) {
                try {
                    int[] mapping = askForMapping(16);
                    round.setSubstitutionMapping(mapping);
                    addRoundLabel("Substitution Round");
                    cipher.addRound(round);
                    JOptionPane.showMessageDialog(null, "Added substitution round");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "An error occurred."
                            + " Please input 16 space-separated integers");
                    e.printStackTrace();
                }
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
        // MODIFIES: this
        // EFFECTS: prompts the user to add a permutation round to the cipher
        public void actionPerformed(ActionEvent evt) {
            PermutationRound round = new PermutationRound(cipher.getBlockSize());
            int input = JOptionPane.showConfirmDialog(null,
                    "Would you like to randomize the permutation round?");

            if (input == 0) {
                round.fillWithRandomPermutation();
                addRoundLabel("Permutation Round");
                cipher.addRound(round);
                JOptionPane.showMessageDialog(null, "Added random permutation round");
            } else if (input == 1) {
                int mappingSize = 8 * cipher.getBlockSize();
                try {
                    int[] mapping = askForMapping(mappingSize);
                    round.setPermutationMapping(mapping);
                    addRoundLabel("Permutation Round");
                    cipher.addRound(round);
                    JOptionPane.showMessageDialog(null, "Added permutation round");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "An error occurred."
                            + " Please input " + mappingSize + " space-separated integers");
                }
            }
        }
    }

    // EFFECTS: presents an input dialog that asks the user for a mapping of the given size
    private int[] askForMapping(int mappingSize) {
        int[] mapping = new int[mappingSize];
        String[] stringMapping = JOptionPane.showInputDialog(null,
                "Mapping:").split(" ");
        for (int i = 0; i < mappingSize; i++) {
            mapping[i] = Integer.parseInt(stringMapping[i]);
        }
        return mapping;
    }

    // EFFECTS: converts the given byte array into a space-separated string
    public String byteArrayToString(Byte[] input) {
        String output = "";
        for (Byte b : input) {
            output += (b & 0xff) + " ";
        }
        return output;
    }
}
