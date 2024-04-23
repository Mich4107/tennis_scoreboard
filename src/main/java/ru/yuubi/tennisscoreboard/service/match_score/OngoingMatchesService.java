package ru.yuubi.tennisscoreboard.service.match_score;

import org.springframework.stereotype.Component;
import ru.yuubi.tennisscoreboard.dto.MatchScore;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class OngoingMatchesService {
    private ConcurrentMap<Integer, MatchScore> currentMatches = new ConcurrentHashMap<>();

    public void addMatch(int uuid, MatchScore matchScore) {
        currentMatches.put(uuid, matchScore);
    }

    public MatchScore getMatch(int uuid) {
        return currentMatches.get(uuid);
    }

    public void removeMatch(int uuid) {
        currentMatches.remove(uuid);
    }

}
