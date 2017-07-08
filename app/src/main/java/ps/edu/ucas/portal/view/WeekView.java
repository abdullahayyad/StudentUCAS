package ps.edu.ucas.portal.view;

/**
 * Created by Ayyad on 7/14/2015.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.OverScroller;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import ps.edu.ucas.portal.R;
import ps.edu.ucas.portal.model.Day;
import ps.edu.ucas.portal.model.EventCourse;


/**
 * Created by Raquib-ul-Alam Kanak on 7/21/2014.
 * Website: http://april-shower.com
 */
public class WeekView extends View {

    public static final int LENGTH_SHORT = 1;
    public static final int LENGTH_LONG = 2;
    private final Context mContext;

    private Paint mTimeTextPaint;
    private float mTimeTextWidth;
    private float mTimeTextHeight;
    private Paint mHeaderTextPaint;
    private Paint mHeaderTextPaintDay;
    private float mHeaderTextHeight;
    private GestureDetectorCompat mGestureDetector;
    private OverScroller mScroller;
    private PointF mCurrentOrigin = new PointF(0f, 0f);
    private Direction mCurrentScrollDirection = Direction.NONE;
    private Paint mHeaderBackgroundPaint;
    private float mWidthPerDay;
    private Paint mDayBackgroundPaint;
    private Paint mHourSeparatorPaint;
    private Paint mHalfHourSeparatorPaint;
    private float mHeaderMarginBottom;
    private Paint mTodayBackgroundPaint;
    private Paint mTodayHeaderTextPaint;
    private Paint mEventBackgroundPaint;
    private float mHeaderColumnWidth;
    private List<EventRect> mEventRects;
    private List<MyWeekDays> mDayTitle;
    private float mHoursTops[] = new float[24];
    private float mDaysLefts[] = new float[7];
    private TextPaint mEventTextPaint;
    private Paint mHeaderColumnBackgroundPaint;
    private Scroller mStickyScroller;

    private boolean mRefreshEvents = false;
    private float mDistanceY = 0;
    private float mDistanceX = 0;
    private Direction mCurrentFlingDirection = Direction.NONE;

    // Attributes and their default values.
    private int mHourHeight = 50;
    private int mColumnGap = 3;
    private int mFirstDayOfWeek = Calendar.SUNDAY;
    private int mTextSize = 12;
    private int mHeaderColumnPadding = 10;
    private int mHeaderColumnTextColor = Color.BLACK;
    private int mHeaderColumnTextColorDay = Color.rgb(0,153,204);
    private int mNumberOfVisibleDays = 20;
    private int mHeaderRowPadding = 10;
    private int mHeaderRowBackgroundColor = Color.WHITE;
    private int mDayBackgroundColor = Color.rgb(245, 245, 245);
    private int mHourSeparatorColor = Color.rgb(220, 220, 230);
    private int mTodayBackgroundColor = Color.rgb(0, 0, 254);
    private int mHourSeparatorHeight = 3;
    private int mHalfHourSeparatorHeight = 1;
    private int mTodayHeaderTextColor = Color.rgb(39, 137, 228);
    private int mEventTextSize = 12;
    private int mEventTextColor = Color.WHITE;
    private int mEventPadding = 8;
    private int mHeaderColumnBackgroundColor = Color.WHITE;
    private int mDefaultEventColor;

    private int mDayNameLength = LENGTH_LONG;

    // Listeners.
    private EventClickListener mEventClickListener;
    private TimeClickListener mTimeClickListener;
    private EventLongPressListener mEventLongPressListener;

