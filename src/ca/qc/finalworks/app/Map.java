package ca.qc.finalworks.app;

import ca.qc.finalworks.app.Biome.BiomeType;
import ca.qc.finalworks.app.Biome.Humidity;
import ca.qc.finalworks.app.Biome.TemperatureRange;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author final
 */
public class Map extends JLayeredPane {

    private BufferedImage map;
    private Dimension mapDimension;
    private Dimension tileDim;
    private JLabel lblMap;
    private JLabel lblTest;
    private JPanel pnlMap;
    private JPanel pnlTiles;
    private JPanel[][] tileHolder;
    private Biome[][] biomeTiles;
    private int tileMapWidth;
    private int tileMapHeight;
    private double lattitudeSubConstant;
    private double avgPolarTemp, avgEquatorTemp;
    private String saveImagePath;
    private Thread mainthread = new Thread() {
        @Override
        public void run() {

            while (true) {

                update();
                invalidate();
                repaint();

            }
        }
    };

    public Map() {
        pnlMap = new JPanel(new BorderLayout());

    }

    public void update() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {

        }
        revalidate();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    public void setMapImage(String newPath) throws IOException {
        setBackground(Color.blue);
        map = ImageIO.read(new File(newPath));
        mapDimension = new Dimension(map.getWidth(), map.getHeight());
        lblMap = new JLabel(new ImageIcon(map));
        pnlMap.add(lblMap);
        lblMap.setPreferredSize(mapDimension);
        setPreferredSize(mapDimension);
        pnlMap.setBounds(0, 0, mapDimension.width, mapDimension.height);
        add(pnlMap, 1);
        tileHolder = createTileHolder(map);
        pnlTiles = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        for (int i = 0; i < tileMapWidth; i++) {
            for (int j = 0; j < tileMapHeight; j++) {
                gbc.gridy = j;
                gbc.gridx = i;
                if (i == tileMapWidth - 1 || j == tileMapHeight - 1) {
                    gbc.weightx = gbc.weighty = 1;
                }
                pnlTiles.add(tileHolder[i][j], gbc);
            }
        }
        pnlTiles.setOpaque(false);
        pnlTiles.setBounds(0, 0, mapDimension.width, mapDimension.height);
        add(pnlTiles, 0);

    }

