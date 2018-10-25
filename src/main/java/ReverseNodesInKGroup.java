/**
 * Created by weishubin on 2018/10/25.
 */
public class ReverseNodesInKGroup {
    public ListNode reverseKGroup(ListNode head, int k) {
        int e = 0;
        ListNode tmp = head;
        while (tmp != null && e < k) {
            tmp = tmp.next;
            e++;
        }
        if (e < k) {
            return head;
        }

        int count = 0;
        ListNode last = head;
        ListNode current = head.next;
        while (count < k-1) {
            if (current == null) {
                break;
            }
            ListNode newNext = current.next;
            last.next = current.next;
            current.next = head;


            head = current;

            current = newNext;

            count++;
        }

        last.next = reverseKGroup(last.next, k);

        return head;
    }


    public static void main(String[] args) {
        int[] a = new int[]{1,2,3,4,5};
        ListNode head = new ListNode(a[0]);
        ListNode previous = head;
        for (int i = 1; i < a.length; i++) {
            ListNode listNode = new ListNode(a[i]);
            previous.next = listNode;
            previous = listNode;
        }

        ReverseNodesInKGroup s = new ReverseNodesInKGroup();
        ListNode result = s.reverseKGroup(head, 3);
        while (result != null) {
            System.out.print(result.val + " ");
            result = result.next;

        }
    }

}
