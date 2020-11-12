package Data;

import java.util.ArrayList;

import Model.Question;

public interface AnswerListAsyncResponse {
void processedFinished(ArrayList<Question> questionArrayList);

}
