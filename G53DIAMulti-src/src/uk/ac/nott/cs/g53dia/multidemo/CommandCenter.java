package uk.ac.nott.cs.g53dia.multidemo;
import uk.ac.nott.cs.g53dia.multilibrary.*;
import java.util.*;
import java.nio.file.Files;

public class CommandCenter {

	private int caseCount = 0;
	
	public ArrayList<int[]> seenTasks = new ArrayList<int[]>();

    public CommandCenter(){

    }

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
    
    public void storeTasks(int[] taskDetail){
    	if (isInList(seenTasks, taskDetail) == -1){
//    		System.out.print("size: " + seenTasks.size() + "add:(" + taskDetail[0] + "," + taskDetail[1] + ") ");
    		seenTasks.add(taskDetail);
//    		System.out.println("-> size: " + seenTasks.size());
        }
    	
//    	resortSeenTasks();
    }
    
    private int isInList(ArrayList<int[]> seenList,int[] taskDetail){
    	for (int i = 0; i < seenList.size(); i++){
//    		System.out.println(seenList.get(i).toString() +"/"+taskDetail.toString());
            if (seenList.get(i)[0] == taskDetail[0] && seenList.get(i)[1] == taskDetail[1]){
//            	System.out.println("Already in list");
                return i;
            }
        }
        return -1;
    }
    
//    private void resortSeenTasks(){
//    	for(int i = 0;i < seenTasks.size();i++){
//    		
//    	}
//    }
}
