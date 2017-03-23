package uk.ac.nott.cs.g53dia.multilibrary;

import java.util.*;

public class Fleet extends ArrayList<Tanker> {

    private static final long serialVersionUID = 8031611383212571139L;

    /**
     * The average score achieved by tankers in the fleet
     */
    public long getScore() {
	int disposed = 0;
		
	for (Tanker t:this) {
	    disposed += t.wasteDisposed;    
	}
		
	// Return the average score for each tanker 
	return disposed / this.size();
    }
}
