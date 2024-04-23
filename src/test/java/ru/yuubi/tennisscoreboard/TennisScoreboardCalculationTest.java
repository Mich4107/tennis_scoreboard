package ru.yuubi.tennisscoreboard;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.yuubi.tennisscoreboard.dto.MatchScore;
import ru.yuubi.tennisscoreboard.dto.PlayerScoreDTO;
import ru.yuubi.tennisscoreboard.service.match_score.MatchScoreCalculationService;

import java.util.concurrent.ConcurrentMap;

public class TennisScoreboardCalculationTest {
    MatchScore matchScore = new MatchScore(1, 2);
    int winPlayerId = 1;
    MatchScoreCalculationService service = new MatchScoreCalculationService();
    ConcurrentMap<Integer, PlayerScoreDTO> playersScore;

    @Before
    public void createPlayersScore(){
        playersScore = matchScore.getPlayersScore();
    }

    @Test
    public void gameShouldNotEndWhenBothPlayersHave40points() {
        playersScore.get(1).setPoints(40);
        playersScore.get(2).setPoints(40);
        service.calculation(matchScore, winPlayerId);
        Assert.assertEquals(0, matchScore.getPlayersScore().get(winPlayerId).getGames());
    }

    @Test
    public void gameShouldEndWhenSomePlayerWinPointsOn40points() {
        playersScore.get(1).setPoints(40);
        playersScore.get(2).setPoints(0);
        service.calculation(matchScore, winPlayerId);
        Assert.assertEquals(1, matchScore.getPlayersScore().get(winPlayerId).getGames());
    }

    @Test
    public void matchEndsWhenSomePlayerHave2sets(){
        playersScore.get(1).setSets(1);
        playersScore.get(1).setGames(5);
        playersScore.get(1).setPoints(40);
        playersScore.get(2).setSets(1);
        service.calculation(matchScore, winPlayerId);
        Assert.assertEquals(2, matchScore.getPlayersScore().get(winPlayerId).getSets());
    }

    @Test
    public void whenBothPlayersHave6GamesTieBreakShouldStart(){
        playersScore.get(1).setGames(6);
        playersScore.get(2).setGames(6);
        service.calculation(matchScore, winPlayerId);
        Assert.assertTrue(matchScore.isTieBreak());
    }

    @Test
    public void deuceShouldStartWhenBothPlayersHave40Points(){
        playersScore.get(1).setPoints(40);
        playersScore.get(2).setPoints(40);
        service.calculation(matchScore, winPlayerId);
        Assert.assertTrue(matchScore.isDeuce());
    }


}
