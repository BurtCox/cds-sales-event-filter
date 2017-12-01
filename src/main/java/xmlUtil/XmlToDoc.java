package xmlUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.fusesource.hawtbuf.ByteArrayInputStream;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Component
public class XmlToDoc {
	public Document convert(String xmlString) throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException {
		Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new ByteArrayInputStream(xmlString.getBytes("utf-8"))));
		return document;
	}
}
