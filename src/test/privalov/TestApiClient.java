package test.privalov;

import java.time.LocalDate;
import java.util.List;

import org.jooq.lambda.Unchecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.stubbornjava.common.HttpClient;
import com.stubbornjava.common.Json;
import com.stubbornjava.common.RequestBodies;

import io.undertow.util.StatusCodes;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestApiClient {

    private static final Logger log = LoggerFactory.getLogger(TestApiClient.class);

    private final String host;
    private final OkHttpClient client;
    public TestApiClient(String host, OkHttpClient client) {
        super();
        this.host = host;
        this.client = client;
    }

    public Account createAccount(Account inputAccount) {
        HttpUrl route = HttpUrl.parse(host + "/accounts");
        Request request = new Request.Builder()
            .url(route)
            .post(RequestBodies.jsonObj(inputAccount))
            .build();
        return Unchecked.supplier(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.code() == StatusCodes.CREATED) {
                    Account account = Json.serializer().fromInputStream(response.body().byteStream(), Account.typeRef());
                    return account;
                }

                if (response.code() == StatusCodes.BAD_REQUEST) {
                    log.info(response.body().string());
                    return null;
                }
                throw HttpClient.unknownException(response);
            } catch (RuntimeException e) {
                log.info(e.getMessage());
                return null;
            }
        }).get();
    }

    public Account updateAccount(Account inputAccount) {
        HttpUrl route = HttpUrl.parse(host + "/accounts");
        Request request = new Request.Builder()
                .url(route)
                .put(RequestBodies.jsonObj(inputAccount))
                .build();
        return Unchecked.supplier(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    Account account = Json.serializer().fromInputStream(response.body().byteStream(), Account.typeRef());
                    return account;
                }

                if (response.code() == StatusCodes.NOT_FOUND) {
                    log.info(response.body().string());
                    return null;
                }
                throw HttpClient.unknownException(response);
            } catch (RuntimeException e) {
                log.info(e.getMessage());
                return null;
            }
        }).get();
    }

    public List<Account> listAccounts() {
        HttpUrl route = HttpUrl.parse(host + "/accounts");
        Request request = new Request.Builder().url(route).get().build();
        return Unchecked.supplier(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    List<Account> accounts = Json.serializer().fromInputStream(response.body().byteStream(), Account.listTypeRef());
                    return accounts;
                }
                throw HttpClient.unknownException(response);
            } catch (RuntimeException e) {
                log.info(e.getMessage());
                return null;
            }
        }).get();
    }

    public Account getAccount(String id) {
        HttpUrl route = HttpUrl.parse(host + "/accounts")
                               .newBuilder()
                               .addPathSegment(id)
                               .build();
        Request request = new Request.Builder().url(route).get().build();
        return Unchecked.supplier(() -> {
            try (Response response = client.newCall(request).execute()) {
                // The account exists
                if (response.isSuccessful()) {
                    Account account = Json.serializer().fromInputStream(response.body().byteStream(), Account.typeRef());
                    return account;
                }

                if (response.code() == StatusCodes.NOT_FOUND) {
                    log.info(response.body().string());
                    return null;
                }
                throw HttpClient.unknownException(response);
            } catch (RuntimeException e) {
                log.info(e.getMessage());
                return null;
            }
        }).get();
    }

    public boolean deleteAccount(String id) {
        HttpUrl route = HttpUrl.parse(host + "/accounts")
                               .newBuilder()
                               .addPathSegment(id)
                               .build();
        Request request = new Request.Builder().url(route).delete().build();
        return Unchecked.booleanSupplier(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.code() == StatusCodes.NO_CONTENT) {
                    return true;
                }

                if (response.code() == StatusCodes.NOT_FOUND) {
                    log.info(response.body().string());
                    return false;
                }
                throw HttpClient.unknownException(response);
            } catch (RuntimeException e) {
                log.info(e.getMessage());
                return false;
            }
        }).getAsBoolean();
    }

    public Transaction makeTransaction(Transaction inputTransaction) {
        HttpUrl route = HttpUrl.parse(host + "/accounts")
                               .newBuilder()
                               .addPathSegment(inputTransaction.getSenderId())
                               .addPathSegment("transactions")
                               .build();
        Request request = new Request.Builder()
            .url(route)
            .post(RequestBodies.jsonObj(inputTransaction))
            .build();
        return Unchecked.supplier(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.code() == StatusCodes.CREATED) {
                    Transaction transaction = Json.serializer().fromInputStream(response.body().byteStream(),
                            Transaction.typeRef());
                    return transaction;
                }

                if (response.code() == StatusCodes.BAD_REQUEST) {
                    log.info(response.body().string());
                    return null;
                }
                throw HttpClient.unknownException(response);
            } catch (RuntimeException e) {
                log.info(e.getMessage());
                return null;
            }
        }).get();
    }

    public List<Transaction> listTransactions() {
        HttpUrl route = HttpUrl.parse(host + "/transactions");
        Request request = new Request.Builder().url(route).get().build();
        return Unchecked.supplier(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    List<Transaction> transactions = Json.serializer().fromInputStream(response.body().byteStream(),
                            Transaction.listTypeRef());
                    return transactions;
                }
                throw HttpClient.unknownException(response);
            } catch (RuntimeException e) {
                log.info(e.getMessage());
                return null;
            }
        }).get();
    }

    public List<Transaction> listAccountTransactions(String account) {
        HttpUrl route = HttpUrl.parse(host + "/transactions")
                               .newBuilder()
                               .addPathSegment(account)
                               .addPathSegment("transactions")
                               .build();
        Request request = new Request.Builder().url(route).get().build();
        return Unchecked.supplier(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    List<Transaction> transactions = Json.serializer().fromInputStream(response.body().byteStream(),
                            Transaction.listTypeRef());
                    return transactions;
                }
                throw HttpClient.unknownException(response);
            } catch (RuntimeException e) {
                log.info(e.getMessage());
                return null;
            }
        }).get();
    }

    public static void main(String[] args) {
        TestApiClient client = new TestApiClient("http://localhost:8080", HttpClient.globalClient());

        log.info("**** Creating Accounts ****");
        Account account1 = new Account("account1", 500);
        log.info(Json.serializer().toString(client.createAccount(account1)));
        Account account2 = new Account("account2", 700);
        log.info(Json.serializer().toString(client.createAccount(account2)));
        Account account3 = new Account("account3", 0);
        log.info(Json.serializer().toString(client.createAccount(account3)));
        Account accountNegativeBalance = new Account("accountNegative", -700);
        log.info(Json.serializer().toString(client.createAccount(accountNegativeBalance)));
        Account duplicatingAccount = new Account("account2", 300);
        log.info(Json.serializer().toString(client.createAccount(duplicatingAccount)));

        log.info("\n\n");
        log.info("**** Updating Account ****");
        Account account2Updated = new Account("account2", 650);
        log.info(Json.serializer().toString(client.updateAccount(account2Updated)));
        Account misingAccountUpdated = new Account("account", 650);
        log.info(Json.serializer().toString(client.updateAccount(misingAccountUpdated)));
        Account accountWithNegativeBalanceUpd = new Account("account1", -650);
        log.info(Json.serializer().toString(client.updateAccount(accountWithNegativeBalanceUpd)));

        log.info("\n\n");
        log.info("**** Listing Accounts ****");
        List<Account> accounts = client.listAccounts();
        log.info(Json.serializer().toString(accounts));

        log.info("\n\n");
        log.info("**** Get Accounts ****");
        log.info(Json.serializer().toString(client.getAccount("account1")));
        log.info(Json.serializer().toString(client.getAccount("account2")));
        log.info("**** Get Missing Account ****");
        log.info(Json.serializer().toString(client.getAccount("account")));

        log.info("\n\n");
        log.info("**** Make Transaction ****");
        client.makeTransaction(new Transaction("account1", "account2", 300, LocalDate.now()));
        client.makeTransaction(new Transaction("account1", "account3", 200, LocalDate.now()));
        client.makeTransaction(new Transaction("account2", "account3", 100, LocalDate.now()));
        client.makeTransaction(new Transaction("account2", "account3", -100, LocalDate.now()));
        client.makeTransaction(new Transaction("account", "account3", 100, LocalDate.now()));
        client.makeTransaction(new Transaction("account2", "account10", 100, LocalDate.now()));

        log.info("\n\n");
        log.info("**** Delete Accounts ****");
        client.deleteAccount("account1");
        client.deleteAccount("account");

        log.info("\n\n");
        log.info("**** Listing All Transactions ****");
        List<Transaction> transactions = client.listTransactions();
        log.info(Json.serializer().toString(transactions));

        log.info("\n\n");
        log.info("**** Listing Account's Transactions ****");
        List<Transaction> transactions1 = client.listAccountTransactions("account1");
        log.info(Json.serializer().toString(transactions1));
        List<Transaction> transactions2 = client.listAccountTransactions("account2");
        log.info(Json.serializer().toString(transactions2));

        log.info("\n\n");
        log.info("**** Delete Accounts ****");
        client.deleteAccount("account1");
        client.deleteAccount("account");

        log.info("\n\n");
        log.info("**** Exception ****");
        log.info(Json.serializer().toString(client.createAccount(null)));
    }
}
