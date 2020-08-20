package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.MealsUtil.getFilteredWithExceededByCycle;

public class Demo {
    public static List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
    );

    public static void main(String[] args) {
        List<MealWithExceed> mealsWithExceeded = getFilteredWithExceeded(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsWithExceeded.forEach(System.out::println);
        System.out.println("==========================");
        // System.out.println(getFilteredWithExceededByCycle(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    private static List<MealWithExceed> getFilteredWithExceeded(List<Meal> meals, LocalTime startTime, LocalTime endTime, int i) {

        /* groupingBy => return list*/
        // alt ctrl V => create var
        /* Map < day , summ of calories of 1 day> </>*/
        // groupping  by Date to callory (callories summing on 1 day 24 hours)
        Map<LocalDate, Integer> caloriesPerDaysMap = meals.stream().collect(Collectors.groupingBy(
                meal -> meal.getDateTime().toLocalDate(),
                Collectors.summingInt(meal -> meal.getCalories())));

        // MealWithExceed(LocalDateTime dateTime, String description, int calories, boolean exceed) {
        List<MealWithExceed> resultList =
                meals.stream()
                        .filter(um -> TimeUtil.isBetween(um.getDateTime().toLocalTime(), startTime, endTime))
                        .map(um -> new MealWithExceed(um.getDateTime(), um.getDescription(), um.getCalories(),
                                // boolean exceed if more than 2000 cal per day : true
                                caloriesPerDaysMap.get(um.getDateTime().toLocalDate()) > 2000))
                        .collect(Collectors.toList());
        return resultList;
    }
}
