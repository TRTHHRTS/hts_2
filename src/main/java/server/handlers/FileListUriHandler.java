package server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.commons.codec.binary.Hex;
import ui.MainWindow;
import server.Mapped;
import server.UriHandlerBased;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Получение списка файлов
 */
@Mapped(uri = "/list")
public class FileListUriHandler extends UriHandlerBased {

    /** Текущая директория */
    private static File currentDir = null;
    /** Маппер, для сериализации */
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void process(HttpRequest request, StringBuilder buff) {
        try {
            if (request.headers().contains("path")) {
                String decoded = new String(Hex.decodeHex(request.headers().get("path")));
                currentDir = new File(decoded);
            } else if (request.headers().contains("parent") && currentDir != null) {
                currentDir = currentDir.getParentFile();
            } else {
                currentDir = new File(MainWindow.PROPS.getProperty("init_dir"));
            }
            List<String> result = new ArrayList<>();
            if (currentDir != null && currentDir.isDirectory()) {
                File[] originalList = currentDir.listFiles();
                if (originalList != null) {
                    for (File item : originalList) {
                        if (!item.isHidden()) {
                            Map map = new HashMap<String, String>(){{
                                put("name", item.getName());
                                put("isDirectory", String.valueOf(item.isDirectory()));
                                put("path", item.getAbsolutePath());
                            }};
                            result.add(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map));
                        }
                    }
                }
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                new ObjectOutputStream(out).writeObject(result.toArray());
                String serialized = new String(Hex.encodeHex(out.toByteArray()));
                buff.append(serialized);
            } else {
                throw new IllegalStateException("Путь " + currentDir + " не является директорией или путь null.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
