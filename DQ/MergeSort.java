import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class MergeSort
{
	public static void main (String[] args) throws java.lang.Exception
	{
		// Input from standard input e.g: 555 43 5 5 6 7 65 7 76 12 32 32 33 -12 3 43 0
		Scanner sc = new Scanner(System.in);
		String tokens[] =sc.nextLine().split(" ");
		int x[] = new int[tokens.length]; // integer array
		// populate the array from input tokens
		int k=0;
		for (String val : tokens) {
			if(val.trim().length()==0) {
				continue;
				}
			x[k++] = Integer.parseInt(val);
		}
		
		// call merge sort
		mergeSort(x,0,x.length-1);
		print(x);
	}

	private static void print(int[] x) {
		StringBuffer sb = new StringBuffer();
		for (int val : x) {
			sb.append(val+" ");
		}
		System.out.print(sb.toString());
	}

	/**
	 * merge sort
	 * @param array
	 * @param start
	 * @param end
	 */
	private static void mergeSort(int[] array, int start, int end) {
		// base case
		if(start>=end) {
			return;
		}
		// recursive case
		int mid = (start+end)/2;
		mergeSort(array,start,mid);
		mergeSort(array,mid+1,end);
		// merge 2 sections
		merge(array,start,mid,mid+1,end);
	}

	/**
	 * Merge 
	 * @param array
	 * @param startLeft
	 * @param endLeft
	 * @param startRight
	 * @param endRight
	 */
	private static void merge(int[] array, int startLeft, int endLeft, int startRight, int endRight) {
		// auxiliary list to store the sorted ele2ments 
		List<Integer> tmpList = new ArrayList<>();
		int l = startLeft, r = startRight;
		while(l<=endLeft && r<=endRight) {
			tmpList.add((array[l]<=array[r])?array[l++] : array[r++]);
		}
		// copy remaining elements
		while(l<=endLeft) {
			tmpList.add(array[l++]);
		}
		while(r<=endRight) {
			tmpList.add(array[r++]);
		}
		// copy back the sorted list to the array
		int k=startLeft;
		for (Integer val : tmpList) {
			array[k++]=val;
		}
	}
}
