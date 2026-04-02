import java.util.ArrayList;

public class EDF {
    private ArrayList<RealTimeProcess> queue;
    private int head;
    private int time = 0;
    private double AWT = 0;
    private double longest = 0;
    private RealTimeProcess currentTask;
    private final int initialSize;
    private int initialPriorityTasks = 0;
    private int failedTasks = 0;

    public EDF(ArrayList<RealTimeProcess> queue, int disk){
        this.queue = queue;
        this.head = disk/2;
        this.initialSize = queue.size();
    }

    public double[] work(){
        for(RealTimeProcess p : queue){
            if (p.getIsPriority()){this.initialPriorityTasks++;}
        }
        while(!queue.isEmpty()) {

            if (!checkPriority()) {
                if(queue.getFirst().getTimeOfArrival() <= time){
                    Proces currentTask = queue.getFirst();
                    int distance = Math.abs(head - currentTask.getPosition());
                    time += distance;
                    head = currentTask.getPosition();
                    currentTask.setWaitingTime(time- currentTask.getTimeOfArrival());
                    AWT += currentTask.getWaitingTime();
                    if (currentTask.getWaitingTime() > longest){
                        longest = currentTask.getWaitingTime();
                    }
                    queue.remove(currentTask);
                }
                if(!queue.isEmpty() && queue.getFirst().getTimeOfArrival() > time){
                    time = queue.getFirst().getTimeOfArrival();
                }
            } else {
                while (checkPriority()){
                int timePassed;
                int shortest = Integer.MAX_VALUE;
                for (int i = 0; i < queue.size(); i++) {
                    if (queue.get(i).getTimeOfArrival() <= time && queue.get(i).getIsPriority()) {
                        if (queue.get(i).getDeadline() < shortest){
                            currentTask = queue.get(i);
                            shortest = currentTask.getDeadline();
                        }

                        int distance = Math.abs(currentTask.getPosition() - head);
                        if(distance <= currentTask.getDeadline()) {
                            head = currentTask.getPosition();
                            time += distance;
                            timePassed = distance;
                            currentTask.setWaitingTime(time - currentTask.getTimeOfArrival());
                            AWT += currentTask.getWaitingTime();

                            if (currentTask.getWaitingTime() > longest) {
                                longest = currentTask.getWaitingTime();
                            }
                            queue.remove(currentTask);
                            i--;
                        }
                        else{
                            if (currentTask.getPosition() > head) {
                                head += currentTask.getDeadline();
                            } else {
                                head -= currentTask.getDeadline();
                            }
                            failedTasks++;
                            time += currentTask.getDeadline();
                            timePassed = currentTask.getDeadline();
                            queue.remove(currentTask);
                            i--;
                        }
                        ArrayList<RealTimeProcess> tasksToRemove = new ArrayList<>();
                        for (RealTimeProcess task : queue) {
                            if (task.getIsPriority() && task.getTimeOfArrival() <= time &&  (task.getDeadline() - timePassed) <= 0) {
                                tasksToRemove.add(task);
                            }
                        }
                        queue.removeAll(tasksToRemove);
                        failedTasks += tasksToRemove.size();
                    }
                }
            }
            }
        }
        AWT /= initialSize;
        return new double[] {initialSize,AWT,time, longest, failedTasks, initialPriorityTasks};
    }

    private boolean checkPriority(){
        for(int i = 0; i< queue.size() && queue.get(i).getTimeOfArrival() <= time; i++){
            if(queue.get(i).getIsPriority()){
                return true;
            }
        }
        return false;
    }
}