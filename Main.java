/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Juan Bravo
 * jdb5338
 * 16475
 * Santiago Echeverri
 * se7365
 * 16470
 * Slip days used: <0>
 * Fall 2016
 */
package assignment4; // cannot be in default package
import java.util.Scanner;
import java.util.List;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */

public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing
     */
    public static void main(String[] args) {
		if (args.length != 0) {
			try {
				inputFile = args[0];
				kb = new Scanner(new File(inputFile));
			} catch (FileNotFoundException e) {
				System.out.println("USAGE: java Main OR java Main <input file> <test output>");
				e.printStackTrace();
			} catch (NullPointerException e) {
				System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
			}
			if (args.length >= 2) {
				if (args[1].equals("test")) { // if the word "test" is the second argument to java
					// Create a stream to hold the output
					testOutputString = new ByteArrayOutputStream();
					PrintStream ps = new PrintStream(testOutputString);
					// Save the old System.out.
					old = System.out;
					// Tell Java to use the special stream; all console output will be redirected here from now
					System.setOut(ps);
				}
			}
		} else { // if no arguments to main
			kb = new Scanner(System.in); // use keyboard and console
		}

        /* Do not alter the code above for your submission. */
        /* Write your code below. */
		try {
			while (true) {
				String input = kb.next();

				if (input.equals("quit")) {              //if the command input in console is quit, terminates
					break;
				} else if (input.equals("show")) {          // will show the world
					Critter.displayWorld();
				} else if (input.equals("step")) {          //will invoke WorldTimeStep
					int steps;
					if (kb.hasNext()) {                //has a count
						steps = kb.nextInt();
					} else {
						steps = 1;
					}
					for (int i = 0; i < steps; i++) {      //worldTimeStep for specific amount of times
						Critter.worldTimeStep();
					}
				} else if (input.equals("seed")) {
					Critter.setSeed(kb.nextInt());
				} else if (input.equals("make")) {
					String className = kb.next();
					int countMakes;
					if (kb.hasNextInt()) {
						countMakes = kb.nextInt();
					} else {
						countMakes = 1;
					}
					for (int i = 0; i < countMakes; i++) {
						Critter.makeCritter(className);
					}
				} else if (input.equals("stats")) {
					try {
						String crit = kb.next();
						Class<?> critterClass = Class.forName(myPackage + "." + crit);
						java.util.List<Critter> critters = Critter.getInstances(myPackage + "." + crit);
						try {
							Method method = critterClass.getMethod("runStats", List.class);
							method.invoke(critterClass, critters);
						} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
							e.printStackTrace();
						}
					} catch (ClassNotFoundException | InvalidCritterException e) {
						e.printStackTrace();
					}

				}
			}
		} catch (InvalidCritterException e) {
			e.printStackTrace();
		}
	}
}
