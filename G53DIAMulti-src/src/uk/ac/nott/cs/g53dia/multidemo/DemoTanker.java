/**
 * Developed by Cheng Duan
 */

package uk.ac.nott.cs.g53dia.multidemo;
import uk.ac.nott.cs.g53dia.multilibrary.*;
import java.util.*;
import java.util.Arrays;

public class DemoTanker extends Tanker {
	/**
	 * Indicates the number of this tanker
	 * Each tanker has one specific number during each run
	 */
	private int tankerCount;
	
	/**
	 * Get the command centre
	 */
	private CommandCenter commandCenter = DemoSimulator.commandCenter;

    private ArrayList<int[]> initialWlakingAroundPoints = new ArrayList<int[]>();
    private boolean isInitialWlakingAround = true;
    private boolean needWalkingAroundPoints = true;
    private int initialWalkingAroundMonitor = 0;

    /**
     * Lists used to store information of wells, stations, tasks and fuelpumps
     */
    private ArrayList<int[]> seenWells = new ArrayList<int[]>();
    private ArrayList<int[]> seenStations = new ArrayList<int[]>();
    private ArrayList<int[]> seenTasks = commandCenter.seenTasks;
    private ArrayList<int[]> seenFuelpumps = new ArrayList<int[]>();

    /**
     * Track tanker's position
     */
    private int tankPosX = 0;
    private int tankPosY = 0;

    /**
     * In situation "tank -> task -> well -> fuelpump", used to indicate the tanker is on its way to a task or a well
     */
    private boolean isToTask = true;

    public DemoTanker() {
        // Add the FuelPump at the root into tanker's memory manually
        seenFuelpumps.add(new int[]{0,0});
        // Get tanker's number when the tanker is created
        tankerCount = DemoSimulator.tankerNumberCount;
    }

    /**
     * SenseAndAct is the main method that can be treated as the brain of the intelligent agent
     * @param   view
     * @param   timestep
     * @return  Action that agent will do
     */
    public Action senseAndAct(Cell[][] view, long timestep) {
        initialSeenThings(view);

        if(isInitialWlakingAround){
        	if(needWalkingAroundPoints){
            	initialWlakingAroundPoints = commandCenter.initialWalkingAroundPoints();
            	needWalkingAroundPoints = false;
        	}
        	System.out.println("Tanker "+tankerCount+" (#) "+"Tanker is walking around to search for new tasks");
            return initialWalkingAround(view, timestep);
        }else{
            return workingProcedure(view, timestep);
        }
    }

    /**
     * Detecting and storing any stations, wells, fuelpumps and tasks
     * @param view
     */
    private void initialSeenThings(Cell[][] view){
        for(int i = 0;i < view.length;i++){
            for(int j = 0;j < view[i].length;j++){
                // Look at each cell of view start from view[0][0]
                int[] focusedPos = new int[]{i+tankPosX-25, -j+tankPosY+25};

                // Storing any detected stations, wells, fuelpumps and tasks
                if (Math.max(Math.abs(focusedPos[0]), Math.abs(focusedPos[1])) <= 99){
                    if (view[i][j] instanceof Station) {
                        if (isInList(seenStations, focusedPos) == -1){
                            seenStations.add(focusedPos);
                        }

                        Station focusedSta = (Station) view[i][j];
                        if (focusedSta.getTask() != null){
                            int[] taskDetails = new int[]{focusedPos[0], focusedPos[1], focusedSta.getTask().getWasteAmount(), 0};
                            commandCenter.storeTasks(taskDetails);
                        }
                    } else if (view[i][j] instanceof Well && isInList(seenWells, focusedPos) == -1) {
                        seenWells.add(focusedPos);
                    } else if (view[i][j] instanceof FuelPump && isInList(seenFuelpumps, focusedPos) == -1) {
                        seenFuelpumps.add(focusedPos);
                    } else {
                        continue;
                    }
                }
            }
        }
    }

