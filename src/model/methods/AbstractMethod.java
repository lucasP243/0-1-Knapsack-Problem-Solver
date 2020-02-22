package model.methods;

import model.Item;

/** Abstract class modeling a solving method. */
public abstract class AbstractMethod {

	/** The maximal value of the charge assigned to the solving method. */
	protected final int MAX_CHARGE;
	
	/** The list of items assigned to solving method. */
	protected Item[] itemList;
	
	/**
	 * Instanciates a solving method.
	 * @param maxCharge the maximal value for the charge
	 * @param list the list of items 
	 */
	public AbstractMethod(int maxCharge, Item[] list)
	{
		MAX_CHARGE = maxCharge;
		itemList = list;
	}
	
	
	/**
	 * Initiates the implemented solving algorithm to solve the backpack.
	 * @return an array of booleans representing if the item with the same
	 * index is inside the backpack in the optimal solution.
	 */
	public abstract boolean[] solve();
}
