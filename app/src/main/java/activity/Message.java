package activity;

public class Message {
    public static final int SENDER_USER = 0;
    public static final int SENDER_BOT = 1;

    private String text;
    private int sender;

    public Message(String text, int sender) {
        this.text = text;
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public int getSender() {
        return sender;
    }
}
