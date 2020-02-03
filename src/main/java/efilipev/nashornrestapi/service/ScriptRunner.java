package efilipev.nashornrestapi.service;

public interface ScriptRunner<T> {
    T execute(T script);
    void destroy(T fileId);
}
