package ru.yuubi.tennisscoreboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yuubi.tennisscoreboard.dto.MatchScore;
import ru.yuubi.tennisscoreboard.entity.Match;
import ru.yuubi.tennisscoreboard.repository.MatchRepository;

import java.util.List;

@Service
public class MatchServiceImpl implements MatchService{
    @Autowired
    private MatchRepository matchRepository;

    @Override
    public void save(Match match) {
        matchRepository.save(match);
    }

    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public void saveMatchFromMatchScore(MatchScore matchScore) {
        int playerOneId = matchScore.getPlayer1();
        int playerTwoId = matchScore.getPlayer2();
        int winner;

        if(matchScore.getPlayersScore().get(playerOneId).getSets() == 2) {
            winner = playerOneId;
        } else {
            winner = playerTwoId;
        }
        Match match = new Match(playerOneId, playerTwoId, winner);
        save(match);
    }


}
