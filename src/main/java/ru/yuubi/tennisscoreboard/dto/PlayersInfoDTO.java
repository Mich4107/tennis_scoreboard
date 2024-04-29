package ru.yuubi.tennisscoreboard.dto;

import lombok.Data;

@Data
public class PlayersInfoDTO {
    private int playerOneId;
    private int playerTwoId;

    private int winPlayerId;

    private int playerOnePoints;
    private int playerTwoPoints;

    private int playerOneGames;
    private int playerTwoGames;

    private int playerOneSets;
    private int playerTwoSets;

    public PlayersInfoDTO(int playerOneId, int playerTwoId, int winPlayerId, int playerOnePoints, int playerTwoPoints, int playerOneGames, int playerTwoGames, int playerOneSets, int playerTwoSets) {
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
        this.winPlayerId = winPlayerId;
        this.playerOnePoints = playerOnePoints;
        this.playerTwoPoints = playerTwoPoints;
        this.playerOneGames = playerOneGames;
        this.playerTwoGames = playerTwoGames;
        this.playerOneSets = playerOneSets;
        this.playerTwoSets = playerTwoSets;
    }
}
