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
	private String password;
	
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Game> games;

	public User () {}
	
	public User (String nickname, String password) {
		this.nickname = nickname;
		this.password = password;
		this.games = new ArrayList<Game>();
	}
	
	public void addGame(Game game){
		this.games.add(game);
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}
	
	public Long getId() {
		return id;
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
	public boolean ifNewGame(){
		if (games.size() == 0 || this.games.get(this.games.size()-1).getVictory() == true) {
			return true;
		} else {
			return false;
		}
	}
	
	// gives the current game
	public Game getCurrentGame(){
		return this.games.get(this.games.size()-1);
	}
}
