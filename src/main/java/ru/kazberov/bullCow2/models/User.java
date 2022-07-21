package ru.kazberov.bullCow2.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nickname;
	private int password; // hashCode of password
	
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Game> games = new ArrayList<Game>();

	public User () {}
	
	public User (String nickname, int password) {
		this.nickname = nickname;
		this.password = password;
	}
	
	// creates a new game if it's the first one or the past ones are completed
	public boolean ifNewGame(){
		if (games.size() == 0 || games.get(games.size()-1).getVictory() == true) {
			return true;
		} else {
			return false;
		}
	}
		
	// gives the current game
	public Game getCurrentGame(){
		return games.get(games.size()-1);
	}
	
	public void addGame(Game game){
		games.add(game);
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setPassword(int password) {
		this.password = password;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}
	
	public Long getId() {
		return id;
	}
	public String getNickname(){
		return nickname;
	}
	public int getPassword(){
		return password;
	}
	public List<Game> getGames(){
		return games;
	}
	
}
