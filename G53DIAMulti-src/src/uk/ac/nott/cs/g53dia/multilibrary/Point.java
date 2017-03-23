package uk.ac.nott.cs.g53dia.multilibrary;
/**
 * A 2D position in the Environment.
 * 
 * @author Neil Madden
 */

/*
 * Copyright (c) 2005 Neil Madden.
 * Copyright (c) 2010 University of Nottingham.
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public class Point implements Cloneable {
    volatile int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object o) {
        Point p = (Point)o;
        if (p==null) return false;
        return (p.x == x) && (p.y == y);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public Object clone() {
        return new Point(x,y);
    }

    /**
     * Override hashCode to make sure identical points produce identical
     * hashes.
     */
    public int hashCode() {
        return (((x & 0xff) << 16) + (y & 0xff));
    }
}

