package uk.ac.nott.cs.g53dia.multidemo;
import uk.ac.nott.cs.g53dia.multilibrary.*;

/**
 * A simple example Tanker
 * 
 * @author Julian Zappala
 */
/*
 * 
 * Copyright (c) 2011 Julian Zappala
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
public class DemoTanker extends Tanker {
	
    public DemoTanker() { }

    /*
     * The following is a very simple demonstration of how to write a tanker. The
     * code below is very stupid and pretty much randomly picks actions to perform. 
     */
    public Action senseAndAct(Cell[][] view, long timestep) {
 
    	// If fuel tank is low and not at the fuel pump then move towards the fuel pump
        if ((getFuelLevel() <= MAX_FUEL/2) && !(getCurrentCell(view) instanceof FuelPump)) {
            return new MoveTowardsAction(FUEL_PUMP_LOCATION);
        } else {
            // otherwise, move randomly
            return new MoveAction((int)(Math.random() * 8));       	
        }
    }

}
