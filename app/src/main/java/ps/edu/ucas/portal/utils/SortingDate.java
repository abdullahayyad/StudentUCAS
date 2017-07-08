package ps.edu.ucas.portal.utils;

import java.util.Comparator;

import ps.edu.ucas.portal.model.AlarmEvent;
import ps.edu.ucas.portal.model.EventAcademicCalendar;

/**
 * Created by Ayyad on 11/24/2015.
 */
public class SortingDate implements Comparator<AlarmEvent> {
    @Override
    public int compare(AlarmEvent o1, AlarmEvent o2) {
        long diff = o1.getTimeInMidiSeconds() - o2.getTimeInMidiSeconds();
        int result = 0;
        if(diff>0){
            return 1;
        }else if (diff < 0){
            return -1;
        }
        return result;

    }}