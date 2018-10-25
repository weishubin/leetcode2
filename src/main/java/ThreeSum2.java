import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 时间复杂度太高
 * Created by weishubin on 2018/10/24.
 */
public class ThreeSum2 {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if (nums == null || nums.length < 3) {
            return result;
        }


        Arrays.sort(nums);

        int zeroIndex = Arrays.binarySearch(nums, 0, nums.length, 0);
        boolean existZero = true;
        if (zeroIndex < 0) {
            zeroIndex = -zeroIndex-1;
            existZero = false;
        }

        if (existZero) {
            int left = zeroIndex;
            while (left >= 0 && nums[left] == 0) {
                left--;
            }
            int right = zeroIndex;
            while (right < nums.length && nums[right] == 0) {
                right++;
            }
            if (right - left >= 4) {
                List<Integer> aAnswer = new ArrayList<Integer>(3);
                aAnswer.add(0);
                aAnswer.add(0);
                aAnswer.add(0);
                result.add(aAnswer);
            }


            int i = 0;
            while (i <= nums.length - 1) {
               int first = nums[i] ;
               if (first >= 0) {
                   break;
               }
               int target = 0 - first;
               int index = Arrays.binarySearch(nums, Math.min(zeroIndex, nums.length - 1), nums.length, target);
               if (index >= 0) {
                   List<Integer> aAnswer = new ArrayList<Integer>(3);
                   aAnswer.add(first);
                   aAnswer.add(0);
                   aAnswer.add(target);
                   result.add(aAnswer);
               }

               while (i < nums.length && nums[i] == first) {
                   i++;
               }
            }

        }

        //找负数
        int i = 0;
        while (i < nums.length - 1) {
//            System.out.println("<=0i:" + i);
            int first = nums[i];
            if (first >= 0) {
                break;
            }
            int j = i + 1;
            while (j < nums.length) {
//                System.out.println("<=0j:" + j);
                int secord = nums[j];
                if (secord >= 0) {
                    break;
                }

                int target = 0 - first - secord;
                int index = Arrays.binarySearch(nums, Math.min(zeroIndex, nums.length - 1), nums.length, target);
                if (index >= 0) {
                    List<Integer> aAnswer = new ArrayList<Integer>(3);
                    aAnswer.add(first);
                    aAnswer.add(secord);
                    aAnswer.add(target);
                    result.add(aAnswer);
                }
                while (j < nums.length && nums[j] == secord) {
                    j++;
                }
            }
            while (i < nums.length && nums[i] == first) {
                i++;
            }
        }

        //找正数
        i = 0;
        while (i < nums.length && nums[i] <= 0) {
                i++;
        }
        while (i < nums.length - 1) {
//            System.out.println(">=0i:" + i);
            int first = nums[i];
            int j = i + 1;
            while (j < nums.length) {
//                System.out.println(">=0j:" + i);
                int secord = nums[j];

                int target = 0 - first - secord;
                int index = Arrays.binarySearch(nums, 0, Math.min(zeroIndex + 1, nums.length), target);
                if (index >= 0) {
                    List<Integer> aAnswer = new ArrayList<Integer>(3);
                    aAnswer.add(target);
                    aAnswer.add(first);
                    aAnswer.add(secord);
                    result.add(aAnswer);
                }
                while (j < nums.length && nums[j] == secord) {
                    j++;
                }
            }
            while (i < nums.length && nums[i] == first) {
                i++;
            }
        }



        return result;
    }

    public static void main(String[] args) {
//        int[] nums = new int[]{0,0,0};
//        int[] nums = new int[]{3,0,-2,-1,1,2};
//        int[] nums = new int[]{-4,-2,-2,-2,0,1,2,2,2,3,3,4,4,6,6};
//        int[] nums = new int[]{-4,-1,-4,0,2,-2,-4,-3,2,-3,2,3,3,-4};
//        int[] nums = new int[]{-1, 0, 1, 2, -1, -4};
//        int[] nums = new int[]{-1, 0, 1, 2, -1, -4};
//        int[] nums = new int[]{-4,-2,-2,-2,0,1,2,2,2,3,3,4,4,6,6};
//        [[-10,4,6],[-8,-1,9],[-6,-3,9],[-6,-2,8],[-5,-4,9],[-5,-3,8],[-5,-1,6],[-4,-2,6],[-3,-1,4],[-2,-2,4]]
        int[] nums = new int[]{6,-5,-6,-1,-2,8,-1,4,-10,-8,-10,-2,-4,-1,-8,-2,8,9,-5,-2,-8,-9,-3,-5};
//        int[] nums = new int[]{-2,-4,-2};
        ThreeSum2 s = new ThreeSum2();
        List<List<Integer>>  r = s.threeSum(nums);
        System.out.println(r);
    }
}
