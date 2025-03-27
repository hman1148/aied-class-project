package Models.Requests;

public class StudentAnswerRequest {

    private String selectedQuestion;

    public StudentAnswerRequest(String selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }

    public String getSelectedQuestion() {
        return selectedQuestion;
    }

    public void setSelectedQuestion(String selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }
}

