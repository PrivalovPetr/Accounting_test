package test.privalov;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

public class Account {
	private final String id;
    private int balance;

    public Account(
            @JsonProperty("id") String id,
            @JsonProperty("balance") int balance) {
        super();
        this.id = id;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }
    
    public int changeBalance(int add) {
    	balance += add;
    	return balance;
    }

    private static final TypeReference<Account> typeRef = new TypeReference<Account>() {};
    public static TypeReference<Account> typeRef() {
        return typeRef;
    }
    private static final TypeReference<List<Account>> listTypeRef = new TypeReference<List<Account>>() {};
    public static TypeReference<List<Account>> listTypeRef() {
        return listTypeRef;
    }
}
