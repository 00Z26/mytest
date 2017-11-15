public class Solution1 {
    public int[] twoSum(int[] nums, int target) {
        int[] ok = new int[2];
            for(int i = 0;i < nums.length-1;i++){
                for(int k = i + 1;k < nums.length;k++){
                    if(nums[i]+nums[k] == target){
                        ok[0] = i;
                        ok[1] = k;
                        return ok;
                    }
                }
            }
        return null;
    }
}
/**
 * leetcode第一题
 * Given nums = [2, 7, 11, 15], target = 9,
 Because nums[0] + nums[1] = 2 + 7 = 9,
 return [0, 1].
 如果用for-while-for 可不可以不用-1
 */
