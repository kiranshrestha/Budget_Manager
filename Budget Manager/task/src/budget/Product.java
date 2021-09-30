package budget;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

class Product implements Serializable, Comparable<Product> {
    String name;
    float price;
    Category category;
    public Product(String name, float price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Product(String all) {
        var list = all.split("\\|");
        this.name = list[0];
        this.price = Float.parseFloat(list[1]);
        this.category = Category.values()[Integer.parseInt(list[2]) - 1];
    }

    public void displayProduct() {
        System.out.printf("%s $%.2f\n", name, price);
    }

    @Override
    public String toString() {
        return name +"|"+ price + "|" + category.val;
    }

    @Override
    public int compareTo(Product product) {
        return (int) (product.price - this.price) == 0 ? 0 : (product.price > this.price) ? 1 : -1;
    }

    public float getPrice() {
        return price;
    }
}

