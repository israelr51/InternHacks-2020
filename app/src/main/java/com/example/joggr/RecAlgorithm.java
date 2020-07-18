package com.example.joggr;
import android.text.format.DateUtils;

import com.google.api.client.util.DateTime;
import com.google.common.collect.Collections2;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class RecAlgorithm {
    static ArrayList<activities> categorizedActivity = new ArrayList<>();
    static String recommendStartTime = "09:45:00";
    static String recommendEndTime = "17:45:00";
    //in minutes
    static int bufferTime = 15;

    public static void populateActivities(){
        //random data categorize by outdoor activities and indoor activities
//        ArrayList<String[]> outdoor = new ArrayList<>();
//        String[] outdoor1 = {"Hiking", "01:30:00"};
//        outdoor.add(outdoor1);
//        String[] outdoor2 = {"Surfing", "02:00:00"};
//        outdoor.add(outdoor2);
//        ArrayList<String[]> indoor = new ArrayList<>();
//        String[] indoor1 = {"Cooking", "01:30:00"};
//        indoor.add(indoor1);
//        String[] indoor2 = {"Art", "00:30:00"};
//        indoor.add(indoor2);
//        categorizedActivity.put("Outdoor", outdoor );
//        categorizedActivity.put("Indoor", indoor );

        categorizedActivity.add(new activities("Cooking", "01:30:00", "indoor"));
        categorizedActivity.add(new activities("Hiking", "01:30:00", "outdoor"));
        categorizedActivity.add(new activities("Art", "00:30:00", "indoor"));
        categorizedActivity.add(new activities("Surfing", "01:30:00", "outdoor"));



    }
    static List<String> events;
    public static void processCalendarInformation(){
//         events = Calendar.events;
         //System.out.println("here" + events.get(1));
    }

        //fake date time information for a day
    static List<CalEvents> fakeData = new ArrayList<>();

    public static void insertFakeData(){
                        //event name, start,end, date
        //fakeData.add(new CalEvents("meeting 12", null, null, "07-09-2020"));
        fakeData.add(new CalEvents("meeting 11", "17:45:00", "18:30:00", "07-09-2020"));
        fakeData.add(new CalEvents("meeting 1", "09:30:00", "10:30:00", "07-10-2020"));
        fakeData.add(new CalEvents("meeting 2", "14:30:00", "15:30:00", "07-10-2020"));
        fakeData.add(new CalEvents("meeting 3", "17:30:00", "17:45:00", "07-10-2020"));
        fakeData.add(new CalEvents("meeting 9", "19:00:00", "19:30:00", "07-10-2020"));
        fakeData.add(new CalEvents("meeting 4", "09:30:00", "10:00:00", "07-11-2020"));
        fakeData.add(new CalEvents("meeting 5", "10:30:00", "11:30:00", "07-11-2020"));
        fakeData.add(new CalEvents("meeting 6", "13:30:00", "14:30:00", "07-11-2020"));
        fakeData.add(new CalEvents("meeting 7", "15:00:00", "18:00:00", "07-11-2020"));

    }

    public static long durationConversion(String duration){
        long hour = Integer.valueOf(duration.substring(0,2));
        long minutes = Integer.valueOf(duration.substring(3,5));
        long seconds = Integer.valueOf(duration.substring(6,8));

        long converted = TimeUnit.HOURS.toMillis(hour)+TimeUnit.MINUTES.toMillis(minutes)+TimeUnit.SECONDS.toMillis(seconds);
        return converted;
    }
    public static ArrayList<activities> filterActivityList(ArrayList<activities> array, long duration, long buffer ){
          ArrayList<activities> filtered = new ArrayList<>();
          for(activities activity : array){
              Date activityDuration = new Date();
              try{
                  SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm:ss");
                  activityDuration = timeformatter.parse(activity.duration);
              }
              catch(ParseException e){

              }
              long conversion = durationConversion(activity.duration);
              System.out.println(activity.name+ ", duration : "+conversion);
              if ((duration >= (conversion + (buffer * 60*1000)))){
                  filtered.add(activity);
              }
          }
          return filtered;
    };
//
//    public static boolean isThisAllDay(){
//        return true;
//    }
    public static void printRec (List<CalEvents> curate){
        System.out.println("printing REc");
        for(CalEvents act : curate){
            System.out.println(act.name+", "+act.start_time+", "+ act.date_of_event);
        }
    }
    public static String timeStringConverstion(long h, long m, long s){
        String temp ="";
        if(h < 10){
            temp+="0"+h+":";
        }
        else{
            temp+=""+h+":";
        }
        if(m < 10){
            temp+="0"+m+":";
        }
        else{
            temp+=""+m+":";
        }
        if(s < 10){
            temp+="0"+s;
        }
        else{
            temp+=""+s+":";
        }
        return temp;
    }
    public static void recommend(){

        Date curDate = new Date();
        List<CalEvents> dayEvents = new ArrayList<>();
        List<CalEvents> curatedActivities = new ArrayList();
        boolean newDay = false;
        int nextindex = 0;
        System.out.println(fakeData.size());
        for(int i = 0; i < fakeData.size(); ){


            CalEvents curevent = fakeData.get(i);
            while(curevent.start_time == null  && curevent.end_time == null){
                System.out.println("IM HERE");
                i++;
                nextindex = i;
                curevent = fakeData.get(i);
            }
            CalEvents nextevent = fakeData.get(nextindex);

            Date curEventST = new Date();
            Date curEventET = new Date();
            Date recST = new Date();
            Date recET = new Date();
            try{
                System.out.println("TIME FORMATTER WORKING currently at : i = " + i );
                SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm:ss");
                timeformatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                curEventET = timeformatter.parse(curevent.end_time);
               // System.out.println("ET" + curEventET);
                curEventST = timeformatter.parse(curevent.start_time);
               // System.out.println("sT" + curEventST);
                recST = timeformatter.parse(recommendStartTime);
                recET = timeformatter.parse(recommendEndTime);

            }
            catch(ParseException e){
                e.printStackTrace();
            }
            if(curEventST.before(recST) && curEventET.after(recST) || curEventET.equals(recST)){
                System.out.println("new recST here");
                recST = curEventET;
                System.out.println(recST);
            }


            while(curevent.date_of_event.equals(nextevent.date_of_event) && nextindex < fakeData.size()) {
                System.out.println("looking at dates");
                System.out.println(curevent.name);
                System.out.println(nextevent.name);

                    Date nexteventET = new Date();
                    Date nexteventST = new Date();
                    try{
                        SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm:ss");
                        timeformatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                        nexteventET = timeformatter.parse(nextevent.end_time);
                        nexteventST = timeformatter.parse(nextevent.start_time);
                }
                    catch(ParseException e){
                        e.printStackTrace();
                    }
                if(nexteventST.before(recET) && nexteventET.after(recET) || nexteventET.equals(recET)){
                    recET = nexteventST;
                    System.out.println("new rec ET "+recET);

                }

                if (((nexteventST.after(recST) && nexteventST.before(recET)) && (nexteventET.after(recST) && nexteventET.before(recET))) || nexteventET.equals(recST) || nexteventST.equals(recET)) {
                    System.out.println("adding day event "+ fakeData.get(nextindex).name);
                    dayEvents.add(fakeData.get(nextindex));

                }
                // System.out.println("value of next index "+nextindex);
                nextindex++;
                if(nextindex < fakeData.size()) nextevent = fakeData.get(nextindex);
                //          nextevent = fakeData.get(nextindex);
                //           System.out.println("next event up coming " + nextevent.name);
            }
            System.out.println("Day event size after 1 day: "+  dayEvents.size());
            //System.out.println("OUT, up next is"+ nextevent.name);

            for(CalEvents events : dayEvents){
                System.out.println(events.name);
            }
            newDay = true;
            //if we are at the end of the day, go through the list of events of that day and try to fit activties in between the events
            if(newDay == true && dayEvents.size()!= 0) {
                System.out.println("new day");
                newDay = false;
                long DurationEvent;
                long buffer;
                CalEvents prevEvent = dayEvents.get(0); //meeting1
                Date prevEventST = new Date();
                Date prevEventET = new Date();

                for(int j  = 0; j <= dayEvents.size(); j++) {
                    CalEvents eventNow;
                    if( j == dayEvents.size()){
                        eventNow = dayEvents.get(dayEvents.size()-1);
                    }
                    else {
                        eventNow=dayEvents.get(j);
                    }
                    Date eventNowST = new Date();
                    Date eventNowET = new Date();
                    try{
                        SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm:ss");
                        timeformatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                        prevEventST= timeformatter.parse(prevEvent.start_time);
                        prevEventET = timeformatter.parse(prevEvent.end_time);
                //        System.out.println("conversion prevEventET.getTime() "+ prevEventET.getTime());
                        eventNowET = timeformatter.parse(eventNow.end_time);
                        System.out.println("Event NOW name : "+eventNow.name);
                        eventNowST = timeformatter.parse(eventNow.start_time);
                        System.out.println("EveNT NOW START: "+eventNowST);
                    }
                    catch(ParseException e){
                        e.printStackTrace();
                    }
                    System.out.println("recET: "+recET);
                    System.out.println("eventnowST: "+eventNowST);
                    System.out.println("prevEventET: "+prevEventET);
                    if(dayEvents.size() > 1 && (eventNowET.equals(recST))){
                        continue;
                    }
                    //if we are at the beginning of the day, we include recommended start time
                    if(j == 0 && eventNowST.after(recST)) {
                        System.out.println(recST.getTime());
                        System.out.println(eventNowST.getTime());
                        prevEventET = recST;
                        DurationEvent = Math.abs(recST.getTime() - eventNowST.getTime());
                        buffer = bufferTime*2;
                    }
                    else if ( j == dayEvents.size() && eventNowST.equals(recET)){
                        continue;
                    }
                    else if(j == dayEvents.size() ){
                        DurationEvent = Math.abs(recET.getTime() - eventNowET.getTime());
                        buffer = bufferTime*2;
                    }
                    //else if we are past the last event, we include recommended end time from current time
//                    else if (j == dayEvents.size()){
//                        DurationEvent = recET.getTime() - curEventET.getTime();
//                        buffer = bufferTime*2;
//                    } //if we are in between events, we calculate duration in btwn the time
                    else{
                        System.out.println("previous end time: " + prevEventET);
                        System.out.println("current start time: "+eventNowST);
                        DurationEvent = Math.abs(eventNowST.getTime() - prevEventET.getTime());
                        buffer = bufferTime*2;
                    }
                    long timeDuration = DurationEvent + (buffer*60*1000);
                    System.out.println("time duration for "+ "j= "+j+": "+timeDuration);
                    List<activities> filteredActivities = filterActivityList(categorizedActivity, DurationEvent, buffer);
                    //see if there is an activity matches the time in between
                    if(filteredActivities.size() > 0) {
                        Date activityTime = new Date();
                        Random rand = new Random();
                        int randNum = rand.nextInt(filteredActivities.size());
                        System.out.println(filteredActivities.size());
                        System.out.println("RANDOM! " + randNum);
                        try {
                            SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm:ss");
                            activityTime = timeformatter.parse(filteredActivities.get(randNum).duration);
                        }
                        catch (ParseException e){
                            e.printStackTrace();
                        }
                        activities curactivity = filteredActivities.get(randNum);
                        if(DurationEvent >= (durationConversion(curactivity.duration)+ (buffer*60*1000))){
                            long starttime =  prevEventET.getTime() + ((buffer/2)*60*1000);

                            long hour = starttime / (60 * 60 * 1000);
                            long minutes = starttime / (60* 1000) % 60;
                            long seconds = starttime / 1000 % 60;



                            long endtime = starttime + (durationConversion(curactivity.duration)+(buffer/2)*60*1000);
                            long endhour = endtime / (60 * 60 * 1000);
                            long endminutes = endtime / (60* 1000) % 60;
                            long endseconds = endtime / 1000 % 60;
                            String startTime = timeStringConverstion(hour, minutes, seconds);
                            String endTime = timeStringConverstion(endhour, endminutes,endseconds);
                            System.out.println("ACTIVITY START AT "+startTime);
                            curatedActivities.add(new CalEvents(curactivity.name, startTime, endTime, eventNow.date_of_event ));
                        }
                        if(dayEvents.size()==1 && (eventNowET.equals(recST) || eventNowST.equals(recET)))break;
                    }
                    prevEvent = eventNow;
                }
                printRec(curatedActivities);
                dayEvents.clear();
            }



            i = nextindex;
        }
    }

}
