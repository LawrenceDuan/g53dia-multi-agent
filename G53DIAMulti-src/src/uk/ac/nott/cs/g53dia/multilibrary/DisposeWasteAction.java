package uk.ac.nott.cs.g53dia.multilibrary;
/**
 * Action that disposes of waste in a well.
 *
 * @author Neil Madden
 */

/*
 * Copyright (c) 2011 Julian Zapppala (jxz@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public class DisposeWasteAction implements Action {
	
    public DisposeWasteAction() { }

    public void execute(Environment env, Tanker tank) throws ActionFailedException {
    	
    	if (!(env.getCell(tank.getPosition()) instanceof Well)) {
	    throw new ActionFailedException("DisposeWaste: Not at Well");
    	}
    	    	
        if (tank.wasteLevel <= 0) {
            throw new ActionFailedException("DisposeWaste: No waste to dispose of");
        } else {
	    tank.wasteLevel = 0;
        }
    }

    public String toString() {
        return "DisposeWaste";
    }
}
