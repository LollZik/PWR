import java.util.Random;
import java.util.ArrayList;

public class Main {
    public final static int diskSize = 1000;

    public static void main(String[] args) {

        Serializer writer1 = new Serializer("FCFS");
        writer1.writerInit("initialSize;Average Waiting Time;Time/Total distance;longest");

        Serializer writer2 = new Serializer("SSTF");
        writer2.writerInit("initialSize;Average Waiting Time;Time/Total distance;longest;starved");

        Serializer writer3 = new Serializer("SCAN");
        writer3.writerInit("initialSize;Average Waiting Time;Time/Total distance;longest");

        Serializer writer4 = new Serializer("C-SCAN");
        writer4.writerInit("initialSize;Average Waiting Time;Time/Total distance;longest");
        //Serializer writer5 = new Serializer("EDF");
        //writer5.writerInit("initialSize;Average Waiting Time;Time/Total distance;longest;failed tasks;total tasks");

        Serializer writer6 = new Serializer("FD-SCAN");
        writer6.writerInit("initialSize;Average Waiting Time;Time/Total distance;longest;failed tasks;total tasks");


        Serializer writer7 = new Serializer("edgeSSTF");
        writer7.writerInit("initialSize;Average Waiting Time;Time/Total distance;longest;starved");

        Serializer writer8 = new Serializer("edgeFCFS");
        writer8.writerInit("initialSize;Average Waiting Time;Time/Total distance;longest");

        for(int i = 0 ; i < 50 ; i++){
            System.out.println(i);

            ArrayList<RealTimeProcess>[] test = generateRTProcesses();
            ArrayList<RealTimeProcess> t1 = test[0];
            ArrayList<RealTimeProcess> t = test[1];

//            FDSCAN test = new FDSCAN(t, diskSize);
//            double[] w = test.work();
//            writer6.write(String.format("%.1f",w[0])+";"+String.format("%.1f",w[1])+";"+String.format("%.1f",w[2])+";"+String.format("%.0f",w[3])+";"+String.format("%.0f",w[4])+";"+String.format("%.0f",w[5]));

            ArrayList<Proces>[] processes = generateProcesses();
//            ArrayList<Proces> p1 = processes[0];
//            ArrayList<Proces> p2 = processes[1];
//            ArrayList<Proces> p3 = processes[2];
//            ArrayList<Proces> p4 = processes[3];
//
//
//            FCFS fcfs = new FCFS(diskSize, p1);
//            SSTF sjf = new SSTF(diskSize, p2);
//            SCAN sc = new SCAN(diskSize, p3);
//            CSCAN csc = new CSCAN(diskSize, p4);
//
//
//            double[] fcfsData = fcfs.work();
//            double[] sjfData = sjf.work();
//            double[] scData = sc.work();
//            double[] cscData = csc.work();
//
//
//            writer1.write(String.format("%.1f",fcfsData[0])+";"+String.format("%.1f",fcfsData[1])+";"+String.format("%.1f",fcfsData[2])+";"+String.format("%.1f",fcfsData[3]));
//            writer2.write(String.format("%.1f",sjfData[0])+";"+String.format("%.1f",sjfData[1])+";"+String.format("%.1f",sjfData[2])+";"+String.format("%.1f",sjfData[3])+";"+String.format("%.1f",sjfData[4]));
//            writer3.write(String.format("%.1f",scData[0])+";"+String.format("%.1f",scData[1])+";"+String.format("%.1f",scData[2])+";"+String.format("%.0f",scData[3]));
//            writer4.write(String.format("%.1f",cscData[0])+";"+String.format("%.1f",cscData[1])+";"+String.format("%.1f",cscData[2])+";"+String.format("%.0f",cscData[3]));
            ArrayList<Proces>[] edges = generateEdge();
            ArrayList<Proces> e1 = edges[0];
            ArrayList<Proces> e2 = edges[1];

            SSTF edgeSSTF = new SSTF(diskSize,e1);
            FCFS edgeFCFS = new FCFS(diskSize, e2);

            double[] edgeResult = edgeSSTF.work();
            double[] edgeResult2 = edgeFCFS.work();
            writer7.write(String.format("%.1f",edgeResult[0])+";"+String.format("%.1f",edgeResult[1])+";"+String.format("%.1f",edgeResult[2])+";"+String.format("%.1f",edgeResult[3])+";"+String.format("%.1f",edgeResult[4]));
            writer8.write(String.format("%.1f",edgeResult2[0])+";"+String.format("%.1f",edgeResult2[1])+";"+String.format("%.1f",edgeResult2[2])+";"+String.format("%.1f",edgeResult2[3]));


        }
        writer1.close();
        writer2.close();
        writer3.close();
        writer4.close();
        writer6.close();
        writer7.close();
        writer8.close();
    }

