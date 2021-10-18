/* *****************************************************************************
 *  Name:    Devin Plumb
 *  NetID:   dplumb
 *  Precept: P06
 *
 *  Description:  A mutable data type that uses a red-black BST to represent a
 *                symbol table whose keys are two-dimensional points.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

public class PointST<Value> {

    private final RedBlackBST<Point2D, Value> st; // RBBST that holds points

    // construct an empty symbol table of points
    public PointST() {
        st = new RedBlackBST<Point2D, Value>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return st.isEmpty();
    }

    // number of points
    public int size() {
        return st.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null)
            throw new IllegalArgumentException("first arg to put() null");
        if (val == null)
            throw new IllegalArgumentException("second arg to put() null");
        st.put(p, val);
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("arg to get() null");
        return st.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("arg to contains() null");
        return get(p) != null;
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        if (isEmpty()) return new Queue<Point2D>();
        return st.keys();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("arg to range() null");
        Queue<Point2D> queue = new Queue<Point2D>();
        Iterable<Point2D> structure = points();
        for (Point2D i : structure) {
            if (rect.contains(i)) {
                queue.enqueue(i);
            }
        }
        return queue;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("arg to nearest() null");
        if (st.isEmpty()) return null;
        double minDist = Double.POSITIVE_INFINITY;
        Point2D nearest = null;
        Iterable<Point2D> structure = points();
        for (Point2D i : structure) {
            if (p.distanceSquaredTo(i) < minDist) {
                minDist = p.distanceSquaredTo(i);
                nearest = i;
            }
        }
        return nearest;
    }

    // unit testing (required)
    public static void main(String[] args) {

        PointST<Integer> st = new PointST<Integer>();
        int n = Integer.parseInt(args[0]);

        for (int i = 0; !StdIn.isEmpty(); i++) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D key = new Point2D(x, y);
            st.put(key, i);
        }

        Stopwatch stopwatch = new Stopwatch();

        int k = 0;

        for (int i = 0; i < n; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            Point2D query = new Point2D(x, y);
            st.nearest(query);
        }

        StdOut.println("elapsed time: " + stopwatch.elapsedTime());
    }
}
