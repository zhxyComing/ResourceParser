package com.app.dixon.resourceparser.core.pub.parser;

public interface ResponseDelivery {

    <T> void postResponse(Request<T> request, Response<T> response);

    void postError(Request<?> request, ParseError error);
}
