package ru.yuubi.tennisscoreboard.dto;

import lombok.Data;
import ru.yuubi.tennisscoreboard.service.PlayerService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Data
public class MatchScore {
    private int player1;
    private int player2;
    private ConcurrentMap<Integer, PlayerScoreDTO> playersScore;

    private boolean isDeuce = false;
    private boolean isTieBreak = false;

    private int playerOneDeucePoints = 0;
    private int playerTwoDeucePoints = 0;

    private int playerOneTieBreakPoints = 0;
    private int playerTwoTieBreakPoints = 0;

    public MatchScore(int player1, int player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.playersScore = new ConcurrentHashMap<>();
        this.playersScore.put(player1, new PlayerScoreDTO());
        this.playersScore.put(player2, new PlayerScoreDTO());
    }

    public void setDefaultPointsAfterDeuce(){
        playerOneDeucePoints = 0;
        playerTwoDeucePoints = 0;
    }
    public void setDefaultTieBreakPoints(){
        playerOneTieBreakPoints = 0;
        playerTwoTieBreakPoints = 0;
    }
    public static MatchScore createMatchScoreFromNewMatchPlayers(NewMatchPlayers newMatchPlayers, PlayerService playerService){
        String playerOneName = newMatchPlayers.getPlayerOneName();
        String playerTwoName = newMatchPlayers.getPlayerTwoName();

        int playerOneId = playerService.getPlayerByName(playerOneName).getId();
        int playerTwoId = playerService.getPlayerByName(playerTwoName).getId();

        MatchScore matchScore = new MatchScore(playerOneId, playerTwoId);
        matchScore.getPlayersScore().get(playerOneId).setPlayerName(playerOneName);
        matchScore.getPlayersScore().get(playerTwoId).setPlayerName(playerTwoName);

        return matchScore;
    }
}
