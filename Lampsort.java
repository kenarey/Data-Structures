import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class Lampsort {
	
	//this is the set that contains the intervals of subarrays
	public static HashSet<Interval> set = new HashSet<Interval>();
	
	private static class Interval {
		
		private int lower;
		private int upper;
		
		/**
		 * Adds an interval of indices of an unsorted subarray to a Set
		 * @param lower the lower bound of an unsorted subarray
		 * @param upper the upper bound of an unsorted subarray 
		 */
		public Interval(int lower, int upper) {
			this.lower = lower;
			this.upper = upper;
		}
		/**
		 * returns the hashCode of an Interval object
		 */
		public int hashCode() {
			return ((lower*lower) + upper);
		}
		/**
		 * @return the lower bound of an interval
		 */
		public int getLower() {
			return this.lower;
		}
		/**
		 * @return the upper bound of an interval
		 */
		public int getUpper() {
			return this.upper;
		}
		/**
		 * @param o an object being compared to an Interval
		 * @return true if "this" Interval and the Object o are the same Interval (i.e. they have the same bounds)
		 */
		public boolean equals(Object o) {
			Interval i = (Interval) o;
			return (i.upper == this.upper && i.lower == this.lower);
		}
	}
	
	/**
	 * Sorts an array in increasing order
	 * @param array The array that is to be sorted
	 */
	public static <T extends Comparable<T>> void sort(T[] array) {
		Interval init = new Interval(0, array.length - 1);
		set.add(init);
		while (!set.isEmpty()){
			Interval i = set.iterator().next();
			if (i.getUpper() - i.getLower() >= 1) {
				int lower = i.getLower();
				int upper = i.getUpper();
				int pivIndex = partition(array, lower, upper);
				set.add(new Interval(lower, pivIndex - 1));
				set.add(new Interval(pivIndex + 1, upper));
			}
			set.remove(i);
		}
	}
	
	/**
	 * Swaps the first element of table with the median of table[first], table[middle], table[last]. The new first element is
	 * called the pivot
	 * @param table An array of T objects.
	 * @param first The first index of the array
	 * @param last	The last index of the array
	 * @return		The index of the pivot of the new array
	 */
	private static <T extends Comparable <T>> int partition(T[] table, int first, int last) {
		//begin median of three
		T fst = table[first];
		T mid = table[(last - first)/2];
		T lst = table[last];
		
		//make arraylist of first, middle, and last
		ArrayList<T> a = new ArrayList<T>();
		a.add(0, fst);
		a.add(1, mid);
		a.add(2, lst);
		
		//sort that arraylist in increasing order
		BubbleSort(a);
		
		//sort first, mid, last in array in correct order from bubble sort
		table[first] = a.get(0);
		table[(last - first)/2] = a.get(1);
		table[last] = a.get(2);
		
		//swap first and median value after all three sorted above
		T temp = table[first];
		table[first] = table[(last - first)/2];
		table[(last - first)/2] = temp;
		//end median of three
		
		
		//begin partitioning
		T pivot = table[first];
		int up = first;
		int down = last;
		do {
			while ((up < last) && (pivot.compareTo(table[up]) >= 0)) {
				up++;
			}
			while (pivot.compareTo(table[down]) < 0) {
				down--;
			}
			if (up < down) {
				T temporary = table[up];
				table[up] = table[down];
				table[down] = temporary;
			}
		} while (up < down);
		T temporary = table[first];
		table[first] = table[down];
		table[down] = temporary;
		return down;
		//end partitioning
	}
	
	/**
	 * Performs Bubblesort on an arraylist
	 * @param arr an unsorted arraylist
	 */
	private static <T extends Comparable <T>> void BubbleSort(ArrayList<T> arr) {
		int pass = 1;
		boolean exchanges = false;
		do {
			exchanges = false;
			for (int i = 0; i < arr.size() - pass; i++) {
				if (arr.get(i).compareTo(arr.get(i+1)) > 0) {
					T temp = arr.get(i);
					arr.set(i, arr.get(i+1));
					arr.set(i+1, temp);
					exchanges = true;
				}
			}
		}while(exchanges);
	}
	
	public static void main(String[] args) {
		//Integer[] a = {3,2,1};
		//Integer[] a = {9,2,5,6,7,4,3,8,1};
		//Integer[] a = {10,9,8,7,6,5,4,3,2,1};
		//Integer[] a = {2,5,3,0,2,3,0,3};
		//Integer[] a = {3,4,7,1,8,5,2,9,0,6};
		//Integer[] a  = {3,4,7,1,5,8,2,9,0,6};
		Integer[] a = {5,4,7,1,8,3,2,9,0,6};
		System.out.print("Original: ");
		for (Integer i: a) {
			System.out.print(i + " ");
		}
		System.out.println("\n");
		
		sort(a);
		System.out.print("Sorted: ");
		for (Integer i: a) {
			System.out.print(i + " ");
		}
	}
}