    /**
     * Looking around to detect environment around
     * @param   view
     * @param   timestep
     * @return  Action that the agent will do
     */
    private Action initialWalkingAround(Cell[][] view, long timestep){
    	// When the tanker has finished the set path, stop walking around procedure by set "isInitialWlakingAround" to false
        if(initialWalkingAroundMonitor >= initialWlakingAroundPoints.size()){
            isInitialWlakingAround = false;
            return senseAndAct(view, timestep);
        }

        int[] targetPos = initialWlakingAroundPoints.get(initialWalkingAroundMonitor);
        if(targetPos[0] == tankPosX && targetPos[1] == tankPosY){
            initialWalkingAroundMonitor++;
            if(getCurrentCell(view) instanceof FuelPump){
                if(getFuelLevel() == 100) {
                    return senseAndAct(view, timestep);
                } else {
                    return new RefuelAction();
                }
            } else {
                return senseAndAct(view, timestep);
            }
        } else {
            return moveTowardsPointsAction(view, targetPos);
        }
    }

    /**
     * Method implements thinking procedure of agent
     * @param view
     * @param timestep
     * @return An action to do
     */
    private Action workingProcedure(Cell[][] view, long timestep){
        // taskAmount >= 1
        if(seenTasks.size() >= 1){
            // Find nearest task going from current tank position
            int taskChosenIndex = commandCenter.getClosestNonallocatedTaskIndex(seenTasks, new int[]{tankPosX, tankPosY}, tankerCount);

            // -1 means there is no task that can be reached from current tanker position even with full fulelevel
            if(taskChosenIndex == -1){
                int[] root = {0,0};
                // Find nearest fuelPump going from current tank position
                int[] nearestFuelpumpGoingFromCurrent = seenFuelpumps.get(commandCenter.getClosestIndexBetween(seenFuelpumps, new int[]{tankPosX, tankPosY}));
                int distanceBetweenRootAndCurrent = Math.max(Math.abs(root[0] - tankPosX),Math.abs(root[1] - tankPosY));

                if(getFuelLevel() > distanceBetweenRootAndCurrent){
                    return walkingAround(view, timestep);
                } else {
                	System.out.println("Tanker "+tankerCount+" (1) $FuelPumpToGo:"+"("+nearestFuelpumpGoingFromCurrent[0]+","+nearestFuelpumpGoingFromCurrent[1]+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")");
                    return goToFuelPump(view, timestep, nearestFuelpumpGoingFromCurrent);
                }
            }

            // Get the chosen task from seenTasks
            int[] taskToGo = seenTasks.get(taskChosenIndex);
            int taskToGoX = taskToGo[0];
            int taskToGoY = taskToGo[1];
            // Find nearest fuelPump going from taskToGo
            int[] nearestFuelpumpGoingFromTask = seenFuelpumps.get(commandCenter.getClosestIndexBetween(seenFuelpumps, taskToGo));
            // Find nearest fuelPump going from current tank position
            int[] nearestFuelpumpGoingFromCurrent = seenFuelpumps.get(commandCenter.getClosestIndexBetween(seenFuelpumps, new int[]{tankPosX, tankPosY}));
            // Find nearest well going from taskToGo
            int[] nearestWellGoingFromTask = seenWells.get(commandCenter.getClosestIndexBetween(seenWells, taskToGo));
            // Find nearest fuelPump going from well
            int[] nearestFuelpumpGoingFromWell = seenFuelpumps.get(commandCenter.getClosestIndexBetween(seenFuelpumps, nearestWellGoingFromTask));
            // Find nearest well going from tank
            int[] nearestWellGoingFromTank = seenWells.get(commandCenter.getClosestIndexBetween(seenWells, new int[]{tankPosX, tankPosY}));
            // Find nearest fuelPump going from well
            int[] nearestFuelpumpGoingFromWell2 = seenFuelpumps.get(commandCenter.getClosestIndexBetween(seenFuelpumps, nearestWellGoingFromTank));
            int distanceBetweenTankAndTask = Math.max(Math.abs(taskToGo[0] - tankPosX),Math.abs(taskToGo[1] - tankPosY));
            int distanceBetweenTaskAndFuelPump = Math.max(Math.abs(taskToGo[0] - nearestFuelpumpGoingFromTask[0]),Math.abs(taskToGo[1] - nearestFuelpumpGoingFromTask[1]));
            int fuelleftAfterTask = getFuelLevel() - distanceBetweenTankAndTask;

            // tankCapacity() > taskAmount
            if(getWasteCapacity() > taskToGo[2]){
                // tank -> task -> fuelpump
                if(fuelleftAfterTask >= distanceBetweenTaskAndFuelPump){
                    System.out.println("Tanker "+tankerCount+" (2) $TaskToGo:"+"("+taskToGoX+","+taskToGoY+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")");
                    return goToTask(view, timestep, taskToGo, taskChosenIndex);
                }
                // tank -> fuelpump
                else {
                    System.out.println("Tanker "+tankerCount+" (3) $FuelPumpToGo:"+"("+nearestFuelpumpGoingFromCurrent[0]+","+nearestFuelpumpGoingFromCurrent[1]+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")");
					seenTasks.set(taskChosenIndex, new int[]{seenTasks.get(taskChosenIndex)[0],seenTasks.get(taskChosenIndex)[1],seenTasks.get(taskChosenIndex)[2],0});
					return goToFuelPump(view, timestep,nearestFuelpumpGoingFromCurrent);
                }
            }
            // tankCapacity() == taskAmount
            else if(getWasteCapacity() == taskToGo[2]) {
                int distanceBetweenTaskAndWell = Math.max(Math.abs(taskToGo[0] - nearestWellGoingFromTask[0]),Math.abs(taskToGo[1] - nearestWellGoingFromTask[1]));
                int distanceBetweenWellAndFuelPump = Math.max(Math.abs(nearestFuelpumpGoingFromWell[0] - nearestWellGoingFromTask[0]),Math.abs(nearestFuelpumpGoingFromWell[1] - nearestWellGoingFromTask[1]));
                int fuelAfterTaskWell = getFuelLevel() - distanceBetweenTankAndTask - distanceBetweenTaskAndWell;
                // tank -> task -> well -> fuelpump
                if(fuelAfterTaskWell >= distanceBetweenWellAndFuelPump){
                    if(isToTask){
                    	System.out.println("Tanker "+tankerCount+" (4) $taskToGo:"+"("+taskToGo[0]+","+taskToGo[1]+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")");
                        if(getCurrentCell(view) instanceof Station){
                            if (tankPosX != taskToGo[0] || tankPosY != taskToGo[1]) {
                                return moveTowardsPointsAction(view, taskToGo);
                            } else {
                                Station currentCellStation = (Station) getCurrentCell(view);
                                Task currentTask = currentCellStation.getTask();
                                seenTasks.remove(taskChosenIndex);
                                isToTask = false;
                                return new LoadWasteAction(currentTask);
                            }
                        } else {
                            return moveTowardsPointsAction(view, taskToGo);
                        }
                    } else {
                    	System.out.println("Tanker "+tankerCount+" (5) $wellToGo:"+"("+nearestWellGoingFromTask[0]+","+nearestWellGoingFromTask[1]+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")");
                        if(getCurrentCell(view) instanceof Well){
                            isToTask = true;
                            return new DisposeWasteAction();
                        } else {
                            return moveTowardsPointsAction(view, nearestWellGoingFromTask);
                        }
                    }
                }
                // tank -> task -> fuelpump
                else if(fuelleftAfterTask >= distanceBetweenTaskAndFuelPump) {
                    System.out.println("Tanker "+tankerCount+" (6) $TaskToGo:"+"("+taskToGoX+","+taskToGoY+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")");
                    return goToTask(view, timestep, taskToGo, taskChosenIndex);
                }
                // tank ->fuelpump
                else {
                	System.out.println("Tanker "+tankerCount+" (7) $FuelPumpToGo:"+"("+nearestFuelpumpGoingFromCurrent[0]+","+nearestFuelpumpGoingFromCurrent[1]+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")");
					seenTasks.set(taskChosenIndex, new int[]{seenTasks.get(taskChosenIndex)[0],seenTasks.get(taskChosenIndex)[1],seenTasks.get(taskChosenIndex)[2],0});
                    return goToFuelPump(view, timestep,nearestFuelpumpGoingFromCurrent);
                }
            }
            // tankCapacity() < taskAmount
            else {
                // maxTankCapacity() < taskAmount
                if(MAX_WASTE < taskToGo[2]){
                    // tank -> task -> fuelpump
                    if(fuelleftAfterTask >= distanceBetweenTaskAndFuelPump){
                        System.out.println("Tanker "+tankerCount+" (8) $TaskToGo:"+"("+taskToGoX+","+taskToGoY+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")");
                        if(getCurrentCell(view) instanceof Station){
                            Station currentCellStation = (Station) getCurrentCell(view);
                            Task currentTask = currentCellStation.getTask();
                            int newTask = seenTasks.get(taskChosenIndex)[2] - getWasteCapacity();
                            seenTasks.get(taskChosenIndex)[2] = newTask;
                            return new LoadWasteAction(currentTask);
                        } else {
                            return moveTowardsPointsAction(view, taskToGo);
                        }
                    }
                    // tank -> fuelpump
                    else {
                    	System.out.println("Tanker "+tankerCount+" (9) $FuelPumpToGo:"+"("+nearestFuelpumpGoingFromCurrent[0]+","+nearestFuelpumpGoingFromCurrent[1]+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")");
						seenTasks.set(taskChosenIndex, new int[]{seenTasks.get(taskChosenIndex)[0],seenTasks.get(taskChosenIndex)[1],seenTasks.get(taskChosenIndex)[2],0});
                        return goToFuelPump(view, timestep,nearestFuelpumpGoingFromCurrent);
                    }
                }
                else {
                    int distanceBetweenTankAndWell = Math.max(Math.abs(nearestWellGoingFromTank[0] - tankPosX),Math.abs(nearestWellGoingFromTank[1] - tankPosY));
                    int distanceBetweenFuelPumpAndWell = Math.max(Math.abs(nearestWellGoingFromTank[0] - nearestFuelpumpGoingFromWell2[0]),Math.abs(nearestWellGoingFromTank[1] - nearestFuelpumpGoingFromWell2[1]));
                    int fuelAfterWell = getFuelLevel() - distanceBetweenTankAndWell;
                    // tank -> well -> fuelpump
                    seenTasks.set(taskChosenIndex, new int[]{seenTasks.get(taskChosenIndex)[0],seenTasks.get(taskChosenIndex)[1],seenTasks.get(taskChosenIndex)[2],0});
                    if(fuelAfterWell > distanceBetweenFuelPumpAndWell) {
                        System.out.println("Tanker "+tankerCount+" (10) $WellToGo:"+"("+nearestWellGoingFromTank[0]+","+nearestWellGoingFromTank[1]+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")");
                        if(getCurrentCell(view) instanceof Well){
                            return new DisposeWasteAction();
                        } else {
                            return moveTowardsPointsAction(view, nearestWellGoingFromTank);
                        }
                    }
                    // tank -> fuelpump
                    else {
                        System.out.println("Tanker "+tankerCount+" (11) $FuelPumpToGo:"+"("+nearestFuelpumpGoingFromCurrent[0]+","+nearestFuelpumpGoingFromCurrent[1]+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")");
                        return goToFuelPump(view, timestep,nearestFuelpumpGoingFromCurrent);
                    }
                }
            }
        }
        // No task left seenTasks
        else {
            int[] root = {0,0};
            // Find nearest fuelPump going from current tank position
            int[] nearestFuelpumpGoingFromCurrent = seenFuelpumps.get(commandCenter.getClosestIndexBetween(seenFuelpumps, new int[]{tankPosX, tankPosY}));
            int distanceBetweenRootAndCurrent = Math.max(Math.abs(root[0] - tankPosX),Math.abs(root[1] - tankPosY));

            if(getFuelLevel() > distanceBetweenRootAndCurrent){
                return walkingAround(view, timestep);
            } else {
            	System.out.println("Tanker "+tankerCount+" (12) $FuelPumpToGo:"+"("+nearestFuelpumpGoingFromCurrent[0]+","+nearestFuelpumpGoingFromCurrent[1]+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")");
            	return goToFuelPump(view, timestep,nearestFuelpumpGoingFromCurrent);
            }
        }
    }

