package server.handlers;

import io.netty.handler.codec.http.HttpRequest;
import ui.MainWindow;
import server.Mapped;
import server.UriHandlerBased;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
                        result.add(item.getAbsolutePath());
                    }
                }
                buff.append(result.toString());
            }
        }
    }
}
