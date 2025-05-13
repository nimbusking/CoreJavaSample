package cc.nimbusk.string;

public class Sample2 {

    public static void combineArray(Object[] a1, Object[] a2) {
        Object[] result = new Object[a1.length + a2.length];
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i]);
        }
    }

    public static void main(String[] args) {
        Object[] a1 = {1,2,3,4};
        Object[] a2 = {"a","b","c", 1};
        combineArray(a1, a2);
    }
}
