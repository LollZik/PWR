public class Proces {

    private final int position;
    private final int timeOfArrival;

    private double waitingTime;

    public Proces(int position, int timeOfAppearance){
        this.position = position;
        this.timeOfArrival = timeOfAppearance;
        this.waitingTime = -1;
    }

    public int getPosition(){
        return this.position;
    }

    public void setWaitingTime(double k){
        this.waitingTime = k;
    }

    public int getTimeOfArrival(){
        return  timeOfArrival;
    }
    public double getWaitingTime(){
        return this.waitingTime;
    }
}
