package cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test {

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
	
	private void printUsage() {
		System.out.println("Usage: Java Test [test number (1 or 2)]"
				+ " [1st-level cache size] [2nd-level cache size (for test 2 only)]"
				+ " [file name]");
	}
}
