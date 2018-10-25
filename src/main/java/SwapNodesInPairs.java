/**
 * Created by weishubin on 2018/10/25.
 */
public class SwapNodesInPairs {
    public ListNode swapPairs(ListNode head) {
        if (head == null) {
            return head;
        }


        ListNode emptyNode = new ListNode(0);
        emptyNode.next = head;
        ListNode result = head;
        if (head.next != null) {
            result = head.next;
        }

        ListNode third = head;
        ListNode first = null;
        ListNode second = null;
        ListNode previous = emptyNode;
        while (true) {
             first = third;
             if (first == null) {
                 break;
             }
             second = third.next;
             if (second == null) {
                 break;
             }
             third = second.next;

            first.next = third;
            second.next = first;
            previous.next = second;

            previous = first;
        }

        return result;
    }

    public static void main(String[] args) {
        int[] a = new int[]{1,2,3};
        ListNode head = new ListNode(a[0]);
        ListNode previous = head;
        for (int i = 1; i < a.length; i++) {
            ListNode listNode = new ListNode(a[i]);
            previous.next = listNode;
            previous = listNode;
        }

        SwapNodesInPairs s = new SwapNodesInPairs();
        ListNode result = s.swapPairs(head);
        while (result != null) {
            System.out.print(result.val + " ");
            result = result.next;

        }
    }
}
