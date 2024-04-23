<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    <div class="all-matches">
        <h1>Finished matches</h1>

        <c:if test="${isPlayerMatchesEmpty}">
        <span class="message-info">No matches were found with this player</span>
        </c:if>
        <div class="players-search">
            <form:form action="matches" method="get">
                <span>Name:  </span> <input type="text" name="filter_by_name">
                <input type="submit" value="Search">
                <form:form action="matches" method="get">
                    <c:if test="${isAllMatchesButtonNeeded}">
                        <input type="submit" value="All matches">
                    </c:if>
                </form:form>
            </form:form>
        </div>
        <table class="matches-table">
            <tr class="table-header">
                <th>ID</th>
                <th>Player 1</th>
                <th>Player 2</th>
                <th>Winner</th>
            </tr>

            <c:forEach var="matches" items="${matchesOnPage}" >
                <tr>
                    <td>${matches.id}</td>
                    <td>${matches.first_player.name}</td>
                    <td>${matches.second_player.name}</td>
                    <td>${matches.winning_player.name}</td>
                </tr>
            </c:forEach>
        </table>

        <div class="page-numbers">
            <c:if test="${totalPages > 1}">
                <c:if test="${currentPage-1 >= 1}">
                    <a href="matches?page=${currentPage-1}&filter_by_name=${filter_name}">Prev</a>
                </c:if>
                <c:forEach var="pageNumber" begin="1" end="${totalPages}">
                    <a href="matches?page=${pageNumber}&filter_by_name=${filter_name}">${pageNumber}</a>
                </c:forEach>
                <c:if test="${currentPage+1 <= totalPages}">
                    <a href="matches?page=${currentPage+1}&filter_by_name=${filter_name}">Next</a>
                </c:if>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>
