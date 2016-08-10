package com.lt.hm.wovideo.widget;

import android.widget.SectionIndexer;

import com.lt.hm.wovideo.model.City;
import com.lt.hm.wovideo.model.CityModel;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xuchunhui on 16/8/10.
 */
public class MySectionIndexer<T> implements SectionIndexer {


    private static String[] mSections = {"A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z"};

    private static int OTHER_INDEX = 0; // index of other in the mSections array

    private int[] mPositions; // store the list of starting position index for
    // each section
    // e.g. A start at index 0, B start at index 20,
    // C start at index 41 and so on

    private int mCount; // this is the count for total number of contacts

    // Assumption: the contacts array has been sorted
    public MySectionIndexer(List<T> citys, String sections) {
        mCount = citys.size();
        initPositions(citys);
    }


    /**
     * 得到字符串的第一个字母
     *
     * @param indexableItem
     * @return
     */
    public int getSectionIndex(String indexableItem) {
        if (indexableItem == null) {
            return OTHER_INDEX;
        }

        indexableItem = indexableItem.trim();
        String firstLetter = "A";

        if (indexableItem.length() == 0) {
            return OTHER_INDEX;
        } else {
            // get the first letter
            firstLetter = String.valueOf(indexableItem.charAt(0)).toUpperCase();
        }

        int sectionCount = mSections.length;
        for (int i = 0; i < sectionCount; i++) {

            if (mSections[i].equals(firstLetter)) {
                return i;
            }
        }

        return OTHER_INDEX;

    }

    // initialize the position index
    public void initPositions(List<T> contacts) {
        int sectionCount = mSections.length;
        mPositions = new int[sectionCount];

        Arrays.fill(mPositions, -1); // initialize everything to -1
        int itemIndex = 0;

        for (T contact : contacts) {

            String indexableItem = ((City) contact).getPyOne();
            int sectionIndex = getSectionIndex(indexableItem); // find out which

            if (mPositions[sectionIndex] == -1) { // if not set before, then do
                mPositions[sectionIndex] = itemIndex;

            }
            itemIndex++;

        }

        int lastPos = -1;

        // now loop through, for all the ones not found, set position to the one
        // before them
        // this is to make sure the array is sorted for binary search to work
        for (int i = sectionCount - 1; i >= 0; i--) {
            if (mPositions[i] == -1) {
                mPositions[i] = lastPos;
            }
            lastPos = mPositions[i];

        }
    }

    @Override
    public int getPositionForSection(int section) {
        if (section < 0 || section >= mSections.length) {
            return -1;
        }
        return mPositions[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        if (position < 0 || position >= mCount) {
            return -1;
        }

        int index = Arrays.binarySearch(mPositions, position);

        // 注意这个方法的返回值，它就是index<0时，返回-index-2的原因
        // 解释Arrays.binarySearch，如果搜索结果在数组中，刚返回它在数组中的索引，如果不在，刚返回第一个比它大的索引的负数-1
        // 如果没弄明白，请自己想查看api
        return index >= 0 ? index : -index - 2;
    }

    @Override
    public Object[] getSections() {
        return mSections;
    }


}


