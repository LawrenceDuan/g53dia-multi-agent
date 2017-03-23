package uk.ac.nott.cs.g53dia.multilibrary;
/**
 * Interface allowing the generation of new cells when the environment is resized.
 * 
 * @author Neil Madden
 */

/*
 * Copyright (c) 2005 Neil Madden
 * Copyright (c) 2010 University of Nottingham
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public interface CellFactory {
    /**
     * Generate a grid of cells for the environment. The grid should extend
     * from the point given (x,y) to (x+size,y+size). The method
     * Environment.createCell(Point,Cell) should be invoked to create the cells.
     *
     * @param env environment to which the cell is to be added
     * @param pos position of the new cell
     */
    public void generateCell(Environment env, Point pos);
}