    private OnDayClickListener mOnDayClickListener;
    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            mScroller.forceFinished(true);
            mStickyScroller.forceFinished(true);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mCurrentScrollDirection == Direction.NONE) {
                if (Math.abs(distanceX) > Math.abs(distanceY)){
                    //mCurrentScrollDirection = Direction.HORIZONTAL;
                    //mCurrentFlingDirection = Direction.HORIZONTAL;
                }
                else {
                    mCurrentFlingDirection = Direction.VERTICAL;
                    mCurrentScrollDirection = Direction.VERTICAL;
                }
            }
            mDistanceX = distanceX;
            mDistanceY = distanceY;
            invalidate();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            mScroller.forceFinished(true);
            mStickyScroller.forceFinished(true);

            if (mCurrentFlingDirection == Direction.HORIZONTAL){
                mScroller.fling((int) mCurrentOrigin.x, 0, (int) velocityX, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
            }
            else if (mCurrentFlingDirection == Direction.VERTICAL){
                mScroller.fling(0, (int) mCurrentOrigin.y, 0, (int) velocityY, 0, 0, (int) -(mHourHeight * 14 + mHeaderTextHeight + mHeaderRowPadding * 2 - getHeight()), 0);
            }

            ViewCompat.postInvalidateOnAnimation(WeekView.this);
            return true;
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            for(MyWeekDays myWeekDays : mDayTitle){

                if(myWeekDays.textBounds != null && e.getX()>myWeekDays.textBounds.left && e.getX()<myWeekDays.textBounds.right && e.getY() > myWeekDays.textBounds.top && e.getY() < myWeekDays.textBounds.bottom){

                    mOnDayClickListener.onDayClick(myWeekDays.position);
                    playSoundEffect(SoundEffectConstants.CLICK);
                    break;
                }
            }





            boolean mEvent = false;
            if (mEventRects != null && mEventClickListener != null) {
                List<EventRect> reversedEventRects = mEventRects;
                Collections.reverse(reversedEventRects);
                for (EventRect event : reversedEventRects) {
                    for(int i=0;i<event.rectF.length;i++){
                    if (event.rectF[i] != null && e.getX() > event.rectF[i].left && e.getX() < event.rectF[i].right && e.getY() > event.rectF[i].top && e.getY() < event.rectF[i].bottom) {
                        mEventClickListener.onEventClick(event.event, event.rectF[i],i);
                        playSoundEffect(SoundEffectConstants.CLICK);
                        mEvent = true;
                        break;
                    }
                    }
                }
            }
            if (!mEvent && mTimeClickListener != null) {
                int mClickedHour = -1;
                for (int i = 0; i < 24; i++) {
                    if (i == 23) {
                        mClickedHour = 23;
                        break;
                    }
                    if (mHoursTops[i] > -1 && e.getY() > mHoursTops[i] && e.getY() < mHoursTops[i + 1]) {
                        mClickedHour = i;
                        break;
                    }
                }
                int mClickedDay = -1;
                for (int i = 0; i < 7; i++) {
                    if (i == 6) {
                        mClickedDay = 7;
                        break;
                    }
                    if (i == 0 && e.getX() < mDaysLefts[0]) {
                        mClickedDay = 1;
                        break;
                    }
                    if (mDaysLefts[i] > -1 && e.getX() > mDaysLefts[i] && e.getX() < mDaysLefts[i + 1]) {
                        mClickedDay = i + 1;
                        break;
                    }
                }
                if (mClickedDay == -1) {
                    mClickedDay = 1;
                }

                mTimeClickListener.onTimeClick(mClickedHour, mClickedDay);
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);

            if (mEventLongPressListener != null && mEventRects != null) {
                List<EventRect> reversedEventRects = mEventRects;
                Collections.reverse(reversedEventRects);
                for (EventRect event : reversedEventRects) {
                    for(int i=0;i<event.rectF.length;i++) {

                        if (event.rectF != null && e.getX() > event.rectF[i].left && e.getX() < event.rectF[i].right && e.getY() > event.rectF[i].top && e.getY() < event.rectF[i].bottom) {
                            mEventLongPressListener.onEventLongPress(event.event, event.rectF[i]);
                            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                            break;
                        }
                    }
                }
            }
        }
    };


    private enum Direction {
        NONE, HORIZONTAL, VERTICAL
    }

    public WeekView(Context context) {
        this(context, null);
    }

    public WeekView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Hold references.
        mContext = context;

        // Get the attribute values (if any).
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WeekView, 0, 0);
        try {
            mFirstDayOfWeek = a.getInteger(R.styleable.WeekView_firstDayOfWeek, mFirstDayOfWeek);
            mHourHeight = a.getDimensionPixelSize(R.styleable.WeekView_hourHeight, mHourHeight);
            mTextSize = a.getDimensionPixelSize(R.styleable.WeekView_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, context.getResources().getDisplayMetrics()));
            mHeaderColumnPadding = a.getDimensionPixelSize(R.styleable.WeekView_headerColumnPadding, mHeaderColumnPadding);
            mColumnGap = a.getDimensionPixelSize(R.styleable.WeekView_columnGap, mColumnGap);
            mHeaderColumnTextColor = a.getColor(R.styleable.WeekView_headerColumnTextColor, mHeaderColumnTextColor);
            mNumberOfVisibleDays = a.getInteger(R.styleable.WeekView_noOfVisibleDays, mNumberOfVisibleDays);
            mHeaderRowPadding = a.getDimensionPixelSize(R.styleable.WeekView_headerRowPadding, mHeaderRowPadding);
            mHeaderRowBackgroundColor = a.getColor(R.styleable.WeekView_headerRowBackgroundColor, mHeaderRowBackgroundColor);
            mDayBackgroundColor = a.getColor(R.styleable.WeekView_dayBackgroundColor, mDayBackgroundColor);
            mHourSeparatorColor = a.getColor(R.styleable.WeekView_hourSeparatorColor, mHourSeparatorColor);
            mTodayBackgroundColor = a.getColor(R.styleable.WeekView_todayBackgroundColor, mTodayBackgroundColor);
            mHourSeparatorHeight = a.getDimensionPixelSize(R.styleable.WeekView_hourSeparatorHeight, mHourSeparatorHeight);
            mTodayHeaderTextColor = a.getColor(R.styleable.WeekView_todayHeaderTextColor, mTodayHeaderTextColor);
            mEventTextSize = a.getDimensionPixelSize(R.styleable.WeekView_eventTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mEventTextSize, context.getResources().getDisplayMetrics()));
            mEventTextColor = a.getColor(R.styleable.WeekView_eventTextColor, mEventTextColor);
            mEventPadding = a.getDimensionPixelSize(R.styleable.WeekView_hourSeparatorHeight, mEventPadding);
            mHeaderColumnBackgroundColor = a.getColor(R.styleable.WeekView_headerColumnBackground, mHeaderColumnBackgroundColor);
            mDayNameLength = a.getInteger(R.styleable.WeekView_dayNameLength, mDayNameLength);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        // Get the date today.
       /* mToday = Calendar.getInstance();
        mToday.set(Calendar.HOUR_OF_DAY, 0);
        mToday.set(Calendar.MINUTE, 0);
        mToday.set(Calendar.SECOND, 0);*/

        // Scrolling initialization.
        mGestureDetector = new GestureDetectorCompat(mContext, mGestureListener);
        mScroller = new OverScroller(mContext);
        mStickyScroller = new Scroller(mContext);

        // Measure settings for time column.
        mTimeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimeTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTimeTextPaint.setTextSize(mTextSize);
        mTimeTextPaint.setColor(mHeaderColumnTextColor);
        Rect rect = new Rect();
        mTimeTextPaint.getTextBounds("00 PM", 0, "00 PM".length(), rect);
        mTimeTextWidth = mTimeTextPaint.measureText("00 PM");
        mTimeTextHeight = rect.height();
        mHeaderMarginBottom = mTimeTextHeight / 2;

        // Measure settings for header row.
        mHeaderTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderTextPaint.setColor(mHeaderColumnTextColor);
        mHeaderTextPaint.setTextAlign(Paint.Align.CENTER);
        mHeaderTextPaint.setTextSize(mTextSize);
        mHeaderTextPaint.getTextBounds("00 PM", 0, "00 PM".length(), rect);
        mHeaderTextHeight = rect.height();
        mHeaderTextPaint.setTypeface(Typeface.DEFAULT_BOLD);



        mHeaderTextPaintDay = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderTextPaintDay.setColor(mHeaderColumnTextColorDay);
        mHeaderTextPaintDay.setTextAlign(Paint.Align.CENTER);
        mHeaderTextPaintDay.setTextSize(mTextSize);
        mHeaderTextPaintDay.getTextBounds("00 PM", 0, "00 PM".length(), rect);

        mHeaderTextPaintDay.setTypeface(Typeface.DEFAULT_BOLD);



        // Prepare header background paint.
        mHeaderBackgroundPaint = new Paint();
        mHeaderBackgroundPaint.setColor(mHeaderRowBackgroundColor);

        // Prepare day background color paint.
        mDayBackgroundPaint = new Paint();
        mDayBackgroundPaint.setColor(mDayBackgroundColor);

        // Prepare hour separator color paint.
        mHourSeparatorPaint = new Paint();
        mHourSeparatorPaint.setStyle(Paint.Style.STROKE);
        mHourSeparatorPaint.setStrokeWidth(mHourSeparatorHeight);
        mHourSeparatorPaint.setColor(mHourSeparatorColor);


        // Prepare halfHour separator color paint.
        mHalfHourSeparatorPaint = new Paint();
        mHalfHourSeparatorPaint.setStyle(Paint.Style.STROKE);
        mHalfHourSeparatorPaint.setStrokeWidth(mHalfHourSeparatorHeight);
        mHalfHourSeparatorPaint.setPathEffect(new DashPathEffect(new float[] { 5, 2, 2 }, 0));
        mHalfHourSeparatorPaint.setColor(mHourSeparatorColor);


        // Prepare today background color paint.
        mTodayBackgroundPaint = new Paint();
        mTodayBackgroundPaint.setColor(mTodayBackgroundColor);

        // Prepare today header text color paint.
        mTodayHeaderTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTodayHeaderTextPaint.setTextAlign(Paint.Align.CENTER);
        mTodayHeaderTextPaint.setTextSize(mTextSize);
        mTodayHeaderTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTodayHeaderTextPaint.setColor(mTodayHeaderTextColor);

        // Prepare event background color.
        mEventBackgroundPaint = new Paint();
        mEventBackgroundPaint.setColor(Color.rgb(174, 208, 238));

        // Prepare header column background color.
        mHeaderColumnBackgroundPaint = new Paint();
        mHeaderColumnBackgroundPaint.setColor(mHeaderColumnBackgroundColor);

        // Prepare event text size and color.
        mEventTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        mEventTextPaint.setStyle(Paint.Style.FILL);
        mEventTextPaint.setColor(mEventTextColor);
        mEventTextPaint.setTextSize(mEventTextSize);
       // mStartDate = (Calendar) mToday.clone();

        // Set default event color.
        mDefaultEventColor = Color.parseColor("#9fc6e7");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the header row.
        drawHeaderRowAndEvents(canvas);

        // Draw the time column and all the axes/separators.
        drawTimeColumnAndAxes(canvas);

        // Hide everything in the first cell (top left corner).
        canvas.drawRect(0, 0, mTimeTextWidth + mHeaderColumnPadding * 2, mHeaderTextHeight + mHeaderRowPadding * 2, mHeaderBackgroundPaint);

        // Hide anything that is in the bottom margin of the header row.
        canvas.drawRect(mHeaderColumnWidth, mHeaderTextHeight + mHeaderRowPadding * 2, getWidth(), mHeaderRowPadding * 2 + mHeaderTextHeight + mHeaderMarginBottom + mTimeTextHeight/2, mHeaderColumnBackgroundPaint);
    }

    private void drawTimeColumnAndAxes(Canvas canvas) {
        // Do not let the view go above/below the limit due to scrolling. Set the max and min limit of the scroll.
       if (mCurrentScrollDirection == Direction.VERTICAL) {
            if (mCurrentOrigin.y - mDistanceY > 0) mCurrentOrigin.y = 0;
            else if (mCurrentOrigin.y - mDistanceY < -(mHourHeight * 14 + mHeaderTextHeight + mHeaderRowPadding * 2 - getHeight())) mCurrentOrigin.y = -(mHourHeight * 14 + mHeaderTextHeight + mHeaderRowPadding * 2 - getHeight());
            else mCurrentOrigin.y -= mDistanceY;
        }

        // Draw the background color for the header column.
        canvas.drawRect(0, mHeaderTextHeight + mHeaderRowPadding * 2, mHeaderColumnWidth, getHeight(), mHeaderColumnBackgroundPaint);

        for (int i = 0; i < 12; i++) {
            float top = mHeaderTextHeight + mHeaderRowPadding * 2 + mCurrentOrigin.y + mHourHeight * i + mHeaderMarginBottom;

            // Draw the text if its y position is not outside of the visible area. The pivot point of the text is the point at the bottom-right corner.
            if (top < getHeight()) {
                canvas.drawText(getTimeString(i+8), mTimeTextWidth + mHeaderColumnPadding, top + mTimeTextHeight, mTimeTextPaint);
                mHoursTops[i] = top + mTimeTextHeight;
            }
            else {
                mHoursTops[i] = -1;
            }
        }
    }

    private void drawHeaderRowAndEvents(Canvas canvas) {
        // Calculate the available width for each day.
        mDayTitle = new ArrayList<MyWeekDays>();
        Calendar getDay = Calendar.getInstance();

        mHeaderColumnWidth = mTimeTextWidth + mHeaderColumnPadding *2;
        mWidthPerDay = getWidth() - mHeaderColumnWidth - mColumnGap * mNumberOfVisibleDays ;
        mWidthPerDay = mWidthPerDay/mNumberOfVisibleDays;


        // Consider scroll offset.
        if (mCurrentScrollDirection == Direction.HORIZONTAL) mCurrentOrigin.x -= mDistanceX;
        int leftDaysWithGaps = (int) -(Math.ceil(mCurrentOrigin.x / (mWidthPerDay + mColumnGap)));
        float startFromPixel = mCurrentOrigin.x + (mWidthPerDay+mColumnGap) * leftDaysWithGaps +
                mHeaderColumnWidth;
        float startPixel = startFromPixel;

        // Prepare to iterate for each day.
       // Calendar day = (Calendar) mToday.clone();
       //  day.add(Calendar.HOUR, 6);

        // Prepare to iterate for each hour to draw the hour lines.
        int lineCount = (int) ((getHeight() - mHeaderTextHeight - mHeaderRowPadding * 2 -
                mHeaderMarginBottom) / mHourHeight) + 1;
        lineCount = (lineCount) * (mNumberOfVisibleDays+1);
        float[] hourLines = new float[lineCount * 4];
        float[] halfHourLines = new float[lineCount * 4];
        // Clear the cache for events rectangles.
        if (mEventRects != null) {
            for (EventRect eventRect: mEventRects) {
                //eventRect.rectF = null;
            }
        }
//her allloba
        for (int dayNumber = 0;
             dayNumber < 7;
             dayNumber++) {

            // Check if the day is today.
         //   day = (Calendar) mToday.clone();
          //  day.add(Calendar.DATE, dayNumber - 1);
            boolean sameDay = false;

            // Get more events if necessary. We want to store the events 3 months beforehand. Get
            // events only when it is the first iteration of the loop.
            if (mEventRects == null || mRefreshEvents || (dayNumber == leftDaysWithGaps + 1 )) {

                mRefreshEvents = false;
            }

            // Draw background color for each day.
            float start =  (startPixel < mHeaderColumnWidth ? mHeaderColumnWidth : startPixel);
            if (mWidthPerDay + startPixel - start> 0) canvas.drawRect(start, mHeaderTextHeight + mHeaderRowPadding * 2 + mTimeTextHeight/2 + mHeaderMarginBottom, startPixel + mWidthPerDay, getHeight(), sameDay ? mTodayBackgroundPaint : mDayBackgroundPaint);

            if (getDayToday(getDay.get(Calendar.DAY_OF_WEEK)) == dayNumber ) canvas.drawRect(start, mHeaderTextHeight + mHeaderRowPadding * 2 + mTimeTextHeight/2 + mHeaderMarginBottom, startPixel + mWidthPerDay, getHeight(), mTodayBackgroundPaint  );

            // Prepare the separator lines for hours.
            int i = 0;

            for (int hourNumber = 0; hourNumber < 12; hourNumber++) {
                float top = mHeaderTextHeight + mHeaderRowPadding * 2 + mCurrentOrigin.y + mHourHeight * hourNumber + mTimeTextHeight/2 + mHeaderMarginBottom;
                float top2 = mHeaderTextHeight + mHeaderRowPadding * 2 + mCurrentOrigin.y + ((mHourHeight*0.5f)*2) * hourNumber+ mTimeTextHeight/2 + mHeaderMarginBottom;

                if (top > mHeaderTextHeight + mHeaderRowPadding * 2 + mTimeTextHeight/2 + mHeaderMarginBottom - mHourSeparatorHeight && top < getHeight() && startPixel + mWidthPerDay - start > 0){
                     hourLines[i * 4] = start;
                     hourLines[i * 4 + 1] = top;
                     hourLines[i * 4 + 2] = startPixel + mWidthPerDay;
                     hourLines[i * 4 + 3] = top;


                    i++;

                }
            }
            canvas.drawLines(hourLines, mHourSeparatorPaint);

            int j =0;
            for (int hourNumber = 0; hourNumber < 24; hourNumber++) {
                float top = mHeaderTextHeight + mHeaderRowPadding * 2 + mCurrentOrigin.y + mHourHeight * hourNumber/2 + mTimeTextHeight/2 + mHeaderMarginBottom;

                if (top > mHeaderTextHeight + mHeaderRowPadding * 2 + mTimeTextHeight/2 + mHeaderMarginBottom - mHourSeparatorHeight && top < getHeight() && startPixel + mWidthPerDay - start > 0){

                    halfHourLines[j * 4] = start;
                    halfHourLines[j * 4 + 1] = top ;
                    halfHourLines[j * 4 + 2] = startPixel + mWidthPerDay;
                    halfHourLines[j * 4 + 3] = top;

                    j++;

                }
            }

            canvas.drawLines(halfHourLines, mHalfHourSeparatorPaint);



            // Draw the events.

            drawEvents(dayNumber+1, startPixel, canvas);

            startPixel += mWidthPerDay + mColumnGap;
        }

        // Draw the header background.
        canvas.drawRect(0, 0, getWidth(), mHeaderTextHeight + mHeaderRowPadding * 2, mHeaderBackgroundPaint);

        // Draw the header row texts.
        startPixel = startFromPixel;
        /*for (int dayNumber=leftDaysWithGaps+1; dayNumber <= leftDaysWithGaps + mNumberOfVisibleDays + 1; dayNumber++) {
            // Check if the day is today.
            day = (Calendar) mToday.clone();
            day.add(Calendar.DATE, dayNumber - 1);
            boolean sameDay = isSameDay(day, mToday);
            int dayOfWeek = day.get(Calendar.DAY_OF_WEEK) - 1;
            mDaysLefts[dayOfWeek] = startPixel + mWidthPerDay / 2;
            Log.d("DaysLefts", "Day (" + Integer.toString(dayOfWeek) + ") x: " + Float.toString(mDaysLefts[dayOfWeek]));
            // Draw the day labels.
            String dayLabel = String.format("%s ", getDayName(day));
            //String dayLabel = String.format("%s %d/%02d", getDayName(day), day.get(Calendar.MONTH) + 1, day.get(Calendar.DAY_OF_MONTH));
            canvas.drawText(dayLabel, startPixel + mWidthPerDay / 2, mHeaderTextHeight + mHeaderRowPadding, sameDay ? mTodayHeaderTextPaint : mHeaderTextPaint);
            startPixel += mWidthPerDay + mColumnGap;
        }*/

        startPixel = startFromPixel;

        for (int i = 0; i < 7; i++) {
            mDaysLefts[i] = startPixel + mWidthPerDay / 2;
            String dayLabel = String.format("%s ", getDayName(i + 1));
            //String dayLabel = String.format("%s %d/%02d", getDayName(day), day.get(Calendar.MONTH) + 1, day.get(Calendar.DAY_OF_MONTH));

            Rect textBounds = new Rect();

            mHeaderTextPaintDay.getTextBounds(dayLabel, 0, dayLabel.length(), textBounds);
            textBounds.left = (int) mDaysLefts[i] - textBounds.right;
            textBounds.right = (int) mDaysLefts[i] + textBounds.right;
           // textBounds.top = (int)  mHeaderTextHeight + mHeaderRowPadding + textBounds.top;
            textBounds.top = 0;
            textBounds.bottom = (int)  mHeaderTextHeight + mHeaderRowPadding +  textBounds.bottom;

            mDayTitle.add(new MyWeekDays(i,textBounds));
            if (getDayToday(getDay.get(Calendar.DAY_OF_WEEK)) == i )
                canvas.drawText(dayLabel, startPixel + mWidthPerDay / 2, mHeaderTextHeight + mHeaderRowPadding, mHeaderTextPaintDay);

                else
                    canvas.drawText(dayLabel, startPixel + mWidthPerDay / 2, mHeaderTextHeight + mHeaderRowPadding, mHeaderTextPaint);
            startPixel += mWidthPerDay + mColumnGap;



        }

    }

    /**
     * Draw all the events of a particular day.
     * @param day The day.
     * @param startFromPixel The left position of the day area. The events will never go any left from this value.
     * @param canvas The canvas to draw upon.
     */
    private void drawEvents(int day, float startFromPixel, Canvas canvas) {
        if (mEventRects != null && mEventRects.size() > 0) {


            for (int i = 0; i < mEventRects.size(); i++) {
                for (int j = 0; j < mEventRects.get(i).event.getCourseTimes().size(); j++) {

                    if ((mEventRects.get(i).event.getCourseTimes().get(j).getDayOfWeekNumber() > -1 && mEventRects.get(i).event.getCourseTimes().get(j).getDayOfWeekNumber() == day)) {

                        // Calculate top.
                        float top = (mEventRects.get(i).event.getCourseTimes().get(j).getStartHour() * 60) - 480 + mEventRects.get(i).event.getCourseTimes().get(j).getStartMinute();
                        top = mHourHeight * 24 * top / 1440 + mCurrentOrigin.y + mHeaderTextHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom + mTimeTextHeight / 2;
                        float originalTop = top;
                        if (top < mHeaderTextHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom + mTimeTextHeight / 2)
                            top = mHeaderTextHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom + mTimeTextHeight / 2;

                        // Calculate bottom.
                        float bottom = (mEventRects.get(i).event.getCourseTimes().get(j).getEndHour() * 60) - 480 + mEventRects.get(i).event.getCourseTimes().get(j).getEndMinute();
                        bottom = mHourHeight * 24 * bottom / 1440 + mCurrentOrigin.y + mHeaderTextHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom + mTimeTextHeight / 2;

                        // Calculate left and right.
                        float left = startFromPixel;
                        float right = startFromPixel + mWidthPerDay;
                        if (left < mHeaderColumnWidth) left = mHeaderColumnWidth;

                        RectF eventRectF = new RectF(left, top, right, bottom);
                        if (bottom > mHeaderTextHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom + mTimeTextHeight / 2 && left < right &&
                                eventRectF.right > mHeaderColumnWidth &&
                                eventRectF.left < getWidth() &&
                                eventRectF.bottom > mHeaderTextHeight + mHeaderRowPadding * 2 + mTimeTextHeight / 2 + mHeaderMarginBottom &&
                                eventRectF.top < getHeight() &&
                                left < right
                                ) {
                            mEventRects.get(i).rectF[j] = eventRectF;
                            mEventBackgroundPaint.setColor(Color.parseColor(mContext.getString( mEventRects.get(i).event.getColor(i+1))));
                            canvas.drawRect(mEventRects.get(i).rectF[j], mEventBackgroundPaint);
                            drawText(mEventRects.get(i).event.getCourseName(), mEventRects.get(i).rectF[j], canvas, originalTop, startFromPixel);
                        } else
                            mEventRects.get(i).rectF[j] = null;
                    }
                }
            }
        }
    }

    /**
     * Draw the name of the event on top of the event rectangle.
     * @param text The text to draw.
     * @param rect The rectangle on which the text is to be drawn.
     * @param canvas The canvas to draw upon.
     * @param originalTop The original top position of the rectangle. The rectangle may have some of its portion outside of the visible area.
     * @param originalLeft The original left position of the rectangle. The rectangle may have some of its portion outside of the visible area.
     */
    private void drawText(String text, RectF rect, Canvas canvas, float originalTop, float originalLeft) {
        if (rect.right - rect.left - mEventPadding * 2 < 0) return;


        // Get text dimensions
        StaticLayout mTextLayout = new StaticLayout(text, mEventTextPaint, (int) (rect.right - originalLeft - mEventPadding * 2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        // Crop height
        if (mTextLayout.getHeight() > rect.height() - mEventPadding * 2) {
            int lineCount = mTextLayout.getLineCount();
            int availableLineCount = (int) Math.floor(lineCount * (rect.bottom - originalTop - mEventPadding * 2) / mTextLayout.getHeight());
            float widthAvailable = (rect.right - originalLeft - mEventPadding * 2) * availableLineCount;
            mTextLayout = new StaticLayout(TextUtils.ellipsize(text, mEventTextPaint, widthAvailable, TextUtils.TruncateAt.END), mEventTextPaint, (int) (rect.right - originalLeft - mEventPadding * 2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }

        // Draw text
        canvas.save();
        canvas.translate(originalLeft + mEventPadding, originalTop + mEventPadding);
        mTextLayout.draw(canvas);
        canvas.restore();


    }


    /**
     * A class to hold reference to the events and their visual representation.
     */
    private class EventRect {
        public EventCourse event;
        public RectF[] rectF ;

        public EventRect(EventCourse event, RectF[] rectF) {
            this.event = event;
            this.rectF = rectF;
        }


    }



    private class MyWeekDays {
        public int position;
        public Rect textBounds ;


        public MyWeekDays(int position, Rect textBounds) {
            this.position = position;
            this.textBounds  = textBounds;

        }
    }



    /**
     * Gets more events of one/more month(s) if necessary. This method is called when the user is
     * scrolling the week view. The week view stores the events of three months: the visible month,
     * the previous month, the next month.
     * @param events The day where the user is currently is.
     */


    public void setEvents(List<EventCourse> events) {
        mRefreshEvents = true;
        if (mEventRects == null)
            mEventRects = new ArrayList<EventRect>();
        mEventRects.clear();
        for (EventCourse event : events) {

            mEventRects.add(new EventRect(event, new RectF[event.getCourseTimes().size()]));
        }

        invalidate();
       }


    /////////////////////////////////////////////////////////////////
    //
    //      Functions related to setting and getting the properties.
    //
    /////////////////////////////////////////////////////////////////

    public void setOnEventClickListener (EventClickListener listener) {
        this.mEventClickListener = listener;
    }






    public void setOnDayClickListenerClickListener (OnDayClickListener listener) {
        this.mOnDayClickListener = listener;
    }



    public EventClickListener getEventClickListener() {
        return mEventClickListener;
    }

    public void setOnTimeClickListener (TimeClickListener listener) {
        this.mTimeClickListener = listener;
    }

    public TimeClickListener getTimeClickListener() {
        return mTimeClickListener;
    }


    public EventLongPressListener getEventLongPressListener() {
        return mEventLongPressListener;
    }

    public void setEventLongPressListener(EventLongPressListener eventLongPressListener) {
        this.mEventLongPressListener = eventLongPressListener;
    }

    /**
     * Get the number of visible days in a week.
     * @return The number of visible days in a week.
     */
    public int getNumberOfVisibleDays() {
        return mNumberOfVisibleDays;
    }

    /**
     * Set the number of visible days in a week.
     * @param numberOfVisibleDays The number of visible days in a week.
     */
    public void setNumberOfVisibleDays(int numberOfVisibleDays) {
        this.mNumberOfVisibleDays = numberOfVisibleDays;
        mCurrentOrigin.x = 0;
        mCurrentOrigin.y = 0;
        invalidate();
    }

    public int getHourHeight() {
        return mHourHeight;
    }

    public void setHourHeight(int hourHeight) {
        mHourHeight = hourHeight;
        invalidate();
    }

    public int getColumnGap() {
        return mColumnGap;
    }

    public void setColumnGap(int columnGap) {
        mColumnGap = columnGap;
        invalidate();
    }

    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    /**
     * Set the first day of the week. First day of the week is used only when the week view is first
     * drawn. It does not of any effect after user starts scrolling horizontally.
     * <p>
     *     <b>Note:</b> This method will only work if the week view is set to display more than 6 days at
     *     once.
     * </p>
     * @param firstDayOfWeek The supported values are {@link Calendar#SUNDAY},
     * {@link Calendar#MONDAY}, {@link Calendar#TUESDAY},
     * {@link Calendar#WEDNESDAY}, {@link Calendar#THURSDAY},
     * {@link Calendar#FRIDAY}.
     */
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        mFirstDayOfWeek = firstDayOfWeek;
        invalidate();
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
        mTodayHeaderTextPaint.setTextSize(mTextSize);
        mHeaderTextPaint.setTextSize(mTextSize);
        mTimeTextPaint.setTextSize(mTextSize);
        invalidate();
    }

    public int getHeaderColumnPadding() {
        return mHeaderColumnPadding;
    }

    public void setHeaderColumnPadding(int headerColumnPadding) {
        mHeaderColumnPadding = headerColumnPadding;
        invalidate();
    }

    public int getHeaderColumnTextColor() {
        return mHeaderColumnTextColor;
    }

    public void setHeaderColumnTextColor(int headerColumnTextColor) {
        mHeaderColumnTextColor = headerColumnTextColor;
        invalidate();
    }

    public int getHeaderRowPadding() {
        return mHeaderRowPadding;
    }

    public void setHeaderRowPadding(int headerRowPadding) {
        mHeaderRowPadding = headerRowPadding;
        invalidate();
    }

    public int getHeaderRowBackgroundColor() {
        return mHeaderRowBackgroundColor;
    }

    public void setHeaderRowBackgroundColor(int headerRowBackgroundColor) {
        mHeaderRowBackgroundColor = headerRowBackgroundColor;
        invalidate();
    }

    public int getDayBackgroundColor() {
        return mDayBackgroundColor;
    }

    public void setDayBackgroundColor(int dayBackgroundColor) {
        mDayBackgroundColor = dayBackgroundColor;
        invalidate();
    }

    public int getHourSeparatorColor() {
        return mHourSeparatorColor;
    }

    public void setHourSeparatorColor(int hourSeparatorColor) {
        mHourSeparatorColor = hourSeparatorColor;
        invalidate();
    }

    public int getTodayBackgroundColor() {
        return mTodayBackgroundColor;
    }

    public void setTodayBackgroundColor(int todayBackgroundColor) {
        mTodayBackgroundColor = todayBackgroundColor;
        invalidate();
    }

    public int getHourSeparatorHeight() {
        return mHourSeparatorHeight;
    }

    public void setHourSeparatorHeight(int hourSeparatorHeight) {
        mHourSeparatorHeight = hourSeparatorHeight;
        invalidate();
    }

    public int getTodayHeaderTextColor() {
        return mTodayHeaderTextColor;
    }

    public void setTodayHeaderTextColor(int todayHeaderTextColor) {
        mTodayHeaderTextColor = todayHeaderTextColor;
        invalidate();
    }

    public int getEventTextSize() {
        return mEventTextSize;
    }

    public void setEventTextSize(int eventTextSize) {
        mEventTextSize = eventTextSize;
        mEventTextPaint.setTextSize(mEventTextSize);
        invalidate();
    }

    public int getEventTextColor() {
        return mEventTextColor;
    }

    public void setEventTextColor(int eventTextColor) {
        mEventTextColor = eventTextColor;
        invalidate();
    }

    public int getEventPadding() {
        return mEventPadding;
    }

    public void setEventPadding(int eventPadding) {
        mEventPadding = eventPadding;
        invalidate();
    }

    public int getHeaderColumnBackgroundColor() {
        return mHeaderColumnBackgroundColor;
    }

    public void setHeaderColumnBackgroundColor(int headerColumnBackgroundColor) {
        mHeaderColumnBackgroundColor = headerColumnBackgroundColor;
        invalidate();
    }

    public int getDefaultEventColor() {
        return mDefaultEventColor;
    }

    public void setDefaultEventColor(int defaultEventColor) {
        mDefaultEventColor = defaultEventColor;
        invalidate();
    }

    public int getDayNameLength() {
        return mDayNameLength;
    }


    public void setDayNameLength(int length) {
        if (length != LENGTH_LONG && length != LENGTH_SHORT) {
            throw new IllegalArgumentException("length parameter must be either LENGTH_LONG or LENGTH_SHORT");
        }
        this.mDayNameLength = length;
    }

    /////////////////////////////////////////////////////////////////
    //
    //      Functions related to scrolling.
    //
    /////////////////////////////////////////////////////////////////

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {

            if (mCurrentScrollDirection == Direction.HORIZONTAL) {
                float leftDays = Math.round(mCurrentOrigin.x / (mWidthPerDay + mColumnGap));
                int nearestOrigin = (int) (mCurrentOrigin.x - leftDays * (mWidthPerDay+mColumnGap));
                mStickyScroller.startScroll((int) mCurrentOrigin.x, 0, - nearestOrigin, 0);
                ViewCompat.postInvalidateOnAnimation(WeekView.this);
            }
            mCurrentScrollDirection = Direction.NONE;
        }
        return mGestureDetector.onTouchEvent(event);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            if (Math.abs(mScroller.getFinalX() - mScroller.getCurrX()) < mWidthPerDay + mColumnGap && Math.abs(mScroller.getFinalX() - mScroller.getStartX()) != 0) {
                mScroller.forceFinished(true);
                float leftDays = Math.round(mCurrentOrigin.x / (mWidthPerDay + mColumnGap));
                int nearestOrigin = (int) (mCurrentOrigin.x - leftDays * (mWidthPerDay+mColumnGap));
                mStickyScroller.startScroll((int) mCurrentOrigin.x, 0, - nearestOrigin, 0);
                ViewCompat.postInvalidateOnAnimation(WeekView.this);
            }
            else {
                if (mCurrentFlingDirection == Direction.VERTICAL) mCurrentOrigin.y = mScroller.getCurrY();
                else mCurrentOrigin.x = mScroller.getCurrX();
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
        if (mStickyScroller.computeScrollOffset()) {
            mCurrentOrigin.x = mStickyScroller.getCurrX();
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    /////////////////////////////////////////////////////////////////
    //
    //      Public methods.
    //
    /////////////////////////////////////////////////////////////////



    /////////////////////////////////////////////////////////////////
    //
    //      Interfaces.
    //
    /////////////////////////////////////////////////////////////////

    public interface EventClickListener {
        public void onEventClick(EventCourse event, RectF eventRect,int position);
    }

    public interface TimeClickListener {
        public void onTimeClick(int hour, int day);

    }

    public interface EventLongPressListener {
        public void onEventLongPress(EventCourse event, RectF eventRect);
    }


    public interface OnDayClickListener {
        public void onDayClick(int position);
    }

    /////////////////////////////////////////////////////////////////
    //
    //      Helper methods.
    //
    /////////////////////////////////////////////////////////////////

    /**
     * Checks if an integer array contains a particular value.
     * @param list The haystack.
     * @param value The needle.
     * @return True if the array contains the value. Otherwise returns false.
     */
    private boolean containsValue(int[] list, int value) {
        for (int i = 0; i < list.length; i++){
            if (list[i] == value)
                return true;
        }
        return false;
    }


    /**
     * Converts an int (0-23) to time string (e.g. 12 PM).
     * @param hour The time. Limit: 0-23.
     * @return The string representation of the time.
     */
    private String getTimeString(int hour) {
        String amPm;
        if (hour >= 0 && hour < 12) amPm = "ص";
        else amPm = "م";
        if (hour == 0) hour = 12;
        if (hour > 12) hour -= 12;
        return String.format("%02d %s", hour, amPm);
    }


    /**
     * Checks if two times are on the same day.
     * @param dayOne The first day.
     * @param dayTwo The second day.
     * @return Whether the times are on the same day.
     */


    /**
     * Get the day name of a given date.
     * @param date The date.
     * @return The first the characters of the day name.
     */
    private String getDayName(Calendar date) {
        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
        return getDayName(dayOfWeek);
    }

    private String getDayName(int dayOfWeek) {
        if (Day.MONDAY == Day.getDay(dayOfWeek)) return "الإثنين";
        else if (Day.TUESDAY == Day.getDay(dayOfWeek)) return "الثلاثاء";
        else if (Day.WEDNESDAY == Day.getDay(dayOfWeek)) return "الأربعاء";
        else if (Day.THURSDAY == Day.getDay(dayOfWeek)) return "الخميس";
        else if (Day.FRIDAY == Day.getDay(dayOfWeek)) return "الجمعة";
        else if (Day.SATURDAY == Day.getDay(dayOfWeek)) return "السبت";
        else if (Day.SUNDAY == Day.getDay(dayOfWeek)) return "الأحد";
        /*if (Calendar.MONDAY == dayOfWeek) return (mDayNameLength == LENGTH_SHORT ? "M" : "MON");
        else if (Calendar.TUESDAY == dayOfWeek) return (mDayNameLength == LENGTH_SHORT ? "T" : "TUE");
        else if (Calendar.WEDNESDAY == dayOfWeek) return (mDayNameLength == LENGTH_SHORT ? "W" : "WED");
        else if (Calendar.THURSDAY == dayOfWeek) return (mDayNameLength == LENGTH_SHORT ? "T" : "THU");
        else if (Calendar.FRIDAY == dayOfWeek) return (mDayNameLength == LENGTH_SHORT ? "F" : "FRI");
        else if (Calendar.SATURDAY == dayOfWeek) return (mDayNameLength == LENGTH_SHORT ? "S" : "SAT");
        else if (Calendar.SUNDAY == dayOfWeek) return (mDayNameLength == LENGTH_SHORT ? "S" : "SUN");*/
        return "";
    }




    public static int getDayToday(int dayOfWeek) {
        if(Calendar.MONDAY == dayOfWeek)
            return 2;

        else if (Calendar.TUESDAY == dayOfWeek) return 3;
        else if (Calendar.WEDNESDAY == dayOfWeek) return 4;
        else if (Calendar.THURSDAY == dayOfWeek) return 5;
        else if (Calendar.SATURDAY == dayOfWeek)return 0;
        else if (Calendar.SUNDAY == dayOfWeek) return 1;
        return 6;
    }

}