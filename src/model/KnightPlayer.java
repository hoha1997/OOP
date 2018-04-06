package model;

public class KnightPlayer extends AttackPlayer {

    public KnightPlayer() {
        this.health = 120;
        this.attack = 12;
        this.isStuned = false;
        this.isPowered = false;
    }

    public void attack(AttackPlayer guardPlayer) {
        if (this.isDead() || this.isStuned) {
            return;
        }
        if (isPowered) {
            this.power(guardPlayer);
        } else {
            guardPlayer.health -= this.attack;
            guardPlayer.guard(this);
        }
    }

    @Override
    public boolean guard(AttackPlayer attackPlayer) {
        return true;
    }

    @Override
    public void power(AttackPlayer guardPlayer) {
        if (this.isPowered == false) {
            this.isPowered = true;
        }
        if (numBeingAttacked>5 && this.isPowered==true) {
            this.attack += (numBeingAttacked-5)*2;
        }
        if (opponentHasCorrectAnswer(guardPlayer)) {
            this.attack = 12;
        }
    }
    
    public boolean opponentHasCorrectAnswer(AttackPlayer guardPlayer) {
        Answer answer = guardPlayer.answers.get(guardPlayer.answers.size()-1);
        return this.answers.contains(answer);
    }
}
