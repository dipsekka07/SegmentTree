
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiFunction;

public class SegmentTree<T> {

    private final T[] A; // input
    private final T[] segTree;

    private BiFunction<T, T, T> operator;
    private T sentinel;

    public SegmentTree( Collection<T> iterable, BiFunction<T, T, T> operator) {
        int size = iterable.size();
        this.A = (T[]) new Object[size];
        int i=0;
        for(T t: iterable){
            this.A[i] = t;
            i++; 
        }
        segTree = (T[]) new Object[4*size];
        for(int t=0; t<4*size; t++) {
            this.segTree[i] = sentinel;
        }
        sentinel = (T) new Object();
        this.operator =  (x1, x2) -> {
            if (x1 == sentinel) return x2; 
            if (x2 == sentinel) return x1;
            return operator.apply(x1, x2);
          };
        buildSegmentTree(0, 0, size - 1);
    }

    private void buildSegmentTree(int pos, int lo, int hi) {
        if (lo == hi) {
            segTree[pos] = A[lo];
            return;
        }
        final int mid = lo + (hi - lo) / 2;
        buildSegmentTree( left(pos), lo, mid);
        buildSegmentTree( right(pos) , mid+1, hi );
        segTree[pos] = operator.apply(segTree[left(pos)], segTree[right(pos)]); 
    }

    public String getSegmentTree() {
        ArrayList<T> res = new ArrayList<>();
        for(int i=0; i<segTree.length; i++)
            res.add(segTree[i]);
        return res.toString();
    }

    private int left(int pos){
        return 2*pos + 1;
    }

    private int right(int pos){
        return 2*pos + 2;
    }

    public void update(int index, T newValue){
        updateSegmentTree(index, newValue, 0, 0, this.A.length-1);
    }

    private void updateSegmentTree(int index, T newValue,  int pos, int Rs, int Re) {
        if(Rs == Re){
            segTree[pos] = newValue;
            return;
        }
        int mid = (Rs + Re) / 2;
        if(index <= mid ){
            updateSegmentTree(index, newValue, left(pos), Rs, mid);
        }else{
            updateSegmentTree(index, newValue, right(pos), mid+1, Re);
        }
        
        segTree[pos] = operator.apply(segTree[left(pos)], segTree[right(pos)]); 
    }

    public T query(int Qs, int Qe){
        return querySegmentTree(Qs, Qe, 0, 0, this.A.length-1);
    }

    private T querySegmentTree(int Qs, int Qe, int pos, int Rs, int Re) {
        if(Qe < Rs || Re < Qs){
            return sentinel;
        }else if (Rs == Re){
            return segTree[pos];
        }else if( Qe >= Re && Qs <= Rs){
            return segTree[pos];
        }else{
            int mid = (Rs+Re)/2;
            T left = querySegmentTree(Qs, Qe, left(pos), Rs, mid);
            T right = querySegmentTree(Qs, Qe, right(pos), mid+1, Re);
            return operator.apply(left, right);
        }
    }

}