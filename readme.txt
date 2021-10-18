/* *****************************************************************************
 *  Name:     Devin Plumb
 *  NetID:    dplumb
 *  Precept:  P06
 *
 *  Hours to complete assignment (optional): 7
 *
 **************************************************************************** */

Programming Assignment 5: Kd-Trees


/* *****************************************************************************
 *  Describe the Node data type you used to implement the
 *  2d-tree data structure.
 **************************************************************************** */

    I stored the point, the value, the axis-aligned rectangle for the point, and
    pointers to the right+left nodes as instance variables in the private class
    I used for the data type. The Node constructor takes in a point, a value,
    and a rectangle as arguments (these are the values it assigns to the
    corresponding instance variables).

/* *****************************************************************************
 *  Describe your method for range search in a kd-tree.
 **************************************************************************** */

    I used a private helper function to perform recursion, with the public
    function calling it at the root node. In the helper function, I use a series
    of Queues (one for points that will be returned and one for each of the left
    and right subtrees). If the search range rectangle intersects with the
    axis-aligned rectangle corresponding to the point, I examine that node's
    subtree, otherwise I leave it. If I examine the subtree, I check if the
    search rectangle contains the point, and if so enqueue that point. Lastly,
    I enqueue all points in the left and right subtree range search queues.

/* *****************************************************************************
 *  Describe your method for nearest neighbor search in a kd-tree.
 **************************************************************************** */

    I used a private helper function to perform recursion through Nodes, with
    the public function calling it at the root node. If the distance between the
    Node's point and the search point is less than the smallest distance
    previously found, that Node replaces the previous nearest neighbor Node.
    Next, I check the subtree(s) whose root node rectangles contain p. Finally,
    I check the subtree(s) whose root node rectangles do not contain p in order
    of the minimum distance between those rectangles and p, updating the minimum
    distance as we do (potentially cutting out steps).

/* *****************************************************************************
 *  How many nearest-neighbor calculations can your PointST implementation
 *  perform per second for input1M.txt (1 million points), where the query
 *  points are random points in the unit square?
 *
 *  Show the raw data you used to determine the operations per second.
 *  Use at least 1 second of CPU time for each data point.
 *  (Do not count the time to read in the points or to build the 2d-tree.)
 *
 *  Repeat the question but with your KdTreeST implementation.
 *
 *  Fill in the table below, using one digit after the decimal point
 *  for each entry.
 **************************************************************************** */


                 calls to          CPU time         calls to nearest()
                 nearest()         (seconds)        per second
                ------------------------------------------------------
PointST:         1000000            3.8                263365.8

KdTreeST:          100             10.0                    10.0




/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */

    None.

/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */

    None.

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */

    None.

/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */

    N/A

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on  how helpful the class meeting was and on how much you learned
 * from doing the assignment, and whether you enjoyed doing it.
 **************************************************************************** */

    None.
