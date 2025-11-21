package parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import model.Question;
import org.w3c.dom.*;

public class XmlQuestionParser implements QuestionParser {

    @Override
    public List<Question> parse(File file) {
        List<Question> questions = new ArrayList<>();
        try {
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = fac.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList qNodes = doc.getElementsByTagName("QuestionItem");
            for (int i = 0; i < qNodes.getLength(); i++) {
                Node qNode = qNodes.item(i);
                if (qNode.getNodeType() != Node.ELEMENT_NODE) continue;
                Element qElem = (Element) qNode;

                String category = getChildText(qElem, "Category");
                String questionText = getChildText(qElem, "QuestionText");
                String answer = getChildText(qElem, "CorrectAnswer");
                String valueStr = getChildText(qElem, "Value");
                int value = 0;
                try { value = Integer.parseInt(valueStr.trim()); } catch (Exception ignored) {}

                Map<String, String> opts = new HashMap<>();
                Element optionsElem = (Element) qElem.getElementsByTagName("Options").item(0);
                if (optionsElem != null) {
                    opts.put("A", getChildText(optionsElem, "OptionA"));
                    opts.put("B", getChildText(optionsElem, "OptionB"));
                    opts.put("C", getChildText(optionsElem, "OptionC"));
                    opts.put("D", getChildText(optionsElem, "OptionD"));
                }

                if (category == null || questionText == null || answer == null) continue;
                questions.add(new Question(null, category, value, questionText, answer, opts));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse XML: " + e.getMessage(), e);
        }
        return questions;
    }

    private String getChildText(Element parent, String tagName) {
        NodeList nl = parent.getElementsByTagName(tagName);
        if (nl.getLength() == 0) return null;
        Node n = nl.item(0);
        if (n == null) return null;
        String t = n.getTextContent();
        return t == null ? null : t.trim();
    }
}
