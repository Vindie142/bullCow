package ru.kazberov.bullCow;

import java.util.ArrayList;
import java.util.List;

public class User {

	private String nickname;
	private String password;
	private List<Game> games;
	
	public User (String nickname, String password) {
		Base.addUser(this);
		this.nickname = nickname;
		this.password = password;
		this.games = new ArrayList<Game>();
	}
	
	public void addGame(Game game){
		this.games.add(game);
	}
	
	public String getNickname(){
		return this.nickname;
	}
	public String getPassword(){
		return this.password;
	}
	public List<Game> getGames(){
		return this.games;
	}
	
	// creates a new game if it's the first one or the past ones are completed
	public void ifNewGame(){
		if (games.size() == 0) {
			new Game(this);
		} else if (this.games.get(this.games.size()-1).getVictory() == true) {
			new Game(this);
		}
	}
	
	// gives the current game
	public Game getCurrentGame(){
		return this.games.get(this.games.size()-1);
	}
}
