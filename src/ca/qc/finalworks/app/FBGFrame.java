package ca.qc.finalworks.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author final
 */
public class FBGFrame extends JFrame implements MouseInputListener {

    private Dimension frameDimension = Toolkit.getDefaultToolkit().getScreenSize();
    private double frameWidth = frameDimension.width * 0.8;
    private double frameHeight = frameDimension.height * 0.85;
    private double scrollerWidth = frameWidth / 1.6;
    private double scrollerHeight = frameHeight / 1.3;
    private String defaultInstructions = "Welcome to the Fantasy Biome "
            + "Generator!\n\nClick [New Generator] to start a project, or click "
            + "[Open Project] to open a previous project you saved.";
    private String imagePath, stepScript, iconScript = "";
    private int step = 0;
    private int drawSize = 1;
    private int pTemp = -40, eTemp = 30;
    private int batchSize = 200;
    private int counter = 0, jcounter = 0, icounter = 0;
    private BufferedImage biomesImage;

    public enum ButtonPress {
        SKYLINE, SNOWLINE, ALPINE, SUBALPINE, HIGHLANDS, SEALEVEL,
        ERASER, GTEMP, NEXT, SCREENSHOT
    }
    private ButtonPress btnPressed = null;

    private JMenuBar mnuBar = new JMenuBar();
    private JMenu mnuFile = new JMenu("File");
    private JMenuItem mniNew = new JMenuItem("New");
    private JMenuItem mniOpen = new JMenuItem("Open");
    private JMenuItem mniSave = new JMenuItem("Save");
    private JMenuItem mniQuit = new JMenuItem("Quit");
    private JMenu mnuHelp = new JMenu("?");
    private JMenuItem mniGeneralHelp = new JMenuItem("General Help");
    private JMenuItem mniExample = new JMenuItem("Example");
    private JMenuItem mniCredits = new JMenuItem("Credits");

    private JPanel pnlMain = new JPanel(new GridBagLayout());
    private JLabel lblTools = new JLabel("Toolbar");
    private JLabel lblZoomIn = new JLabel("Zoom In");
    private JLabel lblZoomOut = new JLabel("Zoom Out");
    private JLabel lblScreenshot = new JLabel("Screenshot");
    private JLabel lblInstructions = new JLabel("Instructions:");
    private ImageIcon iconSkyLine, iconSnowLine, iconAlpine, iconSubAlpine, iconHighlands, iconSeaLevel;
    private ImageIcon iconEraser, iconGlobalTemp, iconScreenshot, iconNext;
    private JToolBar toolBar;
    private JButton btnSkyLine, btnSnowLine, btnAlpine, btnSubAlpine, btnHighlands, btnSeaLevel;
    private JButton btnEraser, btnGlobalTemp, btnNewGen, btnOpenInView, btnSave;
    private JButton btnScreenshot, btnNext;
    private JPanel pnlSizeList = new JPanel(new BorderLayout());
    private JLabel lblSizeList = new JLabel("Draw Size:");
    private String[] sizes = {"1", "2", "3", "4"};
    private JComboBox cbxSizeList = new JComboBox(sizes);
    private Map mapViewpoint;
    private JTextArea txaInstructions = new JTextArea();
    private JScrollPane scroller;
    private JScrollPane scrollerInstructions = new JScrollPane(txaInstructions);

    public FBGFrame() throws FileNotFoundException, IOException, URISyntaxException {

        setResizable(false);
        setSize((int) frameWidth, (int) frameHeight);
        setLocation((int) (frameHeight / 5), (int) (frameHeight / 15));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Fantasy Biome Generator v1.00");
        setLayout(new BorderLayout());
        ImageIcon img2 = new ImageIcon("res/FBGIcon.png");
        setIconImage(img2.getImage());
        toolBar = new JToolBar(JToolBar.HORIZONTAL);
        iconSkyLine = new ImageIcon("res/iconSkyLine.png");
        iconSnowLine = new ImageIcon("res/iconSnowLine.png");
        iconAlpine = new ImageIcon("res/iconAlpine.png");
        iconSubAlpine = new ImageIcon("res/iconSubAlpine.png");
        iconHighlands = new ImageIcon("res/iconHighlands.png");
        iconSeaLevel = new ImageIcon("res/iconSeaLevel.png");
        iconEraser = new ImageIcon("res/iconEraser.png");
        iconGlobalTemp = new ImageIcon("res/iconGlobalTemp.png");
        iconScreenshot = new ImageIcon("res/iconScreenshot.png");
        iconNext = new ImageIcon("res/iconNext.png");

        createInterface();
        createEvents();
        mapViewpoint.startThread();
        setVisible(true);

        do {

            uiManager(step);
            txaInstructions.setText(stepScript + iconScript);

        } while (step >= 0);

    }

