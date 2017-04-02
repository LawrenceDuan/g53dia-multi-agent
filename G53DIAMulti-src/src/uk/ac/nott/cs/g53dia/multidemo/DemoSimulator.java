/**
 * Developed by Cheng Duan
 */

package uk.ac.nott.cs.g53dia.multidemo;
import java.util.Random;
import uk.ac.nott.cs.g53dia.multilibrary.*;

public class DemoSimulator {

    /**
     * Time for which execution pauses so that GUI can update.
     * Reducing this value causes the simulation to run faster.
     */
	private static int DELAY = 100;
	
	/** 
	 * Number of tankers in the fleet
	 */
	private static int FLEET_SIZE = 2;
	
	/**
	 * Number of timesteps to execute
	 */
	private static int DURATION = 10000;
	
	/**
	 * Initialise a command centre at the start of the programme
	 */
	public static CommandCenter commandCenter = new CommandCenter();
	/**
	 * Counts the number of tankers created
	 */
	public static int tankerNumberCount = 0;
	
	public static void main(String[] args) {
        // Create an environment
        Environment env = new Environment(Tanker.MAX_FUEL/2, new Random(2));
        //Create a fleet
        Fleet fleet = new Fleet();
        // Create the tankers
        for (int i=0; i<FLEET_SIZE; i++) {
        	tankerNumberCount++;
        	fleet.add(new DemoTanker());
        }
        // Create a GUI window to show our tanker
        TankerViewer tv = new TankerViewer(fleet);
        tv.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        // Start executing the Tanker
        while (env.getTimestep() < DURATION) {
            // Advance the environment timestep
            env.tick();
            // Update the GUI
            tv.tick(env);
            
            for (Tanker t:fleet) {
            	// Get the current view of the tanker
            	Cell[][] view = env.getView(t.getPosition(), Tanker.VIEW_RANGE);
            	// Let the tanker choose an action
            	Action a = t.senseAndAct(view, env.getTimestep());
            	// Try to execute the action
            	try {
            		a.execute(env, t);
            	} catch (OutOfFuelException dte) {
            		System.out.println("Tanker out of fuel!");
            		break;
            	} catch (ActionFailedException afe) {
            		System.err.println("Failed: " + afe.getMessage());
            	}
            }
            System.out.println("===="+env.getTimestep()+"====");
            try { Thread.sleep(DELAY);} catch (Exception e) { }
        }
    }
	
}
