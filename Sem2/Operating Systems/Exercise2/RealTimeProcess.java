public class RealTimeProcess extends Proces{

    private final boolean isPriority;
    private int deadline;
    private int position;
    public RealTimeProcess(int position, int timeOfAppearance, boolean isPriority, int deadline){
        super(position, timeOfAppearance);
        this.isPriority = isPriority;
        this.deadline = deadline;
        this.position = position;
    }

    public int getDeadline(){
        return this.deadline;
    }

    public void setDeadline(int newDeadline){
        this.deadline = newDeadline;
    }

    public boolean getIsPriority(){
        return this.isPriority;
    }
    public void setPosition(int n){
        this.position = n;
    }
}
