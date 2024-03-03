
public class Order {
    private Object[] ingredients;

    public Object[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Object[] ingredients) {
        this.ingredients = ingredients;
    }

    public Order(Object[] ingredients) {
        this.ingredients = ingredients;
    }
}