    private void createInterface() throws IOException {

        mnuFile.add(mniNew);
        mnuFile.add(mniOpen);
        mnuFile.add(mniSave);
        mnuFile.addSeparator();
        mnuFile.add(mniQuit);
        mnuBar.add(mnuFile);
        mnuHelp.add(mniGeneralHelp);
        mnuHelp.add(mniExample);
        mnuHelp.add(mniCredits);
        mnuBar.add(mnuHelp);
        setJMenuBar(mnuBar);

        mapViewpoint = new Map();
        mapViewpoint.setBackground(Color.gray);
        mapViewpoint.addMouseMotionListener(this);
        mapViewpoint.addMouseListener(this);
        scroller = new JScrollPane(mapViewpoint);
        scroller.setPreferredSize(new Dimension((int) scrollerWidth, (int) scrollerHeight));
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);

        GridBagConstraints gbc = new GridBagConstraints();

        btnSkyLine = new JButton(iconSkyLine);
        btnSnowLine = new JButton(iconSnowLine);
        btnAlpine = new JButton(iconAlpine);
        btnSubAlpine = new JButton(iconSubAlpine);
        btnHighlands = new JButton(iconHighlands);
        btnSeaLevel = new JButton(iconSeaLevel);
        btnEraser = new JButton(iconEraser);
        btnGlobalTemp = new JButton(iconGlobalTemp);
        btnScreenshot = new JButton(iconScreenshot);
        btnNext = new JButton(iconNext);

        cbxSizeList.setEditable(false);
        cbxSizeList.setSelectedIndex(0);
        pnlSizeList.add(lblSizeList, BorderLayout.NORTH);
        pnlSizeList.add(cbxSizeList, BorderLayout.SOUTH);
        pnlSizeList.setPreferredSize(new Dimension(60, 50));
        pnlSizeList.setMaximumSize(new Dimension(60, 50));
        pnlSizeList.setMinimumSize(new Dimension(60, 50));
        addToolBarComponents(toolBar);

