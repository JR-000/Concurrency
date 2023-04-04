import java.util.Random;
import java.util.*;

class Concurrency extends Thread {

	private int[] arr;
	private int low, high, partial;

	public Concurrency(int[] arr1, int lowNum, int highNum){

		this.arr = arr1;
		this.low = lowNum;
		this.high = Math.min(highNum, arr1.length);
	}

	public int getPartialSum(){
		return partial;
	}

	@Override
	public void run(){
		partial = sum(arr, low, high);
	}

	public static int sum(int[] arr)	{
		return sum(arr, 0, arr.length);
	}

	public static int sum(int[] arr, int low, int high)	{
		int total = 0;
		for (int i = low; i < high; i++) {
			total += arr[i];
		}
		return total;
	}

	public static int parallelSum(int[] arr)	{
		return parallelSum(arr, Runtime.getRuntime().availableProcessors());
	}

	public static int parallelSum(int[] arr, int threads)
	{
		int size = (int) Math.ceil(arr.length * 1.0 / threads);
		Concurrency[] sums = new Concurrency[threads];
		for (int i = 0; i < threads; i++) {
			sums[i] = new Concurrency(arr, i * size, (i + 1) * size);
			sums[i].start();
		}
		try {
			for (Concurrency sum : sums) {
				sum.join();
			}
		} catch (InterruptedException e) {
		}
		int total = 0;
		for (Concurrency sum : sums) {
			total += sum.getPartialSum();
		}
		return total;
	}

	public static void main(String[] args) {
		Random rand = new Random();
		int[] arr = new int[167000000];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = rand.nextInt(10) + 1;
	}

		long start = System.currentTimeMillis();
		System.out.println(Concurrency.sum(arr));
		System.out.println("Single: " + (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();
		System.out.println(Concurrency.parallelSum(arr));
		System.out.println("Parallel: " + (System.currentTimeMillis() - start));
	}

}
