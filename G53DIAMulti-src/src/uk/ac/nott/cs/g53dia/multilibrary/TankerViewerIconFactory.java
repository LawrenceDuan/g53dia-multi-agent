package uk.ac.nott.cs.g53dia.multilibrary;
import javax.swing.Icon;
/**
 * Interface allowing customisation of the {@link TankerViewer} icons used to depict objects in the environment
 * different types of Tankers and cells.
 *
 * @author Neil Madden.
 */
/*
 * Copyright (c) 2003 Stuart Reeves
 * Copyright (c) 2003-2005 Neil Madden (nem@cs.nott.ac.uk).
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
public interface TankerViewerIconFactory {

    /**
     * Select a suitable icon for the given cell.
     *
     * @param cell The cell to select an icon for.
     * @return An icon representing the cell.
     */
    public Icon getIconForCell(Cell cell);

    /**
     * Select an icon for a tanker.
     *
     * @param tanker The tanker to select an icon for.
     * @return An icon representing the tanker.
     */
    public Icon getIconForTanker(Tanker tanker);

}
