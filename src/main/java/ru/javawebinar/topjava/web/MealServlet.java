package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController mealRestController;
    {
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            mealRestController = appCtx.getBean(MealRestController.class);
        }
    }

    private int userId = AuthorizedUser.id();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("getId");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")), userId);

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        mealRestController.create(meal);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id,userId);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, userId) :
                        mealRestController.get(getId(request), userId);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                log.info(request.getParameter("fromdate") + request.getParameter("todate"));
                log.info(request.getParameter("fromtime") + request.getParameter("totime"));
                if (request.getParameter("fromdate") != null) {
                    if (request.getParameter("fromdate").length() != 0 && request.getParameter("todate").length() != 0) {
                        request.setAttribute("meals",
                                MealsUtil.getFilteredWithExceeded(mealRestController.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY, LocalDate.parse(request.getParameter("fromdate")), LocalDate.parse(request.getParameter("todate"))));
                    } else if (request.getParameter("fromtime").length() != 0 && request.getParameter("totime").length() != 0) {
                        request.setAttribute("meals",
                                MealsUtil.getFilteredWithExceeded(mealRestController.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY, LocalTime.parse(request.getParameter("fromtime")), LocalTime.parse(request.getParameter("totime"))));
                    }
                } else {
                    request.setAttribute("meals",
                            MealsUtil.getWithExceeded(mealRestController.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                }
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("getId"));
        return Integer.parseInt(paramId);
    }
}
