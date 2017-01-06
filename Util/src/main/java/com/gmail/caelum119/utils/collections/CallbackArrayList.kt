package com.gmail.caelum119.utils.collections

import java.util.*
import java.util.Collection

/**
 * First created 1/5/2017 in BalloonEngine
 */
class CallbackArrayList<E>(val elementAdded: (elementAdded: E) -> Unit, val elementRemoved: (elementRemoved: E) -> Unit) : ArrayList<E>() {
    /**
     * Appends the specified element to the end of this list.
     * Also, calls [elementAdded] with [element]
     * @param e element to be appended to this list
     *
     * @return <tt>true</tt> (as specified by [Collection.add])
     */
    override fun add(element: E): Boolean {

        val result = super.add(element)
        elementAdded(element)
        return result
    }

    /**
     * Inserts the specified element at the specified position in this
     * list. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     *
     * Also, calls [elementAdded] with [element]
     *
     * @param index index at which the specified element is to be inserted
     *
     * @param element element to be inserted
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    override fun add(index: Int, element: E) {
        val result = super.add(index, element)
        elementAdded(element)
        return result
    }

    /**
     * Inserts all of the elements in the specified collection into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).  The new elements will appear
     * in the list in the order that they are returned by the
     * specified collection's iterator.
     *
     * [elementAdded] will be called for each element in [elements]
     *
     * @param index index at which to insert the first element from the
     *               specified collection
     *
     * @param c collection containing elements to be added to this list
     * *
     * @return <tt>true</tt> if this list changed as a result of the call
     * *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * *
     * @throws NullPointerException if the specified collection is null
     */
    override fun addAll(index: Int, elements: kotlin.collections.Collection<E>): Boolean {
        val result = super.addAll(index, elements)
        elements.forEach { elementAdded(it) }
        return result
    }

    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the
     * specified collection's Iterator.  The behavior of this operation is
     * undefined if the specified collection is modified while the operation
     * is in progress.  (This implies that the behavior of this call is
     * undefined if the specified collection is this list, and this
     * list is nonempty.)
     *
     * [elementAdded] will be called for each element in [elements]
     *
     * @param c collection containing elements to be added to this list
     * *
     * @return <tt>true</tt> if this list changed as a result of the call
     * *
     * @throws NullPointerException if the specified collection is null
     */
    override fun addAll(elements: kotlin.collections.Collection<E>): Boolean {
        val success = super.addAll(elements)
        elements.forEach { elementAdded(it) }
        return success
    }
}

