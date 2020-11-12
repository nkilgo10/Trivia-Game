package Model;

public class Question {
    private String answer;
    private Boolean isAnswerTrue;

    public Question(String answer, Boolean isAnswerTrue) {
        this.answer = answer;
        this.isAnswerTrue = isAnswerTrue;
    }

    public Question() {
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getAnswerTrue() {
        return isAnswerTrue;
    }

    public void setAnswerTrue(Boolean answerTrue) {
        isAnswerTrue = answerTrue;
    }

    @Override
    public String toString() {
        return "Question{" +
                "answer='" + answer + '\'' +
                ", isAnswerTrue=" + isAnswerTrue +
                '}';
    }
}
