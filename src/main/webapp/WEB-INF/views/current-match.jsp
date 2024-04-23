<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Tennis Scoreboard</title>
    <style><%@ include file="../css/style.css"%></style>
</head>
<body>

<nav>
    <a href="home">HOME</a>
    <a href="new-match">NEW</a>
    <a href="matches">MATCHES</a>
</nav>

<div class="block">
    <div class="current-match">
        <h1>Match score</h1>
        <table>
            <tr class="table-header">
                <th>PLAYER</th>
                <th>SETS</th>
                <th>GAMES</th>
                <th>POINTS</th>
                <c:if test="${isDeuce}">
                    <th class="action-header">40:40 Deuce!</th>
                </c:if>
                <c:if test="${isTieBreak}">
                    <th class="action-header">Tie-Break!</th>
                </c:if>
            </tr>

            <c:forEach var="playersScore" items="${playersScore}" varStatus="loopStatus">

                <c:url var="winsPoint" value="/match-score">
                    <c:param name="winPlayerId" value="${playersScore.key}"/>
                    <c:param name="uuid" value="${uuid}"/>
                </c:url>

                <tr>
                    <td>${playersScore.value.playerName}</td>
                    <td>${playersScore.value.sets}</td>
                    <td>${playersScore.value.games}</td>
                    <td>${playersScore.value.points}</td>
                    <td class="button">

                        <form:form action="${winsPoint}" method="post">
                            <input type="submit" value="WINS POINT">
                            <span class="deuce-tiebreak-points">
                                <c:if test="${isDeuce}">
                                    <c:if test="${loopStatus.index == 0}">
                                        ${playerOneDeucePoints}
                                    </c:if>
                                    <c:if test="${loopStatus.index == 1}">
                                        ${playerTwoDeucePoints}
                                    </c:if>
                                </c:if>
                                <c:if test="${isTieBreak}">
                                    <c:if test="${loopStatus.index == 0}">
                                        ${playerOneTieBreakPoints}
                                    </c:if>
                                    <c:if test="${loopStatus.index == 1}">
                                        ${playerTwoTieBreakPoints}
                                    </c:if>
                                </c:if>
                            </span>
                        </form:form>

                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>
