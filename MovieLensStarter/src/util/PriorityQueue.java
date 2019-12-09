package util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A priority queue class implemented using a min heap.
 * Priorities cannot be negative.
 *
 * @author Harper Shapiro
 * @author Bret Abel
 * @version 9/30/19
 *
 */
public class PriorityQueue {

    protected Map<Integer, Integer> location; //Actual element value maps to index in heap
    protected List<Pair<Integer, Integer>> heap;

    /**
     *  Constructs an empty priority queue
     */
    public PriorityQueue() {
        location = new HashMap<Integer,Integer>();
        heap = new ArrayList<Pair<Integer,Integer>>();
    }

    /**
     *  Insert a new element into the queue with the
     *  given priority.
     *
     *	@param priority priority of element to be inserted
     *	@param element element to be inserted
     *	<br><br>
     *	<b>Preconditions:</b>
     *	<ul>
     *	<li> The element does not already appear in the priority queue.</li>
     *	<li> The priority is non-negative.</li>
     *	</ul>
     *
     */
    public void push(int priority, int element) {
        //check preconditions
        if(isPresent(element) || priority<0){
            if(priority<0){
                throw new AssertionError("Cannot add element to priority queue. Priority was less than 0");
            } else {
                throw new AssertionError("Cannot add element to priority queue. Element was present");

            }
        }

        //add new element, percolate up
        heap.add(new Pair(priority,element));
        location.put(element,size()-1);
        percolateUpLeaf();

    }

    /**
     *  Remove the highest priority element
     *  <br><br>
     *	<b>Preconditions:</b>
     *	<ul>
     *	<li> The priority queue is non-empty.</li>
     *	</ul>
     *
     */
    //MADE SOME CHANGES TO THE RETURN TYPE HERE. Returns the pair we removed instead of nothing
    public Pair pop(){
        //check preconditions
        if(isEmpty()){
            throw new AssertionError("Cannot pop from empty priority queue.");
        }
        //swap root with last element in heap
        swap(0,size()-1);

        //remove last element of heap (old root) and remake heap
        location.remove(heap.get(size()-1).element);
        Pair removal = heap.remove(size()-1);
        pushDownRoot();
        return removal;
    }


    /**
     *  Returns the highest priority in the queue
     *  @return highest priority value
     *  <br><br>
     *	<b>Preconditions:</b>
     *	<ul>
     *	<li> The priority queue is non-empty.</li>
     *	</ul>
     */

    public int topPriority() {
        //check preconditions
        if(isEmpty()){
            throw new AssertionError("Priority Queue is empty.");
        }

        //top priority is at root
        return heap.get(0).priority;
    }


    /**
     *  Returns the element with the highest priority
     *  @return element with highest priority
     *  <br><br>
     *	<b>Preconditions:</b>
     *	<ul>
     *	<li> The priority queue is non-empty.</li>
     *	</ul>
     */

    public int topElement() {
        //check preconditions
        if(isEmpty()){
            throw new AssertionError("Heap is empty.");
        }
        return heap.get(0).element;
        //return pair at root
    }


    /**
     *  Change the priority of an element already in the
     *  priority queue.
     *
     *  @param newpriority the new priority
     *  @param element element whose priority is to be changed
     *  <br><br>
     *	<b>Preconditions:</b>
     *	<ul>
     *	<li> The element exists in the priority queue</li>
     *	<li> The new priority is non-negative </li>
     *	</ul>
     */

    public void changePriority(int newpriority, int element) {
        //check preconditions
        if(!isPresent(element) || newpriority<0){
            throw new AssertionError("Cannot change priority.");
        }

        //change the priority in the heap
        int index = location.get(element);
        heap.get(index).priority = newpriority;

        //either push down or percolate up

        if(isLeaf(index)){
            percolateUp(index); //leaf case
        } else if (index==0){
            pushDownRoot(); //root case
        } else if(newpriority < heap.get(parent(index)).priority){
            percolateUp(index); //smaller than parent case
        } else if (hasTwoChildren(index) && (newpriority > heap.get(left(index)).priority || newpriority > heap.get(right(index)).priority)){
            pushDown(index); //has 2 children, larger than at least one case
        } else if (newpriority > heap.get(left(index)).priority){
            pushDown(index); //larger than only child case
        }

    }


