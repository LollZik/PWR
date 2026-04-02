import java.util.Comparator;
import java.util.Objects;

public class Page {
    public int nr;        // numer strony
    public int procesID;    // ID procesu
    public int ref;       // licznik odniesień (dla algorytmu LRU)

    public Page(int nr, int ref, int proces) {
        this.nr = nr;
        this.ref = ref;
        this.procesID = proces;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }

    public int getProcesID(){
        return this.procesID;
    }

    public int getNr(){
        return this.nr;
    }

    public static Comparator<Page> refComparator = Comparator.comparingInt(o -> o.ref);

    @Override
    public String toString(){
        return "Page{" + "nr=" + nr + ", proces=" + procesID + ", ref=" + ref + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Page)) return false;
        Page other = (Page) o;
        return this.nr == other.nr && this.procesID == other.procesID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nr, procesID);
    }
}
