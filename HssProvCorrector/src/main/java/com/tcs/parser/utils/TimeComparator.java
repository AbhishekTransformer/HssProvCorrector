package com.tcs.parser.utils;

import com.tcs.parser.dto.events;

import java.util.Comparator;

public class TimeComparator implements Comparator<events> {

    @Override
    public int compare(events ev1, events ev2) {
        if (ev1.getTimeEndProcess() == ev2.getTimeEndProcess())
            return 0;
        else if (ev1.getTimeEndProcess() < ev2.getTimeEndProcess())
            return 1;
        else
            return -1;
    }
}

