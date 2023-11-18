package site.nomoreparties.stellarburgers.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private List<String> ingredients;

    public static Order withValidIngredients() {
        Order order = new Order();
        order.ingredients = new ArrayList<>();
        order.ingredients.add("61c0c5a71d1f82001bdaaa73");
        order.ingredients.add("61c0c5a71d1f82001bdaaa76");
        order.ingredients.add("61c0c5a71d1f82001bdaaa6d");
        return order;
    }

    public static Order withoutIngredients() {
        Order order = new Order();
        order.ingredients = new ArrayList<>();
        return order;
    }

    public static Order withWrongIngredients() {
        Order order = new Order();
        order.ingredients = new ArrayList<>();
        order.ingredients.add("undefined");
        return order;
    }
}