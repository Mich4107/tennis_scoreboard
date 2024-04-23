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
    <div class="finished-match">
        <h1>Match finished!</h1>
        <table>
            <tr class="table-header">
                <th>PLAYER</th>
                <th>SETS</th>
            </tr>

            <c:forEach var="playersScore" items="${playersScore}">
                <tr>
                    <td>${playersScore.value.playerName}</td>
                    <td>${playersScore.value.sets}</td>
                </tr>
            </c:forEach>
        </table>
        <br>
        <form:form action="home" method="get">
            <input type="submit" value="HOME">
        </form:form>
    </div>
</div>
</body>
</html>
