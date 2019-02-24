package app.web.page;

import app.config.Config;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class PageGenerator {
    private static final String templatePath = Config.getTemplatePath();
    private static PageGenerator pageGenerator;
    private static Configuration configuration;

    public static synchronized PageGenerator instance() {
        if (pageGenerator == null) {
            pageGenerator = new PageGenerator();
            pageGenerator.configuration.setClassForTemplateLoading(pageGenerator.getClass(), templatePath);
        }
        return pageGenerator;
    }

    public static void generatePage(Writer writer, String filename, Map<String, Object> data) {
        try {
            Template template = instance().configuration.getTemplate(filename);
            template.process(data, writer);
            writer.flush();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generatePage(String filename) {
        return generatePage(filename, null);
    }

    public static String generatePage(String filename, Map<String, Object> data) {
        try {
            Writer writer = new StringWriter();
            Template template = instance().configuration.getTemplate(filename);
            template.process(data, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    private PageGenerator() {
        configuration = new Configuration();
    }
}
