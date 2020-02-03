package efilipev.nashornrestapi.service;

import efilipev.nashornrestapi.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

@Service
public class NashornScriptRunner implements ScriptRunner<String>{

    private Thread thread;
    private ExecutorService es;
    private ScriptEngine engine;
    private Map<String, Thread> worker = new ConcurrentHashMap<>();
    private Map<String, String> store = new ConcurrentHashMap<>();

    @Autowired
    public NashornScriptRunner(ScriptEngine engine) {
        this.engine = engine;
    }

    @Override
    public String execute(final String script) {
        es = Executors.newSingleThreadExecutor();
        final String fileId = Util.generateScriptId();
        es.execute(()-> {
            thread = Thread.currentThread();
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    worker.put(fileId, thread);
                    engine.eval(script);
                    Invocable invocable = (Invocable) engine;
                    final String result = (String) invocable.invokeFunction("func" );
                    store.put(fileId, result);
                } catch (NoSuchMethodException | ScriptException e) {
                    e.printStackTrace();
                }
            }
            thread.interrupt();
        });
        return fileId;
    }

    @Override
    public void destroy(String fileId) {
        Thread thread = worker.get(fileId);
        thread.stop();
        es.shutdown();
        System.out.println(isDestroyed());
    }

    public Thread getThreadFromStore(final String fileId) {
        return worker.get(fileId);
    }

    public String getStore() {
        final Set<String> keys = store.keySet();
        String result = null;
        if (keys.isEmpty()) {
            return "Store is empty";
        }
        for (String key : keys) {
            result = String.format("%s : %s", key, store.get(key));
        }
        return result;
    }

    public String getResult(final String fileId) {
        final Set<String> keys = store.keySet();
        String result = null;
        if (keys.isEmpty()) {
            return "Store is empty";
        }
        for (String key : keys) {
            if (key.equals(fileId)) {
               result = String.format("%s : %s", key, store.get(key));
            }
        }
        return result;
    }

    public void clear(final String fileId) {
        store.remove(fileId);
        worker.remove(fileId);
    }

    public Boolean isDestroyed() {
        return es.isShutdown();
    }

}
