package ru.yuubi.tennisscoreboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewMatchPlayers {
    @NotBlank(message = "The name should not be empty")
    private String playerOneName;

    @NotBlank(message = "The name should not be empty")
    private String playerTwoName;

    public boolean areNamesTheSame(){
        playerOneName = playerOneName.trim();
        playerTwoName = playerTwoName.trim();
        return playerOneName.equals(playerTwoName);
    }
}
