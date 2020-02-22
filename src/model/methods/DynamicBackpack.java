package model.methods;

import model.Item;

/** Implementation of a solving method using a dynamic algorithm. */
class DynamicBackpack extends AbstractMethod {

	/** Constructor calling back the super-constructor. */
	public DynamicBackpack(int maxCharge, Item[] list) {
		super(maxCharge, list);
	}

	@Override
	public boolean[] solve() {
		int[][] T = new int[itemList.length][MAX_CHARGE + 1];

		// Generates the first line of T
		for (int j = 0; j <= MAX_CHARGE; ++j)
		{
			T[0][j] = (int) ((itemList[0].WEIGHT <= j)?
					(itemList[0].VALUE):(0));
		}

		// Generates the next lines of T
		for (int i = 1; i < itemList.length; ++i)
			for (int j = 0; j <= MAX_CHARGE; ++j)
			{
				if (itemList[i].WEIGHT <= j)
					T[i][j] = (int) Math.max(
							itemList[i].VALUE + T[i-1][(int) (j-itemList[i].WEIGHT)],
							T[i-1][j]);
				else
					T[i][j] = T[i-1][j];
			}

		boolean[] s = new boolean[itemList.length];

		// Traces back the values in order to get which items constitues
		// the calculated solution
		for (int i = itemList.length - 1, j = MAX_CHARGE; j > 0; --j)
		{
			if (T[i][j] == T[i][j-1]) continue;
			else if (i == 0 && T[i][j-1] == 0)
			{
				s[0] = true;
				break;
			}
			else if (T[i][j] == T[i-1][j])
			{
				--i; ++j;
			}
			else
			{
				s[i] = true;
				j -= (itemList[i--].WEIGHT - 1);
			}
		}

		return s;
	}
}
