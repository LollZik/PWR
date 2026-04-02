import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import core.AbstractSwappingSortingAlgorithm;
import testing.*;
import testing.generation.*;



public class Writer {
    private BufferedWriter writer;
    private AbstractSwappingSortingAlgorithm<MarkedValue<Integer>> algorithm;
    private Generator<Integer> generator;


    public Writer(AbstractSwappingSortingAlgorithm<MarkedValue<Integer>> algorithm, Generator<Integer> generator){
        this.algorithm = algorithm;
        this.generator = generator;

        writerInit();
    }

    private void writerInit(){
        String filePath = getFileName(algorithm, generator);
        try {
            this.writer = new BufferedWriter(new FileWriter(filePath, false));
        } catch (IOException e) {
            throw new RuntimeException("Writer initialization failed", e);
        }
    }
    public void write(String text){
        try{
            writer.write(text);
            writer.flush();
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public void close(){
        try{
            if(writer != null){
                writer.close();
            }
        }
        catch (IOException e){
            throw new RuntimeException("Nie zamknięto writer'a");
        }
    }

    private static String getFileName(AbstractSwappingSortingAlgorithm<MarkedValue<Integer>> algorithm, Generator<Integer> generator) {
        String name = "";

        if (algorithm instanceof ShakerSort<MarkedValue<Integer>>) {
            name += "shaker_";
        } else if (algorithm instanceof InsertionSort<MarkedValue<Integer>>) {
            name += "insertion_";
        } else if (algorithm instanceof SelectionSort<MarkedValue<Integer>>) {
            name += "selection_";
        }

        if (generator instanceof OrderedIntegerArrayGenerator) {
            name += "ord_arr";
        } else if (generator instanceof ReversedIntegerArrayGenerator) {
            name += "rev_arr";
        } else if (generator instanceof RandomIntegerArrayGenerator) {
            name += "rand_arr";
        } else if (generator instanceof ShuffledIntegerArrayGenerator) {
            name += "shuff_arr";
        }

        return name + ".txt";
    }
}