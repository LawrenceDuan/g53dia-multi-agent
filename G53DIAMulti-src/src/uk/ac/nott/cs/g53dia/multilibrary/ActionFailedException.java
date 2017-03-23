package uk.ac.nott.cs.g53dia.multilibrary;
/**
 * Thrown when a tanker attempts to perform an illegal action.
 *
 * @author Neil Madden.
  */
/*
 * Copyright (c) 2003 Stuart Reeves
 * Copyright (c) 2003-2005 Neil Madden (nem@cs.nott.ac.uk).
 *
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
public class ActionFailedException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1701180809894490123L;

	public ActionFailedException(String reason) {
        super(reason);
    }

}
