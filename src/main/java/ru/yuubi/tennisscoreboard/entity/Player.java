package ru.yuubi.tennisscoreboard.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "player_name")
    private String name;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }
}
