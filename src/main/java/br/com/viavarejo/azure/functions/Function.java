package br.com.viavarejo.azure.functions;

import java.util.*;

import br.com.viavarejo.azure.functions.models.EvaluationCollection;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpTrigger-Java". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpTrigger-Java&code={your function key}
     * 2. curl "{your host}/api/HttpTrigger-Java?name=HTTP%20Query&code={your function key}"
     * Function Key is not needed when running locally, it is used to invoke function deployed to Azure.
     * More details: https://aka.ms/functions_authorization_keys
     */
    @FunctionName("evaluations")
    public HttpResponseMessage evaluations(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.GET, HttpMethod.POST},
                    authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String query = request.getQueryParameters().get("name");
        String name = request.getBody().orElse(query);

        //return ordinaryCall(name, request);
        return callingDatabase(name, request);

    }

    private HttpResponseMessage callingDatabase(String name, HttpRequestMessage<Optional<String>> request) {
        System.out.println("Creating collection object...");
        EvaluationCollection collection = new EvaluationCollection();
        System.out.println("Collection object created...");

        System.out.println("Calling mongodb insert evaluations...");
        String result = collection.insertOne(name);

        System.out.println("Preparing response...");
        if (result == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Result: " + result).build();
        }
    }

    private HttpResponseMessage ordinaryCall(String name, HttpRequestMessage<Optional<String>> request) {
        System.out.println("Result response...");
        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        }
    }
}
