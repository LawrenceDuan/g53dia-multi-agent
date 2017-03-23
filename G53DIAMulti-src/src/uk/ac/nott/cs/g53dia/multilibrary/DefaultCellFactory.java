package uk.ac.nott.cs.g53dia.multilibrary;
import java.util.Random;

/**
 * A default CellFactory which populates the environment with stations, wells and refuelling points.
 * 
 * @author Neil Madden
 */

/*
 * Copyright (c) 2005 Neil Madden.
 * Copyright (c) 2010 University of Nottingham.
 * Copyright (c) 2011 Julian Zappala (jxz@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public class DefaultCellFactory implements CellFactory {
	/**
     * Default station density
     */
    final static double DEFAULT_STATION_DENSITY = 0.003;

    /**
     * Default well density.
     */
    final static double DEFAULT_WELL_DENSITY = 0.0015;
    
     /**
     * Default refuelling point density
     */
    final static double DEFAULT_FUELPUMP_DENSITY = 0.0004;
    
    Random r;
    
    /**
     * Create a DefaultCellFactory with a specified random number generator
     * This is mostly useful for analysis and debugging.
     * @param r random number generator
     */
    public DefaultCellFactory(Random r) { this.r = r; }
    
    /**
     * Create a DefaultCellFactory.
     * 
     */
    public DefaultCellFactory() { this.r = new Random(); }

    /**
     * Create new cells and wells in the environment.
     * @param env environment to which the cell is to be added
     * @param pos position of the new cell
     */

    public void generateCell(Environment env, Point pos) {
    	if (pos.x==0 & pos.y==0) {
    		env.putCell(new FuelPump(pos));
    	} else if (r.nextDouble() < DEFAULT_FUELPUMP_DENSITY) {
    		env.putCell(new FuelPump(pos));
    	} else if (r.nextDouble() < DEFAULT_WELL_DENSITY) {
    		env.putCell(new Well(pos));
    	} else if (r.nextDouble() < DEFAULT_STATION_DENSITY) {
    		env.putCell(new Station(pos, r));
    		env.stations.add((Station)env.getCell(pos));
    	} else {
        	env.putCell(new EmptyCell(pos));
        }
    }
}
