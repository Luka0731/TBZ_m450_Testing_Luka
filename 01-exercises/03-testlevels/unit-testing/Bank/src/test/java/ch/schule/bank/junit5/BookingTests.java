package ch.schule.bank.junit5;

import ch.schule.BankUtils;
import ch.schule.Booking;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;



/**
 * Tests for the class Booking.
 *
 * @author Luigi Cavuoti
 * @version 1.1
 */
public class BookingTests {
    private Booking booking;

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;

    @BeforeEach
    public void setUp() {
        booking = new Booking(31, 100000);
        // catch the console output so print() can be checked
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    @DisplayName("a booking keeps its date and amount")
    public void testInitialization() {
        assertAll("booking",
                () -> assertEquals(31, booking.getDate()),
                () -> assertEquals(100000, booking.getAmount()));

        // a withdrawal is stored as a negative amount
        Booking withdrawal = new Booking(0, -40000);
        assertAll("withdrawal",
                () -> assertEquals(0, withdrawal.getDate()),
                () -> assertEquals(-40000, withdrawal.getAmount()));
    }

    @Test
    @DisplayName("print() writes date, amount and the new balance")
    public void testPrint() {
        booking.print(50000);

        // amounts through BankUtils, the decimal separator depends on the system language
        assertEquals("02.02.1970 "
                + BankUtils.formatAmount(100000) + " "
                + BankUtils.formatAmount(150000), output.toString().trim());

        output.reset();
        Booking withdrawal = new Booking(0, -40000);
        withdrawal.print(100000);

        assertEquals("01.01.1970 "
                + BankUtils.formatAmount(-40000) + " "
                + BankUtils.formatAmount(60000), output.toString().trim());
    }
}