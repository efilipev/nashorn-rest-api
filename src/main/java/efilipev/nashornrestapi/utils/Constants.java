package efilipev.nashornrestapi.utils;

public final class Constants {
    public static final String BASE_URL = "/api/v1";
    public static final String SCRIPT_URL = "/script";
    public static final String FILE_ID = "/{fileId}";
    public static final String SCRIPT_QUEUE_URL = "/script/queue";
    public static final String SCRIPT_RESULT_URL = "/script/result";
    public static final String SCRIPT_DELETE_URL =  "/delete/script/";
    public static final String SCRIPT_QUEUE_LOCATION = "/api/v1/script/queue/%s";
    public static final String SCRIPT_IN_PROGRESS = "Script file in progress";
    public static final String SCRIPT_RESULT_LOCATION = "/api/v1/script/result/%s";
    public static final String DELETE_MESSAGE = "Cancellation is performed. Id: %s";
}
