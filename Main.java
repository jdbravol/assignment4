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

	static Scanner kb; // scanner connected to keyboard input, or input file
	private static String inputFile; // input file, used instead of keyboard
										// input if specified
	static ByteArrayOutputStream testOutputString; // if test specified, holds
													// all console output
	private static String myPackage; // package of Critter file. Critter cannot
										// be in default pkg.
	private static boolean DEBUG = false; // Use it or not, as you wish!
	static PrintStream old = System.out; // if you want to restore output to
											// console

	// Gets the package name. The usage assumes that Critter and its subclasses
	// are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            args can be empty. If not empty, provide two parameters -- the
	 *            first is a file name, and the second is test (for test output,
	 *            where all output to be directed to a String), or nothing
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
				if (args[1].equals("test")) { // if the word "test" is the
												// second argument to java
					// Create a stream to hold the output
					testOutputString = new ByteArrayOutputStream();
					PrintStream ps = new PrintStream(testOutputString);
					// Save the old System.out.
					old = System.out;
					// Tell Java to use the special stream; all console output
					// will be redirected here from now
					System.setOut(ps);
				}
			}
		} else { // if no arguments to main
			kb = new Scanner(System.in); // use keyboard and console
		}

		/* Do not alter the code above for your submission. */
		/* Write your code below. */
		String inputLine = "";
		String[] inputs;

		while (true) {
			System.out.print("critters>");
			try {
				inputLine = kb.nextLine();
				inputs = inputLine.split("\\s++");
				
				if(inputs[0].equals("")){
					for(int i = 1; i < inputs.length; i++){
						inputs[i - 1] = inputs[i];
					}
					String[] newInputs = new String[inputs.length - 1];
					for(int i = 0; i < newInputs.length; i++){
						newInputs[i] = inputs[i];
					}
					inputs = newInputs;
				}

				if (inputs[0].equals("quit")) { // if the command input in
												// console is quit, terminates
					if (inputs.length > 1) {
						throw new Exception();
					}
					break;
				} else if (inputs[0].equals("show")) { // will show the world
					if (inputs.length > 1) {
						throw new Exception();
					}
					Critter.displayWorld();
				} else if (inputs[0].equals("step")) { // will invoke
														// WorldTimeStep
					int steps;
					if (inputs.length > 2) {
						throw new Exception();
					}
					if (inputs.length == 2) { // has a count
						steps = Integer.parseInt(inputs[1]);
					} else {
						steps = 1;
					}
					for (int i = 0; i < steps; i++) { // worldTimeStep for
														// specific amount of
														// times
						Critter.worldTimeStep();
					}
				} else if (inputs[0].equals("seed")) {
					if (inputs.length != 2) {
						throw new Exception();
					}
					Critter.setSeed(Integer.parseInt(inputs[1]));
				} else if (inputs[0].equals("make")) {
					String className = inputs[1];
					int countMakes;
					if (inputs.length > 3) {
						throw new Exception();
					}
					if (inputs.length == 3) {
						countMakes = Integer.parseInt(inputs[2]);
					} else {
						countMakes = 1;
					}
					for (int i = 0; i < countMakes; i++) {
						Critter.makeCritter(className);
					}
				} else if (inputs[0].equals("stats")) {
					if (inputs.length > 2) {
						throw new Exception();
					}

					String crit = inputs[1];
					
					Class<?> critterClass = Class.forName(myPackage + "." + crit);
					java.util.List<Critter> critters = Critter.getInstances(crit);
					
					
					Method method = critterClass.getMethod("runStats", List.class);
					method.invoke(critterClass, critters);

				} else {
					System.out.println("invalid command: " + inputLine);
				}
			} catch (Exception e) {
				System.out.println("error processing: " + inputLine);
			}
		}
	}
}
