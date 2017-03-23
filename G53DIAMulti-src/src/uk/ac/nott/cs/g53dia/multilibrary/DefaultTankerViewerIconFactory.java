package uk.ac.nott.cs.g53dia.multilibrary;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * A default implementation of the TankerViewerIconFactory interface.
 * 
 * @author Neil Madden
 */

/*
 * Copyright (c) 2009 Neil Madden.
 * Copyright (c) 2010 University of Nottingham.
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public class DefaultTankerViewerIconFactory implements TankerViewerIconFactory {
    static ImageIcon tankerIcon;
    static ImageIcon fuelpumpIcon;
    static ImageIcon wellIcon;
    static ImageIcon stationIcon;
    static ImageIcon stationWithTaskIcon;
    
    static {
        // Pre-load the images
        tankerIcon     = createImageIcon("images/tanker.png");
        fuelpumpIcon    = createImageIcon("images/fuelpump.png");
        wellIcon  = createImageIcon("images/well.png");
        stationIcon = createImageIcon("images/station.png");
        stationWithTaskIcon = createImageIcon("images/station_withtask.png");
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL img = DefaultTankerViewerIconFactory.class.getResource(path);
        if (img != null) {
            return new ImageIcon(img);
        } else {
            System.err.println("Couldn't load image: " + path);
            return null;
        }
    }

    public Icon getIconForCell(Cell cell) { 
        if (cell == null) {
            return null;
        } else if (cell instanceof Well) {
            return wellIcon;
        } else if (cell instanceof FuelPump) {
            return fuelpumpIcon;
        } else if (cell instanceof Station) {
        	if (((Station)cell).getTask()==null) {
        		return stationIcon;
        	} else {
        		return stationWithTaskIcon;
        	}
        	
        } else {
            return null;
        }
    }

    public Icon getIconForTanker(Tanker tanker) { return tankerIcon; }

}
