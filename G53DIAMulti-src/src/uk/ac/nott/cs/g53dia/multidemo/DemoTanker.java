package uk.ac.nott.cs.g53dia.multidemo;
import uk.ac.nott.cs.g53dia.multilibrary.*;
import java.util.*;
import java.util.Arrays;
import java.nio.file.Files;

public class DemoTanker extends Tanker {

	private int tankerCount;

    private ArrayList<int[]> initialWlakingAroundPoints = new ArrayList<int[]>();
    private boolean isInitialWlakingAround = true;
    private boolean needWalkingAroundPoints = true;
    private int initialWalkingAroundMonitor = 0;

    private ArrayList<int[]> seenWells = new ArrayList<int[]>();
    private ArrayList<int[]> seenStations = new ArrayList<int[]>();
    private ArrayList<int[]> seenTasks = DemoSimulator.commandCenter.seenTasks;

    private ArrayList<int[]> seenFuelpumps = new ArrayList<int[]>();

    private int tankPosX = 0;
    private int tankPosY = 0;

    private int lastStationX = Integer.MIN_VALUE;
    private int lastStationY = Integer.MIN_VALUE;

    // tank -> task -> well -> fuelpump
    private boolean isToTask = true;
    private boolean isToWell = false;

    public DemoTanker() {
        // Add the FuelPump at the root into tanker's memory manually
        seenFuelpumps.add(new int[]{0,0});
        tankerCount = DemoSimulator.tankerNumberCount;
    }

