package server.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.commons.codec.binary.Hex;
import ui.MainWindow;
import server.Mapped;
import server.UriHandlerBased;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapped(uri = "/list")
public class FileListUriHandler extends UriHandlerBased {
 
    @Override
    public void process(HttpRequest request, StringBuilder buff) {
        String dir = MainWindow.PROPS.getProperty("init_dir");
        File file = new File(dir);
        List<String> result = new ArrayList<>();
        if (file.isDirectory()) {
            File[] originalList = file.listFiles();
            if (originalList != null) {
                for (File item : originalList) {
                    if (!item.isHidden()) {
                        Map map = new HashMap<String, String>(){{
                            put("name", item.getName());
                            put("isDirectory", String.valueOf(item.isDirectory()));
                        }};
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
                            result.add(jsonResult);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    new ObjectOutputStream(out).writeObject(result.toArray());
                    String serialized = new String(Hex.encodeHex(out.toByteArray()));
                    buff.append(serialized);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
