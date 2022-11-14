package assignment4;

import java.util.Random;
import java.util.Stack;

public class Treap<E extends Comparable<E>> {
	// the node class!
	public class Node<E>{
		// data fields
		public E data; // key for the search
		public int priority; // random heap priority
		public Node<E> left;
		public Node<E> right;
		
		// constructors
		public Node(E data, int priority) {
			if (data == null) {
				throw new IllegalArgumentException("Please provide data!");
			}
			this.data = data;
			this.priority = priority;
			this.left = null;
			this.right = null;
		}
		
		// methods
		Node<E> rotateRight(){
			// we can assume that the node we are working with is the root node
			Node<E> top = this.left;
			Node<E> left = top.right;
			top.right = this;
			this.left = left;
			return top;
		}
		Node<E> rotateLeft(){
			Node<E> top = this.right;
			Node<E> right = top.left;
			top.left = this;
			this.right = right;
			return top;
		}
		
		// helper toString for main toString
		public String toString() {
			return "(" + "key: " + this.data + ", priority: " + this.priority + ")";
		}
	}
	
	// data fields
	private Random priorityGenerator;
	private Node<E> root;
	// using priorityArr to keep track of the different priorities
	private int[] priorityArr = new int[100];
	
	// constructors
	public Treap(){
		root = null;
		priorityGenerator = new Random();
	}
	
	public Treap(long seed) {
		root = null;
		priorityGenerator = new Random(seed);
	}
	
