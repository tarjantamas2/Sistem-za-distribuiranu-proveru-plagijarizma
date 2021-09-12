package rs.ac.uns.ftn.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.vaadin.artur.helpers.LaunchUtil;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets and some desktop browsers.
 *
 */
@ComponentScan(basePackages = { "rs.ac.uns.ftn.frontend", "rs.ac.uns.ftn.distributed.plagiarismchecker.common" })
@SpringBootApplication
@Theme(value = "frontend")
@PWA(name = "Frontend", shortName = "Frontend", offlineResources = { "images/logo.png" })
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class FrontendApplication extends SpringBootServletInitializer implements AppShellConfigurator {

  public static void main(String[] args) {
    LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(FrontendApplication.class, args));
  }

}
