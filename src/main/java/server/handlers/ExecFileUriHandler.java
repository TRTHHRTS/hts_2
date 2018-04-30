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
                //START Mpc %mpcExecutable% %fileLocation% /startpos %position%
                String path = new String(Hex.decodeHex(request.headers().get("path")));
                String mpcPath = MainWindow.PROPS.getProperty("mpc_path");
                Runtime.getRuntime().exec(MessageFormat.format("\"{0}\" \"{1}\"", mpcPath, path));
                // TODO
                System.out.println(path);
            } else {
                throw new IllegalStateException("Отсутствует обязательный ппараметр path");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
