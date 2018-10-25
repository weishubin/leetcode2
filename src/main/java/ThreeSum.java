import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 答案不对
 * Created by weishubin on 2018/10/24.
 */
public class ThreeSum {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if (nums == null || nums.length < 3) {
            return result;
        }
        Arrays.sort(nums);


        boolean[][] flags  = new boolean[nums.length][nums.length ];

        threeSum(nums, 0, nums.length - 1, result, flags);

        return result;
    }

    public static void threeSum(int[] nums, int left, int right, List<List<Integer>> result, boolean[][] flags) {

        while (right - left >= 2) {
            if (flags[left][right]) {
                return;
            } else {
                flags[left][right] = true;
            }


            int twoSum = nums[left] + nums[right];
            int taget = 0 - twoSum;

            if (right - left == 2) {
                if (twoSum + nums[left + 1] == 0) {
                    List<Integer> aAnswer = new ArrayList<Integer>(3);
                    aAnswer.add(nums[left]);
                    aAnswer.add(nums[left+1]);
                    aAnswer.add(nums[right]);
                } else {
                    break;
                }
            }

            int innerLeftIndex = left + 1;
            int innerRightIndex = right - 1;
            int index = binarySearch(nums, innerLeftIndex, innerRightIndex, taget);
            if (index == -1) {  //要找的比最小的还小，说明最右的太大了，已无法匹配，需淘汰
                right--;
                continue;
            } else if (index == -2) { //要找的比最大的还大，说明最左的太小了，已无法匹配，需淘汰
                left++;
                continue;
            } else if (index >= 0) {
                List<Integer> aAnswer = new ArrayList<Integer>(3);
                aAnswer.add(nums[left]);
                aAnswer.add(nums[index]);
                aAnswer.add(nums[right]);
                System.out.println(left + "-" + index + "-" + right);
                result.add(aAnswer);

                int newLeft = left;
                while(newLeft < nums.length && nums[newLeft] == nums[left]) {
                    flags[newLeft][right]=true;
                    newLeft++;
                }
                threeSum(nums, newLeft, right, result, flags);

                int newRight = right;
                while(newRight >= 0 && nums[newRight] == nums[right]) {
                    flags[left][newRight]=true;
                    newRight--;
                }
                threeSum(nums, left, newRight, result, flags);

                break;
            } else {
                threeSum(nums, left, right - 1, result, flags);
                threeSum(nums, left + 1, right, result, flags);
                break;
            }
        }
    }


    public static int binarySearch(int[] nums, int leftEdge, int rightEdge, int target) {
        if (target < nums[leftEdge]) {
            return -1;
        }
        if (target > nums[rightEdge]) {
            return -2;
        }
        int l = leftEdge;
        int r = rightEdge;
        if (nums[leftEdge] == target) {
            return leftEdge;
        }
        if (nums[rightEdge] == target) {
            return rightEdge;
        }

        while (l <= r) {
            int mid = (l + r) /2;
            int nMid = nums[mid];
            if (nMid == target) {
                return mid;
            }
            if (nMid < target) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return -3;
    }

    public static void main(String[] args) {
//        int[] nums = new int[]{0,0,0};
//        int[] nums = new int[]{3,0,-2,-1,1,2};
//        int[] nums = new int[]{-4,-2,-2,-2,0,1,2,2,2,3,3,4,4,6,6};
        int[] nums = new int[]{-4,-1,-4,0,2,-2,-4,-3,2,-3,2,3,3,-4};
        ThreeSum s = new ThreeSum();
        List<List<Integer>>  r = s.threeSum(nums);
        System.out.println(r);
    }
}
