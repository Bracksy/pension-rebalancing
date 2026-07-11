package com.sai.javafx.calendar;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * 
 * @author Sai.Dandem
 * @see http://www.e-zest.net/blog/calendar-control-in-javafx-2-0/
 */
public class FXCalendarControls
{

   /**
    * Arrow Control
    * @author Sai.Dandem
    *
    */
   class Arrow extends StackPane
   {
      private SimpleObjectProperty<Color> fillColor = new SimpleObjectProperty<Color>();

      public Arrow()
      {
         this( Side.BOTTOM );
      }

      public Arrow( Side side )
      {
         getStyleClass().add( "fx-calendar-arrow" ); //$NON-NLS-1$
         setFillColor( Color.WHITE );
         setScaleX( 1.2 );
         setScaleY( 1.2 );
         switch( side )
         {
            case LEFT:
               setRotate( 90 );
               break;
            case TOP:
               setRotate( 180 );
               break;
            case RIGHT:
               setRotate( 270 );
               break;
            default:
               setRotate( 0 );
         }
      }

      /**
       * @return the fillColor object
       */
      public SimpleObjectProperty<Color> fillColorProperty()
      {
         return fillColor;
      }

      /**
       * @return the fillColor
       */
      public Color getFillColor()
      {
         return fillColor.get();
      }

      /**
       * @param fillColor the fillColor to set
       */
      public void setFillColor( Color fillColor )
      {
         this.fillColor.set( fillColor );
         setStyle( "-fx-background-color: " + FXCalendarUtility.rgbToHex( fillColor ) + ";" ); //$NON-NLS-1$ //$NON-NLS-2$
      }

   }

   /**
    * BaseNavigatorArrowButton
    * @author Sai.Dandem
    *
    */
   class BaseNavigatorArrowButton extends Group
   {

      /**
       * 
       * Constructor of BaseNavigatorArrowButton. Initialize the default values. 
       *
       * @param side Side, baseColor Color
       */
      public BaseNavigatorArrowButton( Side side, Color baseColor, boolean pHasTwoArrows )
      {
         StackPane sp = new StackPane();
         FXCalendarUtility.setBaseColorToNode( this, baseColor );
         sp.setPrefHeight( 16 );
         sp.setPrefWidth( 16 );

         Rectangle rect = new Rectangle( 15, 15 );
         if( pHasTwoArrows )
         {
            rect = new Rectangle( 20, 15 );
         }
         FXCalendarUtility.setBaseColorToNode( rect, baseColor );
         rect.getStyleClass().add( "fx-calendar-navigator-btn" ); //$NON-NLS-1$
         Arrow arrow;
         Arrow lArrow2 = null;
         Group gp = new Group();
         switch( side )
         {
            case LEFT:
               arrow = new Arrow( Side.LEFT );
               if( pHasTwoArrows )
               {
                  lArrow2 = new Arrow( Side.LEFT );
               }
               gp.setTranslateX( -2 );
               break;
            default:
               arrow = new Arrow( Side.RIGHT );
               if( pHasTwoArrows )
               {
                  lArrow2 = new Arrow( Side.RIGHT );
               }
               gp.setTranslateX( 2 );
         }

         
         if( pHasTwoArrows && lArrow2 != null )
         {
            arrow.setLayoutX( 0 );
            lArrow2.setLayoutX( 8 );
            gp.getChildren().add( arrow );
            gp.getChildren().add( lArrow2 );
         }
         else
         {
            gp.getChildren().add( arrow );
         }
         sp.getChildren().addAll( rect, gp );
         getChildren().addAll( sp );
         getStyleClass().add( "fx-calendar-navigator-btnGrp" ); //$NON-NLS-1$
      }
   }

   /**
    * CalendarToggleButton
    * @author Sai.Dandem
    *
    */
   class CalendarToggleButton extends StackPane
   {
      private Text txt;
      private StackPane sp;

      /**
       * 
       * Initializes the CalendarToggleButton 
       *
       * @param text String, userDate Object
       */
      public CalendarToggleButton( String text, Object userData )
      {

         setUserData( userData );
         setPrefHeight( 18 );
         setPrefWidth( 44 );

         sp = new StackPane();
         sp.getStyleClass().add( "fx-calendar-toggleButton" ); //$NON-NLS-1$
         sp.setPrefHeight( 16 );
         sp.setPrefWidth( 44 );

         txt = new Text( text );
         txt.getStyleClass().add( "fx-calendar-toggleButton-txt" ); //$NON-NLS-1$
         sp.getChildren().add( txt );

         getChildren().add( sp );
      }

      public void setBaseColor( Color color )
      {
         FXCalendarUtility.setBaseColorToNode( sp, color );
         FXCalendarUtility.setBaseColorToNode( txt, color );
      }

      public void setText( String text )
      {
         txt.setText( text );
      }

      public void setData( Object obj )
      {
         setUserData( obj );
      }

   }

   /**
    * NormalButton
    * @author Sai.Dandem
    *
    */
   class NormalButton extends Button
   {
      /**
       * 
       * Initializes a normal button. Set a new skin 
       *
       * @param txt String
       */
      public NormalButton( String txt )
      {
         super( txt );
         getStyleClass().add("calendarButton");
      }
   }

   /**
    * YearNavigatorArrowButton
    * @author Sai.Dandem
    *
    */
   class YearNavigatorArrowButton extends Group
   {

      /**
       * 
       * Initializes the YearNavigatorArrowButton.
       *
       * @param side Side, baseColor Color
       */
      public YearNavigatorArrowButton( Side side, Color baseColor )
      {
         StackPane sp = new StackPane();
         FXCalendarUtility.setBaseColorToNode( this, baseColor );
         sp.setPrefHeight( 16 );
         sp.setPrefWidth( 16 );

         Rectangle rect = new Rectangle( 15, 15 );
         FXCalendarUtility.setBaseColorToNode( rect, baseColor );
         rect.getStyleClass().add( "fx-calendar-year-navigator-btn" ); //$NON-NLS-1$

         Arrow arrow;
         Group gp = new Group();
         switch( side )
         {
            case LEFT:
               arrow = new Arrow( Side.LEFT );
               gp.setTranslateX( -1 );
               break;
            default:
               arrow = new Arrow( Side.RIGHT );
               gp.setTranslateX( 1 );
         }
         arrow.setFillColor( baseColor );
         gp.getChildren().add( arrow );
         sp.getChildren().addAll( rect, gp );
         getChildren().addAll( sp );
         getStyleClass().add( "fx-calendar-year-navigator-btnGrp" ); //$NON-NLS-1$

      }
   }

}
