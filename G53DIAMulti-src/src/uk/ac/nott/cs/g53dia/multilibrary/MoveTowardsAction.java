package uk.ac.nott.cs.g53dia.multilibrary;
/**
 * Action which moves the tanker towards a given {@link Point} in the environment.
 * 
 * @author Neil Madden.
 */

/*
 * Copyright (c) 2005 Neil Madden.
 * Copyright (c) 2010 University of Nottingham.
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public class MoveTowardsAction implements Action {
    private Point target;

    public MoveTowardsAction(Point t) {
        target = t;
    }

    public void execute(Environment env, Tanker tanker) throws ActionFailedException {
        if (tanker.getPosition().equals(target)) {
            throw new ActionFailedException("MoveTowards: already there!");
        }
        
        Point newPosition = (Point)tanker.position.clone();
        
        int dx = target.x - newPosition.x;
        int dy = target.y - newPosition.y;
        if (dx < 0) {
        	newPosition.x--;
        } else if (dx > 0) {
        	newPosition.x++;
        }
        if (dy < 0) {
        	newPosition.y--;
        } else if (dy > 0) {
        	newPosition.y++;
        }
       
        tanker.useFuel(1);
        tanker.position = (Point)newPosition.clone();	        	
    }

    public String toString() {
        return "Move to " + target;
    }

}
