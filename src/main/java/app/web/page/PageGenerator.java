package app.web.page;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class PageGenerator {
    private static PageGenerator pageGenerator;
    private static Configuration configuration;
    private static String templatePath;

    public static void setTemplatePath(String templatePath) {
        PageGenerator.templatePath = templatePath;
    }

    public static synchronized PageGenerator instance() {
        if (pageGenerator == null)
            pageGenerator = new PageGenerator();
        return pageGenerator;
    }

    public static void generatePage(Writer writer, String filename) {
        generatePage(writer, filename, null);
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

    private PageGenerator() {
        configuration = new Configuration();
        configuration.setClassForTemplateLoading(this.getClass(), templatePath);
    }
}
