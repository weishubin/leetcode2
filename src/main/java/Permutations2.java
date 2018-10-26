import java.util.*;

/**
 * 用回溯做的，感觉好麻烦了。
 * 网上有用深度优先做的很简洁
 * Created by weishubin on 2018/10/26.
 */
public class Permutations2 {
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if (nums == null || nums.length == 0) {
            return result;
        }

        Map<Integer, Count> countMap = new HashMap<Integer, Count>();
        for (int n : nums) {
            Count c = countMap.get(n);
            if (c == null) {
                c = new Count(0);
                countMap.put(n, c);
            }
            c.count++;
        }

        List<Integer> uniqueNumsList = new ArrayList<Integer>(countMap.keySet());//去重后排序
        Collections.sort(uniqueNumsList);

        int[] aAnswer = new int[nums.length];  //存的索引值
        Arrays.fill(aAnswer, -1);

        //记录回溯过程中使用的数字是否被使用了
        Map<Integer, Count> countMapUsed = new HashMap<Integer, Count>();
        for (Map.Entry<Integer, Count>  c : countMap.entrySet()) {
            countMapUsed.put(c.getKey(), new Count(c.getValue().count));
        }

        int index = 0;
        while (index >= 0) {
            if (index == nums.length) { //找到了一个答案
                List<Integer> answer = new ArrayList<Integer>(nums.length);
                for (int v : aAnswer) {
                    answer.add(uniqueNumsList.get(v));
                }
                result.add(answer);

                index--;

                continue;
            }

            int pos = aAnswer[index];
            if (pos == uniqueNumsList.size() - 1) { //已经尝试完了，回退
                int posValue = uniqueNumsList.get(aAnswer[index]); //当前位置使用的值

                countMapUsed.get(posValue).count++; //这个值重新可用

                index--;

                continue;
            }



            int posValueIndex = aAnswer[index];
            if (posValueIndex >= 0) {
                int posValue = uniqueNumsList.get(aAnswer[index]); //当前位置使用的值
                countMapUsed.get(posValue).count++; //这个值重新可用
            }
            boolean find = false;
            for (int i =  posValueIndex + 1; i < uniqueNumsList.size(); i++) { //找下一个可用的值
                int num = uniqueNumsList.get(i);
                if (countMapUsed.get(num).count > 0) {



                    aAnswer[index] = i;
                    countMapUsed.get(num).count--; //这个值不再能用

                    index++;
                    if (index < nums.length) {
                       aAnswer[index] = -1;
                    }

                    find = true;

                    break;
                }
            }
            if (!find) {
               index--;
            }
        }

        return result;

    }

    static class Count {
        Count(int c) {
            this.count = c;

        }
        int count = 0;
    }

    public static void main(String[] args) {
        Permutations2 p = new Permutations2();
        int[] nums = new int[] {1,1,2};
        List<List<Integer>> result = p.permuteUnique(nums);
        for (List<Integer> a : result) {
            for (Integer b : a) {
                System.out.print(b + " ");
            }
            System.out.println();
        }
    }

}
