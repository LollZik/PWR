public class Proces {

    private int timeRequired;
    private final int timeOfArrival;

    private double waitingTime;
    private double executionTime;

    public Proces(int timeRequired, int timeOfAppearance){
        this.timeRequired = timeRequired;
        this.timeOfArrival = timeOfAppearance;
        this.waitingTime = -1;
    }

    public void work(int time){
        timeRequired -= time;
    }

    public void setWaitingTime(double k){
        this.waitingTime = k;
    }
    public void setexecutionTime(double k){
        this.executionTime = k;
    }

    public int getTimeRequired(){
        return timeRequired;
    }
    public int getTimeOfArrival(){
        return  timeOfArrival;
    }
    public double getWaitingTime(){
        return this.waitingTime;
    }
    public double getExecutionTime(){
        return this.executionTime;
    }
}
