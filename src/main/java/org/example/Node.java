package org.example;

import java.io.Serializable;

public class Node<E> implements Serializable {

    public Node<E> next = null;
    public E data;

    public Node(E data) {
        this.data = data;
        this.next = null;
    }

    public E getContents() {
        return data;
    }

    public void setData(E d) {
        data=d;
    }

}
