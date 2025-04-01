package UI.Config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource(value = "classpath:config.properties")}
)
@ComponentScan(basePackages = {"UI.Driver", "UI.Pages", "UI.Utils", "API.Model", "API.Request"})
public class ApplicationContextConfig {
}
