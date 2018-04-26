package server;

import io.netty.handler.codec.http.HttpRequest;
 
@Mapped(uri = "/list")
public class FileListUriHandler extends UriHandlerBased {
 
    @Override
    public void process(HttpRequest request, StringBuilder buff) {
        buff.append("HELLO HANDLER1!");
    }
}
