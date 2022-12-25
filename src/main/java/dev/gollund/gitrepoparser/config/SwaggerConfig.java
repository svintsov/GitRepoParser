package dev.gollund.gitrepoparser.config;

import dev.gollund.gitrepoparser.controller.UserRepoController;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(
                        UserRepoController.class.getPackageName()))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("GitRepoParser App",
                "APIs for GitRepoParser App.",
                "1.0",
                "Just don't do something bad",
                new Contact("Kyrylo", "www.no_site_yet.com", "svintcov95@gmail.com"),
                "Svintsov's API license",
                "svintcov95@gmail.com",
                Collections.emptyList());
    }
}
