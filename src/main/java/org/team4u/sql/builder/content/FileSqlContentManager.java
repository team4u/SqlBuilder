package org.team4u.sql.builder.content;


import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.io.watch.WatchMonitor;
import com.xiaoleilu.hutool.io.watch.Watcher;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.team4u.kit.core.log.LogMessage;
import org.team4u.kit.core.util.AssertUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jay Wu
 */
public class FileSqlContentManager implements SqlContentManager {

    private static final Log log = LogFactory.get();

    protected Map<String, String> sqlList = new ConcurrentHashMap<String, String>();

    protected List<File> resources = CollectionUtil.newArrayList();

    protected String[] paths;

    protected boolean allowDuplicate = true;

    public FileSqlContentManager(String... paths) {
        this.paths = paths;
        refresh();
    }

    public FileSqlContentManager watch(long periodSeconds) {
        if (periodSeconds > 0) {
            for (File res : resources) {
                WatchMonitor watchMonitor = WatchMonitor.create(res, WatchMonitor.ENTRY_MODIFY);
                watchMonitor.setWatcher(new Watcher() {

                    @Override
                    public void onCreate(WatchEvent<?> watchEvent, Path path) {
                        refresh();
                    }

                    @Override
                    public void onModify(WatchEvent<?> watchEvent, Path path) {
                        refresh();
                    }

                    @Override
                    public void onDelete(WatchEvent<?> watchEvent, Path path) {
                        refresh();
                    }

                    @Override
                    public void onOverflow(WatchEvent<?> watchEvent, Path path) {

                    }
                });
            }
        }

        return this;
    }

    @Override
    public void refresh() {
        LogMessage logMessage = new LogMessage(this.getClass().getSimpleName(), "refresh");
        for (final String path : paths) {
            File res = FileUtil.file(path);
            resources.add(res);
            logMessage.processing().append("fileName", res.getName());
            log.debug(logMessage.toString());
            try {
                add(FileUtil.getReader(res, CharsetUtil.UTF_8));
            } catch (IOException e) {
                log.debug(logMessage.fail().toString(), e);
            }
            log.debug(logMessage.success().toString());
        }
    }

    public void add(Reader r) throws IOException {
        try {
            BufferedReader br;
            if (r instanceof BufferedReader)
                br = (BufferedReader) r;
            else
                br = new BufferedReader(r);
            StringBuilder key = new StringBuilder();
            StringBuilder sb = new StringBuilder();
            OUT:
            while (br.ready()) {
                String line = nextLineTrim(br);
                if (line == null)
                    break;
                if (line.startsWith("/*")) {
                    if (key.length() > 0 && line.contains("*/") && !line.endsWith("*/")) {
                        sb.append(line);
                        continue;
                    }
                    if (key.length() > 0 && sb.length() > 0) {
                        create(key.toString(), sb.toString());
                    }
                    key.setLength(0);
                    sb.setLength(0);

                    if (line.endsWith("*/")) {
                        if (line.length() > 4)
                            key.append(line.substring(2, line.length() - 2).trim());
                        continue;
                    } else {
                        key.append(line.substring(2).trim());
                        while (br.ready()) {
                            line = nextLineTrim(br);
                            if (line == null)
                                break OUT;
                            if (line.endsWith("*/")) {
                                if (line.length() > 2)
                                    key.append(line.substring(0, line.length() - 2).trim());
                                continue OUT;
                            } else {
                                key.append(line);
                            }
                        }
                    }
                }
                if (key.length() == 0) {
                    log.info("skip not key sql line {}", line);
                    continue;
                }
                if (sb.length() > 0)
                    sb.append("\n");
                sb.append(line);
            }

            // 最后一个sql也许是存在的
            if (key.length() > 0 && sb.length() > 0) {
                create(key.toString(), sb.toString());
            }
        } finally {
            IoUtil.close(r);
        }
    }

    @Override
    public String get(String key) {
        return sqlList.get(key);
    }

    @Override
    public int size() {
        return sqlList.size();
    }

    @Override
    public Set<String> keys() {
        return sqlList.keySet();
    }

    @Override
    public synchronized void create(String key, String value) {
        log.debug("key=[{}], sql=[{}]", key, value);
        AssertUtil.isFalse(!isAllowDuplicate() && sqlList.containsKey(key), "Duplicate sql key=[" + key + "]");
        sqlList.put(key, value);
    }

    @Override
    public void remove(String key) {
        sqlList.remove(key);
    }

    public boolean isAllowDuplicate() {
        return allowDuplicate;
    }

    public void setAllowDuplicate(boolean allowDuplicate) {
        this.allowDuplicate = allowDuplicate;
    }

    @Override
    public String getKey() {
        return "file";
    }

    private String nextLineTrim(BufferedReader br) throws IOException {
        String line = null;
        while (br.ready()) {
            line = br.readLine();
            if (line == null)
                break;
            if (StrUtil.isBlank(line))
                continue;
            return line.trim();
        }
        return line;
    }
}