package efilipev.nashornrestapi.controller;

import efilipev.nashornrestapi.service.NashornScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static efilipev.nashornrestapi.utils.Constants.*;

@RestController
@RequestMapping(path = BASE_URL)
public class NashornController {

    @Autowired
    private NashornScriptRunner scriptRunner;

    @PostMapping(value = SCRIPT_URL)
    public ResponseEntity addScript(@RequestBody final String script) {
        final String fileId = scriptRunner.execute(script);
        return ResponseEntity.accepted()
                        .header(HttpHeaders.LOCATION, String.format(SCRIPT_QUEUE_LOCATION, fileId))
                        .build();
    }

    @GetMapping(value = SCRIPT_QUEUE_URL + FILE_ID)
    public ResponseEntity<Object> getFileFromQueue(@PathVariable final String fileId) {
        final Thread thread = scriptRunner.getThreadFromStore(fileId);
        return  thread.isInterrupted()
                ? ResponseEntity.ok().header(HttpHeaders.LOCATION, String.format(SCRIPT_RESULT_LOCATION, fileId)).build()
                : ResponseEntity.status(HttpStatus.ACCEPTED).body(String.format(SCRIPT_IN_PROGRESS));
    }

    @GetMapping(value = SCRIPT_RESULT_URL)
    public ResponseEntity<String> getResult() {
        return ResponseEntity.ok(scriptRunner.getStore());
    }

    @GetMapping(value = SCRIPT_RESULT_URL + FILE_ID)
    public ResponseEntity<String> getFile(@PathVariable final String fileId) {
        return ResponseEntity.ok(scriptRunner.getResult(fileId));
    }

    @GetMapping(value = SCRIPT_DELETE_URL + FILE_ID)
    public ResponseEntity<String> deleteScriptById(@PathVariable final String fileId) {
        scriptRunner.destroy(fileId);
        scriptRunner.clear(fileId);
        return ResponseEntity.ok(String.format(DELETE_MESSAGE, fileId));
    }
}