    /**
     *  Gets the priority of the element
     *
     *  @param element the element whose priority is returned
     *  @return the priority value
     *  <br><br>
     *	<b>Preconditions:</b>
     *	<ul>
     *	<li> The element exists in the priority queue</li>
     *	</ul>
     */

    public int getPriority(int element) {
        //get the associated priority if preconditions satisfied
        if(isPresent(element)){
            int index = location.get(element);
            return heap.get(index).priority;
        } else {
            throw new AssertionError("Element not found");
        }
    }

    /**
     *  Returns true if the priority queue contains no elements
     *  @return true if the queue contains no elements, false otherwise
     */
    public boolean isEmpty() {
        return heap.size()==0;
    }

    /**
     *  Returns true if the element exists in the priority queue.
     *  @return true if the element exists, false otherwise
     */

    public boolean isPresent(int element) {
        return location.containsKey(element);
    }

    /**
     *  Removes all elements from the priority queue
     */

    public void clear() {
        heap.clear();
        location.clear();
    }

    /**
     *  Returns the number of elements in the priority queue
     *  @return number of elements in the priority queue
     */

    public int size() {
        return heap.size();
    }



    /*********************************************************
     * 				Private helper methods
     *********************************************************/


    /**
     * Push down the element at the given position in the heap
     * @param start_index the index of the element to be pushed down
     * @return the index in the list where the element is finally stored
     */

    private int pushDown(int start_index) {

        if(heap.size()<=1){
            return 0; //heap is only 1 element
        }
        //attempt to find a smallest child
        int currIndex = start_index;
        int currentPrio = heap.get(currIndex).priority;
        int smallChild = small(currIndex);

        //only try to find its priority if current index is not a leaf
        int smallChildPrio=0;
        if(smallChild!=-1) {
            smallChildPrio = heap.get(smallChild).priority;
        }

        //continue if MIN heap property not satisfied and we haven't reached the leaf layer
        while(!isLeaf(currIndex) && smallChildPrio<currentPrio) {
            //swap current element and child element
            swap(currIndex,smallChild);
            //update current index
            currIndex = smallChild;

            //only try to find smallest child if we haven't reached leaf layer
            if(!isLeaf(currIndex)) {
                smallChild = small(currIndex);
                smallChildPrio = heap.get(smallChild).priority;
            }

        }
        return currIndex;
    }

    /**
     * Percolate up the element at the given position in the heap
     * @param start_index the index of the element to be percolated up
     * @return the index in the list where the element is finally stored
     */

    private int percolateUp(int start_index) {
        int currIndex = start_index;
        int parent=parent(currIndex);
        int parentPrio = (heap.get(parent)).priority;
        int currentPrio = (heap.get(currIndex)).priority;
        //continue if MIN heap property not satisfied
        while(currentPrio<parentPrio){
            //swap element with parent
            swap(parent,currIndex);

            //update indices
            currIndex=parent;
            parent = parent(currIndex);
            parentPrio = (heap.get(parent)).priority;
            currentPrio = (heap.get(currIndex)).priority;
        }
        return currIndex;

    }


    /**
     * Swaps two elements in the priority queue by updating BOTH
     * the list representing the heap AND the map
     * @param i The index of the element to be swapped
     * @param j The index of the element to be swapped
     */

    private void swap(int i, int j) {
        //a simple swap in both heap and location map
        Pair<Integer,Integer> jPair = heap.get(j);
        Pair<Integer, Integer> iPair = heap.get(i);
        heap.set(j,iPair);
        heap.set(i,jPair);
        location.replace(iPair.element,j);
        location.replace(jPair.element,i);
    }

