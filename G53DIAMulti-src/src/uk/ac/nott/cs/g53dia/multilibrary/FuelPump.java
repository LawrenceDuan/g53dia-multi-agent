package uk.ac.nott.cs.g53dia.multilibrary;
/**
 * A refuelling location.
 *
 * @author Julian Zappala
 * 
  */
/*
 * Copyright (c) 2005 Julian Zappala (jxz@cs.nott.ac.uk).
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
public class FuelPump extends DefaultCell {
     
    FuelPump(Point point) {
    	super(point);
     }

    public final static int FUEL_YIELD = Tanker.MAX_FUEL;
}
