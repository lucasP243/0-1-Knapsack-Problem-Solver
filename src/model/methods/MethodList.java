package model.methods;

import model.Item;

/** Solving method factory. */
public enum MethodList {

	GREEDY(GreedyBackpack.class),
	DYNAMIC(DynamicBackpack.class),
	BNB(BnBBackpack.class);

	/** The corresponding method. */
	private Class<? extends AbstractMethod> method;

	/** Basic enum-like constructor */
	private MethodList(Class<? extends AbstractMethod> method)
	{
		this.method = method;
	}

	
	/**
	 * Generates a new instance of a solving method.
	 * @param maxCharge the maximal charge to assign to the method
	 * @param itemList the item list to assign to the method
	 * @return the generated method
	 * 
	 * @throws InstantiationException if the instantiation failed somehow
	 */
	public AbstractMethod newInstance(int maxCharge, Item[] itemList)
			throws InstantiationException
	{
		try {
			return method
					.getConstructor(int.class, Item[].class)
					.newInstance(maxCharge, itemList);
		} catch (Exception e) {
			e.printStackTrace();
			e.getCause();
			e.getMessage();
			throw new InstantiationException("Error with accessing methods.");
		}
	}
}
