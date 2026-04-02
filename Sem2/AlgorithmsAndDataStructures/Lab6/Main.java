import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Comparator;
import core.AbstractSwappingSortingAlgorithm;
import pivot.FirstElementPivotSelector;
import pivot.PivotSelector;
import pivot.RandomPivotSelector;
import testing.*;
import testing.comparators.*;
import testing.generation.*;
import testing.generation.conversion.*;

public class Main {

    public static void main(String[] args) {
        int[] sizes = {100,250,500,1000,2000,3000,5000};


        Comparator<MarkedValue<Integer>> markedComparator = new MarkedValueComparator<>(new IntegerComparator());

        Generator<MarkedValue<Integer>> orderedGenerator = new MarkingGenerator<>(new OrderedIntegerArrayGenerator());
        Generator<MarkedValue<Integer>> reversedGenerator = new MarkingGenerator<>(new ReversedIntegerArrayGenerator());
        Generator<MarkedValue<Integer>> randomGenerator = new MarkingGenerator<>(new RandomIntegerArrayGenerator(1000));
        Generator<MarkedValue<Integer>> shuffledGenerator = new MarkingGenerator<>(new ShuffledIntegerArrayGenerator());

        Generator<MarkedValue<Integer>> orderedLLGenerator = new LinkedListGenerator<>(new MarkingGenerator<>(new OrderedIntegerArrayGenerator()));
        Generator<MarkedValue<Integer>> reversedLLGenerator = new LinkedListGenerator<>(new MarkingGenerator<>(new ReversedIntegerArrayGenerator()));
        Generator<MarkedValue<Integer>> randomLLGenerator = new LinkedListGenerator<>(new MarkingGenerator<>(new RandomIntegerArrayGenerator(1000)));
        Generator<MarkedValue<Integer>> shuffledLLGenerator = new LinkedListGenerator<>(new MarkingGenerator<>(new ShuffledIntegerArrayGenerator()));

       testSort("MergeSort", markedComparator, orderedGenerator, orderedLLGenerator,  sizes, "ord");
        testSort("MergeSort", markedComparator, reversedGenerator, reversedLLGenerator, sizes, "rev");
        testSort("MergeSort", markedComparator, randomGenerator, randomLLGenerator, sizes, "rand");
        testSort("MergeSort", markedComparator, shuffledGenerator, shuffledLLGenerator, sizes, "shuff");

        testSort("QuickSortFirst", markedComparator, orderedGenerator, orderedLLGenerator,  sizes, "OrderedGenerator");
        testSort("QuickSortFirst", markedComparator, reversedGenerator, reversedLLGenerator, sizes, "ReversedGenerator");
        testSort("QuickSortFirst", markedComparator, randomGenerator, randomLLGenerator, sizes, "RandomGenerator");
        testSort("QuickSortFirst", markedComparator, shuffledGenerator, shuffledLLGenerator, sizes, "ShuffledGenerator");


        testSort("QuickSortRandom", markedComparator, orderedGenerator, orderedLLGenerator,  sizes, "ord");
        testSort("QuickSortRandom", markedComparator, reversedGenerator, reversedLLGenerator, sizes, "rev");
        testSort("QuickSortRandom", markedComparator, randomGenerator, randomLLGenerator, sizes, "rand");
        testSort("QuickSortRandom", markedComparator, shuffledGenerator, shuffledLLGenerator, sizes, "shuff");


    }

    private static void testSort(String sortType, Comparator<MarkedValue<Integer>> markedComparator, Generator<MarkedValue<Integer>> arrayGenerator, Generator<MarkedValue<Integer>> listGenerator,  int[] sizes, String generatorType) {

        String fileName ="";
        PivotSelector<MarkedValue<Integer>> firstElementPivotSelect = new FirstElementPivotSelector<>();
        PivotSelector<MarkedValue<Integer>> randomPivotSelect = new RandomPivotSelector<>();

        AbstractSwappingSortingAlgorithm<MarkedValue<Integer>> algorithm;
        if (sortType.equals("MergeSort")){
            fileName+="merge_";
            algorithm = new Mergesort<>(markedComparator);
        }
        else if (sortType.equals("QuickSortFirst")){
            algorithm = new Quicksort<>(markedComparator, firstElementPivotSelect);
            fileName+="quick_1piv_";
        }
        else{
            algorithm = new Quicksort<>(markedComparator, randomPivotSelect);
            fileName+="quick_randpiv_";
        }

            fileName+=generatorType+".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {

            writer.println("Size;Time;Stddev;Compare;Stddev;Swaps;Stddev");
            for (int size : sizes) {

                testing.results.swapping.Result result = Tester.runNTimes(algorithm, arrayGenerator, size, 50);
                String formattedOutput = String.format("%5d;%f;%f;%f;%f;%f;%f", size,
                        result.averageTimeInMilliseconds(), result.timeStandardDeviation(),
                        result.averageComparisons(), result.comparisonsStandardDeviation(),
                        result.averageSwaps(), result.swapsStandardDeviation());

                writer.println(formattedOutput.replace('.', ','));

            }
            for (int size : sizes) {

                testing.results.swapping.Result result = Tester.runNTimes(algorithm, listGenerator, size, 50);
                String formattedOutput = String.format("%5d;%f;%f;%f;%f;%f;%f", size,
                        result.averageTimeInMilliseconds(), result.timeStandardDeviation(),
                        result.averageComparisons(), result.comparisonsStandardDeviation(),
                        result.averageSwaps(), result.swapsStandardDeviation());

                writer.println(formattedOutput.replace('.', ','));

            }

            System.out.println("Zapisano wyniki do pliku " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
