package uk.ac.nott.cs.g53dia.multilibrary;

/**
 * An abstract implementation of the Cell interface.
 *
 * @author Julian Zappalla
 */

/*
 * Copyright (c) 2009 Julian Zappala (jxz@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public abstract class DefaultCell implements Cell {
	
	private Point point;
	
	DefaultCell(Point point) {
		this.point = point;
	}
	
	public Point getPoint() {
		return this.point;
	}
	
}
