package ru.kazberov.bullCow2.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.data.repository.CrudRepository;

import ru.kazberov.bullCow2.models.Game;
import ru.kazberov.bullCow2.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	// checks a user for existence
	public default boolean checkUser(String checkName){
		Iterable<User> users = findAll();
		for (User u : users) {
			if (u.getNickname().equals(checkName)) {
				return true;
			}
		}
		return false;
	}
	
	// finds the user by nickname and transfers it
	public default User getUserWithNickname(String Nickname){
		Iterable<User> users = findAll();
		for (User u : users) {
			if (u.getNickname().equals(Nickname)) {
				return u;
			}
		}
		return null; // won't happen
	}
	
	// passes an array with the top players
	public default List<String> getTopPlayers(){
		Iterable<User> users = findAll();
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
}
