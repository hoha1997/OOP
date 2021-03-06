package socket;

import com.google.gson.JsonObject;

public class Message {
    private int status;
    private GameAction action;
    private JsonObject content;

    public Message() {}

    public Message(int status, GameAction action, JsonObject content) {
        this.status = status;
        this.action = action;
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public GameAction getAction() {
        return action;
    }

    public void setAction(GameAction action) {
        this.action = action;
    }

    public JsonObject getContent() {
        return content;
    }

    public void setContent(JsonObject content) {
        this.content = content;
    }
}