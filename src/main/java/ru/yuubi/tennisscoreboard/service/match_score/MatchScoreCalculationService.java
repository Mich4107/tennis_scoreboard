package ru.yuubi.tennisscoreboard.service.match_score;

import org.springframework.stereotype.Component;
import ru.yuubi.tennisscoreboard.dto.MatchScore;
import ru.yuubi.tennisscoreboard.dto.PlayerScoreDTO;

import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class MatchScoreCalculationService {
    public void calculation(MatchScore matchScore, int winPlayerId) {
        ConcurrentMap<Integer, PlayerScoreDTO> playersScore = matchScore.getPlayersScore();

        int playerOneId = matchScore.getPlayer1();
        int playerTwoId = matchScore.getPlayer2();

        int playerOnePoints = playersScore.get(playerOneId).getPoints();
        int playerTwoPoints = playersScore.get(playerTwoId).getPoints();

        int playerOneGames = playersScore.get(playerOneId).getGames();
        int playerTwoGames = playersScore.get(playerTwoId).getGames();

        int playerOneSets = playersScore.get(playerOneId).getSets();
        int playerTwoSets = playersScore.get(playerTwoId).getSets();

        //TIEBREAK!
        if(playerOneGames == 6 && playerTwoGames == 6) {
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

            if(matchScore.getPlayerOneTieBreakPoints() == 7 && matchScore.getPlayerOneTieBreakPoints() - 2 >= matchScore.getPlayerTwoTieBreakPoints()) {
                matchScore.setDefaultTieBreakPoints();
                setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
                playerOneSets++;
                playersScore.get(playerOneId).setSets(playerOneSets);
                matchScore.setTieBreak(false);
            } else if (matchScore.getPlayerTwoTieBreakPoints() == 7 && matchScore.getPlayerTwoTieBreakPoints() - 2 >= matchScore.getPlayerOneTieBreakPoints()){
                matchScore.setDefaultTieBreakPoints();
                setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
                playerTwoSets++;
                playersScore.get(playerOneId).setSets(playerTwoSets);
                matchScore.setTieBreak(false);
            } else if(matchScore.getPlayerOneTieBreakPoints() >= 6 && matchScore.getPlayerTwoTieBreakPoints() >=6) {
                //Checking that some player is ahead by 2 points
                if(Math.abs(matchScore.getPlayerOneTieBreakPoints() - matchScore.getPlayerTwoTieBreakPoints()) == 2){
                    //determining who is this player
                    if(matchScore.getPlayerOneTieBreakPoints()- matchScore.getPlayerTwoTieBreakPoints()==2){
                        setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
                        playerOneSets++;
                        playersScore.get(playerOneId).setSets(playerOneSets);
                        matchScore.setTieBreak(false);
                    } else if(matchScore.getPlayerTwoTieBreakPoints()- matchScore.getPlayerOneTieBreakPoints()==2){
                        setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
                        playerTwoSets++;
                        playersScore.get(playerTwoId).setSets(playerTwoSets);
                        matchScore.setTieBreak(false);
                    }
                }
            }
            //if there is tiebreak, we don't need to go farther, and we just return from this calculation method
            return;
        }


        //setting points for players
        if(playersScore.get(winPlayerId).getPoints() == 0) {
            playersScore.get(winPlayerId).setPoints(15);
        } else if (playersScore.get(winPlayerId).getPoints() == 15) {
            playersScore.get(winPlayerId).setPoints(30);
        } else if (playersScore.get(winPlayerId).getPoints() == 30) {
            playersScore.get(winPlayerId).setPoints(40);

            //checking for deuce
            int playerOnePointsForDeuceCheck = playersScore.get(playerOneId).getPoints();
            int playerTwoPointsForDeuceCheck = playersScore.get(playerTwoId).getPoints();

            if (playerOnePointsForDeuceCheck == 40 && playerTwoPointsForDeuceCheck == 40) {
                matchScore.setDeuce(true);
            }

        } else { //logic of determining the winner of the game
            if(playerOnePoints == 40 && playerOnePoints > playerTwoPoints) {
                setDefaultPointsForPlayers(playersScore, playerOneId, playerTwoId);

                playerOneGames++;
                playersScore.get(playerOneId).setGames(playerOneGames);

            } else if (playerTwoPoints == 40 && playerTwoPoints > playerOnePoints) {
                setDefaultPointsForPlayers(playersScore, playerOneId, playerTwoId);

                playerTwoGames++;
                playersScore.get(playerTwoId).setGames(playerTwoGames);

            } else if (playerOnePoints == 40 && playerTwoPoints == 40) {
                //logic of determining winner of the deuce
                matchScore.setDeuce(true);
                if(playerOneId == winPlayerId) {
                    int playerOneDeucePoints = matchScore.getPlayerOneDeucePoints();
                    playerOneDeucePoints++;
                    matchScore.setPlayerOneDeucePoints(playerOneDeucePoints);

                    matchScore.setPlayerTwoDeucePoints(0);
                } else if(playerTwoId == winPlayerId) {
                    int playerTwoDeucePoints = matchScore.getPlayerTwoDeucePoints();
                    playerTwoDeucePoints++;
                    matchScore.setPlayerTwoDeucePoints(playerTwoDeucePoints);

                    matchScore.setPlayerOneDeucePoints(0);
                }

                if(matchScore.getPlayerOneDeucePoints() == 2) {
                    matchScore.setDefaultPointsAfterDeuce();
                    setDefaultPointsForPlayers(playersScore, playerOneId, playerTwoId);

                    playerOneGames++;
                    playersScore.get(playerOneId).setGames(playerOneGames);
                    matchScore.setDeuce(false);
                } else if (matchScore.getPlayerTwoDeucePoints() == 2){
                    matchScore.setDefaultPointsAfterDeuce();
                    setDefaultPointsForPlayers(playersScore, playerOneId, playerTwoId);

                    playerTwoGames++;
                    playersScore.get(playerTwoId).setGames(playerTwoGames);
                    matchScore.setDeuce(false);
                }

            }
            //logic of determining the winner of the set
            if(playerOneGames == 6 && playerOneGames-2 >= playerTwoGames) {
                setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
                playerOneSets++;
                playersScore.get(playerOneId).setSets(playerOneSets);
            } else if (playerTwoGames == 6 && playerTwoGames-2 >= playerOneGames) {
                setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
                playerTwoSets++;
                playersScore.get(playerTwoId).setSets(playerTwoSets);
            } else if(playerOneGames >= 5 && playerTwoGames >= 5) {
                if(playerOneGames == 7 || playerTwoGames == 7) {
                    if(playerOneGames == 7 && playerOneGames-2 == playerTwoGames) {
                        setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
                        playerOneSets++;
                        playersScore.get(playerOneId).setSets(playerOneSets);
                    }
                    if(playerTwoGames == 7 && playerTwoGames-2 == playerOneGames) {
                        setDefaultGamesForPlayers(playersScore, playerOneId, playerTwoId);
                        playerTwoSets++;
                        playersScore.get(playerTwoId).setSets(playerTwoSets);
                    }
                }

            }
            if(playerOneGames == 6 && playerTwoGames == 6) {
                matchScore.setTieBreak(true);
            }
        }

    }
    public boolean isMatchFinished(MatchScore matchScore){
        int playerOneId = matchScore.getPlayer1();
        int playerTwoId = matchScore.getPlayer2();

        if(matchScore.getPlayersScore().get(playerOneId).getSets() == 2) {
            return true;
        } else if (matchScore.getPlayersScore().get(playerTwoId).getSets() == 2) {
            return true;
        }
        return false;
    }

    private void setDefaultPointsForPlayers(ConcurrentMap<Integer, PlayerScoreDTO> playersScore,
                                            int playerOneId, int playerTwoId){
        playersScore.get(playerOneId).setPoints(0);
        playersScore.get(playerTwoId).setPoints(0);
    }

    private void setDefaultGamesForPlayers(ConcurrentMap<Integer, PlayerScoreDTO> playersScore,
                                           int playerOneId, int playerTwoId) {
        playersScore.get(playerOneId).setGames(0);
        playersScore.get(playerTwoId).setGames(0);
    }


}
