package ru.yuubi.tennisscoreboard.service.match_score;

import org.springframework.stereotype.Component;
import ru.yuubi.tennisscoreboard.dto.MatchScore;
import ru.yuubi.tennisscoreboard.dto.PlayerScoreDTO;
import ru.yuubi.tennisscoreboard.dto.PlayersInfoDTO;

import java.util.concurrent.ConcurrentMap;

@Component
public class MatchScoreCalculationService {
    public void calculation(MatchScore matchScore, int winPlayerId) {
        ConcurrentMap<Integer, PlayerScoreDTO> playersScore = matchScore.getPlayersScore();

        PlayersInfoDTO playersInfo = getPlayersInfo(matchScore, playersScore, winPlayerId);

        if(playersInfo.getPlayerOneGames() == 6 && playersInfo.getPlayerTwoGames() == 6){
            tieBreakCalculation(matchScore, playersScore, playersInfo);
            return;
        }

        pointsCalculation(matchScore, playersScore, playersInfo);
        gamesCalculation(matchScore, playersScore, playersInfo);
        setsCalculation(playersScore, playersInfo);
    }

    private void tieBreakCalculation(MatchScore matchScore, ConcurrentMap<Integer, PlayerScoreDTO> playersScore, PlayersInfoDTO playersInfo) {
        int playerOneId = playersInfo.getPlayerOneId();
        int playerTwoId = playersInfo.getPlayerTwoId();

        int playerOneSets = playersInfo.getPlayerOneSets();
        int playerTwoSets = playersInfo.getPlayerTwoSets();

        int winPlayerId = playersInfo.getWinPlayerId();

        matchScore.setTieBreak(true);

        if(playerOneId == winPlayerId) {
            int playerOneTieBreakPoints = matchScore.getPlayerOneTieBreakPoints();
            playerOneTieBreakPoints++;
            matchScore.setPlayerOneTieBreakPoints(playerOneTieBreakPoints);
        } else if (playerTwoId == winPlayerId){
            int playerTwoTieBreakPoints = matchScore.getPlayerTwoTieBreakPoints();
            playerTwoTieBreakPoints++;
            matchScore.setPlayerTwoTieBreakPoints(playerTwoTieBreakPoints);
        }

        int playerOneTieBreakPoints = matchScore.getPlayerOneTieBreakPoints();;
        int playerTwoTieBreakPoints = matchScore.getPlayerTwoTieBreakPoints();

        if(playerOneTieBreakPoints == 7 && playerOneTieBreakPoints - 2 >= playerTwoTieBreakPoints) {
            matchScore.setDefaultTieBreakPoints();
            setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
            playerOneSets++;
            playersScore.get(playerOneId).setSets(playerOneSets);
            matchScore.setTieBreak(false);
        } else if (playerTwoTieBreakPoints == 7 && playerTwoTieBreakPoints - 2 >= playerOneTieBreakPoints){
            matchScore.setDefaultTieBreakPoints();
            setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
            playerTwoSets++;
            playersScore.get(playerTwoId).setSets(playerTwoSets);
            matchScore.setTieBreak(false);
        } else if(playerOneTieBreakPoints >= 6 && playerTwoTieBreakPoints >= 6) {
            //Checking that some player is ahead by 2 points
            if(Math.abs(playerOneTieBreakPoints - playerTwoTieBreakPoints) == 2){
                //determining who is this player
                if(playerOneTieBreakPoints - playerTwoTieBreakPoints == 2){
                    setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
                    matchScore.setDefaultTieBreakPoints();
                    playerOneSets++;
                    playersScore.get(playerOneId).setSets(playerOneSets);
                    matchScore.setTieBreak(false);
                } else if(playerTwoTieBreakPoints - playerOneTieBreakPoints == 2){
                    setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
                    matchScore.setDefaultTieBreakPoints();
                    playerTwoSets++;
                    playersScore.get(playerTwoId).setSets(playerTwoSets);
                    matchScore.setTieBreak(false);
                }
            }
        }
    }

