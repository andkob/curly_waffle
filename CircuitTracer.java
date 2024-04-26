import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail
 */
public class CircuitTracer {
	private static enum outputTypes {console, gui};
	private static outputTypes outputType;
	private Storage<TraceState> storage;
	private ArrayList<TraceState> bestPaths;

	/** launch the program
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		new CircuitTracer(args); //create this with args
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private void printUsage() {
		System.out.println("Usage: java CircuitTracer [-s stack | -q queue] [-c console output | "
				+ "-g GUI output] fileName (ex. inputFile.dat)");
	}
	
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	public CircuitTracer(String[] args) {
		// Parse and validate command line args - first validation provided
		if (args.length != 3) {
			printUsage();
			return; //exit the constructor immediately
		}
		// Initialize storage to use either a stack or a queue
		if (args[0].equals("-s")) {
			storage = Storage.getStackInstance();
		} else if (args[0].equals("-q")) {
			storage = Storage.getQueueInstance();
		} else {
			printUsage();
			return;
		}
		
		// Initialize bestPaths as an empty list
		bestPaths = new ArrayList<>();
		
		// Select either console output or GUI output
		if (args[1].equals("-c")) {
			outputType = outputTypes.console;
		} else if (args[1].equals("-g")) {
			outputType = outputTypes.gui;
		} else {
			printUsage();
			return;
		}
		
		// Read in the CircuitBoard from the given file
		CircuitBoard startingBoard;
		try {
			startingBoard = new CircuitBoard(args[2]);
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException: " + e.getMessage());
			printUsage();
			return;
		} catch (InvalidFileFormatException e) {
			System.out.println(e.getMessage());
			return;
		}
		
		// Create initial trace states
		Point point = startingBoard.getStartingPoint();
		TraceState ts = null;
		if (startingBoard.isOpen(point.x+1, point.y)) {
			ts = new TraceState(startingBoard, point.x+1, point.y);
			storage.store(ts);
		}
		if (startingBoard.isOpen(point.x, point.y+1)) {
			ts = new TraceState(startingBoard, point.x, point.y+1);
			storage.store(ts);
		}
		if (startingBoard.isOpen(point.x-1, point.y)) {
			ts = new TraceState(startingBoard, point.x-1, point.y);
			storage.store(ts);
		}
		if (startingBoard.isOpen(point.x, point.y-1)) {
			ts = new TraceState(startingBoard, point.x, point.y-1);
			storage.store(ts);
		}
		
		// Run the search for best paths
		search();

		// Output results to console or GUI, according to specified choice
		if (outputType == outputTypes.console) {
			if (!bestPaths.isEmpty()) {
				// Print all shortest paths
				for (int i = 0; i < bestPaths.size(); i++) {
					System.out.println(bestPaths.get(i).getBoard().toString());
				}
			}
		} else {
			System.out.println("GUI is not currently supported");
		}
	}
	
	public void search() {
		while (!storage.isEmpty()) {
			TraceState ts = storage.retrieve();
			if (ts.isComplete()) {
				if (bestPaths.isEmpty() || ts.pathLength() == bestPaths.get(0).pathLength()) {
					bestPaths.add(ts);
				} else if (ts.pathLength() < bestPaths.get(0).pathLength()) {
					bestPaths.clear();
					bestPaths.add(ts);
				}
			} else {
				generateTraceStates(ts);
			}
		}
	}
	
	private void generateTraceStates(TraceState prevTrace) {
		Point point = new Point(prevTrace.getRow(), prevTrace.getCol()); // get last trace made
		TraceState newTrace = null;
		if (prevTrace.isOpen(point.x+1, point.y)) {
			newTrace = new TraceState(prevTrace, point.x+1, point.y);
			storage.store(newTrace);
		}
		if (prevTrace.isOpen(point.x, point.y+1)) {
			newTrace = new TraceState(prevTrace, point.x, point.y+1);
			storage.store(newTrace);
		}
		if (prevTrace.isOpen(point.x-1, point.y)) {
			newTrace = new TraceState(prevTrace, point.x-1, point.y);
			storage.store(newTrace);
		}
		if (prevTrace.isOpen(point.x, point.y-1)) {
			newTrace = new TraceState(prevTrace, point.x, point.y-1);
			storage.store(newTrace);
		}
	}	
} // class CircuitTracer
