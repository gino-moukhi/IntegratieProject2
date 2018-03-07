package be.kdg.kandoe.configuration.other;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class MainWebAppInitializer implements WebApplicationInitializer {
    @Value("${temp.folder}")
    private String tempFolder;

    @Value("${file.max.size}")
    private int maxUploadSize;

    public MainWebAppInitializer() {
        this.maxUploadSize *= 1024 * 1024;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ServletRegistration.Dynamic appServlet = servletContext.addServlet("mvc", new DispatcherServlet(
                new GenericWebApplicationContext()));

        appServlet.setLoadOnStartup(1);

        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(tempFolder,
                maxUploadSize, maxUploadSize * 2, maxUploadSize / 2);

        appServlet.setMultipartConfig(multipartConfigElement);
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {

        return new StandardServletMultipartResolver();
    }
}
