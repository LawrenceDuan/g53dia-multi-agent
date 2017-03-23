package uk.ac.nott.cs.g53dia.multilibrary;
/**
 * Action which moves the tanker one cell in a specified direction.
 *
 * @author Neil Madden.
 */
/*
 * Copyright (c) 2005 Neil Madden (nem@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
public class MoveAction implements Action {
    /**
     * Direction to move in.
     */
    private int direction;
    public final static int
        NORTH       =   0,
        SOUTH       =   1,
        EAST        =   2,
        WEST        =   3,
        NORTHEAST   =   4,
        NORTHWEST   =   5,
        SOUTHEAST   =   6,
        SOUTHWEST   =   7;

    public MoveAction(int dir) {
        direction = dir;
    }

    public void execute(Environment env, Tanker tanker) throws ActionFailedException {
    	
    	Point newPosition = (Point)tanker.position.clone();
    	
        switch (direction) {
            case NORTH:
                newPosition.y++;
                break;
            case SOUTH:
            	newPosition.y--;
                break;
            case EAST:
            	newPosition.x++;
                break;
            case WEST:
            	newPosition.x--;
                break;
            case NORTHEAST:
            	newPosition.x++; newPosition.y++;
                break;
            case NORTHWEST:
            	newPosition.x--; newPosition.y++;
                break;
            case SOUTHEAST:
            	newPosition.x++; newPosition.y--;
                break;
            case SOUTHWEST:
            	newPosition.x--; newPosition.y--;
                break;
            default:
                throw new ActionFailedException("Move: not a direction: "
                        + direction);
        }
                	
        tanker.useFuel(1);
        tanker.position = (Point)newPosition.clone();
    }

    private static final String DIRECTION[] = {
        "NORTH", "SOUTH", "EAST", "WEST", "NORTHEAST",
        "NORTHWEST", "SOUTHEAST", "SOUTHWEST"
    };
    public String toString() {
        return "Move " + DIRECTION[direction];
    }


}
