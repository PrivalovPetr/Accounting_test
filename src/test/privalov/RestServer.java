package test.privalov;

import static com.stubbornjava.common.undertow.handlers.CustomHandlers.timed;

import com.stubbornjava.common.exceptions.ApiException;
import com.stubbornjava.common.undertow.SimpleServer;
import com.stubbornjava.common.undertow.handlers.ApiHandlers;
import com.stubbornjava.common.undertow.handlers.CustomHandlers;
import com.stubbornjava.common.undertow.handlers.Middleware;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;

public class RestServer {

    public static final RoutingHandler ROUTES = new RoutingHandler()
        .get("/accounts", timed("listAccounts", AccountRoutes::listAccounts))
        .get("/accounts/{id}", timed("getAccount", AccountRoutes::getAccount))
        .post("/accounts", timed("createAccount", AccountRoutes::createAccount))
        .put("/accounts", timed("updateAccount", AccountRoutes::updateAccount))
        .delete("/accounts/{id}", timed("deleteAccount", AccountRoutes::deleteAccount))        
        .get("/accounts/{id}/transactions", timed("getTransactions",
        		AccountRoutes::getAccountTransactions))
        .post("/accounts/{id}/transactions", timed("makeTransaction",
        		AccountRoutes::makeTransaction))
        .get("/transactions", timed("getAllTransactions", AccountRoutes::listTransactions))
        .setFallbackHandler(timed("notFound", AccountRoutes::notFoundHandler));

    public static void main(String[] args) {
        // Once again pull in a bunch of common middleware.
    	SimpleServer server = SimpleServer.simpleServer(Middleware.common(ROUTES));
        server.start();
    }
}
