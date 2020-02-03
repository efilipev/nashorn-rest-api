package efilipev.nashornrestapi.utils;

import java.util.UUID;

public final class Util {
    public static String generateScriptId() {
        return UUID.randomUUID().toString();
    }
}
