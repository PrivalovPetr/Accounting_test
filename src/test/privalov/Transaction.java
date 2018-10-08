package test.privalov;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

public class Transaction {
	
	private final String senderId;
	private final String recieverId;
    private final int ammount;
    private final LocalDate date;

    public Transaction(
    		@JsonProperty("senderId") String senderId,
            @JsonProperty("recieverId") String recieverId,
            @JsonProperty("ammount") int ammount,
            @JsonProperty("date") LocalDate date) {
        super();
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.ammount = ammount;
        this.date = date;

    }

    public String getSenderId() {
        return senderId;
    }
    
    public String getRecieverId() {
        return recieverId;
    }
    
    public int getAmmount() {
        return ammount;
    }
    
    public LocalDate getDate() {
        return date;
    }

    private static final TypeReference<Transaction> typeRef = new TypeReference<Transaction>() {};
    public static TypeReference<Transaction> typeRef() {
        return typeRef;
    }
    private static final TypeReference<List<Transaction>> listTypeRef = new TypeReference<List<Transaction>>() {};
    public static TypeReference<List<Transaction>> listTypeRef() {
        return listTypeRef;
    }

}
