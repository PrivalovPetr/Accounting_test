package test.privalov;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionDao {
	private static TransactionDao transactionDao;
	private final LinkedList<Transaction> transactions;
	private AccountDao accountDao;
	
	private TransactionDao() {
		transactions = new LinkedList<>();
		accountDao = AccountDao.getAccountDao();
	}
	
	public static TransactionDao getTransactionDao() {
		if (null == transactionDao)
			transactionDao = new TransactionDao();
		return transactionDao;
	}
	
	public List<Transaction> listTransactions() {
		return transactions.stream()
				.sorted(Comparator.comparing((Transaction t) -> t.getDate()))
                .collect(Collectors.toList());
		}
    
    public synchronized Transaction makeTransaction(String sender, String reciever, int ammount) {
    	if (accountDao.get(sender).getBalance() < ammount || //not enough money for transaction
    			Integer.MAX_VALUE - accountDao.get(reciever).getBalance() < ammount) //technical limitation
	    	return null;
    	accountDao.get(sender).changeBalance(-1*ammount);
    	accountDao.get(reciever).changeBalance(ammount);
    	Transaction transaction = new Transaction(sender, reciever, ammount, LocalDate.now());
    	transactions.add(transaction);
    	return transaction;
    }

    public List<Transaction> listAccountsTransactions(Account acc) {
    	return transactions.stream().filter(t ->
    					(t.getSenderId().equals(acc.getId()) || t.getRecieverId().equals(acc.getId())))
    				.sorted(Comparator.comparing((Transaction t) -> t.getDate()))
    				.collect(Collectors.toList());
    }
}
