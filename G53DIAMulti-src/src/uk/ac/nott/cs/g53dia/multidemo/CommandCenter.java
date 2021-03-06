/**
 * Developed by Cheng Duan
 */

package uk.ac.nott.cs.g53dia.multidemo;
import java.util.*;

public class CommandCenter {

	private int caseCount = 0;
	// Observed tasks' list shared by all tankers
	public ArrayList<int[]> seenTasks = new ArrayList<int[]>();
	public ArrayList<int[]> seenWells = new ArrayList<int[]>();
	public ArrayList<int[]> seenStations = new ArrayList<int[]>();
	public ArrayList<int[]> seenFuelpumps = new ArrayList<int[]>();

	public ArrayList<int[]> walkedStations = new ArrayList<int[]>();

    public CommandCenter(){

    }

    /**
     * When a new DemoTanker is created, it will contact this method and request for a initial viewing path
     * @return initial viewing path
     */
//    public ArrayList<int[]> initialWalkingAroundPoints(){
//    	ArrayList<int[]> initialWalkingAroundPoints = new ArrayList<int[]>();
//		switch (caseCount) {
//            case 0:
//				initialWalkingAroundPoints.add(new int[]{0,0});
//				initialWalkingAroundPoints.add(new int[]{25,25});
//				initialWalkingAroundPoints.add(new int[]{25,-25});
//				initialWalkingAroundPoints.add(new int[]{0,0});
//				caseCount = 1;
//                break;
//            case 1:
//				initialWalkingAroundPoints.add(new int[]{0,0});
//				initialWalkingAroundPoints.add(new int[]{25,-25});
//				initialWalkingAroundPoints.add(new int[]{-25,-25});
//				initialWalkingAroundPoints.add(new int[]{0,0});
//				caseCount = 2;
//                break;
//            case 2:
//				initialWalkingAroundPoints.add(new int[]{0,0});
//				initialWalkingAroundPoints.add(new int[]{-25,-25});
//				initialWalkingAroundPoints.add(new int[]{-25,25});
//				initialWalkingAroundPoints.add(new int[]{0,0});
//				caseCount = 3;
//                break;
//			case 3:
//				initialWalkingAroundPoints.add(new int[]{0,0});
//				initialWalkingAroundPoints.add(new int[]{-25,25});
//				initialWalkingAroundPoints.add(new int[]{25,25});
//				initialWalkingAroundPoints.add(new int[]{0,0});
//				caseCount = 0;
//                break;
//        }
//        return initialWalkingAroundPoints;
//    }

	 public ArrayList<Integer> initialWalkingAroundPoints(){
     	ArrayList<Integer> initialWalkingAroundPoints = new ArrayList<Integer>();
	 	switch (caseCount) {
             case 0:
	 			initialWalkingAroundPoints.add(4);
	 			initialWalkingAroundPoints.add(1);
	 			initialWalkingAroundPoints.add(5);
	 			caseCount = 1;
                 break;
             case 1:
	 			initialWalkingAroundPoints.add(6);
	 			initialWalkingAroundPoints.add(3);
	 			initialWalkingAroundPoints.add(4);
	 			caseCount = 2;
                 break;
             case 2:
	 			initialWalkingAroundPoints.add(7);
	 			initialWalkingAroundPoints.add(0);
	 			initialWalkingAroundPoints.add(6);
	 			caseCount = 3;
                 break;
	 		case 3:
	 			initialWalkingAroundPoints.add(5);
	 			initialWalkingAroundPoints.add(2);
	 			initialWalkingAroundPoints.add(7);
	 			caseCount = 0;
                 break;
         }
         return initialWalkingAroundPoints;
     }

	 public void stationWalked(int[] taskDetail){
		 if(isInList(walkedStations, taskDetail) == -1){
			 walkedStations.add(taskDetail);
		 }
	 }

    /**
     * Storing new observed into seenTasks
     * @param taskDetail
     */
    public void storeTasks(int[] taskDetail){
    	if (isInList(seenTasks, taskDetail) == -1){
    		seenTasks.add(taskDetail);
        }
    }

