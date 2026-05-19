package Liquidacion.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {

    @Autowired
    private BuildProperties buildProperties;

    @GetMapping("/version")
    public String version() {
        return "Version del sistema: " + buildProperties.getVersion() +
                " | Build: " + buildProperties.getTime();
    }
}