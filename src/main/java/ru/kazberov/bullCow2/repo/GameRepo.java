package ru.kazberov.bullCow2.repo;

import org.springframework.data.repository.CrudRepository;

import ru.kazberov.bullCow2.models.Game;

public interface GameRepo extends CrudRepository<Game, Long> {

}
