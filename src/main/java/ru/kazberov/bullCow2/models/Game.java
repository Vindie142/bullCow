package ru.kazberov.bullCow2.models;

import java.util.ArrayList;
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
	private Long id;
	
	@ManyToOne (optional=false)
    @JoinColumn (name="user_id")
	private User user;
	
	private int[] theNumber = new int[4]; // the hidden number
	// !!! the value "-1" is used as "null" !!!
	private int [] checkedNumber = {-1,-1,-1,-1}; // the number that the user entered
	@ElementCollection(targetClass=String.class)
	@Column(name = "attempt")
	private List<String> attempts; // user attempts
	private boolean answerWasGiven = false; // has an answer been given on this attempt
	private boolean victory = false; // did the victory happen
	
	public Game () {}
	
	public Game(User user) { // constructor
		this.user = user;
		user.addGame(this);
		this.attempts = new ArrayList<String>();
		// comes up with a random number
		for (int i = 0; i < this.theNumber.length; i++) {
			// checks the uniqueness of a digit in a number
			int possibleInt;
			int checkingQuantityInLoop = 0;
			do {
				Random random = new Random();
				possibleInt = random.nextInt(10);
				checkingQuantityInLoop = 0;
				for (int j = 0; j < i; j++) {
					if (this.theNumber[j] != possibleInt) {
						checkingQuantityInLoop++;
					}
				}
			} while (!(checkingQuantityInLoop == i));
			
			this.theNumber[i] = possibleInt;
		}
	}
	
	// accepts the entered number
	public void takeEntered(String enteredString) {
	    int enteredInt = Integer.parseInt(enteredString.trim());
	    // updates the array after the last response display
	    if (this.answerWasGiven == true) {
	    	for (int i = 0; i < this.checkedNumber.length; i++) {
	    		this.checkedNumber[i] = -1;
			}
	    	this.answerWasGiven = false;
		}
	    // enter a digit to the desired location of the array
		for (int i = 0; i < this.checkedNumber.length; i++) {
			if (this.checkedNumber[i] == -1) {
				this.checkedNumber[i] = enteredInt;
				break;
			}
		}
		// checks Bulls And Cows if all 4 digits are entered
		if (this.checkedNumber[3] != -1) {
			checkingBullsAndCows();
		}
	}
	
	// checks Bulls And Cows
	private void checkingBullsAndCows() {
		int [] test = new int[4];
		int bulls = 0;
		int cows = 0;
		// creates a test array
		for (int i = 0; i < this.checkedNumber.length; i++) {
			test[i] = this.checkedNumber[i];
		}
		// checks the bulls
		for (int i = 0; i < test.length; i++) {
			if (test[i] == this.theNumber[i]) {
				bulls++;
				test[i] = -1; // resets the cell with a bull so that it isn't a cow
			}
		}
		// checks cows
		for (int i = 0; i < test.length; i++) {
			for (int j = 0; j < this.theNumber.length; j++) {
				if (test[i] == this.theNumber[j]) {
					cows++;
				}
			}
		}
		// writes the current attempt to the sheet
		String attempt = "";
		for (int i = 0; i < this.checkedNumber.length; i++) {
			attempt += this.checkedNumber[i];
		}
		this.attempts.add(attempt += " - "+bulls+"Bulls "+cows+"Cows");
		this.answerWasGiven = true;
		// checks the victory
		if (bulls == 4) {
			this.victory = true;
		}
	}
	
	
	public void setId(Long id) {
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
	
	public Long getId() {
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
		return this.attempts;
	}
	public boolean getVictory() {
		return this.victory;
	}
	public int [] getCheckedNumber() {
		return this.checkedNumber;
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
	
}
