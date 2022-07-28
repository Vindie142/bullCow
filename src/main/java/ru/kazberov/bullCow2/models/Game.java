package ru.kazberov.bullCow2.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="games")
public class Game {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne (optional=false)
    @JoinColumn (name="user_id")
	private User user;
	
	private int[] theNumber = new int[4]; // the hidden number
	// !!! the value "-1" is used as "null" !!!
	private int [] checkedNumber = {-1,-1,-1,-1}; // the number that the user entered
	@ElementCollection(targetClass=String.class)
	@Column(name = "attempt")
	private List<String> attempts = new ArrayList<String>(); // user attempts
	private boolean answerWasGiven = false; // has an answer been given on this attempt
	private boolean victory = false; // did the victory happen
	
	public Game () {}
	
	public Game(User user) { // constructor
		this.user = user;
		user.addGame(this);
		// comes up with a random number
		for (int i = 0; i < theNumber.length; i++) {
			// checks the uniqueness of a digit in a number
			int possibleInt;
			int checkingQuantityInLoop = 0;
			do {
				Random random = new Random();
				possibleInt = random.nextInt(10);
				checkingQuantityInLoop = 0;
				for (int j = 0; j < i; j++) {
					if (theNumber[j] != possibleInt) {
						checkingQuantityInLoop++;
					}
				}
			} while (!(checkingQuantityInLoop == i));
			
			theNumber[i] = possibleInt;
		}
		// theNumber[0] = 1; theNumber[1] = 2; theNumber[2] = 3; theNumber[3] = 4; // for test
	}
	
	// accepts the entered number
	public void takeEntered(String enteredString) {
	    int enteredInt = Integer.parseInt(enteredString);
	    // updates the array after the last response display
	    if (answerWasGiven == true) {
	    	for (int i = 0; i < checkedNumber.length; i++) {
	    		checkedNumber[i] = -1;
			}
	    	answerWasGiven = false;
		}
	    // enter a digit to the desired location of the array
		for (int i = 0; i < checkedNumber.length; i++) {
			if (checkedNumber[i] == -1) {
				checkedNumber[i] = enteredInt;
				break;
			}
		}
		// checks Bulls And Cows if all 4 digits are entered
		if (checkedNumber[3] != -1) {
			checkingBullsAndCows();
		}
	}
	
	// checks Bulls And Cows
	private void checkingBullsAndCows() {
		int [] test = new int[4];
		int bulls = 0;
		int cows = 0;
		// creates a test array
		for (int i = 0; i < checkedNumber.length; i++) {
			test[i] = checkedNumber[i];
		}
		// checks the bulls
		for (int i = 0; i < test.length; i++) {
			if (test[i] == theNumber[i]) {
				bulls++;
				test[i] = -1; // resets the cell with a bull so that it isn't a cow
			}
		}
		// checks cows
		for (int i = 0; i < test.length; i++) {
			for (int j = 0; j < theNumber.length; j++) {
				if (test[i] == theNumber[j]) {
					cows++;
				}
			}
		}
		// writes the current attempt to the sheet
		String attempt = "";
		for (int i = 0; i < checkedNumber.length; i++) {
			attempt += checkedNumber[i];
		}
		attempts.add(attempt += " - "+bulls+"Bulls "+cows+"Cows");
		answerWasGiven = true;
		// checks the victory
		if (bulls == 4) {
			victory = true;
		}
	}
	
	// passes a line to the view
	public String getLine() {
		String line = "";
		// if an answer has been given, it outputs the answer
		if (answerWasGiven == true) {
			line = attempts.get(attempts.size()-1);
		} 
		// if there was no response, then the entered line
		else {
			for (int i = 0; i < checkedNumber.length; i++) {
				if (checkedNumber[i] != -1) {
					line += checkedNumber[i];
				}
			}
		}
		return line;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setTheNumber(int[] theNumber) {
		this.theNumber = theNumber;
	}
	public void setCheckedNumber(int[] checkedNumber) {
		this.checkedNumber = checkedNumber;
	}
	public void setAttempts(List<String> attempts) {
		this.attempts = attempts;
	}
	public void setAnswerWasGiven(boolean answerWasGiven) {
		this.answerWasGiven = answerWasGiven;
	}
	public void setVictory(boolean trueFalse) {
		this.victory = trueFalse;
	}
	
	public long getId() {
		return id;
	}
	public User getUser() {
		return user;
	}
	public int[] getTheNumber() {
		return theNumber;
	}
	public boolean getAnswerWasGiven() {
		return answerWasGiven;
	}
	public List<String> getAttempts() {
		return attempts;
	}
	public boolean getVictory() {
		return victory;
	}
	public int [] getCheckedNumber() {
		return checkedNumber;
	}
	
	@Override
	public String toString() {
		String user = this.user == null ? "null" : this.user.getNickname();
		String theNumber = Arrays.toString(this.theNumber);
		String victory = Boolean.toString(this.victory);
		String attempts = Integer.toString(this.attempts.size());
		return  "Game{"+
				"user="+user+", "+
				"theNumber="+theNumber+", "+
				"victory="+victory+", "+
				"attempts.size()="+attempts+"}";
	}
}
