package cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This is a Test class with a main method that serves as the entry point for the program. 
 * The program takes command-line arguments to determine the type of test to run, cache sizes, 
 * and the file to process. The code utilizes a Cache class to simulate caching behavior, 
 * specifically implementing a one or two-level cache system.
 * 
 * @author Andrew Kobus
 * @see Cache
 *
 */

public class Test {
	
	/**
     * Entry point for the program. Parses command-line arguments to determine the test type,
     * cache sizes, and the file to process. Invokes the runTest method accordingly.
     * Prints usage information if the provided arguments are incorrect.
     *
     * @param args Command-line arguments specifying the test number, cache sizes, and file name.
     */
	public static void main(String[] args) {
		Test tester = new Test();
		if (args[0].equals("1")) {
			tester.runTest(1, args[1], null, args[2]);
		} else if (args[0].equals("2")) {
			tester.runTest(2, args[1], args[2], args[3]);
		} else {
			tester.printUsage();
		}
	}
	
	/**
     * Runs a caching test based on the specified test number, cache sizes, and file name.
     * Creates a first-level cache and optionally a second-level cache.
     * Reads the specified file, updating the caches based on the test requirements.
     * Records and prints statistics such as execution time, number of references, cache hits, and hit ratio.
     *
     * @param testNum    The test number (1 or 2) indicating the type of caching test to run.
     * @param cacheSize  The size of the first-level cache.
     * @param cache2Size The size of the second-level cache (for test 2 only).
     * @param fileName   The name of the file to process.
     */
	public void runTest(int testNum,
						String cacheSize,
						String cache2Size,
						String fileName) {
		int size1 = Integer.parseInt(cacheSize);
		Cache<String> cache = new Cache<String>(size1);
		System.out.println("First level cache with " + cacheSize + " entries has been created");
		Cache<String> cache2 = null;
		if (testNum == 2) {
			int size2 = Integer.parseInt(cache2Size);
			cache2 = new Cache<String>(size2);
			System.out.println("Second level cache with " + cache2Size + " entries has been created"
					+ "\n......................................");
		}
		File file = new File(fileName);
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found");
			printUsage();
			System.exit(0);
		}
		
		long startTime = System.currentTimeMillis();
		if (testNum == 1) {
			while (sc.hasNext()) {
				String word = sc.next();
				if (!cache.search(word)) {
					cache.addObject(word);
				}
			}
		} else if (testNum == 2) {
			while (sc.hasNext()) {
				String word = sc.next();
				if (!cache.search(word)) {
					if (!cache2.search(word)) {
						cache2.addObject(word);
						cache.addObject(word);
					} else {
						cache.addObject(word);
					}
				} else {
					cache2.moveToTop(word);
				}
			}
		}
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		
		if (testNum == 1) {
			System.out.println("Completed in " + totalTime + "ms");
			System.out.println("\nNumber of references: " + cache.getNR()
			+ "\nNumber of cache hits: " + cache.getNH()
			+ "\nCache hit ratio: " + cache.getHR());
		} else if (testNum == 2) {
			int globalNR = cache.getNR();
			int globalNH = cache.getNH() + cache2.getNH();
			double globalHR = (double) globalNH / (double) globalNR;
			System.out.println("Completed in " + totalTime + "ms");
			System.out.println("\nNumber of references: " + globalNR
			+ "\nNumber of cache hits: " + globalNH
			+ "\nCache hit ratio: " + globalHR);
			System.out.println("\n\nNumber of 1st-level references: " + cache.getNR()
			+ "\nNumber of 1st-level cache hits: " + cache.getNH()
			+ "\n1st-level cache hit ratio: " + cache.getHR()
			+ "\n\nNumber of 2nd-level references: " + cache2.getNR()
			+ "\nNumber of 2nd-level cache hits: " + cache2.getNH()
			+ "\n2nd-level cache hit ratio: " + cache2.getHR());
		}
	}
	
	/**
     * Prints a usage message indicating the correct command-line arguments for running the program.
     */
	private void printUsage() {
		System.out.println("Usage: Java Test [test number (1 or 2)]"
				+ " [1st-level cache size] [2nd-level cache size (for test 2 only)]"
				+ " [file name]");
	}
}
