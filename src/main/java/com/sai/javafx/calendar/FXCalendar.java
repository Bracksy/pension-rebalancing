package com.sai.javafx.calendar;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 *
 * @author Sai.Dandem
 * @see <a href="http://www.e-zest.net/blog/calendar-control-in-javafx-2-0/">...</a>
 */
public class FXCalendar extends HBox
{
   private SimpleIntegerProperty mSelectedDate = new SimpleIntegerProperty();
   private SimpleIntegerProperty mSelectedMonth = new SimpleIntegerProperty();
   private SimpleIntegerProperty mSelectedYear = new SimpleIntegerProperty();
   private SimpleBooleanProperty mTriggered = new SimpleBooleanProperty();
   private final SimpleObjectProperty<Color> mBaseColor = new SimpleObjectProperty<Color>();
   private SimpleDoubleProperty mDateTextWidth = new SimpleDoubleProperty( 74 );
   private SimpleObjectProperty<Date> mValue = new SimpleObjectProperty<Date>();
   private FXCalendarUtility mFxCalendarUtility;
   private TextField mDateTxtField;
   private Label mDayLabel = null;
   private static boolean mIsInstanciated = false;

   private Popup mPopup;
   private DatePicker mDatePicker;
   private final SimpleObjectProperty<Locale> mLocale = new SimpleObjectProperty<Locale>();
   private final static String DEFAULT_STYLE_CLASS = "fx-calendar"; //$NON-NLS-1$

   // start Constructor
   public FXCalendar( final TextField pTextField, final Label pLabel, final Locale pLocale )
   {
      // set default settings
      super();
      super.getStyleClass().add( DEFAULT_STYLE_CLASS );
      this.mLocale.set( pLocale );
      this.mBaseColor.set( Color.web( "#313131" ) ); //$NON-NLS-1$
      this.mDateTxtField = pTextField;
      this.mDayLabel = pLabel;
      // config calendar
      configureCalendar();
      // config listeners
      configureListeners();
   }
   
   public FXCalendar( final TextField pTextField, final Locale pLocale )
   {
	   this(pTextField, null, pLocale);
   }

   // calendar configuration
   private void configureCalendar()
   {
      mFxCalendarUtility = new FXCalendarUtility();

      mPopup = new Popup();

      mPopup.setAutoHide( true );
      mPopup.setAutoFix( true );
      mPopup.setHideOnEscape( true );
      
      // set default close operation for popup
      defaultCloseOperationForPopup();
      
      /* Creating the date text field. */
      mDateTxtField.prefWidthProperty().bind( mDateTextWidth );
      this.prefWidthProperty().bind( mDateTextWidth.add( 26 ) );
   }
   
   public void show()
   {
      if( !mIsInstanciated )
      {
        mIsInstanciated = true;
        initiatePopUp();
        showPopup(); 
      }  
   }
   
   private void defaultCloseOperationForPopup()
   {
      if ( mDateTxtField != null && mDateTxtField.getParent() != null)
      {
         mDateTxtField.getParent().getScene().addEventFilter( MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
         {
            @Override
            public void handle( MouseEvent pEvent )
            {
               double lPosX = pEvent.getX();
               double lPosY = pEvent.getY();
               
               if( !contains( lPosX, lPosY ) )
               {
                  hidePopup();
               }
            }
         } );
      }
   }
   
