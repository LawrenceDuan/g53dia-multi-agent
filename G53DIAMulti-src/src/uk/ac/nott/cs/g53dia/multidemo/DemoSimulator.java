/**
 * Developed by Cheng Duan
 */

package uk.ac.nott.cs.g53dia.multidemo;
import java.text.NumberFormat;
import java.util.Random;
import uk.ac.nott.cs.g53dia.multilibrary.*;

public class DemoSimulator {

    /**
     * Time for which execution pauses so that GUI can update.
     * Reducing this value causes the simulation to run faster.
     */
	private static int DELAY = 100;

	/**
	 * Number of timesteps to execute
	 */
	private static int DURATION = 10000;

	/**
	 * Initialise a command centre at the start of the programme
	 */
	public static CommandCenter commandCenter = new CommandCenter();

	public static void main(String[] args) {
        // Create an environment
        Environment env = new Environment(Tanker.MAX_FUEL/2, new Random(8));
		//Create a fleet
        Fleet fleet = new DemoFleet();
        // Create a GUI window to show the fleet
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
            System.out.println("===="+env.getTimestep()+"===="+" ("+commandCenter.seenTasks.size()+")");
            try { Thread.sleep(DELAY);} catch (Exception e) { }
        }

		// Calculating the value of the number of stations was approached by the tankers
		// divides the number of stations was observed by the same tankers
		String coverPercent;
		double p1 = commandCenter.walkedStations.size();
        double p2 = commandCenter.seenStations.size();
		double p3 = p1 / p2;
        NumberFormat nf  =  NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        coverPercent = nf.format(p3);
		// int coverPercent = commandCenter.walkedStations.size()/commandCenter.seenStations.size();
		System.out.println("CoverPercent: "+coverPercent);
    }

}
