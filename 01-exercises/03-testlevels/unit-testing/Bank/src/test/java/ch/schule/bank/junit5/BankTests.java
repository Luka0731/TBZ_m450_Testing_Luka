package ch.schule.bank.junit5;

import ch.schule.Bank;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;



/**
 * Tests for the class 'Bank'.
 *
 * @author xxxx
 * @version 1.0
 */
public class BankTests {
    private Bank bank;

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        // catch the console output so print() can be checked
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    /**
     * Creates 6 savings accounts, on purpose not sorted by balance.
     */
    private void createSixAccounts() {
        bank.deposit(bank.createSavingsAccount(), 1, 30000); // S-1000
        bank.deposit(bank.createSavingsAccount(), 1, 60000); // S-1001
        bank.deposit(bank.createSavingsAccount(), 1, 10000); // S-1002
        bank.deposit(bank.createSavingsAccount(), 1, 50000); // S-1003
        bank.deposit(bank.createSavingsAccount(), 1, 20000); // S-1004
        bank.deposit(bank.createSavingsAccount(), 1, 40000); // S-1005
    }

    @Test
    @DisplayName("accounts are created with an ongoing number")
    public void testCreate() {
        String savingsId = bank.createSavingsAccount();
        String youthId = bank.createPromoYouthSavingsAccount();
        String salaryId = bank.createSalaryAccount(-50000);

        assertAll("account numbers",
                () -> assertEquals("S-1000", savingsId),
                () -> assertEquals("Y-1001", youthId),
                () -> assertEquals("P-1002", salaryId));

        assertEquals(0, bank.getBalance(savingsId));
        assertEquals(0, bank.getBalance(youthId));
        assertEquals(0, bank.getBalance(salaryId));

        // edge case: a credit limit of 0 is allowed
        assertEquals("P-1003", bank.createSalaryAccount(0));
        // a positive credit limit is invalid
        assertNull(bank.createSalaryAccount(1));
        // the number was not used up by the failed attempt
        assertEquals("S-1004", bank.createSavingsAccount());
    }

    @Test
    @DisplayName("deposit through the bank, unknown account is rejected")
    public void testDeposit() {
        String id = bank.createSavingsAccount();

        assertTrue(bank.deposit(id, 1, 10000));
        assertEquals(10000, bank.getBalance(id));

        assertFalse(bank.deposit(id, 2, -1));
        assertEquals(10000, bank.getBalance(id));

        assertFalse(bank.deposit("S-9999", 2, 10000));
    }

    @Test
    @DisplayName("withdraw through the bank, unknown account is rejected")
    public void testWithdraw() {
        String id = bank.createSavingsAccount();
        bank.deposit(id, 1, 10000);

        assertTrue(bank.withdraw(id, 2, 4000));
        assertEquals(6000, bank.getBalance(id));

        // a savings account cannot go below zero
        assertFalse(bank.withdraw(id, 3, 6001));
        assertEquals(6000, bank.getBalance(id));

        assertFalse(bank.withdraw(id, 3, -1));
        assertEquals(6000, bank.getBalance(id));

        assertFalse(bank.withdraw("S-9999", 3, 1000));
    }

    @Test
    @DisplayName("print() writes the statement, an unknown account writes nothing")
    public void testPrint() {
        String id = bank.createSavingsAccount();
        bank.deposit(id, 0, 100000);
        bank.print(id);

        String text = output.toString();
        assertTrue(text.contains("Kontoauszug 'S-1000'"));
        assertTrue(text.contains("01.01.1970"));

        output.reset();
        bank.print("S-9999");
        assertEquals("", output.toString());
    }

    @Test
    @DisplayName("print(year, month) writes only that month, an unknown account writes nothing")
    public void testMonthlyPrint() {
        String id = bank.createSavingsAccount();
        bank.deposit(id, 0, 100000);  // 01.01.1970
        bank.deposit(id, 30, 50000);  // 01.02.1970
        bank.print(id, 1970, 2);

        String text = output.toString();
        assertTrue(text.contains("Kontoauszug 'S-1000' Monat: 2.1970"));
        assertTrue(text.contains("01.02.1970"));
        assertFalse(text.contains("01.01.1970"));

        output.reset();
        bank.print("S-9999", 1970, 2);
        assertEquals("", output.toString());
    }

    @Test
    @DisplayName("the balance of the bank is the negated sum of all accounts")
    public void testBalance() {
        // edge case: empty bank
        assertEquals(0, bank.getBalance());

        String savingsId = bank.createSavingsAccount();
        String salaryId = bank.createSalaryAccount(-100000);
        bank.deposit(savingsId, 1, 30000);
        bank.deposit(salaryId, 1, 20000);

        // the money of the customers is debt for the bank, so the sign is negative
        assertEquals(-50000, bank.getBalance());

        // the salary account goes to -50000, now the customer owes money to the bank
        bank.withdraw(salaryId, 2, 70000);
        assertEquals(20000, bank.getBalance());

        // an unknown account returns 0
        assertEquals(0, bank.getBalance("S-9999"));
    }

    @Test
    @DisplayName("printTop5() writes the 5 highest accounts, highest first")
    public void testTop5() {
        // edge case: an empty bank writes nothing
        bank.printTop5();
        assertEquals("", output.toString());

        createSixAccounts();
        bank.printTop5();

        String nl = System.lineSeparator();
        assertEquals("S-1001: 60000" + nl
                + "S-1003: 50000" + nl
                + "S-1005: 40000" + nl
                + "S-1000: 30000" + nl
                + "S-1004: 20000" + nl, output.toString());
    }

    @Test
    @DisplayName("printBottom5() writes the 5 lowest accounts, lowest first")
    public void testBottom5() {
        // edge case: an empty bank writes nothing
        bank.printBottom5();
        assertEquals("", output.toString());

        createSixAccounts();
        bank.printBottom5();

        String nl = System.lineSeparator();
        assertEquals("S-1002: 10000" + nl
                + "S-1004: 20000" + nl
                + "S-1000: 30000" + nl
                + "S-1005: 40000" + nl
                + "S-1003: 50000" + nl, output.toString());
    }
}