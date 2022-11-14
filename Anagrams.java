package assignment6;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Anagrams {
	// data fields
	final Integer[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101};
	Map<Character,Integer> letterTable;
	Map<Long,ArrayList<String>> anagramTable;
	
	// constructor
	public Anagrams () {
		letterTable = new HashMap<Character,Integer>();
		anagramTable = new HashMap<Long,ArrayList<String>>();
		buildLetterTable();
	}
	
	/**
	 * method that creates a hash table that maps each letter of the alphabet to a unique prime number
	 */
	public void buildLetterTable() {
		Character[] letters = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
		for (int i = 0; i < 26; i++) {
			letterTable.put(letters[i],primes[i]);
		}
	}
	
	/**
	 * method that adds words to the anagram hash table
	 * @param s word to be added
	 */
	public void addWord(String s) {
		boolean inTable = false;
		long currentHash = myHashCode(s);
		
		// checking if String s is already in the table
		ArrayList<String> checkList = anagramTable.get(currentHash);
		if(checkList != null) {
			for(int i = 0; i < checkList.size(); i++) {
				if(checkList.get(i) == s) {
					inTable = true;
					break;
				}
			}
		}
		
		// if String s is in the table, throw an IllegalArgumentException. if not, add it!
		if(inTable == true) {
			throw new IllegalArgumentException("addWord: duplicate value");
		}
		else {
			if(anagramTable.containsKey(currentHash)) {
				ArrayList<String> currentList = anagramTable.get(currentHash);
				currentList.add(s);
			}
			else {
				ArrayList<String> newList = new ArrayList<String>();
				newList.add(s);
				anagramTable.put(currentHash, newList);
			}
		}
	}
	
	/**
	 * method that computes the hash code of each word being added to the anagram hash table
	 * @param s word that needs hash code computed for
	 * @return the hash code of the given word
	 */
	public long myHashCode(String s) {
		// checking if string is null, if not compute its hashcode (product of unique prime numbers)
		if(s == null) {
			throw new IllegalArgumentException("String must be non-empty.");
		}
		long tracker = 1;
		for(int i = 0; i < s.length(); i++) {
			char current = s.charAt(i);
			int value = letterTable.get(current);
			tracker *= value;
		}
		return tracker;
	}
	
	public void processFile(String s) throws IOException {
		FileInputStream fstream = new FileInputStream(s);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream ));
		String strLine;
		while (( strLine = br.readLine ()) != null) {
			this.addWord(strLine );
		}
		br.close ();
	}
	
	/**
	 * method to make a list of the group(s) of the most anagrams
	 * @return an array list containing all group(s) with the maximum number of anagrams
	 */
	public ArrayList<Map.Entry<Long,ArrayList<String>>> getMaxEntries(){
		// making a tracker for the current max
		int currentMax = 0;
		
		// making an array list of map entries to store the entries with the max number of anagrams
		ArrayList<Map.Entry<Long,ArrayList<String>>> maxTracker = new ArrayList<Map.Entry<Long,ArrayList<String>>>();
		
		// for-each loop to iterate through hash map and add entries with max entries to arraylist
		for(Map.Entry<Long,ArrayList<String>> mapElement : anagramTable.entrySet()) {
			ArrayList<String> keyList = mapElement.getValue();
			if(keyList.size() > currentMax) {
				currentMax = keyList.size();
				maxTracker.clear();
				maxTracker.add(mapElement);
			}
			else if(keyList.size() == currentMax) {
				maxTracker.add(mapElement);
			}
		}
		return maxTracker;
	}
	
	public static void main(String [] args) {
//		Anagrams a = new Anagrams ();
//		final long startTime = System.nanoTime ();
//		try {
//			a.processFile("words_alpha.txt");
//		} catch (IOException e1) {
//			e1.printStackTrace ();
//		}
//		a.addWord("beep");
//		a.addWord("bepe");
//		a.addWord("eebp");
//		a.addWord("peeb");
//		a.addWord("pebe");
//		a.addWord("next");
//		a.addWord("enxt");
//		a.addWord("tenx");
//		a.addWord("texn");
//		a.addWord("cat");
//		a.addWord("tac");
//		a.addWord("act");
//		ArrayList <Map.Entry <Long ,ArrayList <String >>> maxEntries = a.getMaxEntries ();
//		final long estimatedTime = System.nanoTime () - startTime;
//		final double seconds = (( double) estimatedTime /1000000000);
//		System.out.println("Time: "+ seconds );
//		System.out.println("List of max anagrams: "+ maxEntries );
	}
}
