// iterates through an array and returns the sum of the values
public class IterateSum {
    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println(arrSum(arr));
    }

    private static int arrSum(int[] arr){
        int sum = 0;
        for (int x: arr){
            sum += x;
        }
        return sum;
    }
}
