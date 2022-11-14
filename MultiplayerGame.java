package assignment3;

//Name: Makena Williamson
//Pledge: "I pledge my honor that I have abided by the Stevens Honor System." - MW

public class MultiplayerGame {
	//data fields (given)
	private GameEntity turnTracker;
	private GameEntity[] index;
	
	/**@param Takes in an integer n (the amount of players in the game)
	 * @return Returns an array of n players starting at Player0
	 * All players "point" to each other, with Player4 connecting back to Player0
	 */
	public MultiplayerGame(int n) {
		//Checking to see if n is less than or equal to 0, if so throwing an exception
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		
		//Creating a new GameEntity array of length n
		this.index = new GameEntity[n];
		
		//Assigning a player of the corresponding id number to each index in index[]
		int idcounter = 0;
		for (int i = 0; i < n; i++) {
			GameEntity player = new GamePlayer(null,null,idcounter);
			index[i] = player;
			idcounter++;
		}
		
		//Checking for a one player game
		if (n == 1) {
			index[0].next = index[0];
			index[0].prev = index[0];
		}
		else {
			//Assigning "pointers" to the players in index
			for (int i = 0; i < n; i++) {
				//(i == 0) and (i == n-1) are our edge cases since they will create the "circle"
				//The first players "prev" field should point to the last player
				//The last players "next" field should point to the first player
				if (i == 0) {
					index[i].prev = index[n-1];
					index[i].next = index[i+1];
				}
				else if (i == (n-1)) {
					index[i].prev = index[n - 2];
					index[i].next = index[0];
				}
				else {
					index[i].prev = index[i-1];
					index[i].next = index[i+1];
				}
			}
		}
	}
	
	/**@return Returns the amount of game pieces in the game
	 */
	public int size() {
		int counter = 0;
		GameEntity currentEntity = index[0];
		while (currentEntity.next != index[0]) {
			if (currentEntity.isGamePlayer() == false) {
				counter++;
			}
			currentEntity = currentEntity.next;
		}
		if (currentEntity.isGamePlayer() == false) {
			counter++;
		}
		return counter;
	}
	
	/**@param Assigns a piece with name of name and strength of strength to the given playerId
	 */
	public void addGamePiece(int playerId, String name, int strength) {
		//Check for non-existent playerId
		if (playerId >= index.length || playerId < 0) {
			throw new IllegalArgumentException("addGamePiece: no such player");
		}
		
		//Check for duplicate pieces
		GameEntity currentEntity = index[playerId].next;
		while (currentEntity.isGamePlayer() == false) {
			if (currentEntity.getName() == name) {
				throw new IllegalArgumentException("addGamePiece: duplicate entity");
			}
			currentEntity = currentEntity.next;
		}
		
		//Inserting GamePiece
		GameEntity newPiece = new GamePiece(null,null,name,strength);
		GameEntity current = index[playerId];
		GameEntity oldNext = current.next;
		current.next = newPiece;
		oldNext.prev = newPiece;
		newPiece.prev = current;
		newPiece.next = oldNext;
	}
	
	/**@param Removes the piece with name name from the player with the given playerId
	 */
	public void removeGamePiece(int playerId, String name) {
		//Check for non-existent playerId
		if (playerId >= index.length || playerId < 0) {
			throw new IllegalArgumentException("removeGamePiece: no such player");
		}
		
		//Check for non-exist GamePiece
		boolean pieceExists = false;
		GameEntity currentEntity = index[playerId].next;
		while (currentEntity.isGamePlayer() == false) {
			if (currentEntity.getName() == name) {
				pieceExists = true;
				break;
			}
			currentEntity = currentEntity.next;
		}
		
		if (pieceExists == false) {
			throw new IllegalArgumentException("removeGamePiece: entity does not exist");
		}
		
		//Remove GamePiece
		GameEntity current = index[playerId].next;
		while (current.isGamePlayer() == false) {
			if (current.getName() == name) {
				GameEntity oldPrev = current.prev;
				GameEntity oldNext = current.next;
				oldPrev.next = oldNext;
				oldNext.prev = oldPrev;
			}
			current = current.next;
		}
	}
	
	/**@param name Looks if a game piece with name name exists in the game
	 * @return true if the game piece is in the game and false if the game piece is not in the game
	 */
	public boolean hasGamePiece(String name) {
		GameEntity current = index[0].next;
		while (current != index[0]) {
			if (current.isGamePlayer() == false && current.getName() == name) {
				return true;
			}
			current = current.next;
		}
		return false;
	}
	
	/**@param playerId Removes all pieces for the player with the given playerId
	 */
	public void removeAllGamePieces(int playerId) {
		//Check for non-existent player
		if (playerId >= index.length || playerId < 0) {
			throw new IllegalArgumentException("removeAllGamePieces: no such player");
		}
		
		//Removing all pieces
		GameEntity current = index[playerId].next;
		while (current.isGamePlayer() == false) {
			current = current.next;
		}
		index[playerId].next = current;
		current.prev = index[playerId];
	}
	
