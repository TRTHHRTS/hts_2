package server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.commons.codec.binary.Hex;
import server.Mapped;
import server.UriHandlerBased;
import ui.MainWindow;

import java.text.MessageFormat;

/**
 * Получение списка файлов
 */
@Mapped(uri = "/exec")
public class ExecFileUriHandler extends UriHandlerBased {

    /** Маппер, для сериализации */
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void process(HttpRequest request, StringBuilder buff) {
        try {
            if (request.headers().contains("path")) {
                String path = new String(Hex.decodeHex(request.headers().get("path")));
                String mpcPath = MainWindow.PROPS.getProperty("mpc_path");
                String execString = MessageFormat.format("\"{0}\" \"{1}\"", mpcPath, path);
                if (request.headers().contains("position")) {
                    execString += " /startpos " + request.headers().get("position");
                }
                Runtime.getRuntime().exec(execString);
                // TODO
                System.out.println(path);
            } else {
                throw new IllegalStateException("Отсутствует обязательный параметр path");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
