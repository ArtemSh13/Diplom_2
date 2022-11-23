import java.util.List;

public class Order {
    private List<String> ingredients;

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getIngredientByIndex(int index) {
        return ingredients.get(index);
    }

    public int getNumberOfIngredients() {
        return ingredients.size();
    }

    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }
}
