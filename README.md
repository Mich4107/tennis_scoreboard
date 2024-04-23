# TennisScoreboard

TennisScoreboard is a web application that provides tennis match scoreboard.
This application enables users to create new matches, view finished matches,
search for matches by player names, and keep track of the points in a current match.

## Functionality

### Match Management:

- Create new match
- View finished matches
- Search matches by player names
- Score tracking in the current match

### Scoring Rules:

Each match is played according to the following rules:

- A match is played best of 3 sets (First to win 2 sets wins the match)
- If the score in a set reaches 6/6, a tie-break is played to 7 points (If the score is 6-6, the tie-break will continue until there is a 2-point difference).

## Pages:

### Home Page

- Contains links to the page for creating a new match and the page for displaying completed matches.

### New Match Page

URL: `/new-match`.

Interface:

- An HTML form with fields for entering the names of the first and second players, as well as a button for initiating the match.
- Clicking the button for initiating the match sends a POST request to `/new-match`.

### Match Score Page

URL: `/match-score?uuid=$match_id`. The GET parameter uuid contains the match UUID.

Interface:

- A table displaying player names and current score.
- Forms and buttons for actions - one button for recording a point won by the first player, and another for recording a point won by the second player.
- Clicking these buttons sends a POST request to `/match-score?uuid=match_id`, the fields of the sent form contain the ID of the player who won the point.

### Played Matches Page

URL: `/matches?page=page_number&filter_by_name=player_name`. GET parameters:

- `page` - page number. If the parameter is not set, the first page is assumed.
- `filter_by_name` - the name of the player whose matches are searched. If the parameter is not set, all matches are displayed.

This page displays the list of played matches page by page. It allows to search for a player's matches by their name.

Interface:

- A form for filtering matches by player name, with an input field for entering a player's name and a button for initiating the search. Clicking this button forms a GET request of the type `/matches?filter_by_name=name`.
- A list displaying the results of the search for matches.
- A page switcher if more matches are found than fit on one page.