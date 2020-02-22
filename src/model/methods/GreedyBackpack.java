package model.methods;

import java.util.Arrays;
import java.util.Comparator;

import model.Item;

/** Implementation of a solving method using a greedy algorithm. */
class GreedyBackpack extends AbstractMethod {

	/** Constructor calling back the super-constructor. */
	public GreedyBackpack(int maxCharge, Item[] list) {
		super(maxCharge, list);
	}

	@Override
	public boolean[] solve()
	{

		boolean[] itemsInBag = new boolean[itemList.length];
		float actualWeight = 0;
		for (int i : getSortedIndexes())
		{
			if (actualWeight + itemList[i].WEIGHT <= MAX_CHARGE)
			{
				actualWeight += itemList[i].WEIGHT;
				itemsInBag[i] = true;
			}
			else break;
		}

		return itemsInBag;
	}

	
	/**
	 * Gets an array of integers reflecting the indexes of the items, sorted
	 * using the implemented comparator.
	 * @return the sorted indexes
	 */
	public Integer[] getSortedIndexes()
	{
		Integer[] sortedIndexes = new Integer[itemList.length];
		for (int i = 0; i < itemList.length; ++i) sortedIndexes[i] = i;
	
		// Arrays.sort => Θ(nlog(n))
		Arrays.sort(sortedIndexes, new GreedyComparator());
		return sortedIndexes;
	}

	
	/** Implements a comparator which for two integers compares the ratio
	 * value by weight of the corresponding items.
	 */
	private class GreedyComparator implements Comparator<Integer> {

		@Override
		public int compare(Integer a, Integer b) {
			float ratioA = itemList[a].VALUE / itemList[a].WEIGHT;
			float ratioB = itemList[b].VALUE / itemList[b].WEIGHT;
			return -Float.compare(ratioA, ratioB);
		}

	}

}
