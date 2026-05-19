package Liquidacion.confi;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component
public class version {

    private final BuildProperties buildProperties;

    public version(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    public String getVersion() {
        return buildProperties.getVersion();
    }

    public String getBuildTime() {
        return buildProperties.getTime().toString();
    }
}
