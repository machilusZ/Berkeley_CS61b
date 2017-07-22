import org.junit.*;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by yunan on 6/30/17.
 */
public class AccountTest {
    @org.junit.Test
    public void testInvalidArgs () throws Exception {
        Account c = new Account(6);
        c.deposit(-1);
        assertTrue(c.getBalance() == 6);
        c.withdraw(-1);
        assertTrue(c.getBalance() == 6);
    }

    @org.junit.Test
    public void testDeposit () throws Exception {
        Account c = new Account(6);
        c.deposit(10);
        assertTrue(c.getBalance() == 16);
    }

    @org.junit.Test
    public void testWithdraw () throws Exception {
        Account c = new Account(6);
        c.withdraw(4);
        assertTrue(c.getBalance() == 2);
    }

    @org.junit.Test
    public void testOverdraft () throws Exception {
        Account p = new Account(6);
        Account c = new Account(6,p);
        c.withdraw(10);
        assertTrue(c.getBalance() == 0);
        assertTrue(p.getBalance() == 2);
    }

    @org.junit.Test
    public void testInit () throws Exception {
        Account c = new Account(6);
        assertTrue(c.getBalance() == 6);
    }


}