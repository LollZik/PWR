import java.util.ArrayList;

public class CPU{
    private int currentLoad;
    ArrayList<Task> localTasks;

    public CPU(){
        this.currentLoad = 0;
        this.localTasks = new ArrayList<>();
    }

    public void addTask(Task task){
        this.localTasks.add(task);
        this.currentLoad += task.getUsage();
    }

    public void removeTask(Task task){
        if(this.localTasks.remove(task)){
            this.currentLoad -= task.getUsage();
        }
    }

    public void workOnAll(){
        Task task;
        for(int i = 0; i < this.localTasks.size(); i++){
            task = this.localTasks.get(i);
            task.work();
            if(task.isDone()){
                removeTask(task);
                i--;
            }
        }
    }
    public boolean isNotWorking(){
        return this.localTasks.isEmpty();
    }

    public int getUsage(){
        return this.currentLoad;
    }

    public Task getFirst(){
        return this.localTasks.getFirst();
    }
}
