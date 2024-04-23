package ru.yuubi.tennisscoreboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yuubi.tennisscoreboard.dto.NewMatchPlayers;
import ru.yuubi.tennisscoreboard.repository.PlayerRepository;
import ru.yuubi.tennisscoreboard.entity.Player;

@Service
public class PlayerServiceImpl implements PlayerService{
    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Player getPlayerByName(String name) {
        return playerRepository.findByName(name);
    }

    @Override
    public void savePlayer(Player player) {
        playerRepository.save(player);
    }
    @Override
    public void savePlayerIfHeDoesntExist(NewMatchPlayers newMatchPlayers){
        if(getPlayerByName(newMatchPlayers.getPlayerOneName()) == null){
            Player player1 = new Player(newMatchPlayers.getPlayerOneName());
            savePlayer(player1);
        }
        if(getPlayerByName(newMatchPlayers.getPlayerTwoName()) == null){
            Player player2 = new Player(newMatchPlayers.getPlayerTwoName());
            savePlayer(player2);
        }
    }
}
