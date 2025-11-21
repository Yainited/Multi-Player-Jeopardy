package parser;

import model.Question;
import java.io.File;
import java.util.List;

public interface QuestionParser {
    List<Question> parse(File file) throws Exception;
}
