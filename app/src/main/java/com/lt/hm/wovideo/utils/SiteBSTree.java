package com.lt.hm.wovideo.utils;

import com.lt.hm.wovideo.model.City;
import com.lt.hm.wovideo.model.CityModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 二叉查找树
 *
 * @author 李垭超
 * @date 2015/04/07
 */

public class SiteBSTree<T extends Comparable<T>> {

    public BSTNode<T> mRoot; // 根结点

    public List<City> citys;


    public class BSTNode<T extends Comparable<T>> {
        T key; // 关键字(键值)
        T name;
        BSTNode<T> left; // 左孩子
        BSTNode<T> right; // 右孩子
        BSTNode<T> parent; // 父结点

        public BSTNode(T key, T name, BSTNode<T> parent, BSTNode<T> left,
                       BSTNode<T> right) {
            this.key = key;
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.name = name;
        }

        public T getKey() {
            return key;
        }

        public String toString() {
            return "key:" + key;
        }
    }

    public SiteBSTree() {
        mRoot = null;
    }

    /*
     * 前序遍历"二叉树"
     */
    private void preOrder(BSTNode<T> tree) {
        if (tree != null) {
            System.out.print(tree.name + " ");
            preOrder(tree.left);
            preOrder(tree.right);
        }
    }

    public void preOrder() {
        preOrder(mRoot);
    }

    /*
     * 中序遍历"二叉树"
     */
    private void inOrder(BSTNode<T> tree) {
        if (tree != null) {
            inOrder(tree.left);
            City cityInfo = new City();
            cityInfo.city = tree.name + "";
            cityInfo.pyOne = tree.key + "";
            citys.add(cityInfo);
            System.out.print(tree.name + " ");
            inOrder(tree.right);
        }
    }

    public List<City> inOrder() {
        citys = new ArrayList<City>();
        inOrder(mRoot);
        return citys;
    }

    /*
     * 后序遍历"二叉树"
     */
    private void postOrder(BSTNode<T> tree) {
        if (tree != null) {
            postOrder(tree.left);
            postOrder(tree.right);

            System.out.print(tree.name + " ");
        }
    }

    public void postOrder() {

        postOrder(mRoot);
    }

    /*
     * (递归实现)查找"二叉树x"中键值为key的节点
     */
    private BSTNode<T> search(BSTNode<T> x, T key) {
        if (x == null)
            return x;

        int cmp = key.compareTo(x.key);
        if (cmp < 0)
            return search(x.left, key);
        else if (cmp > 0)
            return search(x.right, key);
        else
            return x;
    }

    public BSTNode<T> search(T key) {
        return search(mRoot, key);
    }

    /*
     * (非递归实现)查找"二叉树x"中键值为key的节点
     */
    private BSTNode<T> iterativeSearch(BSTNode<T> x, T key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);

            if (cmp < 0)
                x = x.left;
            else if (cmp > 0)
                x = x.right;
            else
                return x;
        }

        return x;
    }

    public BSTNode<T> iterativeSearch(T key) {
        return iterativeSearch(mRoot, key);
    }

    /*
     * 查找最小结点：返回tree为根结点的二叉树的最小结点。
     */
    private BSTNode<T> minimum(BSTNode<T> tree) {
        if (tree == null)
            return null;

        while (tree.left != null)
            tree = tree.left;
        return tree;
    }

    public T minimum() {
        BSTNode<T> p = minimum(mRoot);
        if (p != null)
            return p.key;

        return null;
    }

    /*
     * 查找最大结点：返回tree为根结点的二叉树的最大结点。
     */
    private BSTNode<T> maximum(BSTNode<T> tree) {
        if (tree == null)
            return null;

        while (tree.right != null)
            tree = tree.right;
        return tree;
    }

    public T maximum() {
        BSTNode<T> p = maximum(mRoot);
        if (p != null)
            return p.key;

        return null;
    }

    public BSTNode<T> successor(BSTNode<T> x) {
        if (x.right != null)
            return minimum(x.right);

        BSTNode<T> y = x.parent;
        while ((y != null) && (x == y.right)) {
            x = y;
            y = y.parent;
        }

        return y;
    }

    public BSTNode<T> predecessor(BSTNode<T> x) {
        if (x.left != null)
            return maximum(x.left);

        BSTNode<T> y = x.parent;
        while ((y != null) && (x == y.left)) {
            x = y;
            y = y.parent;
        }

        return y;
    }

    private void insert(SiteBSTree<T> bst, BSTNode<T> z) {
        int cmp;
        BSTNode<T> y = null;
        BSTNode<T> x = bst.mRoot;

        while (x != null) {
            y = x;
            cmp = z.key.compareTo(x.key);
            if (cmp < 0)
                x = x.left;
            else
                x = x.right;
        }

        z.parent = y;
        if (y == null)
            bst.mRoot = z;
        else {
            cmp = z.key.compareTo(y.key);
            if (cmp < 0)
                y.left = z;
            else
                y.right = z;
        }
    }

    public void insert(T key, T name) {
        BSTNode<T> z = new BSTNode<T>(key, name, null, null, null);

        // 如果新建结点失败，则返回。
        if (z != null)
            insert(this, z);
    }

    private BSTNode<T> remove(SiteBSTree<T> bst, BSTNode<T> z) {
        BSTNode<T> x = null;
        BSTNode<T> y = null;

        if ((z.left == null) || (z.right == null))
            y = z;
        else
            y = successor(z);

        if (y.left != null)
            x = y.left;
        else
            x = y.right;

        if (x != null)
            x.parent = y.parent;

        if (y.parent == null)
            bst.mRoot = x;
        else if (y == y.parent.left)
            y.parent.left = x;
        else
            y.parent.right = x;

        if (y != z)
            z.key = y.key;

        return y;
    }

    public void remove(T key) {
        BSTNode<T> z, node;

        if ((z = search(mRoot, key)) != null)
            if ((node = remove(this, z)) != null)
                node = null;
    }

    private void destroy(BSTNode<T> tree) {
        if (tree == null)
            return;

        if (tree.left != null)
            destroy(tree.left);
        if (tree.right != null)
            destroy(tree.right);

        tree = null;
    }

    public void clear() {
        destroy(mRoot);
        mRoot = null;
    }

    private void print(BSTNode<T> tree, T key, int direction) {

        if (tree != null) {

            if (direction == 0) // tree是根节点
                System.out.printf("%2s is root\n", tree.key);
            else
                // tree是分支节点
                System.out.printf("%2s is %2s's %6s child\n", tree.key, key,
                        direction == 1 ? "right" : "left");

            print(tree.left, tree.key, -1);
            print(tree.right, tree.key, 1);
        }
    }

    public void print() {
        if (mRoot != null)
            print(mRoot, mRoot.key, 0);
    }
}
