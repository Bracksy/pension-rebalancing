package com.sai.javafx.calendar;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * 
 * @author Sai.Dandem
 * @see http://www.e-zest.net/blog/calendar-control-in-javafx-2-0/
 */
public class FXCalendarCell
{

   /**
    * AbstractCell
    * @author Sai.Dandem
    *
    */
   abstract class AbstractCell extends StackPane
   {

      protected Text mText;

      public AbstractCell()
      {
         super();
      }

      public abstract void setCellId( String id );

      public abstract void setCellWidth( double width );

      public abstract void setCellHeight( double height );

      public abstract void setCellStyle( String styleClass );

      /**
       * @return the text
       */
      public Text getTxt()
      {
         return mText;
      }

      /**
       * @param txt the txt to set
       */
      public void setTxt( Text txt )
      {
         this.mText = txt;
      }

   }

   /**
    * DateCell
    * @author Sai.Dandem
    *
    */
   class DateCell extends AbstractCell
   {

      private SimpleIntegerProperty mCellDate = new SimpleIntegerProperty();
      private SimpleIntegerProperty mCellMonth = new SimpleIntegerProperty();
      private SimpleIntegerProperty mCellYear = new SimpleIntegerProperty();
      private boolean mPreviousState = false;
      private boolean mWeekNumCell = false;

      public DateCell( String id, double width, double height )
      {
         super();
         setCellId( id );
         setCellWidth( width - 1 );
         setCellHeight( height - 1 );
         
         super.mText = new Text();
         
         mText.getStyleClass().add( "fx-calendar-datetext" ); //$NON-NLS-1$
         getStyleClass().add( "fx-calendar-basic-datecell" ); //$NON-NLS-1$
 
        
         getChildren().add( mText );
         
         setOnMouseEntered( new EventHandler<MouseEvent>()
         {
            @Override
            public void handle( MouseEvent arg0 )
            {
               mPreviousState = mText.isDisable();
               mText.setDisable( false );
            }
         } );

         setOnMouseExited( new EventHandler<MouseEvent>()
         {
            @Override
            public void handle( MouseEvent arg0 )
            {
               mText.setDisable( mPreviousState );
            }
         } );
         // Disabling the cell and clearing the text if the date is below 01/01/01.
         cellYearProperty().addListener( new InvalidationListener()
         {
            @Override
            public void invalidated( Observable paramObservable )
            {
               if( getCellYear() < 1 )
               {
                  mText.setText( "" ); //$NON-NLS-1$
                  DateCell.this.setDisable( true );
               }
               else
               {
                  DateCell.this.setDisable( false );
               }
            }
         } );
      }

      @Override
      public void setCellId( String id )
      {
         super.setId( id );
      }

      @Override
      public void setCellWidth( double width )
      {
         super.setPrefWidth( width );
      }

      @Override
      public void setCellHeight( double height )
      {
         super.setPrefHeight( height );
      }

      public void setCellStyle( String styleClass )
      {
         getStyleClass().remove( 0 );
         getStyleClass().add( styleClass );
      }

      /**
       * @return the cellDate
       */
      public SimpleIntegerProperty cellDateProperty()
      {
         return mCellDate;
      }

      /**
       * @return the cellDate
       */
      public Integer getCellDate()
      {
         return mCellDate.getValue();
      }

      /**
       * @param cellDate the cellDate to set
       */
      public void setCellDate( Integer cellDate )
      {
         this.mCellDate.set( cellDate );
      }

      /**
       * @return the cellMonth
       */
      public SimpleIntegerProperty cellMonthProperty()
      {
         return mCellMonth;
      }

      /**
       * @return the cellMonth
       */
      public Integer getCellMonth()
      {
         return mCellMonth.getValue();
      }

      /**
       * @param cellMonth the cellMonth to set
       */
      public void setCellMonth( Integer cellMonth )
      {
         this.mCellMonth.set( cellMonth );
      }

      /**
       * @return the cellYear
       */
      public SimpleIntegerProperty cellYearProperty()
      {
         return mCellYear;
      }

      /**
       * @return the cellYear
       */
      public Integer getCellYear()
      {
         return mCellYear.getValue();
      }

      /**
       * @param cellYear the cellYear to set
       */
      public void setCellYear( Integer cellYear )
      {
         this.mCellYear.set( cellYear );
      }

      /**
       * @return the weekNumCell
       */
      public boolean isWeekNumCell()
      {
         return mWeekNumCell;
      }

      /**
       * @param weekNumCell the weekNumCell to set
       */
      public void setWeekNumCell( boolean weekNumCell )
      {
         this.mWeekNumCell = weekNumCell;
      }

      public void setCellFocused( boolean b )
      {
         super.setFocused( b );
      }

      public boolean getCellFocused()
      {
         return super.isFocused();
      }
   }

   /**
    * WeekCell
    * @author Sai.Dandem
    *
    */
   class WeekCell extends AbstractCell
   {

      public WeekCell( String id, String content, double width, double height )
      {
         super();
         setCellId( id );
         setCellWidth( width - 1 );
         setCellHeight( height );

         super.mText = new Text( content );
         mText.getStyleClass().add( "fx-calendar-weektext" ); //$NON-NLS-1$
         
         getChildren().add( mText );
      }

      public void setContent( String str )
      {
         super.mText.setText( str );
      }

      @Override
      public void setCellId( String id )
      {
         super.setId( id );
      }

      @Override
      public void setCellWidth( double width )
      {
         super.setPrefWidth( width );
      }

      @Override
      public void setCellHeight( double height )
      {
         super.setPrefHeight( height );
      }

      public void setCellStyle( String styleClass )
      {
         getStyleClass().add( styleClass );
      }

   }

}
