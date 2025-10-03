package org.example;

public class MLinkedList<L> { // class MLinkedList that can store elements of type L
    public Node<L> head = null; // declares head of the list pointing to first node
    //private int size; // the number of elements in the linked list

    public void addElement(L d) { // add element to head of list
        Node<L> newNode = new Node<>(d);

        if (head == null) {
            head = newNode; // first element
        }
        else {
            Node<L> current = head;
            while (current.next != null) {
                current = current.next; // go to last node
            }
            current.next = newNode; // link new node at the end
        }
    }

    public Node<L> getHead() {
        return head;
    }

    public void clear() { // empty list
        head = null;
    }
}
