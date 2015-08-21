package de.tu_dortmund.ub.hb_ng.middleware;

import de.tu_dortmund.ub.hb_ng.middleware.preprocessing.PreprocessingException;
import de.tu_dortmund.ub.hb_ng.middleware.preprocessing.PreprocessingInterface;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class PreprocessingUsingXSLT implements PreprocessingInterface {

    private Properties config = null;
    private Logger logger = null;

    @Override
    public void init(Properties config) {

        this.config = config;
        PropertyConfigurator.configure(this.config.getProperty("service.log4j-conf"));
        this.logger = Logger.getLogger(PreprocessingUsingXSLT.class.getName());
    }

    @Override
    public String process(String data) throws PreprocessingException {

        String processedData = null;

        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();

            // Use the TransformerFactory to instantiate a Transformer that will work with
            // the stylesheet you specify. This method call also processes the stylesheet
            // into a compiled Templates object.
            Transformer transformer = tFactory.newTransformer(new StreamSource(this.config.getProperty("preprocessing.xslt")));

            // Use the Transformer to apply the associated Templates object to an XML document
            // (foo.xml) and write the output to a file (foo.out).
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new StreamSource(new StringReader(data)), new StreamResult(stringWriter));

            processedData = stringWriter.toString();

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return processedData;
    }
}
