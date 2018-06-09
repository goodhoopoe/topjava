package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


public class MealTestData {

    private static int id = 100002;

    public static final Meal FIRST = new Meal(id, LocalDateTime.of(2011,05,16,10,36,38), "Админ ланч",510);
    public static final Meal SECOND = new Meal(++id, LocalDateTime.of(2011,05,16,15,36,38), "Админ ужин",1500);
    public static final Meal THIRD = new Meal(++id, LocalDateTime.of(2011,05,16,13,36,38), "Юзер обед",900);
    public static final Meal FOURTH = new Meal(++id, LocalDateTime.of(2011,05,16,21,36,38), "Юзер ужин",1000);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
