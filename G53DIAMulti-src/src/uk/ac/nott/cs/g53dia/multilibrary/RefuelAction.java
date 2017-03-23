package uk.ac.nott.cs.g53dia.multilibrary;
/**
 * Action which replenishes the fuel in the tanker.
 *
 * @author Julian Zappala
 */

/*
 * Copyright (c) 2011 Julian Zappala (jxz@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public class RefuelAction implements Action {

    public RefuelAction() {}

    public void execute(Environment env, Tanker tank)
        throws ActionFailedException
    {
    	Cell c = env.getCell(tank.getPosition());
        if (!(c instanceof FuelPump)) {
            throw new ActionFailedException("Refuel: not at fuelpump");
        }
    	if (tank.fuelLevel >= Tanker.MAX_FUEL) {
            // Not critical, but useful for debugging
            throw new ActionFailedException("Refuel: fuel tank is full");
        }
        tank.fuelLevel += FuelPump.FUEL_YIELD;
        if (tank.fuelLevel > Tanker.MAX_FUEL) {
        	tank.fuelLevel = Tanker.MAX_FUEL;
        }
    }

    public String toString() {
        return "Refuel";
    }
}
