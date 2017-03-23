package uk.ac.nott.cs.g53dia.multilibrary;
import java.util.Random;

/**
 * A class representing a waste disposal task
 *
 * @author Julian Zappala
 */

/*
 * Copyright (c) 2011 Julian  Zappala (jxz@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public class Task {
    /**
     * The maximum amount of waste that must be disposed of in a single task
     */
	
    public static final int MAX_AMOUNT = Tanker.MAX_WASTE;
    
    Station station;
    int amount;
    int disposed;
    boolean completed;
		
    Task(Station s, Random r) {
	    this.station = s;
	    completed = false;
	    amount = r.nextInt(MAX_AMOUNT) + 1;
	    disposed = 0;
    }

    /**
     * Get the position of the station from which the waste should be collected
     * 
     */
	
    public Point getStationPosition() {
	return station.getPoint();
    }
    
    /**
     * Get the amount of waste to be disposed of
     * 
     */
	
    public int getWasteAmount() {
	return amount;
    }
	
    /**
     * How much waste must be disposed of to complete the task?
     * 
     */
    public int getWasteRemaining() {
	return amount - disposed;
    }
	
    /**
     * Is this task completed?
     * 
     */
    public boolean isComplete() {
	return disposed >= amount;
    }
	
    protected void setWasteAmount(int a) {
	this.amount = a;
    }
		
    protected void dispose(int d) {
	disposed += d;
	if (isComplete()) {
	    this.station.removeTask();
	}
    }
}
