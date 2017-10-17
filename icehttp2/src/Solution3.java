import java.util.HashSet;

public class Solution3 {
    public int lengthOfLongestSubstring(String s) {
        char[] sub = s.toCharArray();
        int phase = sub.length ,temp ;
        Set set = new Set();
        for(int i = 0;i < sub.length-1;i++){
            for(int n = i + 1;n < sub.length;n++){
                for(int k = i;k < n;k++){
                    Character ch = s.charAt(i);
                    if (set.contains(ch)) break;
                    else {
                        phase = n;
                        if(sub[i] == sub[n]) {
                            temp = n-i;
                            if(temp > phase) phase = temp;
                        }
                    }
                    set.add(ch);
                }
            }
        }
        return phase;
    }
}

/**
 *条件判断找相同的两个，记录索引值，从这个索引值记录到下一个相同两个的索引，截取字符串返回长度。
 * 应该是下一个字符出现了与之前一样的字符就直接截取字符串
 * 记录每次出现与之前重复的字符的位置
 * 上面的程序返回值是1  错误应该是在 把两个相邻的b相减后给返回了 要做的是 判断两个相距最近的相同字符间的距离 并返回最大值 set类？
 * emmmmmmm,you win.
 * @author:620
 **/