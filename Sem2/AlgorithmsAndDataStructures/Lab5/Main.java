import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import core.AbstractSwappingSortingAlgorithm;
import testing.*;
import testing.comparators.*;
import testing.generation.*;
import testing.generation.conversion.*;
import testing.results.swapping.Result;

public class Main {
	private static final int Reps = 20;

	private static final List<AbstractSwappingSortingAlgorithm<MarkedValue<Integer>>> AlgorithmsLists = new ArrayList<>();
	private static final List<Generator<Integer>> GeneratorsLists = new ArrayList<>();
	private static final List<Integer> TestSizes = new ArrayList<>();


	public static void main(String[] args) {
		createLists();

		for (AbstractSwappingSortingAlgorithm<MarkedValue<Integer>> algorithm : AlgorithmsLists) {
			for (Generator<Integer> generator : GeneratorsLists) {
				run(algorithm, generator);
			}
		}
	}

	private static void run(AbstractSwappingSortingAlgorithm<MarkedValue<Integer>> algorithm, Generator<Integer> generator){
		Result result;
		Writer writer = new Writer(algorithm, generator);
		writer.write("testSize;avgTime;timeStdDev;avgSwap	s;swapsStdDev;avgComparisons;comparisonsStdDev\n");
		for(int testSize: TestSizes){
			result = Tester.runNTimes(algorithm, new MarkingGenerator<>(generator), testSize, Reps);
			String text = String.format("%d;%f;%f;%f;%f;%f;%f%n", testSize, result.averageTimeInMilliseconds(), result.timeStandardDeviation(),result.averageSwaps(), result.swapsStandardDeviation(), result.averageComparisons(), result.comparisonsStandardDeviation());
			writer.write(text);


			printStatistic("time [ms]", result.averageTimeInMilliseconds(), result.timeStandardDeviation());
			printStatistic("comparisons", result.averageComparisons(), result.comparisonsStandardDeviation());
			printStatistic("swaps", result.averageSwaps(), result.swapsStandardDeviation());

			System.out.println("always sorted: " + result.sorted());
			System.out.println("always stable: " + result.stable());
			System.out.println("\n "+algorithm + " "+generator);
		}
		writer.close();
	}

	private static void printStatistic(String label, double average, double stdDev) {
		System.out.println(label + ": " + double2String(average) + " +- " + double2String(stdDev));
	}

	private static String double2String(double value) {
		return String.format("%.12f", value);
	}

	private static void createLists(){
		Comparator<MarkedValue<Integer>> comparator = new MarkedValueComparator<>(new IntegerComparator());

		GeneratorsLists.add(new OrderedIntegerArrayGenerator());
		GeneratorsLists.add(new ReversedIntegerArrayGenerator());
		GeneratorsLists.add(new ShuffledIntegerArrayGenerator());
		GeneratorsLists.add(new RandomIntegerArrayGenerator(1000));

		AlgorithmsLists.add(new InsertionSort<>(comparator));
		AlgorithmsLists.add(new SelectionSort<>(comparator));
		AlgorithmsLists.add(new ShakerSort<>(comparator));

		TestSizes.add(10);
		TestSizes.add(100);
		TestSizes.add(250);
		TestSizes.add(500);
		TestSizes.add(1000);
		TestSizes.add(2000);
		TestSizes.add(3000);
		TestSizes.add(5000);
	}
}
