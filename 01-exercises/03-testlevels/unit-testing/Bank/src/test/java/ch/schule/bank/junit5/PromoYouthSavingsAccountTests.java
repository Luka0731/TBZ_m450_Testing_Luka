package ch.schule.bank.junit5;

import ch.schule.PromoYouthSavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



/**
 * Tests for the promotional youth savings account.
 *
 * @author XXXX
 * @version 1.0
 */
public class PromoYouthSavingsAccountTests {
    private PromoYouthSavingsAccount account;

    @BeforeEach
    public void setUp() {
        account = new PromoYouthSavingsAccount("Y-1000");
    }

    @Test
    @DisplayName("every deposit gets a bonus of 1%")
    public void test() {
        assertTrue(account.deposit(1, 100000));
        assertEquals(101000, account.getBalance());

        // edge case: 100 is the smallest amount that still gives a bonus
        assertTrue(account.deposit(2, 100));
        assertEquals(101101, account.getBalance());

        // below 100 the bonus is rounded down to 0
        assertTrue(account.deposit(3, 99));
        assertEquals(101200, account.getBalance());

        assertFalse(account.deposit(4, -10000));
        assertEquals(101200, account.getBalance());

        // withdrawing gives no bonus and follows the savings account rules
        assertTrue(account.withdraw(5, 1200));
        assertEquals(100000, account.getBalance());
        assertFalse(account.withdraw(6, 100001));
        assertEquals(100000, account.getBalance());
    }
}