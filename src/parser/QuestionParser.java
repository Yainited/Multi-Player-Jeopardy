package parser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import model.Question;


public interface QuestionParser {

    List<Question> parse(File file) throws IOException;
}
