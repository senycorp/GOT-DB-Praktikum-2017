import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * TemplateEngine
 *
 * @author Selcuk Kekec <senycorp@googlemail.com>
 */
public class TemplateEngine {
    /**
     * Template Engine Instance
     */
    protected static FreeMarkerEngine templateEngine = null;

    /**
     * Render main template with blank content
     *
     * @return
     */
    protected static ModelAndView render() {
        Map<String, Object> attributes = getViewMap();
        attributes.put("content", "");
        attributes.put("userData", DAO.getUser());
        attributes.put("figures", DAO.getFigures());
        attributes.put("haueser", DAO.getHaeuser());
        attributes.put("seasons", DAO.getSeasons());
        attributes.put("playlists", DAO.getPlaylists());

        return new ModelAndView(attributes, "index.ftl");
    }

    /**
     * Render main detail template
     *
     * @param modelAndView
     * @param additionalAttributes
     * @return
     */
    protected static ModelAndView renderDetail(ModelAndView modelAndView, Map<String, Object> additionalAttributes) {
        Map<String, Object> attributes = getViewMap();

        attributes.putAll(additionalAttributes);
        attributes.put("content", getTemplateEngine().render(modelAndView));
        attributes.put("userData", DAO.getUser());

        return new ModelAndView(attributes, "detail.ftl");
    }

    /**
     * Render main results template
     *
     * @param modelAndView
     * @param additionalAttributes
     * @return
     */
    protected static ModelAndView renderResults(ModelAndView modelAndView, Map<String, Object> additionalAttributes) {
        Map<String, Object> attributes = getViewMap();

        attributes.putAll(additionalAttributes);
        attributes.put("content", getTemplateEngine().render(modelAndView));
        attributes.put("userData", DAO.getUser());

        return new ModelAndView(attributes, "results.ftl");
    }

    /**
     * Render NotFound template
     *
     * @param attributes
     * @return
     */
    protected static ModelAndView renderNotFound(Map<String, Object> attributes) {
        attributes.put("userData", DAO.getUser());
        return new ModelAndView(attributes, "notFound.ftl");
    }

    /**
     * Render TypeNotFound template
     *
     * @param attributes
     * @return
     */
    protected static ModelAndView renderTypeNotFound(Map<String, Object> attributes) {
        attributes.put("userData", DAO.getUser());
        return new ModelAndView(attributes, "typeNotFound.ftl");
    }

    /**
     * Get View Map
     *
     * @return
     */
    protected static Map<String, Object> getViewMap() {
        return new HashMap<String, Object>();
    }

    /**
     * Get Template Engine Instance
     *
     * @return
     */
    protected static FreeMarkerEngine getTemplateEngine() {
        if (TemplateEngine.templateEngine == null) {
            /**
             * FreeMarker Initialization
             */
            FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine();
            Configuration freeMarkerConfiguration = new Configuration();
            freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(TemplateEngine.class, "/"));
            freeMarkerEngine.setConfiguration(freeMarkerConfiguration);

            TemplateEngine.templateEngine = freeMarkerEngine;
        }

        return TemplateEngine.templateEngine;
    }
}