    /**
     * Computes the index of the element's left child
     * @param parent index of element in list
     * @return index of element's left child in list
     */

    private int left(int parent) {
        return 2*parent+1;
    }

    /**
     * Computes the index of the element's right child
     * @param parent index of element in list
     * @return index of element's right child in list
     */

    private int right(int parent) {
        return 2*parent+2;
    }

    /**
     * Computes the index of the element's parent
     * @param child index of element in list
     * @return index of element's parent in list
     */

    private int parent(int child) {
        return (child-1)/2;
    }

    /**
     * Computes the smaller of the two children
     * @param parent
     * @return the small child's index. -1 if parent is a leaf
     */
    private int small(int parent){
        //default to -1 if parent is a leaf
        int small=-1;
        int smallChildPrio;

        if(hasTwoChildren(parent)) {         //parent has two children
            int right = right(parent);
            int left = left(parent);
            int parentPrio = heap.get(parent).priority;
            int rightChildPrio = heap.get(right).priority;
            int leftChildPrio = heap.get(left).priority;

            //compare
            if (rightChildPrio < leftChildPrio) {
                smallChildPrio = rightChildPrio;
                small = right;
            } else {
                smallChildPrio = leftChildPrio;
                small = left;
            }
        } else {                            //parent has 1 child
            small = left(parent);
        }
        return small;
    }


    /*********************************************************
     * 	These are optional private methods that may be useful
     *********************************************************/


    /**
     * Push down the root element
     * @return the index in the list where the element is finally stored
     */

    private int pushDownRoot() {
        return pushDown(0);
    }

    /**
     * Percolate up the last leaf in the heap, i.e. the most recently
     * added element which is stored in the last slot in the list
     *
     * @return the index in the list where the element is finally stored
     */
    private int percolateUpLeaf(){
        return percolateUp(size()-1);
    }

    /**
     * Returns true if element is a leaf in the heap
     * @param i index of element in heap
     * @return true if element is a leaf
     */

    private boolean isLeaf(int i){
        //true iff i is in range and has no left child (and no right child by extension)
        return (i<size() && 2*i+1>=size());
    }

    /**
     * Returns true if element has two children in the heap
     * @param i index of element in the heap
     * @return true if element in heap has two children
     */

    private boolean hasTwoChildren(int i) {
        //checks if right child would be in range
        return (2*i+2 < size());

    }

    /**
     * Print the underlying list representation
     */
    private void printHeap() {
        for(int i=0;i<size();i++){
            System.out.println("{P: "+heap.get(i).priority + ", E: " + heap.get(i).element +"} ");
        }
        System.out.print("\n");
    }

    /**
     * Print the entries in the location map
     */
    private void printMap() {
        for(Map.Entry e : location.entrySet()){
            System.out.println("[E: "+ e.getKey() + " L: " + e.getValue() + "]");
        }
        System.out.print("\n");
    }

    public static void main(String[] args){
        //Some tests for our methods
        PriorityQueue prio = new PriorityQueue();
        prio.push(6,2);
        prio.push(4,4);
        prio.push(7,5);
        prio.push(1,9);
        prio.push(3,10);
        prio.push(8,20);
        //System.out.println(prio.isPresent(0));
        prio.printHeap();
        prio.printMap();
        System.out.println("Changing element 4 to priority 2...\n");
        prio.changePriority(2,4);
        prio.printHeap();
        prio.printMap();
        System.out.println("Is element 2 present? " + prio.isPresent(2));
        System.out.println("Is element 11 present? " + prio.isPresent(11));
        System.out.println("Top element: " + prio.topElement());
        System.out.println("Top priority: " + prio.topPriority());
        System.out.println("Priority of element 20: " + prio.getPriority(20));

    }


}
