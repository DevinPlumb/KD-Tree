/* *****************************************************************************
 *  Name:    Devin Plumb
 *  NetID:   dplumb
 *  Precept: P06
 *
 *  Description:  A mutable data type that uses a 2d-tree to represent a
 *                symbol table whose keys are two-dimensional points.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class KdTreeST<Value> {

    private Node root; // root of tree
    private int size; // number of nodes in tree

    private class Node {
        private final Point2D p; // sorted by key
        private Value val; // associated data
        private final RectHV rect; // axis-aligned rectangle corresponds to node
        private Node left, right;  // left and right subtrees

        // data type for node in the 2d tree
        public Node(Point2D p, Value val, RectHV rect) {
            this.p = p;
            this.val = val;
            this.rect = rect;
        }
    }

    // construct an empty symbol table of points
    public KdTreeST() {
        root = null;
        size = 0;
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points
    public int size() {
        return size;
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null)
            throw new IllegalArgumentException("first arg to put() null");
        if (val == null)
            throw new IllegalArgumentException("second arg to put() null");
        RectHV rect = new RectHV(Double.NEGATIVE_INFINITY,
                                 Double.NEGATIVE_INFINITY,
                                 Double.POSITIVE_INFINITY,
                                 Double.POSITIVE_INFINITY);
        root = put(root, p, val, rect, true);
    }

    // associate the value val with point p for a specified subtree
    private Node put(Node k, Point2D p, Value val, RectHV rect, boolean xOrY) {
        if (k == null) {
            size++;
            return new Node(p, val, rect);
        }
        if (p.equals(k.p)) {
            k.val = val;
            return k;
        }
        RectHV newRect;
        if (xOrY && p.x() - k.p.x() < 0) {
            newRect = new RectHV(rect.xmin(), rect.ymin(), k.p.x(), rect.ymax());
            k.left = put(k.left, p, val, newRect, false);
        }
        else if (xOrY && p.x() - k.p.x() >= 0) {
            newRect = new RectHV(k.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            k.right = put(k.right, p, val, newRect, false);
        }
        else if (!xOrY && p.y() - k.p.y() < 0) {
            newRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), k.p.y());
            k.left = put(k.left, p, val, newRect, true);
        }
        else {
            newRect = new RectHV(rect.xmin(), k.p.y(), rect.xmax(), rect.ymax());
            k.right = put(k.right, p, val, newRect, true);
        }
        return k;
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("arg to get() null");
        return get(root, p, true);
    }

    // value associated with point p for a specified subtree
    private Value get(Node k, Point2D p, boolean xOrY) {
        if (k == null) return null;
        if (p.equals(k.p)) {
            return k.val;
        }
        double cmp;
        if (xOrY)
            cmp = p.x() - k.p.x();
        else
            cmp = p.y() - k.p.y();
        if (cmp < 0) return get(k.left, p, !xOrY);
        else return get(k.right, p, !xOrY);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("arg to contains() null");
        return get(p) != null;
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        Queue<Point2D> queue1 = new Queue<Point2D>();
        Queue<Node> queue2 = new Queue<Node>();
        Queue<Node> queue3 = new Queue<Node>();

        if (root != null)
            queue2.enqueue(root);

        while (!queue2.isEmpty()) {
            for (Node i : queue2) {
                if (i.left != null)
                    queue3.enqueue(i.left);
                if (i.right != null)
                    queue3.enqueue(i.right);
            }
            while (!queue2.isEmpty())
                queue1.enqueue(queue2.dequeue().p);
            while (!queue3.isEmpty())
                queue2.enqueue(queue3.dequeue());
        }

        return queue1;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("arg to range() null");
        return range(root, rect);
    }

    // all points that are inside the rectangle (or on the boundary) within a
    // specified subtree
    private Queue<Point2D> range(Node k, RectHV rect) {
        Queue<Point2D> queue = new Queue<Point2D>();

        if (k == null) return queue;

        if (rect.intersects(k.rect)) {
            if (rect.contains(k.p))
                queue.enqueue(k.p);
            Queue<Point2D> queueLeft = range(k.left, rect);
            Queue<Point2D> queueRight = range(k.right, rect);
            for (Point2D i : queueLeft)
                queue.enqueue(i);
            for (Point2D i : queueRight)
                queue.enqueue(i);
        }

        return queue;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("arg to nearest() null");
        if (root == null)
            return null;
        return nearest(root, p, root.p);
    }

    // a nearest neighbor of point p within a specified subtree; null if the
    // subtree is empty
    private Point2D nearest(Node k, Point2D p, Point2D min) {

        if (k == null)
            return null;

        if (p.distanceSquaredTo(k.p) < p.distanceSquaredTo(min)) {
            min = k.p;
        }

        if (k.left != null && k.left.rect.contains(p)) {
            Point2D left = nearest(k.left, p, min);
            if (left != null &&
                    p.distanceSquaredTo(left) < p.distanceSquaredTo(min))
                min = left;
        }

        if (k.right != null && k.right.rect.contains(p)) {
            Point2D right = nearest(k.right, p, min);
            if (right != null &&
                    p.distanceSquaredTo(right) < p.distanceSquaredTo(min))
                min = right;
        }

        Node third = null;
        Node fourth = null;
        if (k.left != null && !k.left.rect.contains(p) &&
                k.left.rect.distanceSquaredTo(p) < min.distanceSquaredTo(p)) {
            third = k.left;
        }
        if (k.right != null && !k.right.rect.contains(p) &&
                k.right.rect.distanceSquaredTo(p) < min.distanceSquaredTo(p)) {
            fourth = k.right;
        }
        if (third != null && fourth != null
                && k.right.rect.distanceSquaredTo(p)
                < k.left.rect.distanceSquaredTo(p)) {
            third = fourth;
            fourth = k.left;
        }
        if (third != null &&
                third.rect.distanceSquaredTo(p) < min.distanceSquaredTo(p)) {
            Point2D thirdPoint = nearest(third, p, min);
            if (thirdPoint != null &&
                    p.distanceSquaredTo(thirdPoint) < p.distanceSquaredTo(min))
                min = thirdPoint;
        }
        if (fourth != null &&
                fourth.rect.distanceSquaredTo(p) < min.distanceSquaredTo(p)) {
            Point2D fourthPoint = nearest(fourth, p, min);
            if (fourthPoint != null &&
                    p.distanceSquaredTo(fourthPoint) < p.distanceSquaredTo(min))
                min = fourthPoint;
        }

        return min;

    }

    // unit testing (required)
    public static void main(String[] args) {

        KdTreeST<Integer> st = new KdTreeST<Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D key = new Point2D(x, y);
            st.put(key, i);
        }
        for (Point2D s : st.points())
            StdOut.println(s + " " + st.get(s));
        StdOut.println("size: " + st.size());
        StdOut.println();

        Point2D origin = new Point2D(0, 0);
        RectHV rectangle = new RectHV(1, 1, 2, 2);
        StdOut.println(st.contains(origin));
        StdOut.println(st.nearest(origin));
        Iterable<Point2D> range = st.range(rectangle);
        for (Point2D s : range)
            StdOut.println(s + " " + st.get(s));

    }
}
