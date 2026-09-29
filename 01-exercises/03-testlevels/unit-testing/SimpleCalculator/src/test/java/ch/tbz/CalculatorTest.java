package ch.tbz;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculatorTest {
    private static final double DELTA = 0.0001;
    private final Calculator calculator = new Calculator();



    // |----- add method -----|

    @Test
    @DisplayName("2 + 3 should equal 5")
    void twoPlusThreeShouldEqualFive() {
        assertEquals(5.0, calculator.add(2.0, 3.0), DELTA);
    }

    @Test
    @DisplayName("-2 + -3 should equal -5")
    void minusTwoPlusMinusThreeShouldEqualMinusFive() {
        assertEquals(-5.0, calculator.add(-2.0, -3.0), DELTA);
    }

    @Test
    @DisplayName("5 + 0 should equal 5")
    void fivePlusZeroShouldEqualFive() {
        assertEquals(5.0, calculator.add(5.0, 0.0), DELTA);
    }

    @Test
    @DisplayName("0.1 + 0.2 should equal 0.3")
    void zeroPointOnePlusZeroPointTwoShouldEqualZeroPointThree() {
        assertEquals(0.3, calculator.add(0.1, 0.2), DELTA);
    }



    // |----- subtract method -----|

    @Test
    @DisplayName("5 - 3 should equal 2")
    void fiveMinusThreeShouldEqualTwo() {
        assertEquals(2.0, calculator.subtract(5.0, 3.0), DELTA);
    }

    @Test
    @DisplayName("3 - 5 should equal -2")
    void threeMinusFiveShouldEqualMinusTwo() {
        assertEquals(-2.0, calculator.subtract(3.0, 5.0), DELTA);
    }

    @Test
    @DisplayName("5 - 0 should equal 5")
    void fiveMinusZeroShouldEqualFive() {
        assertEquals(5.0, calculator.subtract(5.0, 0.0), DELTA);
    }



    // |----- multiply method -----|

    @Test
    @DisplayName("4 * 2.5 should equal 10")
    void fourTimesTwoPointFiveShouldEqualTen() {
        assertEquals(10.0, calculator.multiply(4.0, 2.5), DELTA);
    }

    @Test
    @DisplayName("-3 * 4 should equal -12")
    void minusThreeTimesFourShouldEqualMinusTwelve() {
        assertEquals(-12.0, calculator.multiply(-3.0, 4.0), DELTA);
    }

    @Test
    @DisplayName("5 * 0 should equal 0")
    void fiveTimesZeroShouldEqualZero() {
        assertEquals(0.0, calculator.multiply(5.0, 0.0), DELTA);
    }



    // |----- divide method -----|

    @Test
    @DisplayName("10 / 4 should equal 2.5")
    void tenDividedByFourShouldEqualTwoPointFive() {
        assertEquals(2.5, calculator.divide(10.0, 4.0), DELTA);
    }

    @Test
    @DisplayName("-10 / 4 should equal -2.5")
    void minusTenDividedByFourShouldEqualMinusTwoPointFive() {
        assertEquals(-2.5, calculator.divide(-10.0, 4.0), DELTA);
    }

    @Test
    @DisplayName("7 / 1 should equal 7")
    void sevenDividedByOneShouldEqualSeven() {
        assertEquals(7.0, calculator.divide(7.0, 1.0), DELTA);
    }

    @Test
    @DisplayName("0 / 5 should equal 0")
    void zeroDividedByFiveShouldEqualZero() {
        assertEquals(0.0, calculator.divide(0.0, 5.0), DELTA);
    }

    @Test
    @DisplayName("10 / 0 should throw an ArithmeticException")
    void tenDividedByZeroShouldThrowArithmeticException() {
        ArithmeticException exception = assertThrows(
                ArithmeticException.class,
                () -> calculator.divide(10.0, 0.0));

        assertEquals("Division by zero is not allowed.", exception.getMessage());
    }
}