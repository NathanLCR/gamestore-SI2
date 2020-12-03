package com.unifacisa.gamestore.games.repositories;

import com.unifacisa.gamestore.games.entities.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game,Integer> {
}
