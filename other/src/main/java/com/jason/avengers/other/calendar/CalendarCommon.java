package com.jason.avengers.other.calendar;

import com.jason.avengers.common.database.entity.other.calendar.CalendarEventDBEntity;
import com.jason.avengers.common.database.entity.other.calendar.CalendarOwnerDBEntity;
import com.jason.avengers.other.R;
import com.jason.avengers.other.calendar.beans.Event;
import com.jason.avengers.other.calendar.beans.Owner;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jason
 */
public class CalendarCommon {

    public static final String EMPTY = "无";
    public static final double DEFAULT_MONEY = 0;

    public static final SimpleDateFormat yyyyMMdd_SDF = new SimpleDateFormat("yyyy年MM月dd日");
    public static final SimpleDateFormat yyyyMM_SDF = new SimpleDateFormat("yyyy年MM月");
    public static final SimpleDateFormat yyMMddE_SDF = new SimpleDateFormat("yy年MM月dd日E");
    public static final SimpleDateFormat yyMMdd_SDF = new SimpleDateFormat("yy年MM月dd日");
    public static final SimpleDateFormat ddEHHmm_SDF = new SimpleDateFormat("dd日E HH:mm");
    public static final SimpleDateFormat HHmm_SDF = new SimpleDateFormat("HH:mm");

    public static final DecimalFormat MONEY_DF = new DecimalFormat("#.00");

    public static final Map<String, List<String>> OwnersMap = new HashMap<>();

    static {
        List<String> Names;

        Names = new ArrayList<>();
        Names.add("姜义霏");
        Names.add("妞妞");
        Names.add("Coco");
        Names.add("孙婉仪");
        Names.add("张瑞驰");
        Names.add("熊猫");
        OwnersMap.put("华府禧园", Names);

        Names = new ArrayList<>();
        Names.add("党乐琪");
        Names.add("刘随缘");
        OwnersMap.put("华府珑园", Names);

        Names = new ArrayList<>();
        Names.add("代东啸");
        OwnersMap.put("茉莉园", Names);

        Names = new ArrayList<>();
        Names.add("小鱼儿");
        OwnersMap.put("西北旺", Names);

        Names = new ArrayList<>();
        Names.add("宣黎帆");
        Names.add("小马");
        OwnersMap.put("上地", Names);

        Names = new ArrayList<>();
        Names.add("刘欣怡");
        OwnersMap.put("清河", Names);

        Names = new ArrayList<>();
        Names.add("李秋融");
        OwnersMap.put("润千秋", Names);
    }

    public static final List<Integer> Colors = new ArrayList<>();

    static {
        Colors.add(R.color.event_color_01);
        Colors.add(R.color.event_color_02);
        Colors.add(R.color.event_color_03);
        Colors.add(R.color.event_color_04);
        Colors.add(R.color.event_color_05);
        Colors.add(R.color.event_color_06);
        Colors.add(R.color.event_color_07);
        Colors.add(R.color.event_color_08);
        Colors.add(R.color.event_color_09);
        Colors.add(R.color.event_color_10);
        Colors.add(R.color.event_color_11);
        Colors.add(R.color.event_color_12);
        Colors.add(R.color.event_color_13);
        Colors.add(R.color.event_color_14);
        Colors.add(R.color.event_color_15);
        Colors.add(R.color.event_color_16);
        Colors.add(R.color.event_color_17);
        Colors.add(R.color.event_color_18);
        Colors.add(R.color.event_color_19);
        Colors.add(R.color.event_color_20);
    }

    public static final String EACH_DAY = "每天";
    public static final String EACH_WEEK = "每周";
    public static final String EACH_MONTH = "每月";
    public static final List<String> Cycles = new ArrayList<>();

    static {
        Cycles.add(EACH_DAY);
        Cycles.add(EACH_WEEK);
        Cycles.add(EACH_MONTH);
    }

    public static final List<String> Times = new ArrayList<>();

    static {
        Times.add("2次");
        Times.add("3次");
        Times.add("4次");
        Times.add("5次");
        Times.add("6次");
        Times.add("7次");
        Times.add("8次");
        Times.add("9次");
        Times.add("10次");
    }

    public static final List<String> Levels = new ArrayList<>();

    static {
        Levels.add("一级");
        Levels.add("二级");
        Levels.add("三级");
        Levels.add("四级");
        Levels.add("五级");
        Levels.add("六级");
        Levels.add("七级");
        Levels.add("八级");
        Levels.add("九级");
        Levels.add("十级");
    }

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

    public static String buildTitle(CalendarEventDBEntity entity, CalendarOwnerDBEntity ownerDBEntity) {
        String title = String.format("%s_[%s]:%s-%s_%s",
                ownerDBEntity.getOwner(),
                ownerDBEntity.getLocaltion(),
                simpleDateFormat.format(entity.getStartTime()),
                simpleDateFormat.format(entity.getEndTime()),
                entity.getLevel());
        return title;
    }