   // Listener configuration
   private void configureListeners()
   {
      /* Adding listeners when the date cell is selected. */
      triggeredProperty().addListener( new ChangeListener<Boolean>()
      {
         @Override
         public void changed( ObservableValue<? extends Boolean> paramObservableValue, Boolean paramT1, Boolean paramT2 )
         {
            if( paramT2 )
            {
               final Integer day = selectedDateProperty().get();
               final Integer month = selectedMonthProperty().get();
               final Integer year = selectedYearProperty().get();
               if( day != 0 && month > -1 && year != 0 )
               {
                  String lDateStr = FXCalendarUtility.getDateStr( day, month + 1, year, getLocale() );
                  try
                  {
                     valueProperty().set( FXCalendarUtility.getDate( lDateStr, getLocale() ) );
                  }
                  catch( ParseException pException )
                  {
                     System.out.println( "Could not parse date: " + lDateStr ); //$NON-NLS-1$
                  }
               }
               setTriggered( false );
            }
         }
      } );

      /*
       * Changes to be done in text box on change of seletedDate , selectedMonth and selectedYear in DatePicker.
       */
      ChangeListener<Object> listener = new ChangeListener<Object>()
      {
         @Override
         public void changed( ObservableValue<? extends Object> arg0, Object arg1, Object arg2 )
         {
            showDateInTextField();
         }
      };

      selectedDateProperty().addListener( listener );
      selectedMonthProperty().addListener( listener );
      selectedYearProperty().addListener( listener );

      /* Adding change listeners for locale. */
      ChangeListener<Locale> localeListener = new ChangeListener<Locale>()
      {
         @Override
         public void changed( ObservableValue<? extends Locale> arg0, Locale arg1, Locale arg2 )
         {
            if( mDatePicker != null )
            {
               refreshLocale( getLocale() );
            }
         }
      };
      localeProperty().addListener( localeListener );

      /* Adding listeners for styles. */
      getStyleClass().addListener( new ListChangeListener<String>()
      {
         @Override
         public void onChanged( Change<? extends String> paramChange )
         {
            mDateTxtField.getStyleClass().clear();
            mDateTxtField.getStyleClass().addAll( "text-input", "text-field" ); //$NON-NLS-1$ //$NON-NLS-2$
            for( String clazz : getStyleClass() )
            {
               if( !clazz.equals( DEFAULT_STYLE_CLASS ) )
               {
                  mDateTxtField.getStyleClass().add( clazz );
               }
            }
         }
      } );
   }

   	// shows the date in the text field
	public void showDateInTextField() {
		int date = selectedDateProperty().get();
		int month = selectedMonthProperty().get();
		int year = selectedYearProperty().get();
		if (date != 0 && month != -1 && year != 0) {
			String selectedDateStr = FXCalendarUtility.getDateStr(date, month + 1, year, getLocale());

			mDateTxtField.setText(selectedDateStr);

			if (mDayLabel != null) {
				String dayStr = ""; //$NON-NLS-1$
				try {
					dayStr = FXCalendarUtility.getDayStr(selectedDateStr, getLocale()) + ","; //$NON-NLS-1$
				} catch (ParseException e) {
					System.out.println(String.format("Could not parse date <%s>", selectedDateStr)); //$NON-NLS-1$
				}
				mDayLabel.setText(dayStr);
			}
		} else {
			mDateTxtField.setText(""); //$NON-NLS-1$
			if (mDayLabel != null) {
				mDayLabel.setText(""); //$NON-NLS-1$
			}
		}
	}

   // resets locale value
   public void refreshLocale( Locale locale )
   {
      mFxCalendarUtility.resetShortestWeekDays( locale );
      mFxCalendarUtility.resetShortMonths( locale );
      mFxCalendarUtility.resetMonths( locale );
      mDatePicker.getBasePane().setLabelText(locale);
      mDatePicker.getBasePane().setWeekLabels();
   }

   /**
    * Method to initiate the pop up before showing.
    */
   private void initiatePopUp()
   {  
      if( mDatePicker == null )
      {
         mDatePicker = new DatePicker( FXCalendar.this, getLocale() );
         mPopup.getContent().add( mDatePicker );
      }
      // set selected year to 0 if the date textfield is empty
      if( mDateTxtField.getText().equals( "" ) ) //$NON-NLS-1$
      {
         setSelectedYear( 0 );
      }

      try
      {
         setDateValue( FXCalendarUtility.getDate( mDateTxtField.getText(), getLocale() ) );
      }
      catch( ParseException pException )
      {
         // No valid date. Just continue with no date
      }

      // If there is no date selected, then setting the system date.
      if( getSelectedYear() == 0 )
      {
         Calendar currentDate = FXCalendarUtility.getCurrentDateCalendar();
         mDatePicker.selectedDateProperty().set( currentDate.get( Calendar.DAY_OF_MONTH ) );
         mDatePicker.selectedMonthProperty().set( currentDate.get( Calendar.MONTH ) );
         mDatePicker.selectedYearProperty().set( currentDate.get( Calendar.YEAR ) );
      }
      else
      {
         // Copying the date from calendar to date picker.
         mDatePicker.selectedDateProperty().set( selectedDateProperty().get() );
         mDatePicker.selectedMonthProperty().set( selectedMonthProperty().get() );
         mDatePicker.selectedYearProperty().set( selectedYearProperty().get() );
      }
      
      mDatePicker.getStylesheets().add( FXCalendar.class.getResource( "/styles/calendar_styles.css" ).toExternalForm() ); //$NON-NLS-1$
      mDatePicker.getBasePane().generateDates();
      mDatePicker.showBasePane();
   }

