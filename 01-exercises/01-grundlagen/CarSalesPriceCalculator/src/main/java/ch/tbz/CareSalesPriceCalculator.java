package ch.tbz;



public class CareSalesPriceCalculator {
    static public double calculatePrice(double basePrice, double specialPrice, double extraPrice, int extras, double discount) {
        double addonDiscount;
        if (extras >= 3) {
            addonDiscount = 10;
        } else if (extras >= 5) {
            addonDiscount = 15;
        } else {
            addonDiscount = 0;
        }
        if (discount > addonDiscount) {
            addonDiscount = discount;
        }
        return basePrice / 100.0 * (100 - discount) + specialPrice + extraPrice / 100.0 * (100 - addonDiscount);
    }
}