    public static final int QUERY_STYLE_DEFAULT = 0;
    public static final int QUERY_STYLE_DAY = 1;
    public static final int QUERY_STYLE_WEEK = 2;
    public static final int QUERY_STYLE_MONTH = 3;

    public static List<Event> buildEventsByEventEntities(List<CalendarEventDBEntity> eventEntities,
                                                         List<CalendarOwnerDBEntity> ownerEntities,
                                                         int queryStyle, Date now) {
        if (eventEntities == null || eventEntities.isEmpty()) {
            return null;
        }

        List<Event> events;
        Collections.sort(eventEntities, new Comparator<CalendarEventDBEntity>() {
            @Override
            public int compare(CalendarEventDBEntity o1, CalendarEventDBEntity o2) {
                return o1.getStartTime().before(o2.getStartTime()) ? -1 : 1;
            }
        });
        switch (queryStyle) {
            case QUERY_STYLE_DAY:
                events = buildDataInDay(eventEntities, ownerEntities, now);
                break;
            case QUERY_STYLE_WEEK:
                events = buildDataInWeek(eventEntities, ownerEntities, now);
                break;
            case QUERY_STYLE_MONTH:
                events = buildDataInMonth(eventEntities, ownerEntities, now);
                break;
            default:
                events = buildDataInDefault(eventEntities, ownerEntities, now);
                break;
        }
        return events;
    }

    private static List<Event> buildDataInDefault(List<CalendarEventDBEntity> eventEntities,
                                                  List<CalendarOwnerDBEntity> ownerEntities,
                                                  Date now) {
        List<Event> events = new ArrayList<>();
        Date groupStartTime = null;
        Date groupEndTime = null;
        double groupTotalMoney = 0;
        Event event;
        for (CalendarEventDBEntity eventEntity : eventEntities) {
            event = buildEvent(eventEntity, ownerEntities, now, CalendarCommon.yyMMdd_SDF, CalendarCommon.HHmm_SDF);
            events.add(event);

            if (groupStartTime == null || groupStartTime.after(eventEntity.getStartTime())) {
                groupStartTime = eventEntity.getStartTime();
            }
            if (groupEndTime == null || groupEndTime.before(eventEntity.getEndTime())) {
                groupEndTime = eventEntity.getEndTime();
            }
            groupTotalMoney += eventEntity.getMoney();
        }

        Event groupEvent = new Event();
        groupEvent.setEventTime(String.format("%s-%s",
                CalendarCommon.yyMMdd_SDF.format(groupStartTime),
                CalendarCommon.yyMMdd_SDF.format(groupEndTime)));
        groupEvent.setMoney(groupTotalMoney);
        groupEvent.setGroup(true);
        events.add(0, groupEvent);
        return events;
    }

    private static List<Event> buildDataInDay(List<CalendarEventDBEntity> eventEntities,
                                              List<CalendarOwnerDBEntity> ownerEntities,
                                              Date now) {
        Map<String, List<Event>> eventListMap = new LinkedHashMap<>();
        List<Event> eventList;
        Event groupEvent;
        Event event;
        for (CalendarEventDBEntity eventEntity : eventEntities) {
            String groupEventTime = CalendarCommon.yyMMddE_SDF.format(eventEntity.getStartTime());

            if (eventListMap.containsKey(groupEventTime)) {
                eventList = eventListMap.get(groupEventTime);
            } else {
                eventList = new ArrayList<>();
            }

            if (!eventList.isEmpty()) {
                groupEvent = eventList.get(0);
            } else {
                groupEvent = new Event();
            }
            groupEvent.setEventTime(groupEventTime);
            groupEvent.setGroup(true);
            groupEvent.setMoney(groupEvent.getMoney() + eventEntity.getMoney());


            if (!eventList.isEmpty()) {
                eventList.set(0, groupEvent);
            } else {
                eventList.add(groupEvent);
            }
            event = buildEvent(eventEntity, ownerEntities, now, CalendarCommon.HHmm_SDF, CalendarCommon.HHmm_SDF);
            eventList.add(event);

            eventListMap.put(groupEventTime, eventList);
        }

        List<Event> events = new ArrayList<>();
        for (Map.Entry<String, List<Event>> entry : eventListMap.entrySet()) {
            events.addAll(entry.getValue());
        }
        return events;
    }

