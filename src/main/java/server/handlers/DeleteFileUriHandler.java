package server.handlers;

import io.netty.handler.codec.http.HttpRequest;
import org.apache.commons.codec.binary.Hex;
import server.Mapped;
import server.UriHandlerBased;

import java.io.File;

@Mapped(uri = "/delete")
public class DeleteFileUriHandler extends UriHandlerBased {

    @Override
    public void process(HttpRequest request, StringBuilder buff) throws Exception {
        if (request.headers().contains("path")) {
            String path = new String(Hex.decodeHex(request.headers().get("path")));
            File file = new File(path);
            boolean delete = file.delete();
            if (!delete) {
                throw new RuntimeException("Deleting file error");
            }
        } else {
            throw new IllegalStateException("Required request parameter 'path' not found in request object");
        }
    }
}
