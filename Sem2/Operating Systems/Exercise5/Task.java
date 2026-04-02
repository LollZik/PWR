public class Task {
    private int usage;
    private int timeLeft;


    public Task(int usage, int timeRequired) {
        this.usage = usage;
        this.timeLeft = timeRequired;
    }

    public void work(){
        this.timeLeft--;
    }

    public boolean isDone(){
        return !(this.timeLeft>0);
    }

    public int getUsage(){
        return this.usage;
    }

    public int getTimeLeft(){return this.timeLeft;}
}