   /**
    * Method to show the pop up.
    */
   private void showPopup()
   { 
      final Point2D lPoint = mDateTxtField.localToScene( 0.0, 0.0 );
      double lHeight = mDateTxtField.getHeight();
      
      mPopup.show( mDateTxtField.getParent(),lPoint.getX() + mDateTxtField.getScene().getX() + mDateTxtField.getScene().getWindow().getX(),
            lPoint.getY() + mDateTxtField.getScene().getY() + mDateTxtField.getScene().getWindow().getY() + lHeight );
   }

   /**
    * Method to set the date value, if the text field is not empty
    */
   private void setDateValue( final Date pDate )
   {
      if ( pDate != null )
      {
         Calendar lCalendar = new GregorianCalendar();
         lCalendar.setTime( pDate );
         setSelectedDate( lCalendar.get( Calendar.DAY_OF_MONTH ) );
         setSelectedMonth( lCalendar.get( Calendar.MONTH ) );
         setSelectedYear( lCalendar.get( Calendar.YEAR ) );
         mDatePicker.selectedDateProperty().set( getSelectedDate() );
         mDatePicker.selectedMonthProperty().set( getSelectedMonth() );
         mDatePicker.selectedYearProperty().set( getSelectedYear() );
      }
   }

   /**
    * Method to hide the pop up.
    */
   public void hidePopup()
   {
      mIsInstanciated = false;
      mPopup.hide();
   }

   /**
    * @return the baseColor
    */
   public Color getBaseColor()
   {
      return mBaseColor.get();
   }

   /**
    * @param color
    *            the baseColor to set
    */
   public void setBaseColor( Color color )
   {
      this.mBaseColor.set( color );
   }

   /**
    * @return baseColor Property
    */
   public SimpleObjectProperty<Color> baseColorProperty()
   {
      return mBaseColor;
   }

   /**
    * @return the locale
    */
   public Locale getLocale()
   {
      return mLocale.get();
   }

   /**
    * @param locale
    *            the locale to set
    */
   public void setLocale( Locale locale )
   {
      this.mLocale.set( locale );
   }

   /**
    * @return locale Property
    */
   public SimpleObjectProperty<Locale> localeProperty()
   {
      return mLocale;
   }

   /**
    * @return the dateTextWidth
    */
   public Double getDateTextWidth()
   {
      return mDateTextWidth.get();
   }

   /**
    * @param width
    *            the dateTextWidth to set
    */
   public void setDateTextWidth( Double width )
   {
      this.mDateTextWidth.set( width );
   }

   /**
    * @return dateTextWidth Property
    */
   public SimpleDoubleProperty dateTextWidthProperty()
   {
      return mDateTextWidth;
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

   /**
    * @return the value
    */
   public Date getValue()
   {
      return this.mValue.get();
   }

   /**
    * @param pDate
    *            the value to set
    */
   public void setValue( Date pDate )
   {
      this.mValue.set( pDate );

      if( pDate != null )
      {
         Calendar lCal = new GregorianCalendar();
         lCal.setTime( pDate );
         selectedDateProperty().set( lCal.get( Calendar.DAY_OF_MONTH ) );
         selectedMonthProperty().set( lCal.get( Calendar.MONTH ) );
         selectedYearProperty().set( lCal.get( Calendar.YEAR ) );
      }
      else
      {
         selectedDateProperty().set( 0 );
         selectedMonthProperty().set( 0 );
         selectedYearProperty().set( 0 );
      }
   }

   /**
    * Method to clear the value in the calendar.
    */
   public void clear()
   {
      setValue( null );
   }

   public SimpleObjectProperty<Date> valueProperty()
   {
      return mValue;
   }

   public FXCalendarUtility getFXCalendarUtility()
   {
      return mFxCalendarUtility;
   }

   public void setTriggered( Boolean triggered )
   {
      this.mTriggered.set( triggered );
   }

   public SimpleBooleanProperty triggeredProperty()
   {
      return mTriggered;
   }

   public TextField getTextField()
   {
      return mDateTxtField;
   }
}