    /**
     * Method agent will call when there is no task left in seenTasks or there is no reachable task in seenTasks from current position
     * @param   view
     * @param   timestep
     * @return  Action that agent will do
     */
    private Action walkingAround(Cell[][] view, long timestep){
        isInitialWlakingAround = true;
        initialWalkingAroundMonitor = 0;
        return initialWalkingAround(view, timestep);
    }

    /**
     * Moveing to specified Point
     * @param   view
     * @param   targetPos
     * @return  Action that agent will do next
     */
    private Action moveTowardsPointsAction(Cell[][] view, int[] targetPos){
        int horizontalDifference = targetPos[0] - tankPosX;
        int verticalDifference = targetPos[1] - tankPosY;
        int verticalMovement, horizontalMovement;

        if(horizontalDifference > 0){
            horizontalMovement = 1;
        } else if (horizontalDifference == 0){
            horizontalMovement = 0;
        } else {
            horizontalMovement = -1;
        }

        if(verticalDifference > 0){
            verticalMovement = 1;
        } else if (verticalDifference == 0){
            verticalMovement = 0;
        } else {
            verticalMovement = -1;
        }

        int directionToGo = 0;
        switch (horizontalMovement) {
            case 0:
                if(verticalMovement == 1){
                    directionToGo = 0;
                } else {
                    directionToGo = 1;
                }
                break;
            case 1:
                if(verticalMovement == 1){
                    directionToGo = 4;
                } else if (verticalMovement == 0) {
                    directionToGo = 2;
                } else {
                    directionToGo = 6;
                }
                break;
            case -1:
                if(verticalMovement == 1){
                    directionToGo = 5;
                } else if (verticalMovement == 0) {
                    directionToGo = 3;
                } else {
                    directionToGo = 7;
                }
                break;
        }

        tankMovement(directionToGo);
        return new MoveAction(directionToGo);
    }

