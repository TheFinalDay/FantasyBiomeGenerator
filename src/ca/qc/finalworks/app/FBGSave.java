package ca.qc.finalworks.app;

import java.io.Serializable;
import javax.swing.JPanel;

/**
 *
 * @author final
 */
public class FBGSave implements Serializable{

    private JPanel[][] tileHolder = null;
    private String path;

    public FBGSave() {
    }
    
    public FBGSave(JPanel[][] th, String path) {
        this.tileHolder = th;
        this.path = path;
        
    }
    
    public String getPath(){
        return this.path;
    }
    
    public JPanel[][] getTileHolder(){
        return this.tileHolder;
    }
    
    
    
    
}
