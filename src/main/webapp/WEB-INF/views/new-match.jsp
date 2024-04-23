<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>New match</title>
    <style><%@ include file="../css/style.css"%></style>
</head>
<body>
<nav>
    <a href="home">HOME</a>
    <a href="new-match">NEW</a>
    <a href="matches">MATCHES</a>
</nav>

<div class="block">
    <div class="new-page-block">
        <h1>New match</h1>
        <form:form action="new-match" method="post" modelAttribute="players">
            <div class="player-names">
                <span class="form-error">
                    <form:errors path="playerOneName"/>
                    <c:if test="${areNamesTheSame}">
                        Players names should not be the same
                    </c:if>
                </span>
                PLAYER 1: <form:input path="playerOneName"/>
                <br>
                <span class="form-error">
                    <form:errors path="playerTwoName"/>
                </span>
                PLAYER 2: <form:input path="playerTwoName"/>
                <br>
            </div>
            <input type="submit" value="START">
        </form:form>
    </div>
</div>
</body>
</html>