	/**@param Increases the strength of each game piece that playerId by int n
	 */
	public void increaseStrength(int playerId, int n) {
		//Check for non-existent player
		if (playerId >= index.length || playerId < 0) {
			throw new IllegalArgumentException("increaseStrength: no such player");
		}
		
		//Increasing strength
		GameEntity current = index[playerId].next;
		while (current.isGamePlayer() == false) {
			((GamePiece) current).updateStrength(n);
			current = current.next;
		}
	}
	
	/** @return String representation of the multiplayer game
	 */
	public String toString() {
		String finalString = "[";
		GameEntity currentEntity = index[0];
		//This while loop adds the first player all the way up to the second to last player
		while (currentEntity.next != null && currentEntity.next != index[0]) {
			finalString += currentEntity.toString() + ";";
			currentEntity = currentEntity.next;
		}
		//Manually adding the last player 
		finalString += currentEntity.toString();
		finalString += "]";
		return finalString;
	}
	
	/**Sets the turnTracker to the first player (Player0)
	 */
	public void initializeTurnTracker() {
		turnTracker = index[0];
	}
	
	/**Sets the turnTracker to the next game player
	 */
	public void nextPlayer() {
		GameEntity current = turnTracker.next;
		while (current.isGamePlayer() == false) {
			current = current.next;
		}
		turnTracker = current;
	}
	
	/**Sets the turnTracker to the next game entity
	 */
	public void nextEntity() {
		turnTracker = turnTracker.next;
	}
	
	/**Sets the turnTracker to the previous game player
	 */
	public void prevPlayer() {
		GameEntity current = turnTracker.prev;
		while (current.isGamePlayer() == false) {
			current = current.prev;
		}
		turnTracker = current;
	}
	
	/**@return String representation of the current entity that turnTracker is on
	 */
	public String currentEntityToString() {
		return turnTracker.toString();
	}
	
	public static void main(String[] args) {
//		MultiplayerGame game1 = new MultiplayerGame(4);
//		System.out.println(game1);
//		game1.addGamePiece( 2, "A", -1 );
//	    game1.addGamePiece( 3, "B", 1 );
//	    game1.addGamePiece( 2, "E", 5 );
//	    game1.addGamePiece( 0, "C", 4 );
//	    game1.addGamePiece( 2, "D", 6 );
//	    System.out.println(game1);
//	    game1.increaseStrength(2,-7);
//	    game1.increaseStrength(3,4);
//	    System.out.println(game1);
//	    System.out.println(game1.hasGamePiece("F"));
//	    System.out.println(game1.hasGamePiece("A"));
//	    game1.removeAllGamePieces(2);
//	    System.out.println(game1);
//	    System.out.println(game1.size());
//	    game1.addGamePiece(4, "A", 5);
//	    System.out.println(game1);
//		System.out.println(game1.size());
//		game1.initializeTurnTracker();
//		System.out.println(game1.turnTracker);
//		game1.nextPlayer();
//		System.out.println(game1.turnTracker);
//		game1.nextEntity();
//		System.out.println(game1.turnTracker);
//		game1.prevPlayer();
//		System.out.println(game1.turnTracker);
//		game1.nextPlayer();
//		game1.nextEntity();
//		game1.nextPlayer();
//		System.out.println(game1.turnTracker);
//		game1.nextPlayer();
//		System.out.println(game1.turnTracker);
//		game1.prevPlayer();
//		System.out.println(game1.turnTracker);
//		game1.nextEntity();
//		System.out.println(game1.currentEntityToString());
//		game1.nextEntity();
//		System.out.println(game1.turnTracker);
//		game1.nextPlayer();
//		System.out.println(game1.turnTracker);
//		game1.nextPlayer();
//		System.out.println(game1.turnTracker);
//		game1.nextPlayer();
//		System.out.println(game1.turnTracker);
//		game1.increaseStrength(2, -4);
//		System.out.println(game1);
//		game1.removeAllGamePieces(2);
//		System.out.println(game1);
//		System.out.println(game1.size());
//		System.out.println(game1.hasGamePiece("Player0"));
//		System.out.println(game1);
//		System.out.println(game1.index[3]);
//		System.out.println(game1.size());
//		System.out.println(game1);
//		game1.removeGamePiece(4, "beep");
//		System.out.println(game1.size());
//		System.out.println(game1);
//		System.out.println(game1.index[0].prev);
//		System.out.println(game1.index[0].next);
//		System.out.println(game1.index[3].next);
//		System.out.println(game1.index[3].prev);
//		System.out.println(game1.index[4].prev);
	}
}