    public String openFBGSave(String savePath) throws FileNotFoundException, IOException, ClassNotFoundException {
        int result = 0;
        FileInputStream fis = new FileInputStream(savePath);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(fis);
        } catch (StreamCorruptedException e) {
            result = 1;
        }
        if (result != 1) {
            FBGSave openedSave = null;
            try {
                openedSave = (FBGSave) ois.readObject();
            } catch (InvalidClassException e) {
                result = 3;
            }
            if (result != 3) {
                tileHolder = openedSave.getTileHolder();
                this.saveImagePath = openedSave.getPath();
//                System.out.println("save's image path:\n" + openedSave.getPath());
                try {
                    map = ImageIO.read(new File(openedSave.getPath()));
                } catch (IIOException e) {
                    result = 2;
                }
                switch (result) {
                    case 0:
                        mapDimension = new Dimension(map.getWidth(), map.getHeight());
                        lblMap = new JLabel(new ImageIcon(map));
                        pnlMap.add(lblMap);
                        setPreferredSize(mapDimension);
                        pnlMap.setBounds(0, 0, mapDimension.width, mapDimension.height);
                        add(pnlMap, 1);
                        tileDim = new Dimension(20, 20);
                        tileMapWidth = map.getWidth() / tileDim.width;
                        tileMapHeight = map.getHeight() / tileDim.height;
                        lattitudeSubConstant = mapDimension.height / 18;
                        for (int i = 0; i < tileMapWidth; i++) {
                            for (int j = 0; j < tileMapHeight; j++) {
                                tileHolder[i][j].setPreferredSize(tileDim);
                                tileHolder[i][j].setOpaque(false);
                                tileHolder[i][j].setBorder(BorderFactory.createLineBorder(Color.gray));
                                ((Biome) tileHolder[i][j].getComponentAt(5, 5)).setPreferredSize(tileDim);
                                ((Biome) tileHolder[i][j].getComponentAt(5, 5)).setOpaque(false);
                            }
                        }
                        pnlTiles = new JPanel(new GridBagLayout());
                        GridBagConstraints gbc = new GridBagConstraints();
                        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
                        for (int i = 0; i < tileMapWidth; i++) {
                            for (int j = 0; j < tileMapHeight; j++) {
                                gbc.gridy = j;
                                gbc.gridx = i;
                                if (i == tileMapWidth - 1 || j == tileMapHeight - 1) {
                                    gbc.weightx = gbc.weighty = 1;
                                }
                                pnlTiles.add(tileHolder[i][j], gbc);
                            }
                        }
                        pnlTiles.setOpaque(false);
                        pnlTiles.setBounds(0, 0, mapDimension.width, mapDimension.height);
                        add(pnlTiles, 0);
                        return null;
                    case 2:
                        return openedSave.getPath();
                    default:
                        return null;
                }
            } else {
                return "InvalidClassException";
            }
        } else {
            return "StreamCorruptedException";
        }
    }

    public void createNewFBGSave(String imgPath) throws FileNotFoundException, IOException {
        FBGSave newSave = new FBGSave(tileHolder, imgPath);
        String fileName = JOptionPane.showInputDialog(null, "Chose a file name\n(Do not include extensions) ", "Save as", JOptionPane.QUESTION_MESSAGE);
        int choice = JOptionPane.showConfirmDialog(null, "Choose a directory in which to save your project.", "Notice", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (choice == 0) {
            JButton open = new JButton();
            JFileChooser jfc = new JFileChooser();
            jfc.setDialogTitle("Choose a directory");
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jfc.setAcceptAllFileFilterUsed(false);
            if (jfc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
            }
            String path = jfc.getSelectedFile().getAbsolutePath();
//            System.out.println("File saved at:\n" + path);
            FileOutputStream fos = new FileOutputStream(path + "/" + fileName + ".FBG");
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(newSave);
                oos.flush();
            }
        }

    }

    public String getSaveImagePath() {
        return saveImagePath;
    }

    public void setSaveImagePath(String sp) {
        this.saveImagePath = sp;
    }

    public void startThread() {
        this.mainthread.start();
    }

    public int getTileMapWidth() {
        return tileMapWidth;
    }

    public int getTileMapHeight() {
        return tileMapHeight;
    }

    public void addAltitudeTile(Biome.Altitude altitude, int x, int y, int size) {
        int thx = x / tileDim.width;
        int thy = y / tileDim.height;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                try {
                    if (((Biome) tileHolder[thx + i][thy + j].getComponentAt(5, 5)).isTileEmpty()
                            || !(((Biome) tileHolder[thx + i][thy + j].getComponentAt(5, 5)).isSameAltitude(altitude))) {
                        setNewBiomeAltitude(altitude, thx + i, thy + j);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
    }

    void addTestLabel() {
        lblTest = new JLabel("test");
        pnlTiles.add(lblTest);

    }

    private JPanel[][] createTileHolder(BufferedImage map) {
        tileMapWidth = map.getWidth() / 20;
        tileMapHeight = map.getHeight() / 20;
        tileDim = new Dimension(20, 20);
        lattitudeSubConstant = mapDimension.height / 18;
        JPanel[][] th = new JPanel[tileMapWidth][tileMapHeight];
        biomeTiles = new Biome[tileMapWidth][tileMapHeight];
        for (int i = 0; i < tileMapWidth; i++) {
            for (int j = 0; j < tileMapHeight; j++) {
                th[i][j] = new JPanel(new BorderLayout());
                th[i][j].setPreferredSize(tileDim);
                th[i][j].setOpaque(false);
                th[i][j].setBorder(BorderFactory.createLineBorder(Color.gray));
                biomeTiles[i][j] = new Biome(i, j);
                biomeTiles[i][j].setPreferredSize(tileDim);
                biomeTiles[i][j].setOpaque(false);
                th[i][j].add(biomeTiles[i][j]);
            }
        }
        return th;
    }

    public int[] setPolarEquatorTemp(int apt, int aet) {
        this.avgPolarTemp = apt;
        this.avgEquatorTemp = aet;
        int[] newPETemp = {apt, aet};
        return newPETemp;
    }

    private void setNewBiomeAltitude(Biome.Altitude altitude, int x, int y) {

        ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setAltitude(altitude);

    }

    public void removeAltitudeTileToMap(int x, int y, int size) {
        int thx = x / tileDim.width;
        int thy = y / tileDim.height;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                try {
                    ((Biome) tileHolder[thx + i][thy + j].getComponentAt(5, 5)).setAltitudeNull();
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }

    }

    public void generateBiomes() {

        try {
            Biome.loadImages();
        } catch (IOException ex) {
            Logger.getLogger(FBGFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int j = 0; j < tileMapHeight; j++) {

            Humidity hdt = null;
            double fLat;
            double fLatTemp = (((j + 1) * tileDim.height) - 10) / lattitudeSubConstant;
            if (fLatTemp < 9) {
                fLat = 9 - fLatTemp;
            } else {
                fLat = fLatTemp - 9;
            }

            if (fLat < 9 && fLat >= 8.5) {
                hdt = Humidity.SPA;
            } else if (fLat < 8.5 && fLat >= 8) {
                hdt = Humidity.PAR;
            } else if (fLat < 8 && fLat >= 7.5) {
                hdt = Humidity.ARD;
            } else if (fLat < 7.5 && fLat >= 7) {
                hdt = Humidity.SMA;
            } else if (fLat < 7 && fLat >= 6.5) {
                hdt = Humidity.SBH;
            } else if (fLat < 6.5 && fLat >= 6) {
                hdt = Humidity.HMD;
            } else if (fLat < 6 && fLat >= 5.5) {
                hdt = Humidity.PHM;
            } else if (fLat < 5.5 && fLat >= 5) {
                hdt = Humidity.HMD;
            } else if (fLat < 5 && fLat >= 4.5) {
                hdt = Humidity.SMA;
            } else if (fLat < 4.5 && fLat >= 4) {
                hdt = Humidity.ARD;
            } else if (fLat < 4 && fLat >= 3.5) {
                hdt = Humidity.PAR;
            } else if (fLat < 3.5 && fLat >= 3) {
                hdt = Humidity.SPA;
            } else if (fLat < 3 && fLat >= 2.5) {
                hdt = Humidity.PAR;
            } else if (fLat < 2.5 && fLat >= 2) {
                hdt = Humidity.ARD;
            } else if (fLat < 2 && fLat >= 1.5) {
                hdt = Humidity.SBH;
            } else if (fLat < 1.5 && fLat >= 1) {
                hdt = Humidity.HMD;
            } else if (fLat < 1 && fLat >= 0.5) {
                hdt = Humidity.PHM;
            } else if (fLat < 0.5 && fLat >= 0) {
                hdt = Humidity.SPH;
            }

            for (int i = 0; i < tileMapWidth; i++) {
                
                try {

                    ((Biome) tileHolder[i][j].getComponentAt(5, 5)).setHumidity(hdt);

                    double tLat;
                    double tLatTemp = ((avgEquatorTemp * ((double) 7 / 3)) * Math.cos(toRadians(fLat * 10))) + avgPolarTemp;
                    try {
                        switch (((Biome) tileHolder[i][j].getComponentAt(5, 5)).getAltitude()) {
                            case SKY:
                                tLat = tLatTemp - 15;
                                break;
                            case SNO:
                                tLat = tLatTemp - 8;
                                break;
                            case ALP:
                                tLat = tLatTemp - 3;
                                break;
                            case SAL:
                                tLat = tLatTemp;
                                break;
                            case HGL:
                                tLat = tLatTemp + 8;
                                break;
                            case SLV:
                                tLat = tLatTemp + 3;
                                break;
                            default:
                                tLat = 0;
                        }
                    } catch (NullPointerException e) {
                        tLat = 1000;
                    }

                    if (tLat <= -30 || (tLat <= -20 && tLat > -30)) {
                        ((Biome) tileHolder[i][j].getComponentAt(5, 5)).setTemperatureRange(TemperatureRange.POL);
                    } else if (tLat <= -10 && tLat > -20) {
                        ((Biome) tileHolder[i][j].getComponentAt(5, 5)).setTemperatureRange(TemperatureRange.SPL);
                    } else if (tLat <= 0 && tLat > -10) {
                        ((Biome) tileHolder[i][j].getComponentAt(5, 5)).setTemperatureRange(TemperatureRange.BRL);
                    } else if (tLat <= 10 && tLat > 0) {
                        ((Biome) tileHolder[i][j].getComponentAt(5, 5)).setTemperatureRange(TemperatureRange.MTP);
                    } else if (tLat <= 20 && tLat > 10) {
                        ((Biome) tileHolder[i][j].getComponentAt(5, 5)).setTemperatureRange(TemperatureRange.STR);
                    } else if ((tLat <= 30 && tLat > 20) || tLat > 30) {
                        ((Biome) tileHolder[i][j].getComponentAt(5, 5)).setTemperatureRange(TemperatureRange.TRO);
                    } else {
//                        System.out.println("Something weird?");
                    }

                    if (tLat == 1000) {
                        ((Biome) tileHolder[i][j].getComponentAt(5, 5)).setBiomeType(BiomeType.WATER);

                    } else {
                        ((Biome) tileHolder[i][j].getComponentAt(5, 5)).setAverageAnnualTemp(tLat);
                        determineBiome(i, j);

                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    i = 0;
                    if (j >= tileMapHeight && i >= tileMapWidth) {
                        i = tileMapWidth;
                        j = tileMapHeight;
                    }
//                    System.out.println("line done");
                }
            }
        }
    }

    private double toRadians(double angle) {
        return angle * (Math.PI / 180);
    }

    private void determineBiome(int x, int y) {

        Humidity h = ((Biome) tileHolder[x][y].getComponentAt(5, 5)).getHumidity();
        TemperatureRange tr = ((Biome) tileHolder[x][y].getComponentAt(5, 5)).getTemperatureRange();
        boolean validated;
        do {
            validated = false;
            switch (tr) {
                case POL:
                    if (h == Humidity.SPA || h == Humidity.PAR || h == Humidity.ARD) {
                        ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.DES_POL);
                        validated = true;
                    }
                    break;
                case SPL:
                    switch (h) {
                        case SPA:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.DTR_SPL);
                            validated = true;
                            break;
                        case PAR:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.MTR_SPL);
                            validated = true;
                            break;
                        case ARD:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.WTR_SPL);
                            validated = true;
                            break;
                        case SMA:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.RTR_SPL);
                            validated = true;
                    }
                    break;
                case BRL:
                    switch (h) {
                        case SPA:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.DES_BRL);
                            validated = true;
                            break;
                        case PAR:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.DYS_BRL);
                            validated = true;
                            break;
                        case ARD:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.MFR_BRL);
                            validated = true;
                            break;
                        case SMA:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.WFR_BRL);
                            validated = true;
                            break;
                        case SBH:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.RFR_BRL);
                            validated = true;
                    }
                    break;
                case MTP:
                    switch (h) {
                        case SPA:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.DES_MTP);
                            validated = true;
                            break;
                        case PAR:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.DSS_MTP);
                            validated = true;
                            break;
                        case ARD:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.STP_MTP);
                            validated = true;
                            break;
                        case SMA:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.MFR_MTP);
                            validated = true;
                            break;
                        case SBH:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.WFR_MTP);
                            validated = true;
                            break;
                        case HMD:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.RFR_MTP);
                            validated = true;
                    }
                    break;
                case STR:
                    switch (h) {
                        case SPA:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.DES_STR);
                            validated = true;
                            break;
                        case PAR:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.DSS_STR);
                            validated = true;
                            break;
                        case ARD:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.TSW_STR);
                            validated = true;
                            break;
                        case SMA:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.DFR_STR);
                            validated = true;
                            break;
                        case SBH:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.MFR_STR);
                            validated = true;
                            break;
                        case HMD:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.WFR_STR);
                            validated = true;
                            break;
                        case PHM:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.RFR_STR);
                            validated = true;
                    }
                    break;
                case TRO:
                    switch (h) {
                        case SPA:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.DES_TRO);
                            validated = true;
                            break;
                        case PAR:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.DSS_TRO);
                            validated = true;
                            break;
                        case ARD:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.TWL_TRO);
                            validated = true;
                            break;
                        case SMA:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.VDF_TRO);
                            validated = true;
                            break;
                        case SBH:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.DFR_TRO);
                            validated = true;
                            break;
                        case HMD:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.MFR_TRO);
                            validated = true;
                            break;
                        case PHM:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.WFR_TRO);
                            validated = true;
                            break;
                        case SPH:
                            ((Biome) tileHolder[x][y].getComponentAt(5, 5)).setBiomeType(BiomeType.RFR_TRO);
                            validated = true;
                    }
            }

            if (!validated) {

                switch (h) {
                    case SPH:
                        h = Humidity.PHM;
                        break;
                    case PHM:
                        h = Humidity.HMD;
                        break;
                    case HMD:
                        h = Humidity.SBH;
                        break;
                    case SBH:
                        h = Humidity.SMA;
                        break;
                    case SMA:
                        h = Humidity.ARD;
                }
            }
        } while (!validated);

    }

    public void drawImageOnly(BufferedImage biomesImage, int step, double sWidth, double sHeight) {
        pnlMap.setBackground(Color.cyan);
        pnlTiles.setBackground(Color.yellow);
        lblMap.setBackground(Color.red);
        if (step == 2) {
            remove(pnlTiles);
        }
        if (sWidth > map.getWidth() || sHeight > map.getHeight()) {
            biomesImage = biomesImage.getSubimage(0, 0, map.getWidth(), map.getHeight());
        }
        lblMap.setIcon(new ImageIcon(biomesImage));
        lblMap.setOpaque(true);
    }
}
