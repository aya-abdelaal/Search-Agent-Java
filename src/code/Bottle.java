package code;

import java.util.Objects;
import java.util.Stack;

public class Bottle implements Cloneable {
	public Stack<Character> stack;
	public int size;

	public Bottle(int s, Stack<Character> stack) {
		this.size = s;
		this.stack = stack;
	}

	@Override
	public Object clone() {
		// Clone the current object
		return new Bottle(this.size, (Stack<Character>) this.stack.clone());

	}

	//pours and returns cost
	public int pour(Bottle i) {
		if (this.stack.isEmpty()) {
			return -1;
		}
		if (i.isFull()) {
			return -1;
		}
		if (!i.stack.isEmpty() && !Objects.equals(this.stack.peek(), i.stack.peek())) {
			return -1;
		}
		int x = 0;
		while (!this.stack.isEmpty() && !i.isFull() && (i.stack.isEmpty() || Objects.equals(this.stack.peek(), i.stack.peek()))) {
			i.stack.push(this.stack.pop());
			x++;
		}

		return x;
	}

	public boolean isSameColor() {
		Stack<Character> copy = new Stack<>();
		if (this.stack.isEmpty()) {
			return true;
		}

		char top = this.stack.peek();
		boolean flag = true;

		// Use a temporary stack to iterate without modifying the original stack
		while (!this.stack.isEmpty()) {
			char current = this.stack.pop(); // Pop element from the original stack
			copy.push(current); // Push it to the copy stack

			if (current != top) {
				flag = false; // If any element differs, set flag to false
			}
		}

		// Restore the original stack from the copy
		while (!copy.isEmpty()) {
			this.stack.push(copy.pop());
		}

		return flag; // Return whether all elements were the same
	}

	@Override
	public String toString() {
		// Join the layers with commas for a clear representation
		return stack.toString();
	}

	public boolean isFull() {
		if (this.stack.isEmpty()) {
			return false;
		}
		return this.stack.size() == this.size; // Improved for clarity
	}

	public boolean isEmpty() {
		return this.stack.isEmpty();
	}

	public int getMisplacedCount() {
		Stack<Character> copy = new Stack<>();
		if(this.stack.isEmpty())
			return 0;
		while(!this.stack.isEmpty()) {
			copy.push(this.stack.pop());
		}
		int i=0;
		char bottom=copy.peek();
		while(!copy.isEmpty()) {
			this.stack.push(copy.pop());
			if(bottom!=stack.peek()) {
				i++;
			}
			
		}
		return i;
	}

	// Getter for stack (if needed)
	public Stack<Character> getStack() {
		return stack;
	}
}
