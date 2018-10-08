package test.privalov;

import com.stubbornjava.common.undertow.Exchange;

import io.undertow.server.HttpServerExchange;

public class AccountRequests {
	public String id(HttpServerExchange exchange) {
        return Exchange.pathParams().pathParam(exchange, "id").orElse(null);
    }

    public Account account(HttpServerExchange exchange) {
        return Exchange.body().parseJson(exchange, Account.typeRef());
    }
    
    public Transaction transaction(HttpServerExchange exchange) {
        return Exchange.body().parseJson(exchange, Transaction.typeRef());
    }

    public void exception(HttpServerExchange exchange) {
        boolean exception = Exchange.queryParams()
                                    .queryParamAsBoolean(exchange, "exception")
                                    .orElse(false);
        if (exception) {
            throw new RuntimeException("Some random exception. Could be anything!");
        }
    }
}