    private void pointsCalculation(MatchScore matchScore, ConcurrentMap<Integer, PlayerScoreDTO> playersScore, PlayersInfoDTO playersInfo){
        int winPlayerId = playersInfo.getWinPlayerId();

        int playerOneId = playersInfo.getPlayerOneId();
        int playerTwoId = playersInfo.getPlayerTwoId();


        if(playersScore.get(winPlayerId).getPoints() == 0) {
            playersScore.get(winPlayerId).setPoints(15);
        } else if (playersScore.get(winPlayerId).getPoints() == 15) {
            playersScore.get(winPlayerId).setPoints(30);
        } else if (playersScore.get(winPlayerId).getPoints() == 30) {
            playersScore.get(winPlayerId).setPoints(40);

            checkForDeuce(matchScore, playersScore, playerOneId, playerTwoId);

        }
    }

    private void gamesCalculation(MatchScore matchScore, ConcurrentMap<Integer, PlayerScoreDTO> playersScore, PlayersInfoDTO playersInfo) {
        int winPlayerId = playersInfo.getWinPlayerId();

        int playerOneId = playersInfo.getPlayerOneId();
        int playerTwoId = playersInfo.getPlayerTwoId();

        int playerOnePoints = playersInfo.getPlayerOnePoints();
        int playerTwoPoints = playersInfo.getPlayerTwoPoints();

        int playerOneGames = playersInfo.getPlayerOneGames();
        int playerTwoGames = playersInfo.getPlayerTwoGames();

        if (playerOnePoints == 40 || playerTwoPoints == 40) {
            if ((playerOnePoints == 40 && playerOneId == winPlayerId) || (playerTwoPoints == 40 && playerTwoId == winPlayerId)) {
                if (playerOnePoints == 40 && playerOnePoints > playerTwoPoints) {
                    setDefaultPointsForPlayers(playersScore, playerOneId, playerTwoId);

                    playerOneGames++;
                    playersScore.get(playerOneId).setGames(playerOneGames);
                    //updating games value in playersInfo for setsCalculation() method
                    playersInfo.setPlayerOneGames(playerOneGames);

                } else if (playerTwoPoints == 40 && playerTwoPoints > playerOnePoints) {
                    setDefaultPointsForPlayers(playersScore, playerOneId, playerTwoId);

                    playerTwoGames++;
                    playersScore.get(playerTwoId).setGames(playerTwoGames);
                    //updating games value in playersInfo for setsCalculation() method
                    playersInfo.setPlayerTwoGames(playerTwoGames);
                } else if (playerOnePoints == 40 && playerTwoPoints == 40) {
                    //logic of determining winner of the deuce
                    matchScore.setDeuce(true);
                    deuceCalculation(matchScore, playersScore, playersInfo);
                }
            }
        }

        if (playerOneGames == 6 && playerTwoGames == 6) {
            matchScore.setTieBreak(true);
        }
    }

    private void deuceCalculation(MatchScore matchScore, ConcurrentMap<Integer, PlayerScoreDTO> playersScore, PlayersInfoDTO playersInfo){
        int winPlayerId = playersInfo.getWinPlayerId();

        int playerOneId = playersInfo.getPlayerOneId();
        int playerTwoId = playersInfo.getPlayerTwoId();

        int playerOneGames = playersInfo.getPlayerOneGames();
        int playerTwoGames = playersInfo.getPlayerTwoGames();

        if (playerOneId == winPlayerId) {
            int playerOneDeucePoints = matchScore.getPlayerOneDeucePoints();
            playerOneDeucePoints++;
            matchScore.setPlayerOneDeucePoints(playerOneDeucePoints);

            matchScore.setPlayerTwoDeucePoints(0);
        } else if (playerTwoId == winPlayerId) {
            int playerTwoDeucePoints = matchScore.getPlayerTwoDeucePoints();
            playerTwoDeucePoints++;
            matchScore.setPlayerTwoDeucePoints(playerTwoDeucePoints);

            matchScore.setPlayerOneDeucePoints(0);
        }

        if (matchScore.getPlayerOneDeucePoints() == 2) {
            matchScore.setDefaultPointsAfterDeuce();
            setDefaultPointsForPlayers(playersScore, playerOneId, playerTwoId);

            playerOneGames++;
            playersScore.get(playerOneId).setGames(playerOneGames);
            //updating games value in playersInfo for setsCalculation() method
            playersInfo.setPlayerOneGames(playerOneGames);
            matchScore.setDeuce(false);
        } else if (matchScore.getPlayerTwoDeucePoints() == 2) {
            matchScore.setDefaultPointsAfterDeuce();
            setDefaultPointsForPlayers(playersScore, playerOneId, playerTwoId);

            playerTwoGames++;
            playersScore.get(playerTwoId).setGames(playerTwoGames);
            //updating games value in playersInfo for setsCalculation() method
            playersInfo.setPlayerTwoGames(playerTwoGames);
            matchScore.setDeuce(false);
        }
    }

