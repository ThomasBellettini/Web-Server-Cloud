package fr.shurisko.http.api;

import fr.shurisko.http.RouteFunctionManager;
import fr.shurisko.http.api.enumeration.RequestType;

public class CloudRoute {

    private String routeName;
    private RequestType requestType;
    private RouteFunctionManager functionExecutable;

    public CloudRoute(String routeName, RequestType requestType, RouteFunctionManager functionExecutable) {
        this.routeName = routeName;
        this.requestType = requestType;
        this.functionExecutable = functionExecutable;
    }

    public String getRouteName() {
        return routeName;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public RouteFunctionManager getFunctionExecutable() {
        return functionExecutable;
    }
}
