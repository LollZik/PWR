import java.util.ArrayList;

public class RR {
    private ArrayList<Proces> queue;
    private final int processesAmount;
    private double time = 0;
    private final double switchTime = 0.2;

    private int switches = 0;
    private Proces currentTask;
    private final int quantTime;

    private double AWT = 0;
    private double AET = 0;
    private double longest = -1;

    public RR(ArrayList<Proces> queue, int quantTime){
        this.queue = queue;
        this.quantTime = quantTime;
        this.processesAmount = queue.size();
    }

    public double[] work(){

        while(!queue.isEmpty()){
            int timeLeft;
            int bonusTime = 0;

            for(int i = 0; i < queue.size() && queue.get(i).getTimeOfArrival() <= time; i++){

                timeLeft = quantTime+bonusTime;
                currentTask = queue.get(i);
                time += switchTime;
                switches++;

                if (currentTask.getWaitingTime() == -1){
                    currentTask.setWaitingTime(time - currentTask.getTimeOfArrival());
                    AWT += currentTask.getWaitingTime();
                    if (currentTask.getWaitingTime() > longest){
                        longest = currentTask.getWaitingTime();
                    }
                }
                if(currentTask.getTimeRequired() <= (timeLeft+bonusTime)){
                    time += currentTask.getTimeRequired();
                    timeLeft -=currentTask.getTimeRequired();
                    bonusTime = timeLeft;

                    currentTask.work(currentTask.getTimeRequired());

                    currentTask.setexecutionTime(time-(currentTask.getWaitingTime()));
                    AET += currentTask.getExecutionTime();

                    queue.remove(currentTask);
                }
                else{
                    time += timeLeft+bonusTime;
                    currentTask.work(timeLeft+bonusTime);
                    bonusTime = 0;
                }
            }
            if(!queue.isEmpty()){
                if(queue.getFirst().getTimeOfArrival() > time){
                    time = queue.getFirst().getTimeOfArrival();
                }
            }
        }
        AWT /= processesAmount;
        AET /= processesAmount;
        return new double[] {processesAmount, AWT, AET, time,longest,switches};
    }
}