    /**
     * Check if the new observed task has been stored in the seenTasks
     * @param seenList
     * @param taskDetail
     * @return -1 indicates the new observed task is not in the seenTasks, otherwise the task exists in the seenTasks
     */
    private int isInList(ArrayList<int[]> seenList,int[] taskDetail){
    	for (int i = 0; i < seenList.size(); i++){
            if (seenList.get(i)[0] == taskDetail[0] && seenList.get(i)[1] == taskDetail[1]){
                return i;
            }
        }
        return -1;
    }

    /**
     * Specially used to search through seenTasks
     * @param indicesList
     * @param point
     * @return Nearest go-able task's index or previously chosen task's index
     */
    public int getClosestNonallocatedTaskIndex(ArrayList<int[]> indicesList, int[] point, int tankerCount){
        int furthestGoableDistance = 100;
    	// int lowestNumberOfWaste = 0;
        int closestIndex = -1;

        // If the tanker has chosen a task before, return the task's index
        for(int i = 0;i < indicesList.size();i++){
        	int[] positionGoable = indicesList.get(i);
        	if(positionGoable[3] == tankerCount){
        		return i;
        	}
        }

        // If the tanker has not chosen a task, search through seenTasks to get and return the Nearest go-able task's index
        for(int i = 0;i < indicesList.size();i++){
            int[] positionGoable = indicesList.get(i);
        	if(positionGoable[3] == 0){
            	int distanceBetween = Math.max(Math.abs(positionGoable[0] - point[0]),Math.abs(positionGoable[1] - point[1]));
                // int numberOfWaste = positionGoable[2];
	           	if(distanceBetween <= furthestGoableDistance){
	                   furthestGoableDistance = distanceBetween;
	                   closestIndex = i;
	            }
                // if(distanceBetween <= furthestGoableDistance && numberOfWaste >= lowestNumberOfWaste){
                //     furthestGoableDistance = distanceBetween;
                //     lowestNumberOfWaste = numberOfWaste;
                //     closestIndex = i;
                // }
            }
        }
        if(closestIndex != -1){
        	taskLocked(closestIndex, tankerCount);
        }
        return closestIndex;
    }

    /**
     * Find a closest int[]'s index in the ArrayList from a given int[] point
     * @param   indicesList
     * @param   point
     * @return  the index of ArrayList
     */
    public int getClosestIndexBetween(ArrayList<int[]> indicesList, int[] point){
        int furthestGoableDistance = 100;
        int closestIndex = -1;
        for(int i = 0;i < indicesList.size();i++){
            int[] positionGoable = indicesList.get(i);
        	int distanceBetween = Math.max(Math.abs(positionGoable[0] - point[0]),Math.abs(positionGoable[1] - point[1]));
            if(distanceBetween <= furthestGoableDistance){
                furthestGoableDistance = distanceBetween;
                closestIndex = i;
            }
        }
        return closestIndex;
    }

    /**
     * Locking the task which is chosen by a tanker
     * @param closestIndex
     * @param tankerCount
     */
    public void taskLocked(int closestIndex, int tankerCount){
    	seenTasks.set(closestIndex, new int[]{seenTasks.get(closestIndex)[0],seenTasks.get(closestIndex)[1],seenTasks.get(closestIndex)[2],tankerCount});
    }

    /**
     * Unlock the task which is abandoned by a tanker
     * @param taskChosenIndex
     */
    public void releaseTask(int taskChosenIndex){
    	seenTasks.set(taskChosenIndex, new int[]{seenTasks.get(taskChosenIndex)[0],seenTasks.get(taskChosenIndex)[1],seenTasks.get(taskChosenIndex)[2],0});
    }

    /**
     * When the task is accomplished by a tanker, the tanker will inform the command center that the task has been accomplished
     * Then the command center will remove the accomplished task from the seenTasks.]
     * @param taskChosenIndex
     */
    public void taskAccomplished(int taskChosenIndex){
    	seenTasks.remove(taskChosenIndex);
    }
}
