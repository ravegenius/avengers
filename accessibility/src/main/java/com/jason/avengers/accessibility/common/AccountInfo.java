package com.jason.avengers.accessibility.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum AccountInfo {
    /**
     * 单例
     */
    INFO;

    public static final String ACCOUNT = "13401073452";
    public static final String PASSWORD = "RGjason8513053";

    private static final int CLICK_COUNT = 4;
    private static final int START = 0;
    private static final int END = 59;
    private static final Random mRandom = new Random();

    private List<Integer> mPoints = new ArrayList<>();
    private List<Integer> mClockTimes = new ArrayList<>();
    private boolean isClocked = false;

    public List<Integer> getPoints() {
        return mPoints;
    }

    public List<Integer> getClockTimes() {
        return mClockTimes;
    }

    public boolean isClocked() {
        return isClocked;
    }

    public void setClocked(boolean isClocked) {
        this.isClocked = isClocked;
    }

    public void init() {
        setClocked(false);
        initPoints();
        initClockTimes();
    }

    private void initPoints() {
        mPoints.clear();
        mPoints.add(0);
        mPoints.add(1);
        mPoints.add(2);
        mPoints.add(4);
        mPoints.add(6);
        mPoints.add(7);
        mPoints.add(8);
        Utils.log("初始化 mPoints<" + mPoints + ">");
    }

    public void initClockTimes() {
        mClockTimes.clear();
        while (mClockTimes.size() < CLICK_COUNT) {
            int random = random();
            if (mClockTimes.contains(random)) {
                continue;
            }
            if (mClockTimes.isEmpty()) {
                mClockTimes.add(random);
            } else {
                int addnessIndex = -1;
                for (Integer randomTime : mClockTimes) {
                    if (randomTime > random) {
                        addnessIndex = mClockTimes.indexOf(randomTime);
                        break;
                    }
                }
                if (addnessIndex == -1) {
                    mClockTimes.add(random);
                } else {
                    mClockTimes.add(addnessIndex, random);
                }
            }
        }

        List<Integer> newRandomTimes = new ArrayList<>();
        for (int index = 0; index < CLICK_COUNT; index++) {
            if (index == 0) {
                newRandomTimes.add(9 * 100 + mClockTimes.get(index));
            } else if (index == 1) {
                newRandomTimes.add(22 * 100 + mClockTimes.get(index));
            } else if (index == 2) {
                newRandomTimes.add(21 * 100 + mClockTimes.get(index));
            } else {
                newRandomTimes.add(8 * 100 + mClockTimes.get(index));
            }
        }
        mClockTimes = newRandomTimes;
        Utils.log("初始化 mClockTimes<" + mClockTimes + ">");
    }

    private int random() {
        return mRandom.nextInt(END - START + 1) + START;
    }

    public void clear() {
        setClocked(false);
        mPoints.clear();
        mClockTimes.clear();
    }
}
