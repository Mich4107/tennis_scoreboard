package ru.yuubi.tennisscoreboard.entity;


import jakarta.persistence.*;
import lombok.Data;
import ru.yuubi.tennisscoreboard.dto.PlayerScoreDTO;

import java.util.HashMap;

@Data
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private int player1;
    @Column
    private int player2;
    @Column
    private int winner;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "player1", insertable = false, updatable = false)
    private Player first_player;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "player2", insertable = false, updatable = false)
    private Player second_player;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "winner", insertable = false, updatable = false)
    private Player winning_player;

    public Match() {
    }

    public Match(int player1, int player2, int winner) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", first_player=" + first_player +
                ", second_player=" + second_player +
                ", winning_player=" + winning_player +
                '}';
    }
}