	// helper function for generating a random priority
	public boolean isDuplicate(int randomNum) {
		// checking current randomly generated priority against priorities already in the tree
		for(int i = 0; i < priorityArr.length; i++) {
			if (randomNum == priorityArr[i]) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * add function!
	 * @param key : key of node that we want to add, we will generate a random priority
	 * @return : after generating random priority, call add(E key, int priority) with the key and random priority as inputs
	 */
	// add function for if we need to generate a random priority
	boolean add(E key) {
		// if the key already exists in the tree, return false
		if (find(key) == true) {
			return false;
		}
		
		// make a random integer from 0-99 using priorityGenerator
		int currentInt = priorityGenerator.nextInt(100);
		
		// checking for duplicate priority using helper function isDuplicate()
		while (isDuplicate(currentInt) == true) {
			currentInt = priorityGenerator.nextInt(100);
		}
		// once we are out of the while loop, currentInt is now a unique priority
		
		// i will add it into the priorityArr later in add(E key, int priority)
		// calling add with the randomly generated priority!
		return add(key, currentInt);
	}
	
	/**
	 * real add function!
	 * @param key : key of node, it will be compared to other keys to determine where to place new node
	 * @param priority : priority of node, it will be compared to other priorities, with the highest being at the root
	 * @return : true if the node is successfully added, false if the node is not successfully added
	 */
	boolean add(E key, int priority) {
		// checking for duplicate keys
		if (find(key) == true) {
			return false;
		}		
		
		// checking if user input duplicate priority
		//   - if duplicate, return false and don't add the node
		//   - else, add priority to the priorityArr for future reference
		if (isDuplicate(priority) == true) {
			return false;
		}
		else {
			priorityArr[priority] = priority;
		}
		
		// creating newNode with key and priority
		Node<E> newNode = new Node<E>(key, priority);
		
		// initializing stack to push ancestors into
		Stack<Node<E>> ancestorStack = new Stack<Node<E>>();
		
		// starting current node at root, will change as traveling down tree
		Node<E> current = root;
		
		// if root is empty, we add the node to be the root, else we find its place and insert it as a leaf
		if (root == null) {
			root = newNode;
			return true;
		}
		else {
			while (current != null){
				int cmpData = current.data.compareTo(key);
				if ((cmpData < 0) && (current.right == null)) {
					ancestorStack.push(current);
					current.right = newNode;
					reheap(ancestorStack,newNode);
					return true;
				}
				if ((cmpData < 0) && (current.right != null)) {
					ancestorStack.push(current);
					current = current.right;
				}
				if ((cmpData > 0) && (current.left == null)) {
					ancestorStack.push(current);
					current.left = newNode;
					reheap(ancestorStack,newNode);
					return true;
				}
				if ((cmpData > 0) && (current.left != null)) {
					ancestorStack.push(current);
					current = current.left;
				}
			}
		}
		return false;
	}
	
	/**
	 * reheap method! the purpose of this method is to restructure the tree after adding a node to have the correct priority order (max heap)
	 * @param stack : created when adding node, stores ancestors of the node we added
	 * @param nodeAdded : node that we have added to the tree
	 */
	// reheap restructures the tree after adding a node so that we can ensure correct priority order
	private void reheap(Stack<Node<E>> stack, Node<E> nodeAdded) {
		while(!stack.empty()) {
			Node<E> currentAncestor = stack.pop();
			// need a separate approach for when current ancestor is root in order to replace the root as our node
			if(currentAncestor == root) {
				if((currentAncestor.priority < nodeAdded.priority) && (currentAncestor.left == nodeAdded)) {
					nodeAdded = currentAncestor.rotateRight();
					root = nodeAdded;
				}
				if((currentAncestor.priority < nodeAdded.priority) && (currentAncestor.right == nodeAdded)){
					nodeAdded = currentAncestor.rotateLeft();
					root = nodeAdded;
				}
				else {
					break;
				}
			}
			else {
				if((currentAncestor.priority < nodeAdded.priority) && (currentAncestor.left == nodeAdded)) {
					nodeAdded = currentAncestor.rotateRight();
					Node<E> upperAncestor = stack.peek();
					if(upperAncestor.left == currentAncestor){
						upperAncestor.left = nodeAdded;
					}
					else {
						upperAncestor.right = nodeAdded;
					}
				}
				if((currentAncestor.priority < nodeAdded.priority) && (currentAncestor.right == nodeAdded)) {
					nodeAdded = currentAncestor.rotateLeft();
					Node<E> upperAncestor = stack.peek();
					if(upperAncestor.left == currentAncestor){
						upperAncestor.left = nodeAdded;
					}
					else {
						upperAncestor.right = nodeAdded;
					}
				}
				// had an else break here but deleted and it works?
			}
		}
	}
	
	/**
	 * delete function!
	 * @param key : key of node, the node with this key will be removed from the tree, the tree will be altered accordingly
	 * @return true if node is successfully removed, false if the node is not successfully removed
	 */
	boolean delete(E key) {
		// if key is not in tree, return false since there is nothing to remove
		if (find(key) == false) {
			return false;
		}
		
		// starting to search for node at root
		Node<E> current = root;
		
		// temp variable to store the parent of the node at each step (eventually need access to it to "cut" the node off)
		Node<E> parent = null;
		
		// searching for our node
		while(current.data != key) {
			int cmpData = key.compareTo(current.data);
			if(cmpData > 0) {
				parent = current;
				current = current.right;
			}
			else {
				parent = current;
				current = current.left;
			}
		}
		
		// now im at the correct node, time to start rotating
		while(current.left != null || current.right != null) {
			// if node we are removing is root, we need to treat the parent differently
			if(current == root) {
				if(current.left == null && current.right != null) {
					parent = current.right;
					root = parent;
					current.rotateLeft();
				}
				if(current.right == null && current.left != null) {
					parent = current.left;
					root = parent;
					current.rotateRight();
				}
				if(current.right != null && current.left != null) {
					if (current.right.priority > current.left.priority) {
						parent = current.right;
						root = parent;
						current.rotateLeft();
					}
					else {
						parent = current.left;
						root = parent;
						current.rotateRight();
					}
				}
			}
			
			// node we are removing is no longer root
			if(current.left == null && current.right != null) {
				if(parent.left == current) {
					parent.left = current.right;
				}
				else {
					parent.right = current.right;
				}
				parent = current.right;
				current.rotateLeft();
			}
			if(current.right == null && current.left != null) {
				if(parent.left == current) {
					parent.left = current.left;
				}
				else {
					parent.right = current.left;
				}
				parent = current.left;
				current.rotateRight();
			}
			if (current.left != null && current.right != null) {
				if(current.left.priority > current.right.priority) {
					if(parent.left == current) {
						parent.left = current.left;
					}
					else {
						parent.right = current.left;
					}
					parent = current.left;
					current.rotateRight();
				}
				else {
					if(parent.left == current) {
						parent.left = current.right;
					}
					else {
						parent.right = current.right;
					}
					parent = current.right;
					current.rotateLeft();
				}
			}
		}
		
		// at this point the node i want to remove should be a leaf node
		if(current == parent.left) {
			parent.left = null;
			return true;
		}
		else {
			parent.right = null;
			return true;
		}
	}
	
	/**
	 * find function!
	 * @param root : where we are starting from
	 * @param key : key of node we are looking for
	 * @return true if the node exists in the tree, false if the node does not exist in the tree
	 */
	public boolean find(Node<E> root, E key) {
		// if there is nothing in the tree, return false
		if (root == null) {
			return false;
		}
		
		int compareResult = key.compareTo(root.data);
		if (compareResult == 0) { // means they are the same (aka we found it!)
			return true;
		}
		if (compareResult > 0) {
			return find(root.right, key);
		}
		else {
			return find(root.left, key);
		}
	}
	
	/**
	 * wrapper find function!
	 * @param key : key of node we are searching for
	 * @return a call to find with the root of the tree as its local root
	 */
	public boolean find(E key) {
		return find(root, key);
	}
	
	public String toString(Node<E> current, int depth) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<depth;i++) {
			sb.append("-");
		}
		if (current==null) {
			sb.append("null\n");
		} 
		else {
			sb.append(current.toString()+"\n");
			sb.append(toString(current.left, depth+1)); //
			sb.append(toString(current.right,depth+1));
		}
		return sb.toString();
	}
	
