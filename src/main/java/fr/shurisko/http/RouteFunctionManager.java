package fr.shurisko.http;

import spark.Request;
import spark.Response;

public interface RouteFunctionManager {

    String routeFunction(Request request, Response response);

}
