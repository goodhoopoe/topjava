<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
    <style>
        .exceeded {
            background-color: red;
        }

        .normal {
            background-color: green;
        }

        td {
            border: 1px solid black;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table>
    <thead>
    <tr>
        <th>Название</th>
        <th>Время</th>
        <th>Калории</th>
    </tr>
    </thead>
    <c:forEach items="${mealList}" var="meal">
        <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.model.MealWithExceed"/>
        <tr style="${meal.exceed ? 'exceeded' : 'normal'}">
            <td>${meal.description}</td>
            <td>${meal.dateTime}</td>
            <td>${meal.calories}</td>
        </tr>
    </c:forEach>


</table>
</body>
</html>