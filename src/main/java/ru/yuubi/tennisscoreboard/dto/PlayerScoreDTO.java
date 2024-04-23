package ru.yuubi.tennisscoreboard.dto;

import lombok.Data;

@Data
public class PlayerScoreDTO {
    private String playerName;
    private int sets;
    private int games;
    private int points;
}
