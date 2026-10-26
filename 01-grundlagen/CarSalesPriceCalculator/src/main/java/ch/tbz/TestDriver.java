package ch.tbz;

import static ch.tbz.CareSalesPriceCalculator.calculatePrice;



public class TestDriver {
    private static final double EPSILON = 0.01;
    private static final double BASE_PRICE = 20000.0;
    private static final double SPECIAL_PRICE = 2000.0;
    private static final double EXTRA_PRICE = 1000.0;

    public static void runTests() {
        noExtrasNoDiscount();
        twoExtrasNoAddonDiscount();
        threeExtrasTenPercent();
        fourExtrasTenPercent();
        fiveExtrasFifteenPercent();
        sixExtrasFifteenPercent();
        dealerDiscountLowerThanAddonDiscount();
        dealerDiscountEqualsAddonDiscount();
        dealerDiscountWithoutExtras();
        allValuesZero();
    }

    private static void runTest(double expectedResult, double actualResult, String testName) {
        System.out.println("#---------------------------------------#");
        System.out.println(" -- " + testName + " -- ");
        System.out.println("Expected Result  : " + expectedResult);
        System.out.println("Actual Result    : " + actualResult);
        if (Math.abs(expectedResult - actualResult) < EPSILON) {
            System.out.println("Outcome Expected : \u001B[32mYES\u001B[0m");
        } else {
            System.out.println("Outcome Expected : \u001B[31mNO\u001B[0m");
        }
    }



    // |----- tests -----|

    private static void noExtrasNoDiscount() {
        // 20000 + 2000 + 1000 = 23000
        double expectedResult = 23000.0;
        double actualResult = calculatePrice(BASE_PRICE, SPECIAL_PRICE, EXTRA_PRICE, 0, 0);
        runTest(expectedResult, actualResult, "noExtrasNoDiscount");
    }

    private static void twoExtrasNoAddonDiscount() {
        // 20000 + 2000 + 1000 = 23000
        double expectedResult = 23000.0;
        double actualResult = calculatePrice(BASE_PRICE, SPECIAL_PRICE, EXTRA_PRICE, 2, 0);
        runTest(expectedResult, actualResult, "twoExtrasNoAddonDiscount");
    }

    private static void threeExtrasTenPercent() {
        // 20000 + 2000 + (1000 * 0.90 = 900) = 22900
        double expectedResult = 22900.0;
        double actualResult = calculatePrice(BASE_PRICE, SPECIAL_PRICE, EXTRA_PRICE, 3, 0);
        runTest(expectedResult, actualResult, "threeExtrasTenPercent");
    }

    private static void fourExtrasTenPercent() {
        // 20000 + 2000 + (1000 * 0.90 = 900) = 22900
        double expectedResult = 22900.0;
        double actualResult = calculatePrice(BASE_PRICE, SPECIAL_PRICE, EXTRA_PRICE, 4, 0);
        runTest(expectedResult, actualResult, "fourExtrasTenPercent");
    }

    private static void fiveExtrasFifteenPercent() {
        // 20000 + 2000 + (1000 * 0.85 = 850) = 22850
        double expectedResult = 22850.0;
        double actualResult = calculatePrice(BASE_PRICE, SPECIAL_PRICE, EXTRA_PRICE, 5, 0);
        runTest(expectedResult, actualResult, "fiveExtrasFifteenPercent");
    }

    private static void sixExtrasFifteenPercent() {
        // 20000 + 2000 + (1000 * 0.85 = 850) = 22850
        double expectedResult = 22850.0;
        double actualResult = calculatePrice(BASE_PRICE, SPECIAL_PRICE, EXTRA_PRICE, 6, 0);
        runTest(expectedResult, actualResult, "sixExtrasFifteenPercent");
    }

    private static void dealerDiscountLowerThanAddonDiscount() {
        // (20000 * 0.95 = 19000) + 2000 + (1000 * 0.90 = 900) = 21900
        double expectedResult = 21900.0;
        double actualResult = calculatePrice(BASE_PRICE, SPECIAL_PRICE, EXTRA_PRICE, 3, 5);
        runTest(expectedResult, actualResult, "dealerDiscountLowerThanAddonDiscount");
    }

    private static void dealerDiscountEqualsAddonDiscount() {
        // (20000 * 0.90 = 18000) + 2000 + (1000 * 0.90 = 900) = 20900
        double expectedResult = 20900.0;
        double actualResult = calculatePrice(BASE_PRICE, SPECIAL_PRICE, EXTRA_PRICE, 3, 10);
        runTest(expectedResult, actualResult, "dealerDiscountEqualsAddonDiscount");
    }

    private static void dealerDiscountWithoutExtras() {
        // (20000 * 0.90 = 18000) + 2000 + (1000 * 1.00 = 1000) = 21000
        double expectedResult = 21000.0;
        double actualResult = calculatePrice(BASE_PRICE, SPECIAL_PRICE, EXTRA_PRICE, 0, 10);
        runTest(expectedResult, actualResult, "dealerDiscountWithoutExtras");
    }

    private static void allValuesZero() {
        // 0 + 0 + 0 = 0
        double expectedResult = 0.0;
        double actualResult = calculatePrice(0, 0, 0, 0, 0);
        runTest(expectedResult, actualResult, "allValuesZero");
    }
}