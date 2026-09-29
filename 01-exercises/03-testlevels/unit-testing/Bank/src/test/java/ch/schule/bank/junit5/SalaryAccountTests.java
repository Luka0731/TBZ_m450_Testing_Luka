package ch.schule.bank.junit5;

import ch.schule.SalaryAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



/**
 * Tests for the class SalaryAccount.
 *
 * @author XXX
 * @version 1.1
 */
public class SalaryAccountTests {
    private SalaryAccount account;

    @BeforeEach
    public void setUp() {
        account = new SalaryAccount("P-1000", -50000);
    }

    @Test
    @DisplayName("a salary account can go negative down to the credit limit")
    public void test() {
        assertTrue(account.deposit(1, 10000));

        assertTrue(account.withdraw(2, 30000));
        assertEquals(-20000, account.getBalance());

        // edge case: exactly down to the credit limit
        assertTrue(account.withdraw(3, 30000));
        assertEquals(-50000, account.getBalance());

        assertFalse(account.withdraw(4, 1));
        assertEquals(-50000, account.getBalance());

        assertFalse(account.withdraw(4, -1000));
        assertEquals(-50000, account.getBalance());
    }
}