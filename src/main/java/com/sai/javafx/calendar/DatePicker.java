package com.sai.javafx.calendar;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Locale;

/**
 * 
 * @author Sai.Dandem
 * @see http://www.e-zest.net/blog/calendar-control-in-javafx-2-0/
 */
public class DatePicker extends StackPane
{

   private SimpleIntegerProperty mSelectedDate = new SimpleIntegerProperty();
   private SimpleIntegerProperty mSelectedMonth = new SimpleIntegerProperty();
   private SimpleIntegerProperty mSelectedYear = new SimpleIntegerProperty();
   private Rectangle2D mCalendarBounds = new Rectangle2D( 100, 100, 205, 196 );
   private FXCalendar mFxCalendar;
   private BasePane mBasePane;

   /**
    * 
    * Constructor which initialize the DatePicker
    *
    * @param pFxCalendar FXCalendar object
    * @param pLocale Locale to use
    */
   public DatePicker( final FXCalendar pFxCalendar, final Locale pLocale )
   {
      super();
      this.mFxCalendar = pFxCalendar;
      this.mSelectedDate.set( pFxCalendar.getSelectedDate() );
      this.mSelectedMonth.set( pFxCalendar.getSelectedMonth() );
      this.mSelectedYear.set( pFxCalendar.getSelectedYear() );
      this.mFxCalendar.setLocale( pLocale );
      setPrefHeight( this.mCalendarBounds.getHeight() );
      setPrefWidth( this.mCalendarBounds.getWidth() );
      setAlignment( Pos.TOP_LEFT );
      FXCalendarUtility.setBaseColorToNode( this, pFxCalendar.getBaseColor() );
      this.mBasePane = new BasePane( this, pLocale );
      getChildren().addAll( this.mBasePane );
      showBasePane();
   }

   /* GETTER'S FROM FXCALENDAR * */
   public Color getBaseColor()
   {
      return this.mFxCalendar.getBaseColor();
   }

   public FXCalendarUtility getFXCalendarUtility()
   {
      return this.mFxCalendar.getFXCalendarUtility();
   }

   public Locale getLocale()
   {
      return this.mFxCalendar.getLocale();
   }

   public FXCalendar getFxCalendar()
   {
      return this.mFxCalendar;
   }

   public int getSelectedDate()
   {
      return mSelectedDate.get();
   }

   public int getSelectedMonth()
   {
      return mSelectedMonth.get();
   }

   public int getSelectedYear()
   {
      return mSelectedYear.get();
   }

   public void setSelectedDate( int selectedDate )
   {
      this.mSelectedDate.set( selectedDate );
   }

   public void setSelectedMonth( int selectedMonth )
   {
      this.mSelectedMonth.set( selectedMonth );
   }

   public void setSelectedYear( int selectedYear )
   {
      this.mSelectedYear.set( selectedYear );
   }

   public SimpleIntegerProperty selectedDateProperty()
   {
      return mSelectedDate;
   }

   public SimpleIntegerProperty selectedMonthProperty()
   {
      return mSelectedMonth;
   }

   public SimpleIntegerProperty selectedYearProperty()
   {
      return mSelectedYear;
   }

   /* GETTER'S FROM DATEPICKER * */
   public Rectangle2D getBounds()
   {
      return mCalendarBounds;
   }

   public BasePane getBasePane()
   {
      return mBasePane;
   }

   public void showBasePane()
   {
      mBasePane.setVisible( true );
   }

   public void incrementMonth()
   {
      int currentMonth = this.mSelectedMonth.get();
      if( currentMonth >= ( mFxCalendar.getFXCalendarUtility().getMonths( getLocale() ).length - 2 ) )
      {
         this.mSelectedMonth.set( 0 );
         this.mSelectedYear.set( this.mSelectedYear.get() + 1 );
      }
      else
      {
         this.mSelectedMonth.set( currentMonth + 1 );
      }
   }

   public void decrementMonth()
   {
      int currentMonth = this.mSelectedMonth.get();
      if( currentMonth <= 0 )
      {
         this.mSelectedMonth.set( mFxCalendar.getFXCalendarUtility().getMonths(getLocale()).length - 2 );
         this.mSelectedYear.set( this.mSelectedYear.get() - 1 );
      }
      else
      {
         this.mSelectedMonth.set( currentMonth - 1 );
      }
   }
   
   public void incrementYear()
   {
      int lCurrentYear = this.mSelectedYear.get();
      this.mSelectedYear.set( lCurrentYear + 1 );
   }
   
   public void decrementYear()
   {
      int lCurrentYear = this.mSelectedYear.get();
      if( lCurrentYear <= 0 )
      {
         this.mSelectedYear.set( 1 );
      }
      else
      {
         this.mSelectedYear.set( lCurrentYear - 1 );
      }
   }
}
