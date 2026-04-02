import java.util.ArrayList;

public class FDSCAN {
    private ArrayList<RealTimeProcess> queue;
    private int head;
    private int time = 0;
    private double AWT = 0;
    private double longest = 0;
    private final int initialSize;

    private int initialPriorityTasks = 0;
    private int failedTasks = 0;

    private ArrayList<RealTimeProcess> priorityQueue = new ArrayList<>();

    public FDSCAN(ArrayList<RealTimeProcess> queue, int disk){
        this.queue = queue;
        this.head = disk/2;
        this.initialSize = queue.size();
    }

    public double[] work(){
        for(RealTimeProcess p : queue){
            if (p.getIsPriority()){
                this.initialPriorityTasks++;
                priorityQueue.add(p);
            }
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
            }
            else{
                while (checkPriority()){
                       int lowestDeadline = Integer.MAX_VALUE;
                    RealTimeProcess lowestTask = priorityQueue.getFirst();
                       for(RealTimeProcess p: priorityQueue){ //&& p.getDeadline()>=Math.abs(p.getPosition()-head)
                           if(p.getDeadline() < lowestDeadline){
                               lowestDeadline = p.getDeadline();
                               lowestTask = p;
                           }
                       }
                    int distance = lowestTask.getDeadline();
                       if(distance < Math.abs(head - lowestTask.getPosition())){
                           failedTasks++;
                           queue.remove(lowestTask);
                           priorityQueue.remove(lowestTask);
                           break;
                       }

                       if (lowestTask.getPosition() > head){
                            for(int i = 0; i < distance; i++){
                                head++;
                                time++;
                                for(int j = 0; j < queue.size() && queue.get(j).getTimeOfArrival() <= time; j++){
                                    if (queue.get(j).getPosition()==head){
                                        queue.get(j).setWaitingTime(time-queue.get(j).getTimeOfArrival());
                                        AWT+=queue.get(j).getWaitingTime();
                                        if (queue.get(j).getWaitingTime() > longest) {
                                            longest = queue.get(j).getWaitingTime();
                                        }
                                        priorityQueue.remove(queue.get(j));
                                        queue.remove(j);
                                        j--;
                                    }
                                }
                                for(RealTimeProcess p: priorityQueue){
                                    if (p.getTimeOfArrival() <= time){
                                        p.setDeadline(p.getDeadline()-1);
                                    }else{
                                        break;
                                    }
                                }
                            }
                       }
                       else{
                           for(int i = 0; i < distance; i++){
                               head--;
                               time++;
                               for(int j = 0; j < queue.size() && queue.get(j).getTimeOfArrival() <= time; j++){
                                   if (queue.get(j).getPosition()==head){
                                       queue.get(j).setWaitingTime(time-queue.get(j).getTimeOfArrival());
                                       AWT+=queue.get(j).getWaitingTime();
                                       if (queue.get(j).getWaitingTime() > longest) {
                                           longest = queue.get(j).getWaitingTime();
                                       }
                                       priorityQueue.remove(queue.get(j));
                                       queue.remove(j);
                                       j--;
                                   }
                               }
                               for(RealTimeProcess p: priorityQueue){
                                   if (p.getTimeOfArrival()<=time) {
                                       p.setDeadline(p.getDeadline()-1);
                                   }
                                   else{
                                       break;
                                   }
                               }
                           }
                       }
//                    for (int i = priorityQueue.size() - 1; i >= 0; i--) {
//                        if (!priorityQueue.isEmpty() && priorityQueue.get(i).getDeadline() <= 0) {
//                            queue.remove(priorityQueue.get(i));
//                            priorityQueue.remove(i);
//                            failedTasks++;
//                        }
//                    }
                }
            }
        }
        AWT /= initialSize;
        return new double[] {initialSize,AWT,time, longest, failedTasks, initialPriorityTasks};
    }

    private boolean checkPriority(){
        for(int i = 0; i < queue.size() && queue.get(i).getTimeOfArrival() <= time; i++){
            if(queue.get(i).getIsPriority()){
                return true;
            }
        }
        return false;
    }
}