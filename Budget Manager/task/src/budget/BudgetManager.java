package budget;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class BudgetManager {
    Scanner s;
    private double balance;
    private double purchaseAmount;
    private ArrayList<Product> purchaseList;

    public BudgetManager(Scanner s) {
        this.s = s;
        purchaseList = new ArrayList<>();
    }

    public void showMenu() {
        System.out.println("Choose your action:\n" +
                "1) Add income\n" +
                "2) Add purchase\n" +
                "3) Show list of purchases\n" +
                "4) Balance\n" +
                "5) Save\n" +
                "6) Load\n" +
                "7) Analyze (Sort)\n" +
                "0) Exit");
        var input = s.nextInt();
        s.nextLine();
        System.out.println();
        switch (input) {
            case 1 : {
                System.out.println("Enter income:");
                balance = s.nextLong();
                System.out.println("Income was added!\n");
                showMenu();
                break;
            }

            case 2 : {
                showPurchaseMenu();
                System.out.println();
                showMenu();
                break;
            }

            case 3 : {
                if(purchaseList.isEmpty()) {
                    System.out.println("The purchase list is empty!");
                } else {
                    showPurchaseListType();
                }
                System.out.println();
                showMenu();
                break;
            }

            case 4 : {
                System.out.printf("Balance: $%.2f\n\n", balance - purchaseAmount);
                showMenu();
                break;
            }

            case 5 : {
                try(PrintWriter printWriter = new PrintWriter("purchases.txt")) {
                    printWriter.println(balance);
                    printWriter.println(purchaseAmount);
                    purchaseList.forEach(product -> printWriter.println(product.toString()));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("Purchases were saved!\n");
                showMenu();
                break;
            }

            case 6 : {
                try(BufferedReader bufferedReader = new BufferedReader(new FileReader("purchases.txt"))) {
                    balance = Double.parseDouble(bufferedReader.readLine());
                    purchaseAmount = Double.parseDouble(bufferedReader.readLine());

                    System.out.println("balance = " + balance);
                    System.out.println("purchaseAmount = " + purchaseAmount);
                    while (true) {
                        String line = bufferedReader.readLine();
                        if(line==null)
                            break;
                        purchaseList.add(new Product(line));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Purchases were loaded!\n");
                showMenu();
                break;
            }

            case 7 : {
                showTypesOfSort();
                showMenu();
                break;
            }

            case 0 : {
                System.out.println("Bye!");
            }
        }

    }

    private void showTypesOfSort() {
        System.out.println("How do you want to sort?\n" +
                "1) Sort all purchases\n" +
                "2) Sort by type\n" +
                "3) Sort certain type\n" +
                "4) Back");

        var input = s.nextInt();
        s.nextLine();
        System.out.println();

        switch (input) {
            case 1 : {
                    sortAllByPurchases();
                    showTypesOfSort();
                break;
            }
            case 2 : {
                    sortByTypes();
                    showTypesOfSort();
                break;
            }
            case 3 : {
                    sortByCertainType();
                    showTypesOfSort();
                break;
            }
            case 4 : {
                break;
            }

        }


    }

    private void sortByCertainType() {
        System.out.println("Choose the type of purchase\n" +
                "1) Food\n" +
                "2) Clothes\n" +
                "3) Entertainment\n" +
                "4) Other");
        var input = s.nextInt();
        s.nextLine();
        System.out.println();

        var type = Category.values()[input - 1];
        var typeStr = "";
        switch (input) {
            case 1 : {
                typeStr = "Food";
                break;
            }
            case 2 : {
                typeStr = "Clothes";
                break;
            }
            case 3 : {
                typeStr = "Entertainment";
                break;
            }
            case 4 : {
                typeStr = "Other";
                break;
            }

        }

        var catList = purchaseList.stream()
                .filter(product -> product.category == type)
                .sorted(Comparator.comparingDouble(Product::getPrice).reversed())
                .collect(Collectors.toList());
        getTotalOfProductType(typeStr, catList);
    }

    private void getTotalOfProductType(String typeStr, List<Product> catList) {
        var sum = catList.stream().mapToDouble(product -> product.price).sum();
        if (catList.isEmpty()) {
            System.out.println("The purchase list is empty\n");
        } else {
            System.out.printf("%s:\n", typeStr);
            catList.forEach(Product::displayProduct);
            System.out.printf("Total sum: %.2f\n\n", sum);
        }
    }


    private void sortByTypes() {
        System.out.println("Types:");
        ArrayList<ProductListSum> list = new ArrayList<>();

        var foodCatList = purchaseList.stream().filter(product -> product.category == Category.FOOD).collect(Collectors.toList());
        var foodSum = foodCatList.stream().mapToDouble(product -> product.price).sum();

        list.add(new ProductListSum(foodCatList, foodSum, "Food"));

        var enterCatList = purchaseList.stream().filter(product -> product.category == Category.ENTERTAINMENT).collect(Collectors.toList());
        var EnterSum = enterCatList.stream().mapToDouble(product -> product.price).sum();
        list.add(new ProductListSum(enterCatList, EnterSum, "Entertainment"));

        var clothCatList = purchaseList.stream().filter(product -> product.category == Category.CLOTHES).collect(Collectors.toList());
        var clothSum = clothCatList.stream().mapToDouble(product -> product.price).sum();
        list.add(new ProductListSum(clothCatList, clothSum, "Clothes"));

        var otherCatList = purchaseList.stream().filter(product -> product.category == Category.OTHER).collect(Collectors.toList());
        var otherSum = otherCatList.stream().mapToDouble(product -> product.price).sum();
        list.add(new ProductListSum(otherCatList, otherSum, "Other"));

        list.sort(ProductListSum::compareTo);
        list.forEach(ProductListSum::disPlayRes);
        System.out.printf("Total sum: $%.2f\n", purchaseAmount);
        System.out.println();
    }

    private void sortAllByPurchases() {
        if (purchaseList.isEmpty()) {
            System.out.println("The purchase list is empty!\n");
        } else {
            purchaseList.stream()
                    .sorted(Comparator.comparingDouble(Product::getPrice).reversed())
                    .collect(Collectors.toList())
                    .forEach(Product::displayProduct);


            System.out.printf("Total sum: %.2f\n\n", purchaseAmount);
            //showAllPurchaseList();
        }
    }

    private void showPurchaseListType() {
        System.out.println("Choose the type of purchases\n" +
                "1) Food\n" +
                "2) Clothes\n" +
                "3) Entertainment\n" +
                "4) Other\n" +
                "5) All\n" +
                "6) Back");
        var input = s.nextInt();
        s.nextLine();
        if(input == 6) {
            return;
        }
        System.out.println();
        if(input == 5) {
            if (purchaseList.isEmpty()) {
                System.out.println("The purchase list is empty\n");
            } else {
                showAllPurchaseList();
            }
        } else {
            var type = Category.values()[input - 1];
            var typeStr = "";
            switch (input) {
                case 1 : {
                    typeStr = "Food";
                    break;
                }
                case 2 : {
                    typeStr = "Clothes";
                    break;
                }
                case 3 : {
                    typeStr = "Entertainment";
                    break;
                }
                case 4 : {
                    typeStr = "Other";
                    break;
                }

            }

            var catList = purchaseList.stream().filter(product -> product.category == type).collect(Collectors.toList());
            getTotalOfProductType(typeStr, catList);
        }
        showPurchaseListType();

    }

    private void showAllPurchaseList() {
        purchaseList.forEach(Product::displayProduct);
        System.out.printf("Total sum: %.2f\n\n", purchaseAmount);
    }

    private void showPurchaseMenu() {
        System.out.println("Choose the type of purchase\n" +
                "1) Food\n" +
                "2) Clothes\n" +
                "3) Entertainment\n" +
                "4) Other\n" +
                "5) Back");

        final int input = s.nextInt();
        s.nextLine();
        if(input == 5)
            return;

        var type = Category.values()[input - 1];
        System.out.println();
        System.out.println("Enter purchase name:");
        var name = s.nextLine();
        System.out.println("Enter its price: ");
        var price = s.nextFloat();
        s.nextLine();
        purchaseAmount += price;
        purchaseList.add(new Product(name, price, type));
        System.out.println("Purchase was added!");
        System.out.println();
        showPurchaseMenu();
    }


}

