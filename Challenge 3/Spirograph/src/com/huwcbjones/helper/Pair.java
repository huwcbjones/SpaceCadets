package com.huwcbjones.helper;

/**
 * Created by hjone on 15/10/2015.
 */

public class Pair<L, R> {

    private final L left;
    private final R right;

    public Pair(L left, R right){
        this.left = left;
        this.right = right;
    }

    public L getLeft(){
        return left;
    }
    public R getRight(){
        return right;
    }


    @Override
    public int hashCode(){
        return left.hashCode()  ^ right.hashCode();
    }

    @Override
    public  boolean equals(Object o) {
        if( !(o instanceof Pair)) return false;
        Pair compPair = (Pair)o;

        return (this.getLeft() == compPair.getLeft()) && (this.getRight() == compPair.getRight());
    }
    @Override
    public String toString(){
        return this.getLeft().toString() + ", " + this.getRight().toString();
    }
}
