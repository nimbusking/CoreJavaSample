package cc.nimbusk.alg;

import org.junit.Test;

public class TestSortedAlgUtil {

    @Test
    public void testBubbleSort() {
        int[] a = { 5, 4, 3, 2, 1 };
        SortedAlgUtil.bubbleSort(a);
        for (int j : a) {
            System.out.println(j);
        }
    }

}
