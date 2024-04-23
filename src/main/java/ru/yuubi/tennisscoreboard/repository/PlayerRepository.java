package ru.yuubi.tennisscoreboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yuubi.tennisscoreboard.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Player findByName(String name);
}
