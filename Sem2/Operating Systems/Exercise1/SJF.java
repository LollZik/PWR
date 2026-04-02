import java.util.ArrayList;

public class SJF {
    private ArrayList<Proces> queue;
    private final int processesAmount;
    private double time = 0;
    private final double switchTime = 0.2;

    private int switches = 0;
    private Proces currentTask;
    private int shortestTask = 9999;
    private int shortestIndex;

    private double longest = -1;
    private int starved = 0;
    private double AWT = 0;
    private double AET = 0;

    public SJF(ArrayList<Proces> queue){
        this.queue = queue;
        this.processesAmount = queue.size();
         }

    public double[] work(){

        while(!queue.isEmpty()){
            for(int i = 0; i<queue.size() && queue.get(i).getTimeOfArrival()<=time; i++){
                if(queue.get(i).getTimeRequired() < shortestTask){
                    shortestIndex = i;
                    shortestTask = queue.get(i).getTimeRequired();
                }
            }
            if(currentTask != queue.get(shortestIndex)){
                currentTask = queue.get(shortestIndex);
                time += switchTime;
                switches++;
            }

            if(currentTask.getWaitingTime() == -1){
                currentTask.setWaitingTime(time - currentTask.getTimeOfArrival());
                AWT += currentTask.getWaitingTime();

                if (currentTask.getWaitingTime() > longest){
                    longest = currentTask.getWaitingTime();
                }
            }
            currentTask.work(1);
            time++;
            if(currentTask.getTimeRequired() == 0){

                currentTask.setexecutionTime(time-currentTask.getWaitingTime()-currentTask.getTimeOfArrival());
                AET += currentTask.getExecutionTime();

                if(currentTask.getWaitingTime() > 500){
                    starved++;
                }
                queue.remove(currentTask);
            }
            shortestTask = 9999;
        }
        AWT /= processesAmount;
        AET /= processesAmount;
        return new double[] {processesAmount, AWT, AET, time, starved, longest,switches};
    }
}