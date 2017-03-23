package uk.ac.nott.cs.g53dia.multilibrary;

/**
 * A environment cell which contains nothing.
 * <p>
 * Empty cells can be replaced by a fuelpump.
 *
 * @author Brian Logan
 */

/*
 * Copyright (c) 2010 Brian Logan (bsl@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public class EmptyCell extends DefaultCell {
	
	EmptyCell(Point pos) {
		super(pos);
	}
	
}