    public static ArrayList<Proces>[] generateProcesses(){
        Random random = new Random();
        ArrayList<Proces>[] result = new ArrayList[4];
        ArrayList<Proces> processes1 = new ArrayList<>();
        ArrayList<Proces> processes2 = new ArrayList<>();
        ArrayList<Proces> processes3 = new ArrayList<>();
        ArrayList<Proces> processes4 = new ArrayList<>();

        int amountOfProcesses = random.nextInt(5000,7500);
        int n = 0;

        for (int i = 0; i< amountOfProcesses;i++) {

            for (int j = 0; j < random.nextInt(1,5) ;j++){
                int length = random.nextInt(0, diskSize);
                processes1.add(new Proces(length, n));
                processes2.add(new Proces(length, n));
                processes3.add(new Proces(length, n));
                processes4.add(new Proces(length, n));
            }
            n += random.nextInt(0, 10);
        }
        result[0] = processes1;
        result[1] = processes2;
        result[2] = processes3;
        result[3] = processes4;

        return result;
    }
    public static ArrayList<RealTimeProcess>[] generateRTProcesses(){
        Random random = new Random();
        ArrayList<RealTimeProcess>[] result = new ArrayList[2];
        ArrayList<RealTimeProcess> processes1 = new ArrayList<>();
        ArrayList<RealTimeProcess> processes2 = new ArrayList<>();

        int amountOfProcesses = random.nextInt(5000,7500);
        int n = 0;

        for (int i = 0; i< amountOfProcesses;i++) {

            for (int j = 0; j < random.nextInt(1,5) ;j++){
                int length = random.nextInt(0, diskSize);
                if (random.nextInt() > 3){
                    processes1.add(new RealTimeProcess(length, n, false, -1));
                    processes2.add(new RealTimeProcess(length, n, false, -1));
                }
                else{
                    int time = random.nextInt(350,2000);
                    processes1.add(new RealTimeProcess(length, n, true, time));
                    processes2.add(new RealTimeProcess(length, n, true, time));
                }
            }
            n += random.nextInt(0, 10);
        }
        result[0] = processes1;
        result[1] = processes2;

        return result;
    }

    public static ArrayList<Proces>[] generateEdge(){
        Random random = new Random();
        ArrayList<Proces>[] result = new ArrayList[4];
        ArrayList<Proces> processes1 = new ArrayList<>();
        ArrayList<Proces> processes2 = new ArrayList<>();
        ArrayList<Proces> processes3 = new ArrayList<>();
        ArrayList<Proces> processes4 = new ArrayList<>();

        int amountOfProcesses = random.nextInt(5000,7500);
        int n = 0;

        for (int i = 0; i< amountOfProcesses;i++) {

            for (int j = 0; j < random.nextInt(1,5) ;j++){
                int pos = random.nextInt(1,5);
                if (pos==4 || pos==3){
                    int length = random.nextInt(3*diskSize/4, diskSize);
                    processes1.add(new Proces(length, n));
                    processes2.add(new Proces(length, n));
                    processes3.add(new Proces(length, n));
                    processes4.add(new Proces(length, n));
                }
                else{
                    int length = random.nextInt(0, diskSize/4);
                    processes1.add(new Proces(length, n));
                    processes2.add(new Proces(length, n));
                    processes3.add(new Proces(length, n));
                    processes4.add(new Proces(length, n));
                }
            }
            n += random.nextInt(0, 10);
        }
        result[0] = processes1;
        result[1] = processes2;
        result[2] = processes3;
        result[3] = processes4;

        return result;
    }
}
