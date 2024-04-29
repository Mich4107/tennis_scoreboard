package ru.yuubi.tennisscoreboard.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yuubi.tennisscoreboard.dto.MatchScore;
import ru.yuubi.tennisscoreboard.dto.PlayerScoreDTO;
import ru.yuubi.tennisscoreboard.dto.NewMatchPlayers;
import ru.yuubi.tennisscoreboard.entity.Match;
import ru.yuubi.tennisscoreboard.service.*;
import ru.yuubi.tennisscoreboard.service.match_score.MatchScoreCalculationService;
import ru.yuubi.tennisscoreboard.service.match_score.OngoingMatchesService;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Controller
public class MatchScoreController {
    private static final int ITEMS_PER_PAGE = 6;
    private int UUID = 0;

    @Autowired
    private OngoingMatchesService ongoingMatchesService;

    @Autowired
    private MatchScoreCalculationService matchScoreCalculationService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private MatchService matchService;

    @GetMapping("/home")
    public String showHomePage(){
        return "home";
    }

    @GetMapping("/new-match")
    public String newPage(Model model){
        model.addAttribute("players", new NewMatchPlayers());
        return "new-match";
    }

    @PostMapping("/new-match")
    public String showCurrentMatch(@Valid @ModelAttribute("players") NewMatchPlayers newMatchPlayers,
                                   BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "new-match";
        }
        if(newMatchPlayers.areNamesTheSame()){
            model.addAttribute("areNamesTheSame", true);
            return "new-match";
        }
        playerService.savePlayerIfHeDoesntExist(newMatchPlayers);
        MatchScore matchScore = MatchScore.createMatchScoreFromNewMatchPlayers(newMatchPlayers, playerService);
        ongoingMatchesService.addMatch(UUID, matchScore);
        return "redirect:/match-score?uuid="+UUID++;
    }

    @GetMapping("/match-score")
    public String showCurrentMatch(@RequestParam("uuid") int uuid, Model model){
        MatchScore matchScore = ongoingMatchesService.getMatch(uuid);
        ConcurrentMap<Integer, PlayerScoreDTO> playersScore = matchScore.getPlayersScore();
        model.addAttribute("playersScore", playersScore);
        model.addAttribute("uuid", uuid);

        if(matchScore.isDeuce()){
            model.addAttribute("isDeuce", true);
            model.addAttribute("playerOneDeucePoints", matchScore.getPlayerOneDeucePoints());
            model.addAttribute("playerTwoDeucePoints", matchScore.getPlayerTwoDeucePoints());
        }
        if(matchScore.isTieBreak()){
            model.addAttribute("isTieBreak", true);
            model.addAttribute("playerOneTieBreakPoints", matchScore.getPlayerOneTieBreakPoints());
            model.addAttribute("playerTwoTieBreakPoints", matchScore.getPlayerTwoTieBreakPoints());
        }

        return "current-match";
    }

    @PostMapping("/match-score")
    public String updateCurrentMatchScore(@RequestParam("uuid") int uuid,
                                          @RequestParam("winPlayerId") int winPlayerId,
                                          Model model){
        MatchScore matchScore = ongoingMatchesService.getMatch(uuid);
        matchScoreCalculationService.calculation(matchScore, winPlayerId);

        if(matchScoreCalculationService.isMatchFinished(matchScore)){
            ConcurrentMap<Integer, PlayerScoreDTO> playersScore = matchScore.getPlayersScore();
            model.addAttribute("playersScore", playersScore);

            matchService.saveMatchFromMatchScore(matchScore);
            ongoingMatchesService.removeMatch(uuid);

            return "finished-match";
        }
        return "redirect:/match-score?uuid="+uuid;
    }


    @GetMapping("/matches")
    public String showMatchesPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "filter_by_name", defaultValue = "") String playerName,
                                  Model model){
        List<Match> matches = matchService.getAllMatches();

        if(!playerName.equals("")) {
            matches = matches.stream().filter(match -> match.getFirst_player().getName().equals(playerName) ||
                    match.getSecond_player().getName().equals(playerName)).collect(Collectors.toList());
            model.addAttribute("isAllMatchesButtonNeeded", true);
            model.addAttribute("filter_name", playerName);
            if(matches.isEmpty()) {
                model.addAttribute("isPlayerMatchesEmpty", true);
                model.addAttribute("isAllMatchesButtonNeeded", false);
                matches = matchService.getAllMatches();
            }
        }

        int totalPages = (int) Math.ceil((double) matches.size() / ITEMS_PER_PAGE);
        int startIndex = (page - 1) * ITEMS_PER_PAGE;
        int endIndex = startIndex + Math.min(ITEMS_PER_PAGE, matches.size() - startIndex);

        List<Match> matchesOnPage = matches.subList(startIndex, endIndex);

        model.addAttribute("matchesOnPage", matchesOnPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "matches";
    }
}
