/**
 * 找出这样的序列
 * a(i), a(i+1),..a(k).,a(n)
 * a(i)<a(i+1),a(i+1)往后都是倒序，其中a(k)是比a(i)大的最小值
 * 然后a(i)跟a(k)互换，从a(+1)到a(n)反转
 *
 * 目前最佳答案，算法复杂度O(N)
 * Created by weishubin on 2018/10/26.
 */
public class NextPermutation {
    public void nextPermutation(int[] nums) {
        if (nums == null || nums.length == 1) {
            return;
        }


        //从后开始找，找出第一个比后边的数小的
        int i = nums.length - 2 ;
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            i--;
        }

        //
        if (i >= 0 && nums[i] < nums[i+1]) {
            //找出比它i大的最小数
            int j = i + 1;
            while (j < nums.length - 1 && nums[j+1] > nums[i]) {
               j++;
            }
            //调换位置
            int tmp = nums[i];
            nums[i] = nums[j];
            nums[j] = tmp;
        }

        //从i+1反转
        int left = i+1;
        int right = nums.length - 1;
        while (right > left) {
            int tmp = nums[left];
            nums[left] = nums[right];
            nums[right] = tmp;
            left++;
            right--;
        }

    }

    public static void main(String[] args) {
        int[] nums = new int[] {1,5,4,3};
        NextPermutation s = new NextPermutation();
        s.nextPermutation(nums);
        for (int a : nums) {
            System.out.print(a + " ");
        }
    }
}
