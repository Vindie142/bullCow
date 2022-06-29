package ru.kazberov.bullCow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Base {
	private static List<User> users = new ArrayList<User>();  
    
	// checks a user for existence
	public static boolean checkUser(String checkName){
		for (User u : users) {
			if (u.getNickname().equals(checkName)) {
				return true;
			}
		}
		return false;
	}
	
	public static void addUser(User user){
		users.add(user);
	}
	
	// passes an array with the top players
	public static List<String> getTopPlayers(){
		List<String> topPlayers = new ArrayList<String>();
		Map<Double, User> map = new TreeMap<Double, User>();
		for (User u : users) {
			int amountAttemps = 0;
			int amountGames = 0;
			amountGames += u.getGames().size();
			for (Game uj : u.getGames()) {
				amountAttemps += uj.getAttempts().size();
			}
			// if the last game is not completed, then we do not consider it
			if (!u.getGames().get(u.getGames().size()-1).getVictory()) {
				amountGames -= 1;
				amountAttemps -= u.getGames().get(u.getGames().size()-1).getAttempts().size();
			}
			// we record the players and their result in the map for sorting
			if (amountGames > 0) {
				double amountAttempsD = amountAttemps;
				double amountGamesD = amountGames;
				double playerEfficiency = amountAttempsD / amountGamesD;
				// solves the key uniqueness problem
				while (map.containsKey(playerEfficiency)) {
					playerEfficiency -= 0.000001;
				}
				map.put(playerEfficiency, u);
			}
		}
		// writing the array in the correct order
		int serialNumber = 0;
		for (Map.Entry<Double, User> entry : map.entrySet()) {
		     topPlayers.add(++serialNumber+": "+entry.getValue().getNickname()+
		    		 " - "+String.format("%.3f",entry.getKey())+ " attempts");
		
		}
		return topPlayers;
	}
	
	// finds the user by nickname and transfers it
	public static User getUserWithNickname(String Nickname){
		for (User u : users) {
			if (u.getNickname().equals(Nickname)) {
				return u;
			}
		}
		return null; // won't happen
	}
	public static List<User> getUsers(){
		return users;
	}
}
