package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/** Class modeling a backpack. */
public final class Backpack implements Serializable {

	/** Maximal value for the charge of this backpack. */
	public final int MAX_CHARGE;

	/** List of items which can be put inside the backpack. */
	private Item[] itemList;

	
	/**
	 * Instanciates a backpack.
	 * @param path the path to a file listing the items
	 * @param maxCharge the maximal value for the charge of this backpack
	 * 
	 * @throws FileNotFoundException if the path doesn't lead to a proper file
	 * @throws NumberFormatException if the file's format is incorrect
	 */
	public Backpack(String path, int maxCharge) 
			throws FileNotFoundException, NumberFormatException
	{
		readFile(path);
		if (maxCharge <= 0) throw new NumberFormatException();
		MAX_CHARGE = maxCharge;
	}

	
	/**
	 * Getter for the item list.
	 * @return a copy of the item list
	 */
	public Item[] getItems() {
		return Arrays.copyOf(itemList, itemList.length);
	}

	
	/**
	 * Reads a file, instanciates the proper items and stores them in the list.
	 * @param path the path to a file listing the items
	 * 
	 * @throws FileNotFoundException if the path doesn't lead to a proper file
	 */
	private void readFile(String path) throws FileNotFoundException {
		BufferedReader in = new BufferedReader(new FileReader(path));
		List<Item> list = new LinkedList<>();

		String line;
		try
		{
			while ((line = in.readLine()) != null)
				list.add(new Item(line));
			in.close();
		}
		catch (IOException e) { e.printStackTrace(); }

		list.toArray(itemList = new Item[list.size()]);
	}

	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();

		out.append("Maximal charge for the backpack : " + MAX_CHARGE);
		out.append(System.lineSeparator() + System.lineSeparator());

		out.append("Items that can be put inside : ");
		out.append(System.lineSeparator());
		for (Item i : itemList)
			out.append(i + System.lineSeparator());
		out.append(System.lineSeparator());
		
		return out.toString();
	}
	
	public String showSolution(boolean[] s)
	{
		if (itemList.length != s.length) return "Error.";
		
		float v = 0, w = 0;
		StringBuilder out = new StringBuilder();
		out.append("Selected items :" + System.lineSeparator());
		for (int i = 0; i < itemList.length; ++i) if (s[i])
		{
			out.append(itemList[i].toString() + System.lineSeparator());
			v += itemList[i].VALUE;
			w += itemList[i].WEIGHT;
		}
		out.append(System.lineSeparator());
		out.append("Value : " + v + System.lineSeparator());
		out.append("Weight : " + w + System.lineSeparator());
		
		return out.toString();
	}
}