    /**
     * Method used to check whether a int[] is in the given arraylist or not
     * @param   seemList
     * @param   focused
     * @return  Action that agent will do next
     */
    private int isInList(ArrayList<int[]> seenList, int[] focused){
        for (int i = 0; i < seenList.size(); i++){
            if (Arrays.equals(seenList.get(i), focused)){
                return i;
            }
        }
        return -1;
    }

     /**
      * Refresh the position of the tanker
      * @param dir
      */
    private void tankMovement(int dir){
        switch (dir){
            case 0:
                tankPosY++;
                break;
            case 1:
                tankPosY--;
                break;
            case 2:
                tankPosX++;
                break;
            case 3:
                tankPosX--;
                break;
            case 4:
                tankPosX++;
                tankPosY++;
                break;
            case 5:
                tankPosX--;
                tankPosY++;
                break;
            case 6:
                tankPosX++;
                tankPosY--;
                break;
            case 7:
                tankPosX--;
                tankPosY--;
                break;
        }
    }

    /**
     * Instructing the tanker go to the chosen fuel pump
     * @param view
     * @param timestep
     * @param nearestFuelpumpGoingFromCurrent
     * @return An action to do
     */
	public Action goToFuelPump(Cell[][] view, long timestep, int[] nearestFuelpumpGoingFromCurrent){
		if(getCurrentCell(view) instanceof FuelPump){
			if(getFuelLevel() == 100) {
				return walkingAround(view, timestep);
			} else {
				return new RefuelAction();
			}
		} else {
			return moveTowardsPointsAction(view, nearestFuelpumpGoingFromCurrent);
		}
	}


	/**
	 * Instructing the tanker go to the chosen task
	 * @param view
	 * @param timestep
	 * @param taskToGo
	 * @param taskChosenIndex
	 * @return An action to do
	 */
	public Action goToTask(Cell[][] view, long timestep, int[] taskToGo, int taskChosenIndex){
        if(getCurrentCell(view) instanceof Station){
            if (tankPosX != taskToGo[0] || tankPosY != taskToGo[1]) {
                return moveTowardsPointsAction(view, taskToGo);
            } else {
                Station currentCellStation = (Station) getCurrentCell(view);
                Task currentTask = currentCellStation.getTask();
                seenTasks.remove(taskChosenIndex);
                return new LoadWasteAction(currentTask);
            }
        } else {
            return moveTowardsPointsAction(view, taskToGo);
        }
	}
}
