package test.privalov;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class AccountDao {
	private static AccountDao accountDao;
	private final ConcurrentMap<String, Account> accountMap;
	
	private AccountDao() {
        this.accountMap = new ConcurrentHashMap<>();
    }
	
	public static AccountDao getAccountDao() {
		if (null == accountDao)
			accountDao = new AccountDao();
		return accountDao;
	}

    public Account create(String id, int balance) {
    	Account account = new Account(id, balance);

        // If we get a non null value that means the account already exists in the Map.
        if (null != accountMap.putIfAbsent(id, account)) {
            return null;
        }
        return account;
    }
    
    public Account get(String id) {
        return accountMap.get(id);
    }

    public Account update(Account account) {
        // This means no account existed so update failed. return null
        if (null == accountMap.replace(account.getId(), account)) {
            return null;
        }
        // Update succeeded return the account
        return account;
    }

    public boolean delete(String id) {
        return null != accountMap.remove(id);
    }

    public List<Account> listAccounts() {
        return accountMap.values()
                      .stream()
                      .sorted(Comparator.comparing((Account acc) -> acc.getId()))
                      .collect(Collectors.toList());
    }
}
