package budget;

import java.util.List;

public class ProductListSum implements Comparable<ProductListSum> {

    List<Product> productArrayList;

    double sum;

    String name;

    public ProductListSum(List<Product> foodCatList, double foodSum, String name) {
        productArrayList = foodCatList;
        sum = foodSum;
        this.name = name;
    }

    @Override
    public int compareTo(ProductListSum productListSum) {
        return (int) (productListSum.sum - this.sum);
    }


    public void disPlayRes() {
        System.out.printf("%s - $%.2f\n", name, sum);
    }
}
