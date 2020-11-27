package com.javarush.task.task20.task2028;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* 
Построй дерево(1)
*/
public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {
    Entry<String> root;
    private ArrayList<Entry<String>> list = new ArrayList<>();
    public CustomTree() {
        root = new Entry<>("0");
        list.add(0, root);
    }
    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        int count=0;
        for(Entry e: this.list) {
            if (e!=null) {
                count++;
            }
        }
        return count-1;
    }

    @Override
    public boolean add(String s) {
        Entry current;
        Entry newEntry = new Entry(s);
        for (int i=0; i<list.size(); i++) {
            current = list.get(i);
            if (current.isAvailableToAddChildren()) {
                if(current.leftChild==null) {
                    current.leftChild = newEntry;
                }
                else current.rightChild = newEntry;
                newEntry.parent=current;
                current.checkChildren();
                list.add(newEntry);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof String)) throw new UnsupportedOperationException();
        Entry delObj = getElementByName(o.toString());
        Entry parent = delObj.parent;

        if(delObj.leftChild != null){
            remove(delObj.leftChild.elementName);
        }
        if(delObj.rightChild != null){
            remove(delObj.rightChild.elementName);
        }
        if(delObj.leftChild == null && delObj.rightChild == null) {
            if (parent.rightChild == delObj) {
                parent.rightChild = null;
                parent.availableToAddRightChildren = true;
            } else {
                parent.leftChild = null;
                parent.availableToAddLeftChildren = true;
            }
            list.remove(delObj);

        }

        return true;
    }
    public Entry getElementByName(String elementName){
        Entry element = null;
        for(Entry e : list){
            if(e.elementName.equals(elementName))
                element = e;
        }
        return element;
    }
    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    public String getParent(String s) {
        for (Entry entry : list) {
            if (s.equals(root.elementName)) {
                return null;
            }
            if (entry == null) {
                return null;
            }
            if (entry.elementName.equals(s)) {
                return entry.parent.elementName;
            }
        }
        return null;
    }

    static class Entry<T> implements Serializable {
        String elementName;
        boolean availableToAddLeftChildren = true;
        boolean availableToAddRightChildren = true;
        Entry parent, leftChild, rightChild;

        public Entry(String elementName) {
            this.elementName = elementName;
        }

        public boolean isAvailableToAddChildren() {
            return availableToAddLeftChildren || availableToAddRightChildren;

        }
        void checkChildren() {
            if(leftChild!=null) availableToAddLeftChildren = false;
            if (rightChild!=null) availableToAddRightChildren = false;
        }
    }
}
