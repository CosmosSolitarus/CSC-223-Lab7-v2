package utilities;

/**
 * linked list
 * @author Jackson Tedesco, Tony Song, and Della Avent
 * @date 2/8/2024
 * @param <T>
 */
public class LinkedList<T> {
	protected Node _head;
	protected Node _tail;
	protected int _size;

	/**
	 * class structure for a single linked node
	 */
	private class Node{
		protected Node _next;
		protected T _item;

		/**
		 * Node constructor
		 * @param item
		 * @param next: next node
		 */
		public Node(T item, Node next) {
			_next = next;
			_item = item;
		}
	}


	/**
	 * default constructor
	 */
	public LinkedList() {
		_tail = new Node(null, null);
		_head = new Node(null, _tail);
		_size = 0;
	}

	/**
	 * @return true if LinkedList is empty; False otherwise
	 */
	public boolean isEmpty() {
		return _head._next == _tail;
	}

	/**
	 * clears the LinkedList
	 */
	public void clear() {
		_head._next = _tail;
		_size = 0;
	}

	/**
	 * @return size of LinkedList
	 */
	public int size() {
		return _size;
	}

	/**
	 * Add element to front of LinkedList
	 * @param element: element to be added
	 */
	public void addToFront(T element) {
		_head._next = new Node(element, _head._next);
		_size++;
	}

	/** 
	 * returns the element at the specified index
	 * @param index
	 * @return element, null if index > size
	 */
	public T get(int index) {
		if(isEmpty()) return null;

		Node n = _head._next;

		for (int i = 0; i < index && n != _tail; n = n._next, i++)
		{}

		return n._item;
	}

	/**
	 * @param target: item being searched for
	 * @return True if LinkedList contains target; False otherwise
	 */
	public boolean contains(T target) {
		return contains(target,_head._next);
	}
	private boolean contains(T target, Node node) {
		//base case
		if(node == _tail) return false;

		if(node._item == null && target == null) return true;
		if(node._item == null) return contains(target, node._next);
		if(node._item.equals(target)) return true;

		return contains(target, node._next);
	}

	/**
	 * @return Previous Node of target; If list is empty return null
	 * @param target: data of the node behind the one being removed
	 */
	private Node previous(T target, Node node) {
		//base case, will account for empty
		if(node._next == _tail) return null; 

		if(node._next._item == null && target == null) return node;
		if(node._next._item.equals(target)) return node;

		return previous(target, node._next);
	}

	/**
	 * Removes first occurrence of targeted element
	 * @param target: element to be removed
	 * @return true if target was present; false otherwise
	 */
	public boolean remove(T target) {
		Node prev = previous(target, _head);
		if(prev == null) return false;
		prev._next = prev._next._next;
		_size--;
		return true;
	}

	/**
	 * @return last node
	 */
	private Node last() {
		if(isEmpty()) return _head;

		Node endNode; 
		for(endNode = _head._next; endNode._next != _tail; endNode = endNode._next)
		{}
		
		return endNode;
	}

	/**
	 * add element to end of Linked List
	 * @param element: element to be added
	 */
	public void addToBack(T element) {
		last()._next = new Node(element, _tail);
		_size++;
	}

	/**
	 * Reverses the Linked List
	 */ 
	public void reverse() {
		if(!isEmpty()) reverse(_head._next, size(), _head._next, 0);
	}
	private void reverse(Node n, int size, Node end, int index) {
		if(index < size) {// base case
			addToFront(n._item);
			
			if(index < 1) {
				end = _head._next;//end of new list
			}
			reverse(n._next, size, end, index + 1);
		}else {
			end._next = _tail;// removes old list
			_size = size;
		}
	}

	/**
	 * @return String version of LinkedList
	 */
	public String toString() {
		if(isEmpty()) return "";

		StringBuilder s = new StringBuilder();
		return toString(s, _head._next);
	}
	private String toString(StringBuilder s, Node node) {
		//base
		if(node == _tail) return s.toString();

		s.append(node._item + ",");
		return toString(s, node._next);
	}
}