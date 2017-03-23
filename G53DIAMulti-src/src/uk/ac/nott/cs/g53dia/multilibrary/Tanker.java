package uk.ac.nott.cs.g53dia.multilibrary;

/**
 * An abstract base class for Tankers in the standard {@link Environment}.
 *
 * @author Julian Zappala
 */

/*
 * Copyright (c) 2003 Stuart Reeves
 * Copyright (c) 2003-2005 Neil Madden (nem@cs.nott.ac.uk).
 * Copyright (c) 2011 Julian Zappala (jxz@cs.nott.ac.uk).
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public abstract class Tanker {
    // Fields used by the environment
    /**
     * The initial level of fuel in the tanker
     */
    int fuelLevel = MAX_FUEL;
    
    /**
     * The initial level of waste in the tanker
     */
    int wasteLevel = 0;
  
     /**
     * The total amount of waste disposed of 
     */
     int wasteDisposed = 0;
    
    /**
     * The maximum amount of fuel a Tanker can have.
     * Note: this is assumed to be an even number.
     */
    public final static int MAX_FUEL = 100;
    
    /**
     * The maximum amount of waste a Tanker can have.
     */
    public final static int MAX_WASTE = 1000;

    /**
     * The distance a Tanker can "see".
     */
    public final static int VIEW_RANGE = 25;

    /**
     * Location of central refuelling point
     */
    public final static Point FUEL_PUMP_LOCATION = new Point(0,0);

    /**
     * The Tanker's current position in the environment.
     */
    Point position = new Point(0,0); // Default to origin

    /**
     * Sub-classes must implement this method to provide the "brains" for the
     * Tanker.
     * @param view the cells the Tanker can currently see
     * @param timestep The current timestep
     * @return an action to perform
     */
    public abstract Action senseAndAct(Cell[][] view, long timestep);

    /**
     * Get the Tanker's current position in the environment.
     */
    public Point getPosition() {
        return (Point)position.clone();
    }
        
    /**
     * Get the cell currently occupied by the Tanker.
     * @param view the cells the Tanker can currently see
     * @return a reference to the cell currently occupied by this Tanker
     */
    public Cell getCurrentCell(Cell[][] view) {
    	return view[VIEW_RANGE][VIEW_RANGE];
    }

    /**
     * Use fuel - used by move actions/
     */
    public void useFuel(int a) throws ActionFailedException {
	if (fuelLevel <= 0) {
	    throw new OutOfFuelException("No fuel");
	}
	if (a <= 0) {
	    throw new ActionFailedException("Tanker: Fuel use must be positive");
	}
    	fuelLevel -= a;
    }

    /**
     * How much fuel does this tanker have?
     */
    public int getFuelLevel() {
        return fuelLevel;
    }

    /**
     * The amount of waste the the tanker is currently carrying.
     * 
     */
    public int getWasteLevel() {
    	return wasteLevel;
    }
  
    /**
     * The amount of additional waste the tanker can carry.
     * 
     */
    public int getWasteCapacity() {
    	return MAX_WASTE - wasteLevel;
    }

    /**
     * Get the Tanker's current score
     * @return the Tanker's score
     */
    // This needs to be public to allow logging by the test harness
    public int getScore() {
    	 return wasteDisposed;
     }
     
}
