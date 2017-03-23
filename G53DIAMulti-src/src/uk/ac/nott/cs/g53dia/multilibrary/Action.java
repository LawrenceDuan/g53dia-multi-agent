package uk.ac.nott.cs.g53dia.multilibrary;
/**
 * Interface implemented by all actions.
 *
 * <p>Each tanker is required to return a single instance of an implementation of this interface at each
 * cycle (via the {@link Tanker#senseAndAct senseAndAct} method of the {@link Tanker} class). This action contains
 * the actual code to allow the Tanker to do work.</p>
 *
 * @author Neil Madden.
 */
/*
 * Copyright (c) 2003 Stuart Reeves
 * Copyright (c) 2003-2005 Neil Madden (nem@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
public interface Action {

    /**
     * Execute the action for this Tanker in this Environment.
     * @throws ActionFailedException The action couldn't be performed.
     * @param tanker The Tanker trying to perform this action.
     * @param env The Environment that the Tanker inhabits.
     */
    public abstract void execute(Environment env, Tanker tanker) 
        throws ActionFailedException;
}

