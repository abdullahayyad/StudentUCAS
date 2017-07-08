package ps.edu.ucas.portal.model;

/**
 * Created by Ayyad on 7/14/2015.
 */
public enum Day {
    SATURDAY,SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY,FRIDAY;


    static public Day getDay(int index){
        switch (index){
            case 1 : return Day.SATURDAY;
            case 2 : return Day.SUNDAY;
            case 3 : return Day.MONDAY;
            case 4 : return Day.TUESDAY;
            case 5 : return Day.WEDNESDAY;
            case 6 : return Day.THURSDAY;
            default: return Day.FRIDAY;
        }
    }

    static public int getDay(Day day){
        switch (day){
            case SATURDAY : return 1;
            case SUNDAY : return 2;
            case MONDAY : return 3;
            case TUESDAY : return 4;
            case WEDNESDAY : return 5;
            case THURSDAY : return 6;
            default: return 7;
        }
    }

}
