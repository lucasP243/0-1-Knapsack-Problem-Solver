
package view;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import model.Backpack;
import model.methods.MethodList;

/**
 * Entry point of the application, manages the I/O interaction
 * @author Pinard Lucas
 */
public final class Appli {

	/** Path to the file in which the backpacks are stored. */
	private static final String PATH = "./data";

	/** Table storing the setted backpacks. */ 
	private Map<String, Backpack> backpacks;

	
	
	/**
	 * Initialize the application to load saved backpacks.
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		System.out.println("Initialization...");
		try {
			new File(PATH).createNewFile();
			ObjectInputStream ois = 
					new ObjectInputStream(new FileInputStream(PATH));
			backpacks = (Map<String, Backpack>) ois.readObject();
			ois.close();
		}
		catch (EOFException e)
		{
			System.out.println("No elements found.");
		}
		catch (IOException | ClassNotFoundException e)
		{
			System.err.println("Initialization failed.");
		}
		if (backpacks == null) backpacks = new HashMap<>();
		else System.out.println("Loaded " + backpacks.size() + " elements.");
		System.out.println("Initialization success!");
	}
	
	
	/**
	 * Starts the command prompt.
	 * @param in this application's instance of Scanner
	 */
	private void start(Scanner in)
	{
		String[] cmd;
		do try{
			System.out.print("$>");
			switch ((cmd = in.nextLine().toLowerCase().split(" "))[0].strip())
			{
			case "":
				continue;

			case "set":
				// set <name> <charge> <path>
				if (cmd.length != 4)
				{
					System.err.println(showError());
					break;
				}
				set(cmd[1], cmd[2], cmd[3]);
				break;


			case "show":
				// show [name]
				if (cmd.length > 2)
				{
					System.err.println(showError());
					break;
				}
				if (cmd.length == 1)
					show();
				else
					show(cmd[1]);
				break;


			case "unset":
				// unset <name>
				if (cmd.length != 2)
				{
					System.err.println(showError());
					break;
				}
				unset(cmd[1]);
				break;


			case "solve":
				// solve <name> <method>
				if (cmd.length != 3)
				{
					System.err.println(showError());
					break;
				}
				solve(cmd[1], cmd[2]);
				break;


			case "methods":
				methods();
				break;


			case "help":
				System.out.println(showHelp());
				break;

			case "close":
			case "exit":
			case "quit":
				return;

			default:
				System.err.println(showError());
				continue;
			} 
		} 
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.err.println(showError());
		}
		while (true);
	}

	
	
	/**
	 * Saves the backpacks and closes the command prompt, which brings to the
	 * application's end.
	 */
	private void close() {
		try
		{
			ObjectOutputStream oos =
					new ObjectOutputStream(new FileOutputStream(PATH));
			oos.writeObject(backpacks);
			oos.close();
			System.out.println(
					"Sucessfully saved " + backpacks.size() + " elements.");
		}
		catch (IOException e)
		{
			System.err.println("Saving failed.");
		}
	}

	
	/**
	 * Creates a new backpack and the list of items linked to it.
	 * @param name the name of the backpack
	 * @param charge maximal value for the backpack's charge
	 * @param path a path to the file listing the items
	 */
	private void set(String name, String charge, String path) {
		try
		{
			backpacks.put(name, new Backpack(path, Integer.parseInt(charge)));
		} 
		catch (NumberFormatException e) {
			System.out.println("Invalid number format. "+
					"Charge should be a strictly positive integer.");
		} catch (FileNotFoundException e) {
			System.out.println("Invalid path.");
		}
	}

	
	/**
	 * Show the names of the existing backpacks.
	 */
	private void show() 
	{
		if (backpacks.keySet().isEmpty())
			System.out.println("There is no backpacks.");
		else for (String name : backpacks.keySet())
			System.out.print(name + " ");
		System.out.println();
	}

	
	/**
	 * Shows the details of a backpack, given its name.
	 * @param name the name of the backpack
	 */
	private void show(String name)
	{
		if (backpacks.get(name) == null)
			System.out.println(name + " does not exists.");
		else System.out.println(backpacks.get(name).toString());
	}

	
	
	/**
	 * Deletes a backpack, given its name.
	 * @param name the name of the backpack
	 */
	private void unset(String name)
	{
		if (backpacks.remove(name) == null)
			System.out.println(name + " does not exists.");
	}

	
	
	/**
	 * Instanciate a solving procedure for the given backpack, with the given
	 * method, and displays its output.
	 * @param name the name of the backpack to solve
	 * @param method the method to use to solve the backpack
	 * 
	 * @see Appli#methods()
	 */
	private void solve(String name, String method)
	{
		try
		{
			Backpack b = backpacks.get(name);
			System.out.println(b.showSolution(MethodList.valueOf(
					method.toUpperCase()).newInstance(
							b.MAX_CHARGE, b.getItems()
							).solve()));
		} 
		catch (NullPointerException e)
		{
			System.err.println("Invalid name");
		}
		catch (IllegalArgumentException e)
		{
			System.err.println("Invalid method");
		}
		catch (InstantiationException e)
		{
			System.err.println("Error while getting solver.");
		}
	}

	
	
	/**
	 * Shows the available methods for solving the backpack
	 * 
	 * @see Appli#solve(String, String)
	 */
	private void methods()
	{
		for (MethodList m : MethodList.values())
			System.out.print(m.toString() + " ");
		System.out.println();
	}

	
	/**
	 * Gives a string containing the help page
	 * @return the help page
	 */
	private String showHelp() {
		return
				"--- Help Page ---" +
				System.lineSeparator()+
				" - set <name> <charge> <path> : creates a backpack and the"+
				" list of items that could be put inside.\n"+
				"\tname\t : reference to the backpack\n"+
				"\tcharge\t : maximal charge for the backpack\n"+
				"\tpath\t : path to the CSV-type text file listing the items\n"+
				System.lineSeparator()+
				" - show : shows the names of all the existing backpacks.\n"+
				System.lineSeparator()+
				" - show <name> : show details of an existing backpack.\n"+
				"\tname\t : :reference to the backpack\n"+
				System.lineSeparator()+
				" - unset <name> : deletes an existing backpack.\n"+
				"\tname\t : :reference to the backpack\n"+
				System.lineSeparator()+
				" - solve <backpack> <method> : solves a 0/1 knapsack problem"+
				" using the given backpack and specified method.\n"+
				"\tbackpack : a reference to the backpack to use\n" +
				"\tmethod\t : the solving algorithm to use on the backpack\n"+
				System.lineSeparator()+
				" - methods : shows the methods available.\n"+
				System.lineSeparator()+
				" - close/quit/exit : shut down this application."+
				System.lineSeparator()+
				"-----------------";
	}

	
	/**
	 * Gives a string containing an error message.
	 * @return the error message
	 */
	private String showError() {
		return 
				"Invalid syntax. Type \"help\" to see a list of "
				+ "available commands and their syntax.";
	}


	/** Main method of the project */
	public static void main(String[] args) {
	
		Scanner in = new Scanner(System.in);
		
		Appli thisApp = new Appli();
		thisApp.init();
		thisApp.start(in);
		thisApp.close();
		
		in.close();
	}

}