    private void setsCalculation(ConcurrentMap<Integer, PlayerScoreDTO> playersScore, PlayersInfoDTO playersInfo) {
        int playerOneId = playersInfo.getPlayerOneId();
        int playerTwoId = playersInfo.getPlayerTwoId();

        int playerOneGames = playersInfo.getPlayerOneGames();
        int playerTwoGames = playersInfo.getPlayerTwoGames();

        int playerOneSets = playersInfo.getPlayerOneSets();
        int playerTwoSets = playersInfo.getPlayerTwoSets();


        if (playerOneGames == 6 && playerOneGames - 2 >= playerTwoGames) {
            setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
            playerOneSets++;
            playersScore.get(playerOneId).setSets(playerOneSets);
        } else if (playerTwoGames == 6 && playerTwoGames - 2 >= playerOneGames) {
            setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
            playerTwoSets++;
            playersScore.get(playerTwoId).setSets(playerTwoSets);
        } else if (playerOneGames >= 5 && playerTwoGames >= 5) {
            if (playerOneGames == 7 || playerTwoGames == 7) {
                if (playerOneGames == 7 && playerOneGames - 2 == playerTwoGames) {
                    setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
                    playerOneSets++;
                    playersScore.get(playerOneId).setSets(playerOneSets);
                }
                if (playerTwoGames == 7 && playerTwoGames - 2 == playerOneGames) {
                    setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
                    playerTwoSets++;
                    playersScore.get(playerTwoId).setSets(playerTwoSets);
                }
            }
        }
    }

    public void checkForDeuce (MatchScore matchScore, ConcurrentMap < Integer, PlayerScoreDTO > playersScore,
    int playerOneId, int playerTwoId){

        int playerOnePointsForDeuceCheck = playersScore.get(playerOneId).getPoints();
        int playerTwoPointsForDeuceCheck = playersScore.get(playerTwoId).getPoints();

        if (playerOnePointsForDeuceCheck == 40 && playerTwoPointsForDeuceCheck == 40) {
            matchScore.setDeuce(true);
        }
    }

    public boolean isMatchFinished (MatchScore matchScore){
        int playerOneId = matchScore.getPlayer1();
        int playerTwoId = matchScore.getPlayer2();

        if (matchScore.getPlayersScore().get(playerOneId).getSets() == 2) {
            return true;
        } else if (matchScore.getPlayersScore().get(playerTwoId).getSets() == 2) {
            return true;
        }
        return false;
    }

    private void setDefaultPointsForPlayers (ConcurrentMap < Integer, PlayerScoreDTO > playersScore,
    int playerOneId, int playerTwoId){
        playersScore.get(playerOneId).setPoints(0);
        playersScore.get(playerTwoId).setPoints(0);
    }

    private void setDefaultGamesForPlayers (ConcurrentMap < Integer, PlayerScoreDTO > playersScore, int playerOneId, int playerTwoId){
        playersScore.get(playerOneId).setGames(0);
        playersScore.get(playerTwoId).setGames(0);
    }

    private PlayersInfoDTO getPlayersInfo(MatchScore matchScore, ConcurrentMap<Integer, PlayerScoreDTO> playersScore, int winPlayerId) {
        int playerOneId = matchScore.getPlayer1();
        int playerTwoId = matchScore.getPlayer2();

        int playerOnePoints = playersScore.get(playerOneId).getPoints();
        int playerTwoPoints = playersScore.get(playerTwoId).getPoints();

        int playerOneGames = playersScore.get(playerOneId).getGames();
        int playerTwoGames = playersScore.get(playerTwoId).getGames();

        int playerOneSets = playersScore.get(playerOneId).getSets();
        int playerTwoSets = playersScore.get(playerTwoId).getSets();

        PlayersInfoDTO playersInfo = new PlayersInfoDTO(playerOneId, playerTwoId, winPlayerId, playerOnePoints, playerTwoPoints,
                playerOneGames, playerTwoGames, playerOneSets, playerTwoSets);

        return playersInfo;
    }
}
