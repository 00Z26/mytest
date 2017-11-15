
public class Solution2 {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
       ListNode newHead = new ListNode(0);
        ListNode a = l1,b = l2,c = newHead;
        int flo = 0;
        while(a != null&&b != null) {
            int sum = b.val + a.val + flo;
            if(sum>=10)
            {
                sum = sum-10;
                c.next = new ListNode(sum);
                flo = 1;
            }else{
                c.next = new ListNode(sum);
            }
            c = c.next; //我也不懂为啥加上它就可以有三个值不加就一个值 是因为如果不加上三个值都赋值到这一个地方了吗= =
            a = a.next;
            b = b.next;
        }
        if (flo != 0) c.next = new ListNode(1);
        return newHead.next;
    }
}
/**
 * 各项相加
 * 此项比10大的话此项减去10下一项加1
 * 然后最后一项要是大于10的话要再有一个next存入值1
 * 头结点重新赋值给另一个，最后再返回头结点，可以返回多个emmmm
 */
