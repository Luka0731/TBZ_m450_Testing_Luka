package ch.schule.bank.junit5;

import ch.schule.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



/**
 * Tests for the class SavingsAccount.
 *
 * @author XXX
 * @version 1.0
 */
public class SavingsAccountTests {
    private SavingsAccount account;

    @BeforeEach
    public void setUp() {
        account = new SavingsAccount("S-1000");
    }

    @Test
    @DisplayName("a savings account cannot go below zero")
    public void test() {
        assertTrue(account.deposit(1, 10000));

        assertTrue(account.withdraw(2, 4000));
        assertEquals(6000, account.getBalance());

        // edge case: taking out exactly the whole balance
        assertTrue(account.withdraw(3, 6000));
        assertEquals(0, account.getBalance());

        assertFalse(account.withdraw(4, 1));
        assertEquals(0, account.getBalance());

        assertFalse(account.withdraw(4, -1000));
        assertEquals(0, account.getBalance());
    }
}