    private static List<Event> buildDataInWeek(List<CalendarEventDBEntity> eventEntities,
                                               List<CalendarOwnerDBEntity> ownerEntities,
                                               Date now) {
        Map<String, List<Event>> eventListMap = new LinkedHashMap<>();
        List<Event> eventList;
        Event groupEvent;
        Event event;
        for (CalendarEventDBEntity eventEntity : eventEntities) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(eventEntity.getStartTime());
            startCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(eventEntity.getStartTime());
            endCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

            String groupEventTime = String.format("%s-%s",
                    CalendarCommon.yyMMdd_SDF.format(startCalendar.getTime()),
                    CalendarCommon.yyMMdd_SDF.format(endCalendar.getTime()));

            if (eventListMap.containsKey(groupEventTime)) {
                eventList = eventListMap.get(groupEventTime);
            } else {
                eventList = new ArrayList<>();
            }

            if (!eventList.isEmpty()) {
                groupEvent = eventList.get(0);
            } else {
                groupEvent = new Event();
            }
            groupEvent.setEventTime(groupEventTime);
            groupEvent.setGroup(true);
            groupEvent.setMoney(groupEvent.getMoney() + eventEntity.getMoney());


            if (!eventList.isEmpty()) {
                eventList.set(0, groupEvent);
            } else {
                eventList.add(groupEvent);
            }
            event = buildEvent(eventEntity, ownerEntities, now, CalendarCommon.ddEHHmm_SDF, CalendarCommon.HHmm_SDF);
            eventList.add(event);

            eventListMap.put(groupEventTime, eventList);
        }

        List<Event> events = new ArrayList<>();
        for (Map.Entry<String, List<Event>> entry : eventListMap.entrySet()) {
            events.addAll(entry.getValue());
        }
        return events;
    }

    private static List<Event> buildDataInMonth(List<CalendarEventDBEntity> eventEntities,
                                                List<CalendarOwnerDBEntity> ownerEntities,
                                                Date now) {
        Map<String, List<Event>> eventListMap = new LinkedHashMap<>();
        List<Event> eventList;
        Event groupEvent;
        Event event;
        for (CalendarEventDBEntity eventEntity : eventEntities) {
            String groupEventTime = CalendarCommon.yyyyMM_SDF.format(eventEntity.getStartTime());

            if (eventListMap.containsKey(groupEventTime)) {
                eventList = eventListMap.get(groupEventTime);
            } else {
                eventList = new ArrayList<>();
            }

            if (!eventList.isEmpty()) {
                groupEvent = eventList.get(0);
            } else {
                groupEvent = new Event();
            }
            groupEvent.setEventTime(groupEventTime);
            groupEvent.setGroup(true);
            groupEvent.setMoney(groupEvent.getMoney() + eventEntity.getMoney());


            if (!eventList.isEmpty()) {
                eventList.set(0, groupEvent);
            } else {
                eventList.add(groupEvent);
            }
            event = buildEvent(eventEntity, ownerEntities, now, CalendarCommon.ddEHHmm_SDF, CalendarCommon.HHmm_SDF);
            eventList.add(event);

            eventListMap.put(groupEventTime, eventList);
        }

        List<Event> events = new ArrayList<>();
        for (Map.Entry<String, List<Event>> entry : eventListMap.entrySet()) {
            events.addAll(entry.getValue());
        }
        return events;
    }

    private static Event buildEvent(CalendarEventDBEntity eventEntity,
                                    List<CalendarOwnerDBEntity> ownerEntities,
                                    Date now,
                                    SimpleDateFormat startSdf,
                                    SimpleDateFormat endSdf) {
        Event event = new Event();
        event.setEventTime(String.format("%s-%s",
                startSdf.format(eventEntity.getStartTime()),
                endSdf.format(eventEntity.getEndTime())));
        event.setLevel(eventEntity.getLevel());
        event.setMoney(eventEntity.getMoney());
        event.setGroup(false);
        event.setDone(eventEntity.getEndTime().before(now));
        if (ownerEntities != null && !ownerEntities.isEmpty()
                && eventEntity.getOwner() != null && eventEntity.getOwner().getTargetId() > 0) {
            for (CalendarOwnerDBEntity ownerEntity : ownerEntities) {
                if (ownerEntity.getId() == eventEntity.getOwner().getTargetId()) {
                    Owner owner = new Owner();
                    owner.setId(ownerEntity.getId());
                    owner.setName(ownerEntity.getOwner());
                    owner.setColor(ownerEntity.getColor());
                    event.setOwner(owner);
                    break;
                }
            }
        }
        return event;
    }

    public static List<CalendarEventDBEntity> findOverdueEventEntities(List<CalendarEventDBEntity> eventEntities, Date now) {
        if (eventEntities == null || eventEntities.isEmpty()) {
            return null;
        }

        Iterator<CalendarEventDBEntity> iterator = eventEntities.iterator();
        CalendarEventDBEntity entity;
        while (iterator.hasNext()) {
            entity = iterator.next();
            if (!entity.getEndTime().before(now)) {
                iterator.remove();
            }
        }
        return eventEntities;
    }
}