        gbc.gridx = gbc.gridy = 0;
        gbc.anchor = FIRST_LINE_START;
        gbc.insets = new Insets(10, 10, 10, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 3;
        pnlMain.add(scroller, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 0, 0);
        btnNewGen = new JButton("New Generator");
        pnlMain.add(btnNewGen, gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 0, 0);
        btnOpenInView = new JButton("Open Project");
        pnlMain.add(btnOpenInView, gbc);
        gbc.gridx = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 0, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        btnSave = new JButton("Save");
        pnlMain.add(btnSave, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(10, 12, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        pnlMain.add(lblInstructions, gbc);
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.gridwidth = 6;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        txaInstructions.setLineWrap(true);
        txaInstructions.setWrapStyleWord(true);
        txaInstructions.setEditable(false);
        scrollerInstructions.setPreferredSize(new Dimension(100, 100));
        pnlMain.add(scrollerInstructions, gbc);

        add(pnlMain, BorderLayout.CENTER);

    }

    private void addToolBarComponents(JToolBar toolBar) {

        toolBar.add(btnSkyLine);
        toolBar.add(btnSnowLine);
        toolBar.add(btnAlpine);
        toolBar.add(btnSubAlpine);
        toolBar.add(btnHighlands);
        toolBar.add(btnSeaLevel);
        toolBar.add(btnEraser);
        toolBar.add(btnGlobalTemp);
        toolBar.add(btnScreenshot);
        toolBar.add(btnNext);
        JPanel space = new JPanel();
        space.setPreferredSize(new Dimension(5, 0));
        space.setMaximumSize(new Dimension(5, 0));
        space.setMinimumSize(new Dimension(5, 0));
        toolBar.add(space);
        toolBar.add(pnlSizeList);

    }

    private void createEvents() {

        btnSkyLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iconScript = "Sky Line (Cyan) - 4500 to 10 000 meters\n\n"
                        + "The highest altitude range in Earth's frame of "
                        + "reference. Not much lives up there, the air pressure "
                        + "and the temperatures are generally life-threateningly "
                        + "low. For reference, the mount Everest is about 8800 "
                        + "meters high.";
                btnPressed = ButtonPress.SKYLINE;
            }
        });
        btnSnowLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iconScript = "Snow Line (Blue) - 3000 to 4500 meters\n\n"
                        + "";
                btnPressed = ButtonPress.SNOWLINE;
            }
        });
        btnAlpine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iconScript = "Alpine (Magenta) - 2000 to 3000 meters\n\n"
                        + "";
                btnPressed = ButtonPress.ALPINE;
            }
        });
        btnSubAlpine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iconScript = "Subalpine (Red) - 1500 to 2000 meters\n\n"
                        + "";
                btnPressed = ButtonPress.SUBALPINE;
            }
        });
        btnHighlands.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iconScript = "Highlands (Yellow) - 500 to 1500 meters\n\n"
                        + "";
                btnPressed = ButtonPress.HIGHLANDS;
            }
        });
        btnSeaLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iconScript = "Sea Level (Green) - 0 to 500 meters\n\n"
                        + "";
                btnPressed = ButtonPress.SEALEVEL;
            }
        });
        btnEraser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iconScript = "Eraser\n\n"
                        + "Use this tool to erase filled squares if "
                        + "you made a mistake.";
                btnPressed = ButtonPress.ERASER;
            }
        });
        btnGlobalTemp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iconScript = "Global Average Temperatures\n\n"
                        + "Set the global average temperatures of your planet.\n"
                        + "PLay with the values to create planets that are very hot, "
                        + "very cold, or somewhere in between, where life forms can "
                        + "thrive in a more suitable environment!";
                btnPressed = ButtonPress.GTEMP;
                newGlobalTemp();
            }
        });
        btnScreenshot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String fileName = JOptionPane.showInputDialog(null, "Chose a file name\n(Do not include extensions) ", "Save as", JOptionPane.QUESTION_MESSAGE);
                int choice = JOptionPane.showConfirmDialog(null, "Choose a directory in which to save your screenshot.", "Notice", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (choice == 0) {
                    JButton open = new JButton();
                    JFileChooser jfc = new JFileChooser();
                    jfc.setDialogTitle("Choose a directory");
                    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    jfc.setAcceptAllFileFilterUsed(false);
                    if (jfc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
                    }
                    String path = jfc.getSelectedFile().getAbsolutePath();
//                    System.out.println("Screenshot saved at:\n" + path);
                    try {
                        ImageIO.write(biomesImage, "png", new File(path + "\\" + fileName + ".png"));
                        iconScript = "Screenshot successfully saved at:\n" + path;
                    } catch (IOException ex) {
                        Logger.getLogger(FBGFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        });
        btnNewGen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    newGenerator();
                } catch (IOException ex) {
                    Logger.getLogger(FBGFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        btnOpenInView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (step == 0) {
                    try {
                        locateFBGSave();
                        imagePath = mapViewpoint.getSaveImagePath();
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(FBGFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (step == 1) {
                    try {
                        mapViewpoint.createNewFBGSave(imagePath);
                        iconScript = "File saved.";
                    } catch (IOException ex) {
                        Logger.getLogger(FBGFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        mniNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (step == 0) {
                    try {
                        newGenerator();
                    } catch (IOException ex) {
                        Logger.getLogger(FBGFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "This function is disabled for now.", "Notice", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        mniOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (step == 0) {
                    try {
                        locateFBGSave();
                        imagePath = mapViewpoint.getSaveImagePath();
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(FBGFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "This function is disabled for now.", "Notice", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        mniSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (step == 1) {
                    try {
                        mapViewpoint.createNewFBGSave(imagePath);
                        iconScript = "File saved.";
                    } catch (IOException ex) {
                        Logger.getLogger(FBGFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "This function is disabled for now.", "Notice", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        mniQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure about that?",
                        "Exiting Program", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (choice == 0) {
                    System.exit(0);
                }
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure about that?",
                        "Exiting Program", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (choice == 0) {
                    System.exit(0);
                }
            }
        });
        cbxSizeList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawSize = cbxSizeList.getSelectedIndex() + 1;
            }
        });
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (step == 1) {
                    int choice = JOptionPane.showConfirmDialog(null, "Do you want to save a backup file of\nyour map before proceeding?", "Notice", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (choice == 0) {
                        try {
                            mapViewpoint.createNewFBGSave(imagePath);
                        } catch (IOException ex) {
                            Logger.getLogger(FBGFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        step = 2;
                    } else if (choice == 1) {
                        step = 2;
                    }
                }
            }
        });
        mniGeneralHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Not yet implemented.", "Notice", JOptionPane.INFORMATION_MESSAGE);

            }
        });
        mniExample.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Not yet implemented.", "Notice", JOptionPane.INFORMATION_MESSAGE);

            }
        });
        mniCredits.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "                                    -- Credits --\n\n"
                        + "Project developped by myself, Adrien Champagne,\n"
                        + "in my free time, from July to October 2016.\n\n\n"
                        + "Special thanks:\n\n"
                        + " - Alex David                  Help with research.\n"
                        + " - Gabriel Hamelin        Help with debugging.", "Credits", JOptionPane.PLAIN_MESSAGE);

            }
        });

    }

    private void uiManager(int s) {

        switch (s) {
            case 0: {
                stepScript = defaultInstructions;
                btnSkyLine.setEnabled(false);
                btnSnowLine.setEnabled(false);
                btnAlpine.setEnabled(false);
                btnSubAlpine.setEnabled(false);
                btnHighlands.setEnabled(false);
                btnSeaLevel.setEnabled(false);
                btnEraser.setEnabled(false);
                btnGlobalTemp.setEnabled(false);
                btnScreenshot.setEnabled(false);
                btnNext.setEnabled(false);
                cbxSizeList.setEnabled(false);
                btnNewGen.setEnabled(true);
                btnOpenInView.setEnabled(true);
                btnSave.setEnabled(false);
                scroller.setFocusable(false);
                break;
            }
            case 1: {
                stepScript = "Step 1 - Height Mapping\n\n"
                        + "In order to emulate the variations in temperatures "
                        + "and biomes, this generator needs to know where are "
                        + "the high and low points of altitude in the world.\n"
                        + "Use the toolbar at the top of the window to choose "
                        + "which range of altitude fits with which subdivision "
                        + "on the map! Make sure to only fill the subdivisions "
                        + "on the map where there is land, the subdivisions left "
                        + "empty will be considered as water.\n\nAt all times, "
                        + "information about the last button you pressed will "
                        + "be displayed below. Once you are finished, press "
                        + "the right arrow in the toolbar.\n\n"
                        + "----------------------------------------------------"
                        + "----------------------------------------\n\n";
                btnSkyLine.setEnabled(true);
                btnSnowLine.setEnabled(true);
                btnAlpine.setEnabled(true);
                btnSubAlpine.setEnabled(true);
                btnHighlands.setEnabled(true);
                btnSeaLevel.setEnabled(true);
                btnEraser.setEnabled(true);
                btnGlobalTemp.setEnabled(false);
                btnScreenshot.setEnabled(false);
                btnNext.setEnabled(true);
                cbxSizeList.setEnabled(true);
                btnNewGen.setEnabled(false);
                btnOpenInView.setEnabled(false);
                btnSave.setEnabled(true);
                scroller.setFocusable(true);
                break;
            }
            case 2: {
                stepScript = "Step 2 - Global Average Temperatures\n\n"
                        + "It's now time to set the average temperatures "
                        + "for the two poles and the equator of your planet. "
                        + "The default values are set to be the same as Earth's values."
                        + "\nIf you want to end up with "
                        + "generally hotter or colder climates, just edit the values "
                        + "by pressing the globe button in the toolbar!"
                        + "\n\nAt all times, "
                        + "information about the last button you pressed will "
                        + "be displayed below.\n\n"
                        + "----------------------------------------------------"
                        + "----------------------------------------\n\n";
                btnSkyLine.setEnabled(false);
                btnSnowLine.setEnabled(false);
                btnAlpine.setEnabled(false);
                btnSubAlpine.setEnabled(false);
                btnHighlands.setEnabled(false);
                btnSeaLevel.setEnabled(false);
                btnEraser.setEnabled(false);
                btnGlobalTemp.setEnabled(true);
                btnScreenshot.setEnabled(false);
                btnNext.setEnabled(false);
                cbxSizeList.setEnabled(false);
                btnNewGen.setEnabled(false);
                btnOpenInView.setEnabled(false);
                btnSave.setEnabled(false);
                scroller.setFocusable(false);
                break;
            }
            case 3: {
                stepScript = "Step 3 - It's (almost) Done!\n\n"
                        + "Your first map has been generated, I know, it's "
                        + "awesome, don't tell me. Press any square on the map to "
                        + "learn about the different biomes!\n\nIf the result isn't satisfying, "
                        + "you can always enter different polar and equatorial "
                        + "temperature values and generate a different map.\nOnce you are finished and satisfied "
                        + "with your biomes, press the screenshot button to "
                        + "save the full image on your computer!\n\n"
                        + "----------------------------------------------------"
                        + "----------------------------------------\n\n";
                btnSkyLine.setEnabled(false);
                btnSnowLine.setEnabled(false);
                btnAlpine.setEnabled(false);
                btnSubAlpine.setEnabled(false);
                btnHighlands.setEnabled(false);
                btnSeaLevel.setEnabled(false);
                btnEraser.setEnabled(false);
                btnGlobalTemp.setEnabled(true);
                btnScreenshot.setEnabled(true);
                btnNext.setEnabled(false);
                cbxSizeList.setEnabled(false);
                btnNewGen.setEnabled(false);
                btnOpenInView.setEnabled(false);
                btnSave.setEnabled(false);
                scroller.setFocusable(true);
                break;
            }

        }
    }

    public void setNextStep(int nextStep) {
        step = nextStep;
    }

    private void newGenerator() throws IOException {
        int choice = JOptionPane.showConfirmDialog(null, "Choose the base map "
                + "for the generator.\n\nIt's important that your chosen image "
                + "is a map of the entire planet, \nnot only a section of "
                + "it, otherwise the generator will consider it as the \nfull "
                + "world map and the generated biomes won't make sense.\n\n"
                + "The image file should preferably be of rectangle shape, with "
                + "\nwidth:height proportions of around 2:1.\n\nAlso, the higher "
                + "the image resolution, the higher the \naccuracy of the "
                + "generator will be (optimal dimensions should be \nmultiples of 20).", "Notice", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (choice == 0) {
            JButton open = new JButton();
            JFileChooser jfc = new JFileChooser();
            jfc.setDialogTitle("Choose a file");
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "gif", "png");
            jfc.addChoosableFileFilter(filter);
            jfc.setFileFilter(filter);
            if (jfc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
            }
            try {
                imagePath = jfc.getSelectedFile().getAbsolutePath();
//                System.out.println("Image loaded from: \n" + imagePath);
                mapViewpoint.setMapImage(imagePath);
                iconScript = "Image loaded successfully from: \n" + imagePath;
                step = 1;
            } catch (NullPointerException e) {
            }
        }
    }

    private void locateFBGSave() throws IOException, FileNotFoundException, ClassNotFoundException {
        String path;
        int choice = JOptionPane.showConfirmDialog(null, "Choose a \".FBG\" file on your computer", "Notice", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (choice == 0) {
            JButton open = new JButton();
            JFileChooser jfc = new JFileChooser();
            jfc.setDialogTitle("Choose a file");
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("FBG Save Files", "FBG");
            jfc.addChoosableFileFilter(filter);
            jfc.setFileFilter(filter);
            if (jfc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
            }
            try {
                path = jfc.getSelectedFile().getAbsolutePath();
//                System.out.println("Save loaded from: \n" + path);
                String result = mapViewpoint.openFBGSave(path);
                iconScript = "Save loaded successfully from: \n" + path;
                if (result == null) {
                    step = 1;
                } else if ("StreamCorruptedException".equals(result)) {
                    JOptionPane.showMessageDialog(null, "Sadly, your save file has been corrupted.\n\n"
                            + "But I'm sure you didn't manually edit the save\n"
                            + "file with Notepad, you're better than that!", "File Corrupted", JOptionPane.ERROR_MESSAGE);
                } else if ("InvalidClassException".equals(result)) {
                    JOptionPane.showMessageDialog(null, "Sorry, your save file is not compatible with\n"
                            + "the current version of Fantasy Biome Generator. ", "File Outdated", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "The map image could not be found at it's\n"
                            + "original directory:\n\n \"" + result + "\".", "Image Not Found", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NullPointerException e) {
            }
        }
    }

    public void addAltitudeTileToMap(int x, int y, int size) {
        try {
            switch (btnPressed) {
                case SKYLINE:
                    mapViewpoint.addAltitudeTile(Biome.Altitude.SKY, x, y, size);
                    break;
                case SNOWLINE:
                    mapViewpoint.addAltitudeTile(Biome.Altitude.SNO, x, y, size);
                    break;
                case ALPINE:
                    mapViewpoint.addAltitudeTile(Biome.Altitude.ALP, x, y, size);
                    break;
                case SUBALPINE:
                    mapViewpoint.addAltitudeTile(Biome.Altitude.SAL, x, y, size);
                    break;
                case HIGHLANDS:
                    mapViewpoint.addAltitudeTile(Biome.Altitude.HGL, x, y, size);
                    break;
                case SEALEVEL:
                    mapViewpoint.addAltitudeTile(Biome.Altitude.SLV, x, y, size);
            }
        } catch (NullPointerException e) {

        }

    }

    public void newGlobalTemp() {

        JPanel inputsPanel = new JPanel(new GridBagLayout());
        inputsPanel.setPreferredSize(new Dimension(300, 100));
        JLabel lblPolar = new JLabel("Polar temperatures(°C): ");
        JTextField txfPolar = new JTextField(pTemp + "", 7);
        JLabel lblEquator = new JLabel("Equatorial temperatures(°C): ");
        JTextField txfEquator = new JTextField(eTemp + "", 7);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 0, 0);
        gbc.anchor = FIRST_LINE_START;
        inputsPanel.add(lblPolar, gbc);
        gbc.gridx = 1;
        inputsPanel.add(txfPolar, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputsPanel.add(lblEquator, gbc);
        gbc.gridx = 1;
        inputsPanel.add(txfEquator, gbc);

        int choice = JOptionPane.showConfirmDialog(null, inputsPanel, "Edit Global Temperatures", JOptionPane.OK_CANCEL_OPTION);
        if (choice == 0) {
//            System.out.println("P-Temp: " + txfPolar.getText());
//            System.out.println("E-Temp: " + txfEquator.getText());
            int[] peTemp = mapViewpoint.setPolarEquatorTemp(Integer.parseInt(txfPolar.getText()), Integer.parseInt(txfEquator.getText()));
            pTemp = peTemp[0];
            eTemp = peTemp[1];
            int tWidth = mapViewpoint.getTileMapWidth();
            int tHeight = mapViewpoint.getTileMapHeight();
            mapViewpoint.generateBiomes();
            biomesImage = getScreenshot(mapViewpoint);
            step = 3;
        }

    }

    public BufferedImage getScreenshot(Component c) {

        BufferedImage capture = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_RGB);
        c.print(capture.getGraphics());
        return capture;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (step == 1) {
            if (btnPressed == ButtonPress.SKYLINE
                    || btnPressed == ButtonPress.SNOWLINE
                    || btnPressed == ButtonPress.SKYLINE
                    || btnPressed == ButtonPress.ALPINE
                    || btnPressed == ButtonPress.SUBALPINE
                    || btnPressed == ButtonPress.HIGHLANDS
                    || btnPressed == ButtonPress.SEALEVEL) {
                addAltitudeTileToMap(e.getX(), e.getY(), drawSize);
            } else if (btnPressed == ButtonPress.ERASER) {
                mapViewpoint.removeAltitudeTileToMap(e.getX(), e.getY(), drawSize);
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (step == 1) {
            if (btnPressed == ButtonPress.SKYLINE
                    || btnPressed == ButtonPress.SNOWLINE
                    || btnPressed == ButtonPress.SKYLINE
                    || btnPressed == ButtonPress.ALPINE
                    || btnPressed == ButtonPress.SUBALPINE
                    || btnPressed == ButtonPress.HIGHLANDS
                    || btnPressed == ButtonPress.SEALEVEL) {
                addAltitudeTileToMap(e.getX(), e.getY(), drawSize);
            } else if (btnPressed == ButtonPress.ERASER) {
                mapViewpoint.removeAltitudeTileToMap(e.getX(), e.getY(), drawSize);
            }

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (step == 1) {
            if (btnPressed == ButtonPress.SKYLINE
                    || btnPressed == ButtonPress.SNOWLINE
                    || btnPressed == ButtonPress.SKYLINE
                    || btnPressed == ButtonPress.ALPINE
                    || btnPressed == ButtonPress.SUBALPINE
                    || btnPressed == ButtonPress.HIGHLANDS
                    || btnPressed == ButtonPress.SEALEVEL) {
                addAltitudeTileToMap(e.getX(), e.getY(), drawSize);
            } else if (btnPressed == ButtonPress.ERASER) {
                mapViewpoint.removeAltitudeTileToMap(e.getX(), e.getY(), drawSize);
            }

        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}
