package fr.shurisko.http.api;

import fr.shurisko.http.RouteByteFunctionManager;
import fr.shurisko.http.api.enumeration.RequestType;

public class CloudByteRoute {

    private String routeName;
    private RequestType requestType;
    private RouteByteFunctionManager functionExecutable;

    public CloudByteRoute(String routeName, RequestType requestType, RouteByteFunctionManager functionExecutable) {
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

    public RouteByteFunctionManager getFunctionExecutable() {
        return functionExecutable;
    }
}
