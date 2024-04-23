package ru.yuubi.tennisscoreboard.service;

import ru.yuubi.tennisscoreboard.dto.MatchScore;
import ru.yuubi.tennisscoreboard.entity.Match;

import java.util.List;

public interface MatchService {
    void save(Match match);
    List<Match> getAllMatches();
    void saveMatchFromMatchScore(MatchScore matchScore);
}
