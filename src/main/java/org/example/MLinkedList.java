package org.example;

import java.io.Serializable;

public class MLinkedList<L> implements Serializable { // class MLinkedList that can store elements of type L
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

    public int size() {
        int count = 0;
        Node<L> current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    public L get(int index) {
        int count = 0;
        Node<L> current = head;
        while (current != null) {
            if (count == index) {
                return current.data;
            }
            count++;
            current = current.next;
        }
        return null; // index not valid
    }

    public L find(Object o) {
        Node<L> current = head;
        while (current != null) {
            if (current.data.equals(o)) {
                return current.data;
            }
            current = current.next;
        }
        return null; // Not found
    }

    public boolean remove(L data) {
        if (head == null) return false;

        if (head.data.equals(data)) {
            head = head.next;
            return true;
        }

        Node<L> current = head;
        while (current.next != null) {
            if (current.next.data.equals(data)) {
                current.next = current.next.next;
                return true;
            }
            current = current.next;
        }
        return false; // Not found
    }


}
