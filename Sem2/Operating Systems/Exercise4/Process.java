import java.util.List;

public class Process {
    private final List<Page> pages;
    private final int processId;
    private int frameCount;
    private int pageFaults;

    public Process(int processId, List<Page> pages) {
        this.processId = processId;
        this.pages = pages;
        this.pageFaults = 0;
    }


    public void setFrame(int frames) {
        this.frameCount = frames;
    }

    public int getProcessId(){
        return this.processId;
    }

    public List<Page> getPages() {
        return this.pages;
    }

    public void setPageFaults(int data){
        this.pageFaults = data;
    }

    public int getPageFaults(){
        return this.pageFaults;
    }

    public int getFrameCount(){
        return this.frameCount;
    }
}
