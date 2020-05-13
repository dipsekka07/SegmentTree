import java.util.function.BiFunction;

public class SegmentTree<T> {

    private final T[] A; // input
    private final T[] segTree;

    private BiFunction<T, T, T> operator;
    private Object sentinel = new Object();

    public SegmentTree( T[] A, BiFunction<T, T, T> func) {
        this.A = A;
        final int size = A.length;
        segTree = (T[]) new Object[size];
        this.operator =  (x1, x2) -> {
            if (x1 == sentinel) return x2; 
            if (x2 == sentinel) return x1;
            return func.apply(x1, x2);
          };;
        buildSegmentTree(0, 0, size - 1);
    }

    public void buildSegmentTree(int pos, int lo, int hi) {
        if (lo == hi) {
            segTree[pos] = A[lo];
            return;
        }
        final int mid = lo + (hi - lo) / 2;
        buildSegmentTree(2*pos+1, lo, mid);
        buildSegmentTree(2*pos+1, lo, mid );
        segTree[pos] = operator.apply(segTree[2*pos+1], segTree[2*pos+2]); 
    }

    public void update() {

    }

    public T query(int Qs, int Ql, int pos, int Rs, int Rl) {
        if(Ql < Rs || Rl < Qs){
            return (T)sentinel;
        }else if (Rs == Rl){
            return segTree[pos];
        }else if( Ql <= Rl && Qs >= Rs){
            return segTree[pos];
        }else{
            int mid = (Rs+Rl)/2;
            T left = query(Qs, Ql, 2*pos+1, Rs, mid);
            T right = query(Qs, Ql, 2*pos+2, mid+1, Rl);
            return operator.apply(left, right);
        }
    }

}