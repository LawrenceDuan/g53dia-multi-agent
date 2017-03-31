package uk.ac.nott.cs.g53dia.multidemo;
import java.util.*;

public class CommandCenter {

	private int caseCount = 0;
	// Observed tasks' list shared by all tankers
	public ArrayList<int[]> seenTasks = new ArrayList<int[]>();

    public CommandCenter(){

    }

    /**
     * When a new DemoTanker is created, it will contact this method and request for a initial viewing path 
     * @return initial viewing path
     */
    public ArrayList<int[]> initialWalkingAroundPoints(){
    	ArrayList<int[]> initialWalkingAroundPoints = new ArrayList<int[]>();
		switch (caseCount) {
            case 0:
				initialWalkingAroundPoints.add(new int[]{0,0});
				initialWalkingAroundPoints.add(new int[]{25,25});
				initialWalkingAroundPoints.add(new int[]{25,-25});
				initialWalkingAroundPoints.add(new int[]{0,0});
				caseCount = 1;
                break;
            case 1:
				initialWalkingAroundPoints.add(new int[]{0,0});
				initialWalkingAroundPoints.add(new int[]{25,-25});
				initialWalkingAroundPoints.add(new int[]{-25,-25});
				initialWalkingAroundPoints.add(new int[]{0,0});
				caseCount = 2;
                break;
            case 2:
				initialWalkingAroundPoints.add(new int[]{0,0});
				initialWalkingAroundPoints.add(new int[]{-25,-25});
				initialWalkingAroundPoints.add(new int[]{-25,25});
				initialWalkingAroundPoints.add(new int[]{0,0});
				caseCount = 3;
                break;
			case 3:
				initialWalkingAroundPoints.add(new int[]{0,0});
				initialWalkingAroundPoints.add(new int[]{-25,25});
				initialWalkingAroundPoints.add(new int[]{25,25});
				initialWalkingAroundPoints.add(new int[]{0,0});
				caseCount = 0;
                break;
        }
        return initialWalkingAroundPoints;
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
}
