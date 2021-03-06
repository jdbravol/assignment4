/* CRITTERS Critter.java
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
package assignment4;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> livingCritters = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	
	// this hash set keeps track of all the critters alive at one time
	//public static HashSet<Critter> livingCritters = new HashSet<Critter>(0);		
	
	// this matrix keeps track of critters at each specific location
	public static Locations[][] locationMatrix = locationMatrixInit();

    //inFight is a flag that will detect if a critter is in a fight or not
    private static boolean inFight;
    private boolean fightMoved;

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	
	static private class Locations{
		protected HashSet<Critter> inHere;
		
		Locations(){
			inHere = new HashSet<Critter>(0);
		}
	}
	
	/**
	 * This private method initiates a new locationMatrix
	 * @name locationMatrixInit
	 * @returns an initialized location matrix of empty locations
	 */
	private static Locations[][] locationMatrixInit(){
		Locations[][] newLocations = new Locations[Params.world_height][Params.world_width];
		
		for(int row = 0; row < Params.world_height; row++){
			for(int col = 0; col < Params.world_width; col++){
				newLocations[row][col] = new Locations();
			}
		}
		return newLocations;
	}


    public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
    private boolean haveMoved;

    private void changeCoordinates(int steps, int direction){
        switch (direction) {
            case 0:
                this.x_coord += steps;
                this.x_coord %= Params.world_width;
                break;
            case 1:
                this.x_coord += steps;
                this.x_coord %= Params.world_width;
                this.y_coord -= steps;
                if(this.y_coord < 0){
                	this.y_coord = Params.world_height - 1;
                }
                break;
            case 2:
                this.y_coord -= steps;
                if(this.y_coord < 0){
                	this.y_coord = Params.world_height - 1;
                }
                break;
            case 3:
                this.x_coord -= steps;
                if(this.x_coord < 0){
                	this.x_coord = Params.world_width - 1;
                }
                this.y_coord -= steps;
                if(this.y_coord < 0){
                	this.y_coord = Params.world_height - 1;
                }
                break;
            case 4:
                this.x_coord -= steps;
                if(this.x_coord < 0){
                	this.x_coord = Params.world_width - 1;
                }
                break;
            case 5:
                this.x_coord -= steps;
                if(this.x_coord < 0){
                	this.x_coord = Params.world_width - 1;
                }
                this.y_coord += steps;
                this.y_coord %= Params.world_height;
                break;
            case 6:
                this.y_coord += steps;
                this.y_coord %= Params.world_height;
                break;
            case 7:
                this.x_coord += steps;
                this.x_coord %= Params.world_width;
                this.y_coord += steps;
                this.y_coord %= Params.world_height;
                break;
        }
    }
    
    /**
     * @name checkDeath
     * @description checks if cell is available for moving into during fight
     * @param cell in HashSet form
     * @return boolean telling if available or not
     */
    private boolean checkDeath(HashSet<Critter> cell){
    	if(cell.size() == 0){
    		return true;
    	}
    	
    	// check if all dead
    	for(Critter checkIfDead: cell){
    		if(checkIfDead.energy > 0){
    			return false;
    		}
    	}
    	return true;
    }

    /**
     * moveInFight will perform appropiate movement for object
     */
    private void moveInFight(int steps, int direction){
    	int newX;
    	int newY;
        switch (direction) {
            case 0:
            		newX = this.x_coord + steps;
            		newX %= Params.world_width;
            	
                if (checkDeath(locationMatrix[this.y_coord][newX].inHere)) {
                    this.x_coord += steps;
                    this.x_coord %= Params.world_width;
                    this.fightMoved = true;
                }
                break;
            case 1:
            		newX = this.x_coord + steps;
            		newX %= Params.world_width;
            	
            		newY = this.y_coord - steps;
            		if(newY < 0){
            			newY = Params.world_height - 1;
                }
            		
                if (checkDeath(locationMatrix[newY][newX].inHere)) {
                    this.x_coord += steps;
                    this.x_coord %= Params.world_width;
                    this.y_coord -= steps;
                    if(this.y_coord < 0){
                    	this.y_coord = Params.world_height - 1;
                    }
                    this.fightMoved = true;
                }
                break;
            case 2:
            		newY = this.y_coord - steps;
            		if(newY < 0){
            			newY = Params.world_height - 1;
                }
            		
                if (checkDeath(locationMatrix[newY][this.x_coord].inHere)) {
                    this.y_coord -= steps;
                    if(this.y_coord < 0){
                    	this.y_coord = Params.world_height - 1;
                    }
                    this.fightMoved = true;
                }
                break;
            case 3:
            		newX = this.x_coord - steps;
            		if(newX < 0){
            			newX = Params.world_width - 1;
                }
            	
            		newY = this.y_coord - steps;
            		if(newY < 0){
            			newY = Params.world_height - 1;
                }
            		
                if (checkDeath(locationMatrix[newY][newX].inHere)) {
                    this.x_coord -= steps;
                    if(this.x_coord < 0){
                    	this.x_coord = Params.world_width - 1;
                    }
                    
                    this.y_coord -= steps;
                    if(this.y_coord < 0){
                    	this.y_coord = Params.world_height - 1;
                    }
                    this.fightMoved = true;
                }
                break;
            case 4:
            		newX = this.x_coord - steps;
            		if(newX < 0){
            			newX = Params.world_width - 1;
                }
            	
                if (checkDeath(locationMatrix[this.y_coord][newX].inHere)) {
                    this.x_coord -= steps;
                    if(this.x_coord < 0){
                    	this.x_coord = Params.world_width - 1;
                    }
                    this.fightMoved = true;
                }
                break;
            case 5:
            		newX = this.x_coord - steps;
                if(newX < 0){
                		newX = Params.world_width - 1;
                }
                
                newY = this.y_coord + steps;
                newY %= Params.world_height;
            	
                if (checkDeath(locationMatrix[newY][newX].inHere)) {
                    this.x_coord -= steps;
                    if(this.x_coord < 0){
                    	this.x_coord = Params.world_width - 1;
                    }
                    this.y_coord += steps;
                    this.y_coord %= Params.world_height;
                    this.fightMoved = true;
                }
                break;
            case 6:
                newY = this.y_coord + steps;
                newY %= Params.world_height;
            	
                if (checkDeath(locationMatrix[newY][this.x_coord].inHere)) {
                    this.y_coord += steps;
                    this.y_coord %= Params.world_height;
                    this.fightMoved = true;
                }
                break;
            case 7:
                newX = this.x_coord + steps;
                newX %= Params.world_width;
                
                newY = this.y_coord + steps;
                newY %= Params.world_height;
            	
                if (checkDeath(locationMatrix[newY][newX].inHere)) {
                    this.x_coord += steps;
                    this.x_coord %= Params.world_width;
                    this.y_coord += steps;
                    this.y_coord %= Params.world_height;
                    this.fightMoved = true;
                }
                break;
        }
    }
	protected final void walk(int direction) {
        this.energy -= Params.walk_energy_cost;
        if(!haveMoved){
            locationMatrix[this.y_coord][this.x_coord].inHere.remove(this);
        }
        if(inFight && !haveMoved){
            moveInFight(1, direction);
            locationMatrix[this.y_coord][this.x_coord].inHere.add(this);
            this.haveMoved = true;

        }
        else if(!haveMoved) {
            changeCoordinates(1, direction);
            locationMatrix[this.y_coord][this.x_coord].inHere.add(this);
            this.haveMoved = true;
        }
	}
	
	protected final void run(int direction) {
		this.energy -= Params.run_energy_cost;
        if(!haveMoved){
            locationMatrix[this.y_coord][this.x_coord].inHere.remove(this);
        }
        if(inFight && !haveMoved){
            moveInFight(2, direction);
            locationMatrix[this.y_coord][this.x_coord].inHere.add(this);
            this.haveMoved = true;
        }
        else if(!haveMoved) {
            changeCoordinates(2, direction);
            locationMatrix[this.y_coord][this.x_coord].inHere.add(this);
            this.haveMoved = true;
        }
	}

    /**
     *
     * @param offspring
     * @param direction
     */
	protected final void reproduce(Critter offspring, int direction) {
		// confirm parent has enough energy
		if(this.energy < Params.min_reproduce_energy){
			return;
		}
		
		offspring.energy = this.energy / 2;						// assign offspring half of parent's energy (round down);
		this.energy = (this.energy / 2) + (this.energy % 2);	// assign parent half of parent's energy (round up);
		
		// assign coordinates to child 
		offspring.x_coord = this.x_coord;
		offspring.y_coord = this.y_coord;
		changeCoordinates(1, direction);
		
		// add offspring to array list
		babies.add(offspring);
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String opponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
        try{

        	// dynamically create class:
            Critter newCritter = (Critter) Class.forName(myPackage + "." + critter_class_name).newInstance(); 
            newCritter.x_coord = getRandomInt(Params.world_width);      				// sets random x axis
            newCritter.y_coord = getRandomInt(Params.world_height);     				// sets random y axis
            newCritter.energy = Params.start_energy;                        			// sets starting energy
            livingCritters.add(0, newCritter);				                    			// adds to living hashset
            locationMatrix[newCritter.y_coord][newCritter.x_coord].inHere.add(newCritter);		// add new critter to contents of such location

        }
        // catch invalid critter errors
        catch(ClassNotFoundException ex){
            throw new InvalidCritterException(critter_class_name);
        } catch (InstantiationException e) {
            throw new InvalidCritterException(critter_class_name);
        } catch (IllegalAccessException e) {
            throw new InvalidCritterException(critter_class_name);
        }
    }
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
        try{
        	// dynamically create class:
            for (Critter maybeInstance: livingCritters){
            Class givenCritter = Class.forName(myPackage + "." + critter_class_name);
                if (givenCritter.isInstance(maybeInstance)) {
                    result.add(0, maybeInstance);
                 }

            }
            
        }
        // catch invalid critter errors
        catch(ClassNotFoundException ex) {
            throw new InvalidCritterException(critter_class_name);
        }
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
            locationMatrix[this.getY_coord()][this.getX_coord()].inHere.remove(this);
			super.x_coord = new_x_coord;
            locationMatrix[this.getY_coord()][this.getX_coord()].inHere.add(this);
		}
		
		protected void setY_coord(int new_y_coord) {
            locationMatrix[this.getY_coord()][this.getX_coord()].inHere.remove(this);
			super.y_coord = new_y_coord;
            locationMatrix[this.getY_coord()][this.getX_coord()].inHere.add(this);

		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() { 	//gets the y coordinate of the critter
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return livingCritters;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
        livingCritters.clear();
        locationMatrix = locationMatrixInit();
	}
	
	/**
	 * @name worldTimeStep
	 * @description goes through the seven necessary steps of the World Time Step
	 */
	public static void worldTimeStep() {
		// 1: do everyone's time steps
        for(Critter critter: livingCritters){
            critter.doTimeStep();
        }

        // 2: resolve all encounters
        resolveEncounters();
        
        // 3: update rest energy
        for(Critter critter: livingCritters){
            critter.energy -= Params.rest_energy_cost;
        }
        
        // 4: generate algae
        generateAlgae();
        
        // 5: move babies into population and clear babies list
        Iterator<Critter> babyIterator = babies.iterator();
        Critter newBaby;
        while(babyIterator.hasNext()){
        	newBaby = babyIterator.next();
        	livingCritters.add(0, newBaby);
        	locationMatrix[newBaby.y_coord][newBaby.x_coord].inHere.add(newBaby);
        }
        babies.clear();
        
        // 6: remove all dead
        Iterator<Critter> livingIterator = livingCritters.iterator();
        Critter maybeDeadCrit;
        while(livingIterator.hasNext()){
        	maybeDeadCrit = livingIterator.next();
        	if(maybeDeadCrit.energy <= 0){
        		livingCritters.remove(maybeDeadCrit);
        		locationMatrix[maybeDeadCrit.y_coord][maybeDeadCrit.x_coord].inHere.remove(maybeDeadCrit);
        		livingIterator = livingCritters.iterator();
        	}
        }
        
        // 7: reset haveMoved and fightMoved
        for(Critter critter: livingCritters){
            critter.haveMoved = false;
            critter.fightMoved = false;
        }
	}
	
	
	/**
	 * @name generateAlgae
	 * @description creates algae for the world
	 */
	private static void generateAlgae(){
		for(int i = 0; i < Params.refresh_algae_count; i++){
			Critter newAlgae = new Algae();
			newAlgae.energy = Params.start_energy;
			newAlgae.x_coord = Critter.getRandomInt(Params.world_width);
			newAlgae.y_coord = Critter.getRandomInt(Params.world_height);
			livingCritters.add(0, newAlgae);
			locationMatrix[newAlgae.y_coord][newAlgae.x_coord].inHere.add(newAlgae);
		}

	}

	/**
	 * @name resolveEncounters
	 * @description resolves all encounters in the world
	 */
	private static void resolveEncounters(){
        inFight = true;         // set fighting
        for(int row = 0; row < Params.world_height; row++){
            for (int col = 0; col < Params.world_width; col++){
            	// extract all critters living in current cell
            	HashSet<Critter> livingInCell = new HashSet<Critter>(0);
            	HashSet<Critter> currentCell = locationMatrix[row][col].inHere;
            	for(Critter maybeAlive: currentCell){
            		if(maybeAlive.energy > 0){
            			livingInCell.add(maybeAlive);
            		}
            	}
                while (livingInCell.size() > 1){
                    Iterator<Critter> encounterIt = livingInCell.iterator();
                    Critter critterA = encounterIt.next();
                    Critter critterB = encounterIt.next();

                    boolean responseA = critterA.fight(critterB.toString());
                    boolean responseB = critterB.fight(critterA.toString());

                    if (critterA.fightMoved || critterB.fightMoved){
                        if (critterA.fightMoved && critterB.fightMoved){
                            livingInCell.remove(critterA);
                            livingInCell.remove(critterB);
                            continue;
                        }
                        else if (critterA.fightMoved){
                        	livingInCell.remove(critterA);
                        	continue;
                        }
                        else{
                        	livingInCell.remove(critterB);
                        	continue;
                        }
                    }

                    if (critterA.energy <= 0 || critterB.energy <= 0){
                        if (critterA.energy <= 0 && critterB.energy <=0){
                            livingInCell.remove(critterA);
                            livingInCell.remove(critterB);
                            continue;
                        }
                        else if (critterA.energy <= 0){
                        	livingInCell.remove(critterA);
                        	continue;
                        }
                        else{
                        	livingInCell.remove(critterB);
                        	continue;
                        }
                    }

                    if ((critterA.energy>0) && (critterB.energy>0) && (!critterA.fightMoved) && (!critterB.fightMoved)){
                        //calculate A's roll
                        int rollA;
                        int rollB;
                        if (responseA){
                            rollA = Critter.getRandomInt(critterA.energy+1);
                        }
                        else {
                            rollA = 0;
                        }

                        //calculate B's roll
                        if (responseB){
                            rollB = Critter.getRandomInt(critterB.energy+1);
                        }
                        else {
                            rollB = 0;
                        }

                        //compare rolls
                        if (rollA >= rollB){
                            critterA.energy += (critterB.energy / 2);
                            critterB.energy = 0;
                            livingInCell.remove(critterB);
                            continue;
                        }

                        else{
                            critterB.energy += (critterA.energy / 2);
                            critterA.energy = 0;
                            livingInCell.remove(critterA);
                            continue;
                        }
                    }

                }
            }
        }

        inFight = false;        // clear fighting
	}
	
	

    /**
     * topBottom  will create the top and bottom lines of the world in the display
     */
	private static void topBottom(){
		System.out.print("+");
		for (int i = 0; i < Params.world_width; i++){
			System.out.print("-");
		}
		System.out.println("+");
	}

    /**
     * displayWorld will display in the console the critter world, it utilizes the matrix CritterWorld
     */
	public static void displayWorld() {
		topBottom();        //header of the world
        for (int row = 0; row < Params.world_height; row++) {         //will go row by row outputting critter in location
            System.out.print("|");
            for (int col = 0; col < Params.world_width; col++) {
                if (!locationMatrix[row][col].inHere.isEmpty()){
                    Iterator<Critter> singleCritterIterator = locationMatrix[row][col].inHere.iterator();
                    System.out.print(singleCritterIterator.next().toString());
                }
                else{
                    System.out.print(" ");
                }
            }
            System.out.print("|");
            System.out.println("");
        }
		topBottom();
	}
}
