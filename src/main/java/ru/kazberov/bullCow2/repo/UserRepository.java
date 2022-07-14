package ru.kazberov.bullCow2.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.data.repository.CrudRepository;

import ru.kazberov.bullCow2.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	// checks a user for existence
	public default boolean checkUser(String checkName){
		Iterable<User> sourceUsers = findAll();
		List<User> users = new ArrayList<>();
		sourceUsers.forEach(a -> users.add(a));
		
		return users.stream().anyMatch(u -> u.getNickname().equals(checkName));
	}
	
	// finds the user by nickname and transfers it
	public default User getUserWithNickname(String Nickname){
		Iterable<User> sourceUsers = findAll();
		List<User> users = new ArrayList<>();
		sourceUsers.forEach(a -> users.add(a));
		
		return users.stream().filter(u -> u.getNickname().equals(Nickname)).findAny().orElse(null);
	}
	
	// passes an array with the top players
	public default List<String> getTopPlayers(){
		List<String> topPlayers = new ArrayList<String>();
		Map<Double, User> map = new TreeMap<Double, User>();
		
		Iterable<User> sourceUsers = findAll();
		List<User> users = new ArrayList<>();
		sourceUsers.forEach(a -> users.add(a));		

		for (User u : users) {
			List<Integer> numOfAttemptsInGame = u.getGames().stream()
												.filter(g -> g.getVictory())
												.map(g -> g.getAttempts().size())
												.collect(Collectors.toList());
			// we record the players and their result in the map for sorting
			if (numOfAttemptsInGame.size() > 0) {
				double playerEfficiency = numOfAttemptsInGame.stream().mapToDouble(e -> e).average().orElse(-1);
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
