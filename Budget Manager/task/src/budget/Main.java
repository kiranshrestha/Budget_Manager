package budget;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // write your code here
        Scanner s = new Scanner(System.in);
        BudgetManager budgetManager = new BudgetManager(s);
        budgetManager.showMenu();
    }
}
