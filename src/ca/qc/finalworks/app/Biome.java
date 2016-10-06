package ca.qc.finalworks.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author final
 */
public class Biome extends JPanel {

    public enum BiomeType {
        WATER, DES_POL,
        DTR_SPL, MTR_SPL, WTR_SPL, RTR_SPL,
        DES_BRL, DYS_BRL, MFR_BRL, WFR_BRL, RFR_BRL,
        DES_MTP, DSS_MTP, STP_MTP, MFR_MTP, WFR_MTP, RFR_MTP,
        DES_STR, DSS_STR, TSW_STR, DFR_STR, MFR_STR, WFR_STR, RFR_STR,
        DES_TRO, DSS_TRO, TWL_TRO, VDF_TRO, DFR_TRO, MFR_TRO, WFR_TRO, RFR_TRO
    }

    public enum TemperatureRange {
        POL, SPL, BRL, MTP, STR, TRO
    }

    public enum Altitude {
        SKY, SNO, ALP, SAL, HGL, SLV
    }

    public enum Humidity {
        SPH, PHM, HMD, SBH, SMA, ARD, PAR, SPA
    }

    private BiomeType biomeType = null;
    private static BufferedImage tileset;
    private static BufferedImage testTile; 
    private TemperatureRange tRange = null;
    private double averageAnnualTemp = 0f;
    private Altitude altitude = null;
    private Humidity humidity = null;
    private int xPosition;
    private int yPosition;

    public Biome(int x, int y) {
        
        this.xPosition = x;
        this.yPosition = y;
    }
    
    public static void loadImages() throws IOException{
        Biome.tileset = ImageIO.read(new File("res/biome_tileset.png"));
        Biome.testTile = ImageIO.read(new File("res/testTile.png"));
    }

    public void setAltitude(Altitude alt) {
        this.altitude = alt;
    }

    public Altitude getAltitude() {
        return this.altitude;
    }

    public void setAltitudeNull() {
        this.altitude = null;
        this.setOpaque(false);
    }

    public void setTemperatureRange(TemperatureRange tr) {
        this.tRange = tr;
    }

    public TemperatureRange getTemperatureRange() {
        return this.tRange;
    }

    public Humidity getHumidity() {
        return this.humidity;
    }

    public void setHumidity(Humidity h) {
        this.humidity = h;
    }

    public void setBiomeType(BiomeType bt) {
        this.biomeType = bt;
    }

    public void setAverageAnnualTemp(double avarageAnnualTemp) {
        this.averageAnnualTemp = avarageAnnualTemp;
    }

    public double getAverageAnnualTemp() {
        return averageAnnualTemp;
    }

    public boolean isTileEmpty() {
        return (this.altitude == null);
    }

    public boolean isSameAltitude(Altitude alt) {
        return (this.altitude == alt);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (biomeType != null) {

            Dimension tileDim = new Dimension(20, 20);
            BufferedImage tile;
            
            switch (biomeType) {
                case WATER:
                    tile = tileset.getSubimage(tileDim.width, 0, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case DES_POL:
                    tile = tileset.getSubimage(0, 0, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case DTR_SPL:
                    tile = tileset.getSubimage(0, tileDim.height, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case MTR_SPL:
                    tile = tileset.getSubimage(tileDim.width, tileDim.height, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case WTR_SPL:
                    tile = tileset.getSubimage(tileDim.width*2, tileDim.height, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case RTR_SPL:
                    tile = tileset.getSubimage(tileDim.width*3, tileDim.height, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case DES_BRL:
                    tile = tileset.getSubimage(0, tileDim.height*2, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case DYS_BRL:
                    tile = tileset.getSubimage(tileDim.width, tileDim.height*2, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case MFR_BRL:
                    tile = tileset.getSubimage(tileDim.width*2, tileDim.height*2, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case WFR_BRL:
                    tile = tileset.getSubimage(tileDim.width*3, tileDim.height*2, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case RFR_BRL:
                    tile = tileset.getSubimage(tileDim.width*4, tileDim.height*2, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case DES_MTP:
                    tile = tileset.getSubimage(0, tileDim.height*3, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case DSS_MTP:
                    tile = tileset.getSubimage(tileDim.width, tileDim.height*3, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case STP_MTP:
                    tile = tileset.getSubimage(tileDim.width*2, tileDim.height*3, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case MFR_MTP:
                    tile = tileset.getSubimage(tileDim.width*3, tileDim.height*3, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case WFR_MTP:
                    tile = tileset.getSubimage(tileDim.width*4, tileDim.height*3, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case RFR_MTP:
                    tile = tileset.getSubimage(tileDim.width*5, tileDim.height*3, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case DES_STR:
                    tile = tileset.getSubimage(0, tileDim.height*4, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case DSS_STR:
                    tile = tileset.getSubimage(tileDim.width, tileDim.height*4, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case TSW_STR:
                    tile = tileset.getSubimage(tileDim.width*2, tileDim.height*4, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case DFR_STR:
                    tile = tileset.getSubimage(tileDim.width*3, tileDim.height*4, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case MFR_STR:
                    tile = tileset.getSubimage(tileDim.width*4, tileDim.height*4, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case WFR_STR:
                    tile = tileset.getSubimage(tileDim.width*5, tileDim.height*4, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case RFR_STR:
                    tile = tileset.getSubimage(tileDim.width*6, tileDim.height*4, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case DES_TRO:
                    tile = tileset.getSubimage(0, tileDim.height*5, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case DSS_TRO:
                    tile = tileset.getSubimage(tileDim.width, tileDim.height*5, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case TWL_TRO:
                    tile = tileset.getSubimage(tileDim.width*2, tileDim.height*5, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case VDF_TRO:
                    tile = tileset.getSubimage(tileDim.width*3, tileDim.height*5, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case DFR_TRO:
                    tile = tileset.getSubimage(tileDim.width*4, tileDim.height*5, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case MFR_TRO:
                    tile = tileset.getSubimage(tileDim.width*5, tileDim.height*5, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case WFR_TRO:
                    tile = tileset.getSubimage(tileDim.width*6, tileDim.height*5, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
                    break;
                case RFR_TRO:
                    tile = tileset.getSubimage(tileDim.width*7, tileDim.height*5, tileDim.width, tileDim.height);
                    g.drawImage(tile, 0, 0, this);
            }
        } else if (altitude != null) {
            try {
                switch (this.altitude) {
                    case SKY:
                        setBackground(Color.cyan);
                        setOpaque(true);
                        break;
                    case SNO:
                        setBackground(Color.blue);
                        setOpaque(true);
                        break;
                    case ALP:
                        setBackground(Color.magenta);
                        setOpaque(true);
                        break;
                    case SAL:
                        setBackground(Color.red);
                        setOpaque(true);
                        break;
                    case HGL:
                        setBackground(Color.yellow);
                        setOpaque(true);
                        break;
                    case SLV:
                        setBackground(Color.green);
                        setOpaque(true);
                        break;
                }
            } catch (NullPointerException e) {

            }
        }

    }

}
