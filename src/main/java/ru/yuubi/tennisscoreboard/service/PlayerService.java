package ru.yuubi.tennisscoreboard.service;

import ru.yuubi.tennisscoreboard.dto.NewMatchPlayers;
import ru.yuubi.tennisscoreboard.entity.Player;

public interface PlayerService {
    Player getPlayerByName(String name);

    void savePlayer(Player player);

    void savePlayerIfHeDoesntExist(NewMatchPlayers newMatchPlayers);


}
