package model.methods;

import java.util.Arrays;

import model.Item;

/** Implementation of a solving method using a branch-and-bound algorithm. */
class BnBBackpack extends AbstractMethod {

	/** Constructor calling back the super-constructor. */
	public BnBBackpack(int maxCharge, Item[] list) {
		super(maxCharge, list);
	}

	@Override
	public boolean[] solve() {
		new BnBTree().generate();
		
		// Displays the number of nodes created
		System.err.println(count); 
		
		return optimalSolution.itemsIn;
	}
	
	private int count;
	
	
	/** Class representing a tree node. */
	private class BnBTree
	{
		/** The items in the backpack at this node. */
		private boolean[] itemsIn;
		
		/** The maximal potential value for this node's descendants */
		private float upperBound;
		
		
		/** Constructor generating the root of the tree. */
		public BnBTree()
		{
			count = 1;
			itemsIn = new boolean[0];
			upperBound = calculateUpperBound();
		}
		
		/** Initiates the generation algorithm. */
		public void generate()
		{
			generateLowerBound();
			new BnBTree(this, true);
			new BnBTree(this, false);
		}
		
		
		/**
		 * Generates a child node.
		 * @param parent the node's parent
		 * @param takeNext either if this node represents a state where the
		 * next item is taken or not
		 */
		private BnBTree(BnBTree parent, boolean takeNext)
		{
			count++;
			
			itemsIn = Arrays.copyOf(parent.itemsIn, parent.itemsIn.length + 1);
			itemsIn[depth()] = takeNext;
			
			upperBound = parent.upperBound;
			if (!takeNext) upperBound -= itemList[depth()].VALUE;
			
			
			if (depth() == itemList.length - 1) // if the node is a leaf
			{
				// if this node's value is better than the previous optimal
				// solution, this nodes becomes the new solution
				if (optimalSolution == null || optimalSolution.value() < this.value())
					optimalSolution = this;
			}
			
			// if this node isn't a leaf and the bounds are still consistents
			else if	(optimalSolution == null || upperBound > optimalSolution.value())
			{
				// if the max weight isn't reached, continue generating the tree
				if (weight() + itemList[depth() + 1].WEIGHT <= MAX_CHARGE)
					new BnBTree(this, true);
				
				new BnBTree(this, false);
			}
		}
		
		
		/**
		 * Calculates the total value of the items in a node.
		 * @return the value
		 */
		private float value()
		{
			float value = 0;
			for (int i = 0; i < itemsIn.length; ++i)
			{
				if (this.itemsIn[i])
					value += itemList[i].VALUE;
			}
			return value;
		}
		
		/**
		 * Calculates the total weight of the items in a node.
		 * @return the weight
		 */
		private float weight()
		{
			float weight = 0;
			for (int i = 0; i < itemsIn.length; ++i)
			{
				if (this.itemsIn[i])
					weight += itemList[i].WEIGHT;
			}
			return weight;
		}
		
		/**
		 * Calculates the depth of this node.
		 * @return the depth
		 */
		private int depth()
		{
			return itemsIn.length - 1;
		}
	}
	
	/** The current best solution. */
	private BnBTree optimalSolution;
	
	
	/**
	 * Calculate the initial upper bound.
	 * @return the value of the upper bound
	 */
	protected float calculateUpperBound()
	{
		float v = 0;
		for (int i = 0; i < itemList.length; ++i)
			v += itemList[i].VALUE;
		return v;
	}

	/**
	 * Calculate the initial lower bound.
	 */
	private void generateLowerBound() {
		optimalSolution = new BnBTree();
		optimalSolution.itemsIn = 
				new GreedyBackpack(MAX_CHARGE, itemList).solve();
	}

}
