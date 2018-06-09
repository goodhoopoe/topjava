package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    private final int USER_ID = 100001;

    static {
        SLF4JBridgeHandler.install();
    }
    @Autowired
    MealService mealService;

    @Test
    public void get() {
        assertMatch(mealService.get(100004, USER_ID), THIRD);
    }

    @Test(expected = NotFoundException.class)
    public void getForeighnMeal() {
        assertMatch(mealService.get(100002, USER_ID), THIRD);
    }

    @Test
    public void delete() {
        mealService.delete(100005, USER_ID);
        assertMatch(mealService.getAll(USER_ID), THIRD);

    }

    @Test(expected = NotFoundException.class)
    public void deleteForeignMeal() {
        mealService.delete(100002, USER_ID);
        assertMatch(mealService.getAll(USER_ID), FIRST);
    }

    @Test
    public void getBetweenDates() {
        assertMatch(mealService.getBetweenDates(LocalDateTime.now().toLocalDate(), LocalDateTime.now().toLocalDate(), USER_ID), Collections.EMPTY_LIST);
    }

    @Test
    public void getBetweenDateTimes() {
        assertMatch(mealService.getBetweenDateTimes(LocalDateTime.now(), LocalDateTime.now(), USER_ID), Collections.EMPTY_LIST);
    }

    @Test
    public void getAll() {
        assertMatch(mealService.getAll(USER_ID), FOURTH,THIRD);
    }

    @Test
    public void update() {
        Meal updated = new Meal(FOURTH.getId(), FOURTH.getDateTime(), FOURTH.getDescription(), FOURTH.getCalories());
        updated.setDescription("UpdatedName");
        mealService.update(updated, USER_ID);
        assertMatch(mealService.get(100005, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateForeignMeal() {
        Meal updated = new Meal(FIRST.getId(), FIRST.getDateTime(), FIRST.getDescription(), FIRST.getCalories());
        updated.setDescription("UpdatedName");
        mealService.update(updated, USER_ID);
        assertMatch(mealService.get(100002, USER_ID), updated);
    }


    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.now(),"New", 1555);
        Meal created = mealService.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(mealService.getAll(USER_ID),  newMeal, FOURTH, THIRD);
    }
}