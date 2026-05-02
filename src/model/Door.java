public class Door {
    private boolean isLocked;
    private String question;
    private String answer;

    public Door() {
        this.isLocked = true;
        this.question = "";
        this.answer = "";
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}