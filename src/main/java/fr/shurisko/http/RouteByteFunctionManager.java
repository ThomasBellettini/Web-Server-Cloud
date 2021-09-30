package fr.shurisko.http;

import spark.Request;
import spark.Response;

public interface RouteByteFunctionManager{

    byte[] routeFunctionByte(Request request, Response response);

}
