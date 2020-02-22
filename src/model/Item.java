package model;

import java.io.Serializable;

/** Class modeling an item. */
public final class Item implements Serializable {

	/** The name of the item. */
	public final String NAME;
	
	/** The weight of the item. */
	public final float WEIGHT;
	
	/** The value of the item. */
	public final float VALUE;

	
	/**
	 * Instanciates a new item given its properties.
	 * @param name the name of the item.
	 * @param w the weight of the item.
	 * @param v the value of the item.
	 */
	private Item(String name, float w, float v)
	{
		NAME = name;
		WEIGHT = w;
		VALUE = v;
	}

	
	/**
	 * Instanciate a new item by interpreting a CSV-type line.
	 * @param line the CSV-type line.
	 * 
	 * @throws ArrayIndexOutOfBoundsException if the line format is incorrect
	 * @throws NumberFormatException if the line format is incorrect
	 */
	protected Item(String line) 
			throws ArrayIndexOutOfBoundsException, NumberFormatException
	{
		this(
				line.split(";")[0].strip(),
				Float.parseFloat(line.split(";")[1].strip()),
				Float.parseFloat(line.split(";")[2].strip())
				);
	}
	
	

	@Override
	public String toString()
	{
		return NAME + " - Weight : " + WEIGHT + " , Value : " + VALUE;
	}
}