    /**
     * SenseAndAct is the main method that can be treated as the brain of the intelligent agent
     * @param   view
     * @param   timestep
     * @return  Action that agent will do
     */
    public Action senseAndAct(Cell[][] view, long timestep) {
        if(timestep == 10000){
            System.out.println("Timestep: " + timestep + "; Final Score: " + getScore() + ";");
//            System.exit(0);
        }
        initialSeenThings(view);

        if(isInitialWlakingAround){
        	if(needWalkingAroundPoints){
            	initialWlakingAroundPoints = DemoSimulator.commandCenter.initialWalkingAroundPoints();
            	needWalkingAroundPoints = false;
        	}
        	System.out.print("----"+timestep+"----   Fuck");
            return initialWalkingAround(view, timestep);
        }else{
            System.out.print("----"+timestep+"----   ");
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
                int b = i+tankPosX-25;
                int c = -j+tankPosY+25;

                // Storing any detected stations, wells, fuelpumps and tasks
                if (Math.max(Math.abs(focusedPos[0]), Math.abs(focusedPos[1])) <= 99){
                    if (view[i][j] instanceof Station) {
                        if (isInList(seenStations, focusedPos) == -1){
                            seenStations.add(focusedPos);
                        }

                        Station focusedSta = (Station) view[i][j];
                        if (focusedSta.getTask() != null){
                            int[] taskDetails = new int[]{focusedPos[0], focusedPos[1], focusedSta.getTask().getWasteAmount(), 0};
                            DemoSimulator.commandCenter.storeTasks(taskDetails);
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
        if(initialWalkingAroundMonitor >= initialWlakingAroundPoints.size()){
            isInitialWlakingAround = false;
            System.out.println(isInitialWlakingAround +"/"+initialWalkingAroundMonitor);
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
     * Method inplements thinkging procedure of agent
     * @param  Cell[][] view          [description]
     * @param  long     timestep      [description]
     * @return          [description]
     */
    private Action workingProcedure(Cell[][] view, long timestep){
        // taskAmount >= 1
        if(seenTasks.size() >= 1){
            // Find nearest task going from current tank position
            int taskChosenIndex = getClosestNonallocatedTaskIndex(seenTasks, new int[]{tankPosX, tankPosY});
            System.out.println(tankerCount + ":" + taskChosenIndex);

            // -1 means there is no task that can be reached from current tanker position even with full fulelevel
            if(taskChosenIndex == -1){
                int[] root = {0,0};
                // Find nearest fuelPump going from current tank position
                int[] nearestFuelpumpGoingFromCurrent = seenFuelpumps.get(getClosestIndexBetween(seenFuelpumps, new int[]{tankPosX, tankPosY}));
                int distanceBetweenRootAndCurrent = Math.max(Math.abs(root[0] - tankPosX),Math.abs(root[1] - tankPosY));

                if(getFuelLevel() > distanceBetweenRootAndCurrent){
                    System.out.println("30>>");
                    return walkingAround(view, timestep);
                } else {
                    if(getCurrentCell(view) instanceof FuelPump){
                        if(getFuelLevel() == 100) {
                            System.out.println("31>>");
                            return walkingAround(view, timestep);
                        } else {
                            System.out.println("32>>");
                            return new RefuelAction();
                        }
                    } else {
                        System.out.println("33>>");
                        return moveTowardsPointsAction(view, nearestFuelpumpGoingFromCurrent);
                    }
                }
            }

            int[] taskToGo = seenTasks.get(taskChosenIndex);
            int taskToGoX = taskToGo[0];
            int taskToGoY = taskToGo[1];

            // Find nearest fuelPump going from taskToGo
            int[] nearestFuelpumpGoingFromTask = seenFuelpumps.get(getClosestIndexBetween(seenFuelpumps, taskToGo));
            // Find nearest fuelPump going from current tank position
            int[] nearestFuelpumpGoingFromCurrent = seenFuelpumps.get(getClosestIndexBetween(seenFuelpumps, new int[]{tankPosX, tankPosY}));
            // Find nearest well going from taskToGo
            int[] nearestWellGoingFromTask = seenWells.get(getClosestIndexBetween(seenWells, taskToGo));
            // Find nearest fuelPump going from well
            int[] nearestFuelpumpGoingFromWell = seenFuelpumps.get(getClosestIndexBetween(seenFuelpumps, nearestWellGoingFromTask));
            // Find nearest well going from tank
            int[] nearestWellGoingFromTank = seenWells.get(getClosestIndexBetween(seenWells, new int[]{tankPosX, tankPosY}));
            // Find nearest fuelPump going from well
            int[] nearestFuelpumpGoingFromWell2 = seenFuelpumps.get(getClosestIndexBetween(seenFuelpumps, nearestWellGoingFromTank));
            int distanceBetweenTankAndFuelPump = Math.max(Math.abs(nearestFuelpumpGoingFromCurrent[0] - tankPosX),Math.abs(nearestFuelpumpGoingFromCurrent[1] - tankPosY));
            int distanceBetweenTankAndTask = Math.max(Math.abs(taskToGo[0] - tankPosX),Math.abs(taskToGo[1] - tankPosY));
            int distanceBetweenTaskAndFuelPump = Math.max(Math.abs(taskToGo[0] - nearestFuelpumpGoingFromTask[0]),Math.abs(taskToGo[1] - nearestFuelpumpGoingFromTask[1]));
            int fuelleftAfterTask = getFuelLevel() - distanceBetweenTankAndTask;

            // tankCapacity() > taskAmount
            if(getWasteCapacity() > taskToGo[2]){
                // tank -> task -> fuelpump
                System.out.println("debug:"+distanceBetweenTankAndTask+"/"+fuelleftAfterTask+"/"+distanceBetweenTaskAndFuelPump);
                if(fuelleftAfterTask >= distanceBetweenTaskAndFuelPump){
                    System.out.print("$TaskToGo:"+"("+taskToGoX+","+taskToGoY+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")   $");
                    if(getCurrentCell(view) instanceof Station){
                        if (tankPosX != taskToGo[0] || tankPosY != taskToGo[1]) {
                            System.out.println("23>>");
                            return moveTowardsPointsAction(view, taskToGo);
                        } else {
                            System.out.println("2>>");
                            lastStationX = taskToGo[0];
                            lastStationY = taskToGo[1];
                            Station currentCellStation = (Station) getCurrentCell(view);
                            Task currentTask = currentCellStation.getTask();
                            seenTasks.remove(taskChosenIndex);
                            return new LoadWasteAction(currentTask);
                        }
                    } else {
                        System.out.println("3>>");
                        return moveTowardsPointsAction(view, taskToGo);
                    }
                }
                // tank -> fuelpump
                else {
                    System.out.print("$FuelPumpToGo:"+"("+nearestFuelpumpGoingFromCurrent[0]+","+nearestFuelpumpGoingFromCurrent[1]+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")   $");
					seenTasks.set(taskChosenIndex, new int[]{seenTasks.get(taskChosenIndex)[0],seenTasks.get(taskChosenIndex)[1],seenTasks.get(taskChosenIndex)[2],0});
					if(getCurrentCell(view) instanceof FuelPump){
                        if(getFuelLevel() == 100) {
                            System.out.println("20>>");
                            return walkingAround(view, timestep);
                        } else {
                            System.out.println("50");
                            return new RefuelAction();
                        }
                    } else {
                        System.out.println("4>>");
                        return moveTowardsPointsAction(view, nearestFuelpumpGoingFromCurrent);
                    }
                }
            }
            // tankCapacity() == taskAmount
            else if(getWasteCapacity() == taskToGo[2]) {
                System.out.print("5>>");
                int distanceBetweenTaskAndWell = Math.max(Math.abs(taskToGo[0] - nearestWellGoingFromTask[0]),Math.abs(taskToGo[1] - nearestWellGoingFromTask[1]));
                int distanceBetweenWellAndFuelPump = Math.max(Math.abs(nearestFuelpumpGoingFromWell[0] - nearestWellGoingFromTask[0]),Math.abs(nearestFuelpumpGoingFromWell[1] - nearestWellGoingFromTask[1]));
                int fuelAfterTaskWell = getFuelLevel() - distanceBetweenTankAndTask - distanceBetweenTaskAndWell;

                // tank -> task -> well -> fuelpump
                if(fuelAfterTaskWell >= distanceBetweenWellAndFuelPump){
                    System.out.print("$taskToGo:"+"("+taskToGo[0]+","+taskToGo[1]+")   $"+"$wellToGo:"+"("+nearestWellGoingFromTask[0]+","+nearestWellGoingFromTask[1]+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")   $");
                    if(isToTask){
                        if(getCurrentCell(view) instanceof Station){
                            if (tankPosX != taskToGo[0] || tankPosY != taskToGo[1]) {
                                System.out.println("26>>");
                                return moveTowardsPointsAction(view, taskToGo);
                            } else {
                                System.out.println("6>>");
                                Station currentCellStation = (Station) getCurrentCell(view);
                                Task currentTask = currentCellStation.getTask();
                                seenTasks.remove(taskChosenIndex);

                                isToTask = false;
                                isToWell = true;
                                return new LoadWasteAction(currentTask);
                            }
                        } else {
                            System.out.println("7>>");
                            return moveTowardsPointsAction(view, taskToGo);
                        }
                    } else {
                        if(getCurrentCell(view) instanceof Well){
                            System.out.println("8>>");
                            Well currentCellStation = (Well) getCurrentCell(view);

                            isToTask = true;
                            isToWell = false;
                            return new DisposeWasteAction();
                        } else {
                            System.out.println("9>>");
                            return moveTowardsPointsAction(view, nearestWellGoingFromTask);
                        }
                    }
                }
                // tank -> task -> fuelpump
                else if(fuelleftAfterTask >= distanceBetweenTaskAndFuelPump) {
                    System.out.print("$TaskToGo:"+"("+taskToGoX+","+taskToGoY+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")   $");
                    if(getCurrentCell(view) instanceof Station){
                        if (tankPosX != taskToGo[0] || tankPosY != taskToGo[1]) {
                            System.out.println("100>>");
                            return moveTowardsPointsAction(view, taskToGo);
                        } else {
                            System.out.println("10>>");
                            lastStationX = taskToGo[0];
                            lastStationY = taskToGo[1];
                            Station currentCellStation = (Station) getCurrentCell(view);
                            Task currentTask = currentCellStation.getTask();
                            seenTasks.remove(taskChosenIndex);
                            return new LoadWasteAction(currentTask);
                        }
                    } else {
                        System.out.println("12>>");
                        return moveTowardsPointsAction(view, taskToGo);
                    }
                }
                // tank ->fuelpump
                else {
					seenTasks.set(taskChosenIndex, new int[]{seenTasks.get(taskChosenIndex)[0],seenTasks.get(taskChosenIndex)[1],seenTasks.get(taskChosenIndex)[2],0});
                    System.out.print("$FuelPumpToGo:"+"("+nearestFuelpumpGoingFromCurrent[0]+","+nearestFuelpumpGoingFromCurrent[1]+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")   $13>>");
                    if(getCurrentCell(view) instanceof FuelPump){
                        if(getFuelLevel() == 100) {
                            System.out.println("20>>");
                            return walkingAround(view, timestep);
                        } else {
                            return new RefuelAction();
                        }
                    } else {
                        return moveTowardsPointsAction(view, nearestFuelpumpGoingFromCurrent);
                    }
                }
            }
            // tankCapacity() < taskAmount
            else {
                // maxTankCapacity() < taskAmount
                if(MAX_WASTE < taskToGo[2]){
                    // tank -> task -> fuelpump
                    if(fuelleftAfterTask >= distanceBetweenTaskAndFuelPump){
                        System.out.print("$TaskToGo:"+"("+taskToGoX+","+taskToGoY+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")   $");
                        if(getCurrentCell(view) instanceof Station){
                            System.out.println("14>>");
                            lastStationX = Integer.MIN_VALUE;
                            lastStationY = Integer.MIN_VALUE;
                            Station currentCellStation = (Station) getCurrentCell(view);
                            Task currentTask = currentCellStation.getTask();

                            int newTask = seenTasks.get(taskChosenIndex)[2] - getWasteCapacity();
                            seenTasks.get(taskChosenIndex)[2] = newTask;
                            return new LoadWasteAction(currentTask);
                        } else {
                            System.out.println("15>>");
                            return moveTowardsPointsAction(view, taskToGo);
                        }
                    }
                    // tank -> fuelpump
                    else {
						seenTasks.set(taskChosenIndex, new int[]{seenTasks.get(taskChosenIndex)[0],seenTasks.get(taskChosenIndex)[1],seenTasks.get(taskChosenIndex)[2],0});
                        System.out.print("$FuelPumpToGo:"+"("+nearestFuelpumpGoingFromCurrent[0]+","+nearestFuelpumpGoingFromCurrent[1]+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")   $16>>");
                        if(getCurrentCell(view) instanceof FuelPump){
                            if(getFuelLevel() == 100) {
                                System.out.println("20>>");
                                return walkingAround(view, timestep);
                            } else {
                                return new RefuelAction();
                            }
                        } else {
                            return moveTowardsPointsAction(view, nearestFuelpumpGoingFromCurrent);
                        }
                    }
                }
                else {
                    int distanceBetweenTankAndWell = Math.max(Math.abs(nearestWellGoingFromTank[0] - tankPosX),Math.abs(nearestWellGoingFromTank[1] - tankPosY));
                    int distanceBetweenFuelPumpAndWell = Math.max(Math.abs(nearestWellGoingFromTank[0] - nearestFuelpumpGoingFromWell2[0]),Math.abs(nearestWellGoingFromTank[1] - nearestFuelpumpGoingFromWell2[1]));
                    int fuelAfterWell = getFuelLevel() - distanceBetweenTankAndWell;
                    // tank -> well -> fuelpump
                    seenTasks.set(taskChosenIndex, new int[]{seenTasks.get(taskChosenIndex)[0],seenTasks.get(taskChosenIndex)[1],seenTasks.get(taskChosenIndex)[2],0});
                    if(fuelAfterWell > distanceBetweenFuelPumpAndWell) {
                        System.out.print("$FuelPumpToGo:"+"("+nearestWellGoingFromTank[0]+","+nearestWellGoingFromTank[1]+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")   $");
                        if(getCurrentCell(view) instanceof Well){
                            System.out.println("17>>");
                            Well currentCellStation = (Well) getCurrentCell(view);
                            return new DisposeWasteAction();
                        } else {
                            System.out.println("18>>");
                            return moveTowardsPointsAction(view, nearestWellGoingFromTank);
                        }
                    }
                    // tank -> fuelpump
                    else {
                        System.out.print("$FuelPumpToGo:"+"("+nearestFuelpumpGoingFromCurrent[0]+","+nearestFuelpumpGoingFromCurrent[1]+")   $"+"CurrentPosition:"+"("+tankPosX+","+tankPosY+")   $19>>");
                        if(getCurrentCell(view) instanceof FuelPump){
                            if(getFuelLevel() != 100){
                                return new RefuelAction();
                            }else{
                                return null;
                            }
                        } else {
                            return moveTowardsPointsAction(view, nearestFuelpumpGoingFromCurrent);
                        }
                    }
                }
            }
        }
        // No task left seenTasks
        else {
            int[] root = {0,0};
            // Find nearest fuelPump going from current tank position
            int[] nearestFuelpumpGoingFromCurrent = seenFuelpumps.get(getClosestIndexBetween(seenFuelpumps, new int[]{tankPosX, tankPosY}));
            int distanceBetweenRootAndCurrent = Math.max(Math.abs(root[0] - tankPosX),Math.abs(root[1] - tankPosY));

            if(getFuelLevel() > distanceBetweenRootAndCurrent){
                System.out.println("$WalkingAound 20>>");
                return walkingAround(view, timestep);
            } else {
                if(getCurrentCell(view) instanceof FuelPump){
                    if(getFuelLevel() == 100) {
                        System.out.println("$WalkingAound 40>>");
                        return walkingAround(view, timestep);
                    } else {
                        System.out.println("$Refuel 21>>");
                        return new RefuelAction();
                    }
                } else {
                    System.out.println("$GoToNearestFuelPump 25>>");
                    return moveTowardsPointsAction(view, nearestFuelpumpGoingFromCurrent);
                }
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

        int[] directionToGoArray = {horizontalMovement, verticalMovement};
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
      * Referesh the position of the tanker
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
     * Find a closest int[]'s index in the ArrayList from a given int[] point
     * @param   indicesList
     * @param   point
     * @return  the index of ArrayList
     */
    private int getClosestIndexBetween(ArrayList<int[]> indicesList, int[] point){
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

    private int getClosestNonallocatedTaskIndex(ArrayList<int[]> indicesList, int[] point){
        int furthestGoableDistance = 100;
        int closestIndex = -1;
        for(int i = 0;i < indicesList.size();i++){
        	int[] positionGoable = indicesList.get(i);
        	if(positionGoable[3] == tankerCount){
        		return i;
        	}
        }

        for(int i = 0;i < indicesList.size();i++){
            int[] positionGoable = indicesList.get(i);
        	if(positionGoable[3] == 0){
//                	System.out.println("size: " + indicesList.size() + " choose: " + i);
            	int distanceBetween = Math.max(Math.abs(positionGoable[0] - point[0]),Math.abs(positionGoable[1] - point[1]));
                if(distanceBetween <= furthestGoableDistance){
                    furthestGoableDistance = distanceBetween;
                    closestIndex = i;
                }
            }
        }
        if(closestIndex != -1){
        	seenTasks.set(closestIndex, new int[]{seenTasks.get(closestIndex)[0],seenTasks.get(closestIndex)[1],seenTasks.get(closestIndex)[2],tankerCount});
        }
        return closestIndex;
    }

	public void getInitialWalkingAroundPoints(ArrayList<int[]> initialWalkingAroundPoints){
		// Manually set four points which are followed by tanker in two cases:
        //  1. At the start of agent. In order to detect and memory a limit range of environment around the root point
        //  2. When there is no detected task known by tanker, looking around by following this path
		this.initialWlakingAroundPoints = initialWalkingAroundPoints;
	}
}