	public String toString() {
		return toString(root, 0);
	}
	
	public static void main(String[] args) {
//		Treap<Integer> testTree = new Treap <Integer>();
//		testTree.add (4 ,19);
//		testTree.add (2 ,31);
//		testTree.add (6 ,70);
//		testTree.add (1 ,84);
//		testTree.add (3 ,12);
//		testTree.add (5 ,83);
//		testTree.add (7 ,26);
//		System.out.println(testTree);
//		Treap<Integer> testTree = new Treap<Integer>();
//		testTree.add(6,16);
//		testTree.add(1,52);
//		testTree.add(2,30);
//		testTree.add(2,10);
//		testTree.add(8,17);
//		testTree.add(12,53);
//		testTree.add(9,11);
//		testTree.add(5,11);
//		testTree.add(13,40);
		//testTree.delete();
//		System.out.println(testTree);
//		System.out.println(testTree.find(10));
//		System.out.println(testTree.find(8));
		
//		Treap<Integer> testTree = new Treap<Integer>();	
//		testTree.add (4 ,19);
//		testTree.add (2 ,31);
//		testTree.add (6 ,70);
//		testTree.add (1 ,84);
//		testTree.add (3 ,12);
//		testTree.add (5 ,83);
//		testTree.add (7 ,71);
//		testTree.add(11);
		//testTree.delete(1);
		//testTree.delete(4);
//		System.out.println(testTree);
		//System.out.println(testTree.find(9));
//		System.out.println(testTree.find(7));
//		System.out.println(testTree.find(6));
		
//		testTree.add(6,99);
//		testTree.add(4,97);
//		testTree.add(12,91);
//		testTree.add(3,17);
//		testTree.add(2,12);
//		testTree.add(5,80);
//		testTree.add(9,70);
//		testTree.add(8,60);
//		testTree.add(7,49);
//		testTree.add(10,51);
//		testTree.add(14,83);
//		testTree.add(13,14);
//		testTree.add(15,72);
//		testTree.delete(12);
//		System.out.println(testTree);
		
		
//		testTree.add (4 ,19);
//		testTree.add (2 ,31);
//		testTree.add (6 ,70);
//		testTree.add (3 ,12);
//		testTree.add (5 ,83);
//		testTree.add (8 ,26);
//		testTree.add(7, 11);
//		testTree.add(9,99);
		
		//System.out.println(testTree);
	}
}
