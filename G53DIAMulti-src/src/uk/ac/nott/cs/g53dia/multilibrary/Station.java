package uk.ac.nott.cs.g53dia.multilibrary;
import java.util.Random;

/**
 * An environment cell which contains a station (source of tasks).
 * 
 * @author Julian  Zappala
 */

/*
 * Copyright (c) 2010 Julian  Zappala (jxz@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */

public class Station extends DefaultCell {
	
	final static double NEW_TASK_PROBABILITY = 0.001;
	private Task task;
	private Random r;
	
	Station(Point pos) {
	    super(pos);
	    this.r = new Random();
	}

	Station(Point pos, Random r) {
	    super(pos);
	    this.r = r;
	}
	
	protected void generateTask() {
	    if (this.task == null) {
		if (r.nextDouble() < NEW_TASK_PROBABILITY) {
		    this.task = new Task(this, r);
		}
	    }
	}
	
	public Task getTask() {
	    return this.task;
	}
	
	protected void removeTask() {
	    this.task = null;
	}
	
	protected Station clone() {
	    Station s = new Station(this.getPoint());
	    s.task = this.task;
	    return s;
	}
	
	public boolean equals(Object o) {
	    Station s = (Station)o;
	    if (this.getPoint().equals(s.getPoint())) {
		return true;
	    } else {
		return false;
	    }
	}
}
