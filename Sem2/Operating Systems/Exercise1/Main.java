import java.util.Random;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Serializer writer1 = new Serializer("FCFS");
        writer1.writerInit("Processes amount;Avg waiting time;Avg execution time;Total time;longest;switches");

        Serializer writer2 = new Serializer("SJF");
        writer2.writerInit("Processes amount;Avg waiting time;Avg execution time;Total time;Starved amount;Longest waiting time;switches");

        Serializer writer3 = new Serializer("RR");
        writer3.writerInit("Processes amount;Avg waiting time;Avg execution time;Total time;longest;switches");

        Serializer writer4 = new Serializer("RR1");
        writer4.writerInit("Processes amount;Avg waiting time;Avg execution time;Total time;longest;switches");

        Serializer writer5 = new Serializer("RR2");
        writer5.writerInit("Processes amount;Avg waiting time;Avg execution time;Total time;longest;switches");

        Serializer writer6 = new Serializer("RR3");
        writer6.writerInit("Processes amount;Avg waiting time;Avg execution time;Total time;longest;switches");


        for(int i = 0 ; i < 200 ; i++){

        ArrayList<Proces>[] processes = generateProcesses();
        ArrayList<Proces> p1 = processes[0];
        ArrayList<Proces> p2 = processes[1];
        ArrayList<Proces> p3 = processes[2];

        ArrayList<Proces>[] timeslices = generateProcesses();
        ArrayList<Proces> t1 = timeslices[0];
        ArrayList<Proces> t2 = timeslices[1];
        ArrayList<Proces> t3 = timeslices[2];



        FCFS fcfs = new FCFS(p1);
        SJF sjf = new SJF(p2);
        RR rr = new RR(p3,5);

        RR rr1 = new RR(t1,1);
        RR rr2 = new RR(t2,10);
        RR rr3 = new RR(t3,15);

        double[] fcfsData = fcfs.work();
        double[] sjfData = sjf.work();
        double[] rrData = rr.work();
        double[] r1Data = rr1.work();
        double[] r2Data = rr2.work();
        double[] r3Data = rr3.work();


        writer1.write(String.format("%.1f",fcfsData[0])+";"+String.format("%.1f",fcfsData[1])+";"+String.format("%.1f",fcfsData[2])+";"+String.format("%.1f",fcfsData[3])+";"+String.format("%.1f",fcfsData[4])+";"+String.format("%.0f",fcfsData[5]));
        writer2.write(String.format("%.1f",sjfData[0])+";"+String.format("%.1f",sjfData[1])+";"+String.format("%.1f",sjfData[2])+";"+String.format("%.1f",sjfData[3])+";"+String.format("%.1f",sjfData[4])+";"+String.format("%.1f",sjfData[5])+";"+String.format("%.1f",sjfData[6]));
        writer3.write(String.format("%.1f",rrData[0])+";"+String.format("%.1f",rrData[1])+";"+String.format("%.1f",rrData[2])+";"+String.format("%.0f",rrData[3])+";"+String.format("%.1f",rrData[4])+";"+String.format("%.1f",rrData[5]));
        writer4.write(String.format("%.1f",r1Data[0])+";"+String.format("%.1f",r1Data[1])+";"+String.format("%.1f",r1Data[2])+";"+String.format("%.0f",r1Data[3])+";"+String.format("%.1f",r1Data[4])+";"+String.format("%.1f",r1Data[5]));
        writer5.write(String.format("%.1f",r2Data[0])+";"+String.format("%.1f",r2Data[1])+";"+String.format("%.1f",r2Data[2])+";"+String.format("%.0f",r2Data[3])+";"+String.format("%.1f",r2Data[4])+";"+String.format("%.1f",r2Data[5]));
        writer6.write(String.format("%.1f",r3Data[0])+";"+String.format("%.1f",r3Data[1])+";"+String.format("%.1f",r3Data[2])+";"+String.format("%.0f",r3Data[3])+";"+String.format("%.1f",r3Data[4])+";"+String.format("%.1f",r3Data[5]));

        }

        writer1.close();
        writer2.close();
        writer3.close();
        writer4.close();
        writer5.close();
        writer6.close();
    }

        public static ArrayList<Proces>[] generateProcesses(){
            Random random = new Random();
            ArrayList<Proces>[] result = new ArrayList[3];
            ArrayList<Proces> processes1 = new ArrayList<>();
            ArrayList<Proces> processes2 = new ArrayList<>();
            ArrayList<Proces> processes3 = new ArrayList<>();

            int amountOfProcesses = random.nextInt(100,300);
            int n = 0;

            for (int i = 0; i< amountOfProcesses;i++) {
                int length = random.nextInt(0, 30) % 20;
                length++;

                processes1.add(new Proces(length, n));
                processes2.add(new Proces(length, n));
                processes3.add(new Proces(length, n));

                n += random.nextInt(0, 10);
            }
            result[0] = processes1;
            result[1] = processes2;
            result[2] = processes3;
            return result;
        }
}
