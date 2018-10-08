package test.privalov;

import java.util.List;

import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.common.undertow.handlers.ApiHandlers;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

public class AccountRoutes {
	private static final AccountRequests accountRequests = new AccountRequests();
    private static final AccountDao accountDao = AccountDao.getAccountDao();
    private static final TransactionDao transactionDao = TransactionDao.getTransactionDao();
    
    public static void notFoundHandler(HttpServerExchange exchange) {
        exchange.setStatusCode(404);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Page Not Found!!");
    }
    
    public static void listAccounts(HttpServerExchange exchange) {
		List<Account> accounts = accountDao.listAccounts();
        Exchange.body().sendJson(exchange, accounts);
	}

	public static void getAccount(HttpServerExchange exchange) {
        String id = accountRequests.id(exchange);
        Account account = accountDao.get(id);
        if (null == account) {
            ApiHandlers.notFound(exchange, String.format("Account %s not found.", id));
            return;
        }
        Exchange.body().sendJson(exchange, account);
    }

	public static void createAccount(HttpServerExchange exchange) {
		Account accountInput = accountRequests.account(exchange);
		if (accountInput.getBalance() < 0) {
			ApiHandlers.badRequest(exchange, "Balance should be positive.");
            return;
		}

		Account account = accountDao.create(accountInput.getId(), accountInput.getBalance());
        if (null == account) {
            ApiHandlers.badRequest(exchange,
            		String.format("Account %s already exists.", accountInput.getId()));
            return;
        }
        exchange.setStatusCode(StatusCodes.CREATED);
        Exchange.body().sendJson(exchange, account);
    }

	public static void updateAccount(HttpServerExchange exchange) {
		Account accountInput = accountRequests.account(exchange);
		if (accountInput.getBalance() < 0) {
			ApiHandlers.badRequest(exchange, "Balance should be positive.");
            return;
		}
		
		Account account = accountDao.update(accountInput);
        if (null == account) {
            ApiHandlers.notFound(exchange,
            		String.format("Account %s not found.", accountInput.getId()));
            return;
        }
        Exchange.body().sendJson(exchange, account);
    }

	public static void deleteAccount(HttpServerExchange exchange) {
        String id = accountRequests.id(exchange);

        // If you care about it you can handle it.
        if (false == accountDao.delete(id)) {
            ApiHandlers.notFound(exchange, String.format("Account %s not found.", id));
            return;
        }
        exchange.setStatusCode(StatusCodes.NO_CONTENT);
        exchange.endExchange();
    }

	public static void makeTransaction(HttpServerExchange exchange) {
		String id = accountRequests.id(exchange);
		Transaction transactionInput = accountRequests.transaction(exchange);
		
		if (null == accountDao.get(id)) {
			ApiHandlers.notFound(exchange, String.format("Account %s not found.", id));
            return;
		}
		if (null == accountDao.get(transactionInput.getRecieverId())) {
			ApiHandlers.notFound(exchange,
					String.format("Account %s not found.", transactionInput.getRecieverId()));
            return;
		}

		Transaction transaction = transactionDao.makeTransaction(id, transactionInput.getRecieverId(), 
				transactionInput.getAmmount());
        if (null == transaction) {
            ApiHandlers.badRequest(exchange,
            		String.format("Insufficient funds in the account %s.", id));
            return;
        }
        exchange.setStatusCode(StatusCodes.CREATED);
        Exchange.body().sendJson(exchange, transaction);
	}

	public static void getAccountTransactions(HttpServerExchange exchange) {
		String id = accountRequests.id(exchange);
		
		if (null == accountDao.get(id)) {
			ApiHandlers.notFound(exchange, String.format("Account %s not found.", id));
            return;
		}
		List<Transaction> transactions = transactionDao.listAccountsTransactions(accountDao.get(id));
		Exchange.body().sendJson(exchange, transactions);
	}

	public static void listTransactions(HttpServerExchange exchange) {
		List<Transaction> transactions = transactionDao.listTransactions();
        Exchange.body().sendJson(exchange, transactions);
	}
}
