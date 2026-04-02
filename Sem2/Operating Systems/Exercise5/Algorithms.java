import java.util.ArrayList;
import java.util.Random;

public class Algorithms{
    private final ArrayList<CPU> cpus;
    private final CPU x;
    private final ArrayList<Task> globalTasks;
    private final ArrayList<Task> copy2 = new ArrayList<>();
    private final ArrayList<Task> copy3 = new ArrayList<>();

    private final int N;
    private final int p;
    private final int z;
    private final int r;

    private final StatisicsCollector stats1;
    private final StatisicsCollector stats2;
    private final StatisicsCollector stats3;

    private final Serializer writer1;
    private final Serializer writer2;
    private final Serializer writer3;

    Random random = new Random();

    public Algorithms(int N, int p, int z, int r, Serializer writer1, Serializer writer2, Serializer writer3){
        this.N = N;
        this.p = p;
        this.z = z;
        this.r = r;

        this.cpus = new ArrayList<>();
        this.globalTasks = new ArrayList<>(generateTasks());
        this.writer1 = writer1;
        this.writer2 = writer2;
        this. writer3 = writer3;
        this.stats1 = new StatisicsCollector();
        this.stats2 = new StatisicsCollector();
        this.stats3 = new StatisicsCollector();
        init(N);
        this.x = cpus.getFirst();

        // deep copy

        for(Task t : globalTasks){
            copy2.add(new Task(t.getUsage(), t.getTimeLeft()));
            copy3.add(new Task(t.getUsage(), t.getTimeLeft()));
        }

        stats1.setSize(globalTasks.size());
        stats2.setSize(globalTasks.size());
        stats3.setSize(globalTasks.size());
    }

    public void alg1(){
        boolean isDone = false;
        int timer = 0;
        ArrayList<Task> unfinished = new ArrayList<>();
        // Add all tasks to CPUs
        for(Task task : globalTasks){
            for(int j = 0; j < z; j++){ // Try adding process to random processor
                CPU processor = cpus.get(random.nextInt(1,N));
                stats1.incrementQueries();
                if (processor.getUsage() < this.p){
                    processor.addTask(task);
                    break;
                }
                else{
                    if (j+1 == z){ // No random CPU had usage < p
                        stats1.incrementQueries();
                        if(this.x.getUsage() + task.getUsage() < 100){
                            this.x.addTask(task); // add task to x
                        }
                        else{
                            unfinished.add(task);
                        }
                    }
                }
            }
            workOnAll();
            timer++;
            if(timer%100==0){
            stats1.recordCpuLoads(cpus);
            }
        }
        // Finish all remaining tasks
        for(CPU cpu: cpus){
            if(unfinished.isEmpty()){
                break;
            }
            Task task = unfinished.getFirst();
            if(cpu.getUsage() + task.getUsage() < 100){
                cpu.addTask(task);
                unfinished.remove(task);
            }
        }
        while(!isDone){
            isDone = true;
            for(CPU cpu : cpus){
                cpu.workOnAll();
                timer++;
                if(timer%100==0){
                    stats1.recordCpuLoads(cpus);
                }
                if(!cpu.isNotWorking()){
                    // If at least 1 is still working - repeat loop
                    isDone = false;
                }
            }
        }
        writer1.export(stats1);
    }

    public void alg2(){
        boolean isDone = false;
        boolean added;
        int timer = 0;
        // Add all tasks to CPUs
        for(Task task : copy2){
            added = false;
            while(!added){
                stats2.incrementQueries();
                if(this.x.getUsage() < p){
                    this.x.addTask(task);
                    break;
                }
                else{
                    for(int j = 1; j < 2*cpus.size(); j++){ // Try adding process to random processor
                        CPU processor = cpus.get(random.nextInt(1,N));
                        stats2.incrementQueries();
                        if (processor.getUsage() < this.p){
                            processor.addTask(task);
                            added = true;
                            break;
                        }
                    }
                    // If no cpu could take the task in, work on tasks and try again
                    if(!added){
                        for(int i = 0; i < 20; i++){
                            workOnAll();
                        }
                    }
                }
            }
            workOnAll();
            timer++;
            if(timer%100==0){
                stats2.recordCpuLoads(cpus);
            }
        }
        // Finish all remaining tasks
        while(!isDone){
            isDone = true;
            for(CPU cpu : cpus){
                cpu.workOnAll();
                timer++;
                if(timer%100==0){
                    stats2.recordCpuLoads(cpus);
                }
                if(!cpu.isNotWorking()){
                    // If at least 1 is working - continue the loop
                    isDone = false;
                }
            }
        }
        writer2.export(stats2);
    }

    public void alg3(){
        boolean isDone = false;
        boolean added;
        int timer = 0;
        // Add all tasks to CPUs
        for(Task task : copy3){
            added = false;
            while(!added){
                stats3.incrementQueries();
                if(this.x.getUsage() < p){
                    this.x.addTask(task);
                    break;
                }
                else{
                    for(int j = 1; j < 2*cpus.size(); j++){ // Try adding process to random processor
                        CPU processor = cpus.get(random.nextInt(1,N));
                        stats3.incrementQueries();
                        if (processor.getUsage() < this.p){
                            processor.addTask(task);
                            added = true;
                            break;
                        }
                    }
                    // If no cpu could take the task in, work on tasks and try again
                    if(!added){
                        for(int i = 0; i < 20; i++){
                            workOnAll();
                        }
                    }
                }
            }
            workOnAll();
            timer++;
            if(timer%100==0){
                stats3.recordCpuLoads(cpus);
            }
            // If any CPU has usage < r, try to move 20% of tasks from the CPU with surplus
            for(CPU cpu : cpus){
                stats3.incrementQueries();
                if (cpu.getUsage() < r){
                    for(CPU c : cpus){
                        stats3.incrementQueries();
                        if(c.getUsage() > p){
                            int usage = 0;
                            while(usage < 20){
                                task = c.getFirst();
                                cpu.addTask(task);
                                c.removeTask(task);
                                usage += task.getUsage();
                                stats3.incrementMigrations();
                            }
                        }
                    }
                }
            }
        }
        // Finish all remaining tasks
        while(!isDone){
            isDone = true;
            for(CPU cpu : cpus){
                cpu.workOnAll();
                timer++;
                if(timer%100==0){
                    stats3.recordCpuLoads(cpus);
                }
                if(!cpu.isNotWorking()){
                    isDone = false;
                }
            }
        }
        writer3.export(stats3);
    }

    private void init(int N){
        for(int i = 0; i < N; i++){
            this.cpus.add(new CPU());
        }
    }

    private ArrayList<Task> generateTasks(){
        // Usage: 1-20%
        // Time required: 200-400
        // Amount:  (800-1200) * N
        ArrayList<Task> tasks = new ArrayList<>();
        int amount = this.N * random.nextInt(1000,1500);
        for(int i = 0; i < amount; i++){
            int usage = random.nextInt(20)+1;
            int timeRequired = random.nextInt(200)+200;
            tasks.add(new Task(usage, timeRequired));
        }
        return tasks;
    }

    private void workOnAll(){
        for(CPU cpu : cpus){
            cpu.workOnAll();
        }
    }
}
