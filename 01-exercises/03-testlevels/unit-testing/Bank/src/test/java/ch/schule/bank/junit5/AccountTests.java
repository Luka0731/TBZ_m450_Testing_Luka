package ch.schule.bank.junit5;

import ch.schule.Account;
import ch.schule.BankUtils;
import ch.schule.Booking;
import ch.schule.SalaryAccount;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;



/**
 * Tests for the class Account.
 *
 * @author xxxx
 * @version 1.0
 */
public class AccountTests {
    private SavingsAccount savingsAccount;
    private SalaryAccount salaryAccount;

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;

    @BeforeEach
    public void setUp() {
        savingsAccount = new SavingsAccount("S-1000");
        salaryAccount = new SalaryAccount("P-1001", -50000);
        // catch the console output so print() can be checked
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    @DisplayName("a new account has the given id and a balance of 0")
    public void testInit() {
        assertAll("account",
                () -> assertEquals("S-1000", savingsAccount.getId()),
                () -> assertEquals(0, savingsAccount.getBalance()),
                () -> assertEquals("P-1001", salaryAccount.getId()),
                () -> assertEquals(0, salaryAccount.getBalance()));
    }

    @Test
    @DisplayName("deposit raises the balance, negative amounts are rejected")
    public void testDeposit() {
        assertTrue(savingsAccount.deposit(1, 10000));
        assertEquals(10000, savingsAccount.getBalance());

        assertTrue(savingsAccount.deposit(2, 5000));
        assertEquals(15000, savingsAccount.getBalance());

        // edge case: depositing 0 is allowed
        assertTrue(savingsAccount.deposit(3, 0));
        assertEquals(15000, savingsAccount.getBalance());

        assertFalse(savingsAccount.deposit(4, -1));
        assertEquals(15000, savingsAccount.getBalance());
    }

    @Test
    @DisplayName("withdraw lowers the balance, negative amounts are rejected")
    public void testWithdraw() {
        savingsAccount.deposit(1, 10000);

        assertTrue(savingsAccount.withdraw(2, 4000));
        assertEquals(6000, savingsAccount.getBalance());

        // edge case: withdrawing 0 is allowed
        assertTrue(savingsAccount.withdraw(3, 0));
        assertEquals(6000, savingsAccount.getBalance());

        assertFalse(savingsAccount.withdraw(4, -1));
        assertEquals(6000, savingsAccount.getBalance());
    }

    @Test
    @DisplayName("a SavingsAccount used through an Account reference and its Booking reference")
    public void testReferences() {
        Account account = savingsAccount;
        Account otherAccount = salaryAccount;
        Booking booking = new Booking(1, 10000);

        assertSame(savingsAccount, account);

        // withdraw() of SavingsAccount is used, even though the reference is of type Account
        assertFalse(account.withdraw(1, 5000));
        // the same withdrawal works on the salary account because of the credit limit
        assertTrue(otherAccount.withdraw(1, 5000));

        assertNull(account.getBooking());
        account.setBooking(booking);
        assertSame(booking, savingsAccount.getBooking());
    }

    @Test
    @DisplayName("transactions before the last booking are not allowed")
    public void testCanTransact() {
        // without bookings every date works
        assertTrue(savingsAccount.canTransact(0));
        assertTrue(savingsAccount.canTransact(100));

        savingsAccount.deposit(10, 10000);

        assertFalse(savingsAccount.canTransact(9));
        // edge case: same day as the last booking
        assertTrue(savingsAccount.canTransact(10));
        assertTrue(savingsAccount.canTransact(11));

        assertFalse(savingsAccount.deposit(9, 1000));
        assertFalse(savingsAccount.withdraw(9, 1000));
        assertEquals(10000, savingsAccount.getBalance());
    }

    @Test
    @DisplayName("print() lists all bookings with a running balance")
    public void testPrint() {
        savingsAccount.deposit(0, 100000);
        savingsAccount.withdraw(31, 40000);
        savingsAccount.print();

        String text = output.toString();
        assertTrue(text.contains("Kontoauszug 'S-1000'"));
        assertTrue(text.contains("Datum          Betrag      Saldo"));
        // amounts through BankUtils, the decimal separator depends on the system language
        assertTrue(text.contains("01.01.1970 "
                + BankUtils.formatAmount(100000) + " "
                + BankUtils.formatAmount(100000)));
        assertTrue(text.contains("02.02.1970 "
                + BankUtils.formatAmount(-40000) + " "
                + BankUtils.formatAmount(60000)));
    }

    @Test
    @DisplayName("print(year, month) lists only the bookings of that month")
    public void testMonthlyPrint() {
        savingsAccount.deposit(0, 100000);  // 01.01.1970
        savingsAccount.deposit(35, 50000);  // 06.02.1970
        savingsAccount.withdraw(65, 20000); // 06.03.1970
        savingsAccount.print(1970, 2);

        String text = output.toString();
        assertTrue(text.contains("Kontoauszug 'S-1000' Monat: 2.1970"));
        // the balance of january is still counted in
        assertTrue(text.contains("06.02.1970 "
                + BankUtils.formatAmount(50000) + " "
                + BankUtils.formatAmount(150000)));
        assertFalse(text.contains("01.01.1970"));
        assertFalse(text.contains("06.03.1970"));
    }
}