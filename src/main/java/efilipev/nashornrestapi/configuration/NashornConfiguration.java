package efilipev.nashornrestapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Configuration
public class NashornConfiguration {

    @Bean
    public ScriptEngine init() {
        return new ScriptEngineManager().getEngineByName("nashorn");
    }
}
