package server;

import io.netty.handler.codec.http.HttpRequest;

public abstract class UriHandlerBased {

    public abstract void process(HttpRequest request, StringBuilder buff) throws Exception;

    String getContentType() {
        return "text/plain; charset=UTF-8";
    }
}