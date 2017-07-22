/**
 * This class represents a bank account whose current balance is a nonnegative
 * amount in US dollars.
 */
public class Account {
<<<<<<< HEAD
	Account parentAccount;
=======
>>>>>>> ae681060fd4403686810ca9b1cbf9e5942039129

	private int balance;

	/** Initialize an account with the given BALANCE. */
	public Account(int balance) {
		this.balance = balance;
<<<<<<< HEAD
		this.parentAccount=null;
	}
	public Account(int balance,Account parentAccount){
	
		this.balance=balance;
		this.parentAccount=parentAccount;
=======
>>>>>>> ae681060fd4403686810ca9b1cbf9e5942039129
	}

	/** Return the number of dollars in the account. */
	public int getBalance() {
		return this.balance;
	}

	/** Deposits AMOUNT into the current account. */
	public void deposit(int amount) {
		if (amount < 0) {
			System.out.println("Cannot deposit negative amount.");
		} else {
			this.balance = this.balance + amount;
		}
	}

	/** Subtract AMOUNT from the account if possible. If subtracting AMOUNT
	 *	would leave a negative balance, print an error message and leave the
	 *	balance unchanged.
	 */
<<<<<<< HEAD
	public boolean withdraw(int amount) {
		boolean result=true;
		if(this.balance<0){
		result=false;
		}
		else if (this.balance<amount) {
			if(this.parentAccount==null){
				result=false;
			}else{
				result=this.parentAccount.withdraw(amount-this.getBalance());
				if(result){
					this.balance=0;
				}
			}
			
		} else {
			int newbalance=this.balance-amount;
			this.balance=newbalance;	//update balance	
			}
		return result;
	}

	/** Merge account 

 into this account by removing all money from OTHER
	 *	and depositing it into this account.
     */
    public void merge(Account other) {
        this.deposit(other.getBalance());
	int b=other.getBalance();
	other.withdraw(b);
=======
	public void withdraw(int amount) {
		if (amount < 0) {
			System.out.println("Cannot withdraw negative amount.");
		} else if (this.balance < amount) {
			System.out.println("Insufficient funds");
		} else {
			this.balance = this.balance - amount;
		}
	}

	/** Merge account OTHER into this account by removing all money from OTHER
	 *	and depositing it into this account.
     */
    public void merge(Account other) {
        // TODO Put your own code here
>>>>>>> ae681060fd4403686810ca9b1cbf9e5942039129
    }
}
