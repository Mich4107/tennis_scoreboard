package ru.yuubi.tennisscoreboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yuubi.tennisscoreboard.entity.Match;

public interface MatchRepository extends JpaRepository<Match, Integer>{
}
