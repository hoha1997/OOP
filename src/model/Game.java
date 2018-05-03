package model;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static int gameCount = 0;

    private int id;
    private Character masterCharacter;
    private Character guestCharacter;
    private Topic topic;
    private int mode;
    private GameStatus status;
    private long timeStarted;
    private long timeEnd;

    public Game() {
        this.id = gameCount++;
        this.status = GameStatus.INITIAL;
    }

//    public Game(Character masterCharacter, Character guestCharacter) {
//        this.id = gameCount++;
//        this.masterCharacter = masterCharacter;
//        this.guestCharacter = guestCharacter;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public long getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(long timeStarted) {
        this.timeStarted = timeStarted;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Character getMasterCharacter() {
        return masterCharacter;
    }

    public void setMasterCharacter(Character masterCharacter) {
        this.masterCharacter = masterCharacter;
    }

    public Character getGuestCharacter() {
        return guestCharacter;
    }

    public void setGuestCharacter(Character guestCharacter) {
        this.guestCharacter = guestCharacter;
    }

    public boolean checkMaster(Character character) {
        return this.masterCharacter.equals(character);
    }

    public boolean checkGuest(Character character) {
        return this.guestCharacter.equals(character);
    }

    public boolean isEmpty() {
        return this.masterCharacter == null && this.guestCharacter == null;
    }

    public boolean isWaiting() {
        return this.guestCharacter == null;
    }

    public boolean isFull() {
        return this.guestCharacter != null;
    }

    public boolean isGuestReady() {
        return (status == GameStatus.GUEST_READY);
    }

    public boolean isStarted() {
        return (status == GameStatus.STARTED);
    }

    public boolean isGameOver() {
        return (status == GameStatus.GAME_OVER);
    }

    public Topic getTopic() {
        return topic;
    }

    public void removeGuest() {
        this.guestCharacter = null;
    }

    public boolean start() {
        if (this.isFull() && this.isGuestReady()) {
            this.status = GameStatus.STARTED;
            this.timeStarted = System.nanoTime();
            this.topic = TopicFactory.getRandomTopic();
            return true;
        } else {
            return false;
        }
    }

    public long getPlayedTime() {
        long currentTime = System.nanoTime();
        return (currentTime - this.timeStarted);
    }

    public boolean destroy() {
        return (masterCharacter == null && guestCharacter == null);
    }

    public JsonObject getStateAsJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", this.id);
        json.addProperty("mode", this.mode);
        json.addProperty("topic", this.topic != null ? this.topic.getValue() : "");
        json.addProperty("status", this.status.toString());
//        json.add("masterCharacter", this.masterCharacter.getStateAsJson());
//        if (this.guestCharacter != null) {
//            json.add("guestCharacter", this.guestCharacter.getStateAsJson());
//        }
        return json;
    }
}
