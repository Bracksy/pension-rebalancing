package com.sai.javafx.calendar;

import com.sai.javafx.calendar.FXCalendarCell.DateCell;
import com.sai.javafx.calendar.FXCalendarCell.WeekCell;
import com.sai.javafx.calendar.FXCalendarControls.BaseNavigatorArrowButton;
import com.sai.javafx.calendar.FXCalendarControls.NormalButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Sai.Dandem
 * @see <a href="https://www.e-zest.net/blog/calendar-control-in-javafx-2-0/">...</a>
 */
public class BasePane extends Group
{
	private DatePicker mDatePicker;
	private StackPane mNavigatorPane;
	private StackPane mWeekPane;
	private StackPane mDeskPane;
	private StackPane mFooterPane;
	private Label mDisplayLabel;
	private ObservableList<WeekCell> mWeekCellList = FXCollections.observableArrayList();
	private ObservableList<DateCell> mDateCellList = FXCollections.observableArrayList();
	private BaseNavigatorArrowButton mPrevMonthBtn;

	public BasePane(DatePicker datePicker, Locale locale)
	{
		super();
		this.mDatePicker = datePicker;
		configureNavigator(locale);
		configureWeekHeader();
		configureDesk();
		configureFooter();
	}

	/*
	 * *********************************************************************************************************************
	 * ****************************** MONTH NAVIGATOR
	 * ******************************
	 * *******************************************
	 * *******************************
	 * *******************************************
	 */
	private void configureNavigator(final Locale locale)
	{
		mNavigatorPane = new StackPane();
		/*
		 * Changes to be done in BasePaneNavigator on change of selectedMonth
		 * and selectedYear in DatePicker.
		 */
		ChangeListener<Object> listener = new ChangeListener<Object>()
		{
			@Override
			public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2)
			{
				setLabelText(locale);
			}
		};

		mDatePicker.selectedMonthProperty().addListener(listener);
		mDatePicker.selectedYearProperty().addListener(listener);

		FXCalendarUtility.setBaseColorToNode(mNavigatorPane, mDatePicker.getBaseColor());
		mNavigatorPane.setPrefWidth(mDatePicker.getBounds().getWidth());
		mNavigatorPane.setPrefHeight(26);
		mNavigatorPane.getStyleClass().add("fx-calendar-navigator"); //$NON-NLS-1$

		/* Displaying the Month & Year of the selected date. */
		mDisplayLabel = new Label();
		mDisplayLabel.getStyleClass().add("fx-calendar-navigator-label"); //$NON-NLS-1$
		setLabelText(locale);

		/* Calculating the distance for the arrow buttons from the center. */
		double pos = (mDatePicker.getBounds().getWidth() / 2) - 15;
		double lPos2 = pos - 20;

		// Getting the Next Year Button
		BaseNavigatorArrowButton lNextYearButton = new FXCalendarControls().new BaseNavigatorArrowButton(Side.RIGHT,
				mDatePicker.getBaseColor(), true);
		lNextYearButton.setTranslateX(pos);
		lNextYearButton.setOnMouseClicked(new EventHandler<Event>()
		{

			@Override
			public void handle(Event pEvent)
			{
				mDatePicker.incrementYear();

			}
		});

		/* Getting the Next Month Button. */
		BaseNavigatorArrowButton nextMonthBtn = new FXCalendarControls().new BaseNavigatorArrowButton(Side.RIGHT,
				mDatePicker.getBaseColor(), false);
		nextMonthBtn.setTranslateX(lPos2);
		nextMonthBtn.setOnMouseClicked(new EventHandler<Event>()
		{
			@Override
			public void handle(Event arg0)
			{
				mDatePicker.incrementMonth();
			}
		});

		// Getting the Previous Year Button
		BaseNavigatorArrowButton lPrevYearButton = new FXCalendarControls().new BaseNavigatorArrowButton(Side.LEFT,
				mDatePicker.getBaseColor(), true);
		lPrevYearButton.setTranslateX(-pos);
		lPrevYearButton.setOnMouseClicked(new EventHandler<Event>()
		{
			@Override
			public void handle(Event pEvent)
			{
				mDatePicker.decrementYear();

			}
		});

		/* Getting the Previous Month Button. */
		mPrevMonthBtn = new FXCalendarControls().new BaseNavigatorArrowButton(Side.LEFT, mDatePicker.getBaseColor(),
				false);
		mPrevMonthBtn.setTranslateX(-lPos2);
		mPrevMonthBtn.setOnMouseClicked(new EventHandler<Event>()
		{
			@Override
			public void handle(Event arg0)
			{
				if (!(mDatePicker.getSelectedMonth() == 0 && mDatePicker.getSelectedYear() == 1))
				{
					mDatePicker.decrementMonth();
				}
			}
		});

		mNavigatorPane.getChildren().addAll(mDisplayLabel, nextMonthBtn, mPrevMonthBtn, lNextYearButton,
				lPrevYearButton);
		getChildren().add(mNavigatorPane);
	}

	public void setLabelText(Locale locale)
	{
		mDisplayLabel.setText(this.mDatePicker.getFXCalendarUtility().getMonths(locale)[this.mDatePicker.getSelectedMonth()]
				+ " " + this.mDatePicker.getSelectedYear()); //$NON-NLS-1$
	}

	/*
	 * *********************************************************************************************************************
	 * ****************************** WEEK HEADER ******************************
	 * *
	 * *************************************************************************
	 * *******************************************
	 */
	private void configureWeekHeader()
	{
		mWeekPane = new StackPane();

		FXCalendarUtility.setBaseColorToNode(mWeekPane, mDatePicker.getBaseColor());
		mWeekPane.setPrefWidth(mDatePicker.getBounds().getWidth());
		mWeekPane.setPrefHeight(18);
		mWeekPane.getStyleClass().add("fx-calendar-weekpane"); //$NON-NLS-1$

		int daysOfWeek = 7;

		TilePane tp = new TilePane();
		tp.setPrefColumns(daysOfWeek);

		generateWeekCells(daysOfWeek);
		for (WeekCell weekCell : mWeekCellList)
		{
			tp.getChildren().add(weekCell);
		}

		mWeekPane.getChildren().add(tp);
		mWeekPane.setTranslateY(mNavigatorPane.getPrefHeight());
		getChildren().add(mWeekPane);
	}

	// Generate week cells
	private void generateWeekCells(int count)
	{
		Rectangle2D cellBounds = calculateBounds();
		WeekCell cell;
		List<WeekCell> wkCells = new ArrayList<WeekCell>(count);

		String[] wks = mDatePicker.getFXCalendarUtility().getShortestWeekDays(mDatePicker.getLocale());
		for (int i = 1; i < wks.length; i++)
		{
			cell = new FXCalendarCell().new WeekCell("week_" + wks[i], wks[i], cellBounds.getWidth(), //$NON-NLS-1$
					cellBounds.getHeight());
			FXCalendarUtility.setBaseColorToNode(cell.getTxt(), mDatePicker.getBaseColor());

			wkCells.add(cell);
		}
		mWeekCellList.addAll(wkCells);
	}

	public void setWeekLabels()
	{
		String[] wks = mDatePicker.getFXCalendarUtility().getShortestWeekDays(mDatePicker.getLocale());
		int pos = 0;
		for (int i = 1; i < wks.length; i++)
		{
			mWeekCellList.get(pos).setContent(wks[i]);
			pos++;
		}
	}

	private Rectangle2D calculateBounds()
	{
		int divFactor = 7;
		double width = mDatePicker.getBounds().getWidth() / divFactor;
		double height = 18;
		return new Rectangle2D(0, 0, width, height);
	}

	/*
	 * *********************************************************************************************************************
	 * ****************************** DATE DESK ******************************
	 * **
	 * ************************************************************************
	 * *******************************************
	 */

	private void configureDesk()
	{
		mDeskPane = new StackPane();
		FXCalendarUtility.setBaseColorToNode(mDeskPane, mDatePicker.getBaseColor());
		mDeskPane.setPrefWidth(mDatePicker.getBounds().getWidth());
		mDeskPane.setPrefHeight(120);
		mDeskPane.getStyleClass().add("fx-calendar-desk"); //$NON-NLS-1$

		TilePane tp = new TilePane();
		tp.setPrefColumns(7);

		generateDateCells();

		for (DateCell dateCell : mDateCellList)
		{
			tp.getChildren().add(dateCell);
		}

		generateDates();

		/*
		 * Changes to be done in BasePaneDesk on change of selectedMonth and
		 * selectedYear in DatePicker.
		 */
		ChangeListener<Object> listener = new ChangeListener<Object>()
		{
			@Override
			public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2)
			{
				generateDates();
			}
		};

		mDatePicker.selectedDateProperty().addListener(listener);
		mDatePicker.selectedMonthProperty().addListener(listener);
		mDatePicker.selectedYearProperty().addListener(listener);

		mDeskPane.getChildren().add(tp);
		mDeskPane.setTranslateY(mNavigatorPane.getPrefHeight() + mWeekPane.getPrefHeight());
		getChildren().add(mDeskPane);

	}

	private void generateDateCells()
	{
		int daysOfWeek = 7;
		Rectangle2D cellBounds = calculateDeskBounds();
		DateCell dateCell;
		List<DateCell> dateCells = new ArrayList<DateCell>(daysOfWeek * 6);

		for (int i = 0; i < (daysOfWeek * 6); i++)
		{
			dateCell = new FXCalendarCell().new DateCell("cell" + i, cellBounds.getWidth(), cellBounds.getHeight()); //$NON-NLS-1$
			FXCalendarUtility.setBaseColorToNode(dateCell, mDatePicker.getBaseColor());
			dateCells.add(dateCell);
		}
		mDateCellList.addAll(dateCells);
	}

	public void generateDates()
	{

		Calendar firstDayOfMonth = FXCalendarUtility.getDate(1, mDatePicker.getSelectedMonth(),
				mDatePicker.getSelectedYear());
		Calendar paneFirstDate = (Calendar) firstDayOfMonth.clone();

		// If Monday is first day of week.
		if (Calendar.getInstance(mDatePicker.getLocale()).getFirstDayOfWeek() == 2)
		{
			int diff = 0;
			if (firstDayOfMonth.get(Calendar.DAY_OF_WEEK) == 1)
			{
				diff = 6;
			} else
			{
				diff = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 2;
			}
			paneFirstDate.add(Calendar.DAY_OF_YEAR, -diff);
		} else
		{
			// If Sunday is first day of week.
			paneFirstDate.add(Calendar.DAY_OF_YEAR, -(firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1));
		}

		Calendar dummyDate = (Calendar) paneFirstDate.clone();
		Calendar systemDate = FXCalendarUtility.getCurrentDateCalendar();

		int fxDate = mDatePicker.getFxCalendar().getSelectedDate();
		int fxMonth = mDatePicker.getFxCalendar().getSelectedMonth();
		int fxYear = mDatePicker.getFxCalendar().getSelectedYear();

		for (final DateCell dateCell : mDateCellList)
		{
			if (!dateCell.isWeekNumCell())
			{
				dateCell.getStyleClass().remove("fx-calendar-basic-datecell-selected"); //$NON-NLS-1$
				dateCell.getTxt().setText(dummyDate.get(Calendar.DAY_OF_MONTH) + ""); //$NON-NLS-1$

				// Setting the date details of the cell.
				dateCell.setCellDate(dummyDate.get(Calendar.DAY_OF_MONTH));
				dateCell.setCellMonth(dummyDate.get(Calendar.MONTH));
				dateCell.setCellYear(dummyDate.get(Calendar.YEAR));

				// Highlighting the current month cells.
				if (dummyDate.get(Calendar.MONTH) == mDatePicker.getSelectedMonth())
				{
					dateCell.getTxt().setDisable(false);
				} else
				{
					dateCell.getTxt().setDisable(true);
					// Not showing the dates below 01/01/01
					if ((mDatePicker.getSelectedMonth() == 0 && mDatePicker.getSelectedYear() == 1)
							&& dateCell.getCellMonth() != 1)
					{
						dateCell.setCellYear(0);
					}
				}

				// Highlighting the current system date.
				if (systemDate.get(Calendar.DAY_OF_MONTH) == dummyDate.get(Calendar.DAY_OF_MONTH)
						&& systemDate.get(Calendar.MONTH) == dummyDate.get(Calendar.MONTH)
						&& systemDate.get(Calendar.YEAR) == dummyDate.get(Calendar.YEAR))
				{
					dateCell.setCellFocused(true);
				} else
				{
					dateCell.setCellFocused(false);
				}

				// Highlighting the Selected date.
				if (fxDate == dummyDate.get(Calendar.DAY_OF_MONTH) && fxMonth == dummyDate.get(Calendar.MONTH)
						&& fxYear == dummyDate.get(Calendar.YEAR))
				{
					// Overriding the dotted line with selected class.
					if (dateCell.getCellFocused())
					{
						dateCell.setCellFocused(false);
					}
					dateCell.getStyleClass().add("fx-calendar-basic-datecell-selected"); //$NON-NLS-1$
				}

				// Setting the event handler for the selected date.
				dateCell.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent event)
					{
						int year = dateCell.getCellYear();
						int month = dateCell.getCellMonth();
						int date = dateCell.getCellDate();
						mDatePicker.setSelectedYear(year);
						mDatePicker.setSelectedMonth(month);
						mDatePicker.setSelectedDate(date);

						mDatePicker.getFxCalendar().setSelectedDate(date);
						mDatePicker.getFxCalendar().setSelectedMonth(month);
						mDatePicker.getFxCalendar().setSelectedYear(year);
						mDatePicker.getFxCalendar().setTriggered(true);

						mDatePicker.getFxCalendar().getTextField().requestFocus();
						mDatePicker.getFxCalendar().showDateInTextField();
						mDatePicker.getFxCalendar().hidePopup();
					}
				});

				// Incrementing the date.
				dummyDate.add(Calendar.DAY_OF_YEAR, 1);
			} else
			{
				// Updating the week number
				if (dummyDate.get(Calendar.DAY_OF_WEEK) == 1)
				{
					dateCell.getTxt().setText((dummyDate.get(Calendar.WEEK_OF_YEAR) - 1) + ""); //$NON-NLS-1$
					dateCell.getTxt().getStyleClass().add("fx-calendar-weektext"); //$NON-NLS-1$
				}
			}
		}
	}

	private Rectangle2D calculateDeskBounds()
	{
		int divFactor = 7;
		double width = mDatePicker.getBounds().getWidth() / divFactor;
		double height = 120 / 6;
		return new Rectangle2D(0, 0, width, height);
	}

	/*
	 * *********************************************************************************************************************
	 * ****************************** FOOTER ******************************
	 * *****
	 * *********************************************************************
	 * *******************************************
	 */
	private void configureFooter()
	{
		mFooterPane = new StackPane();
		FXCalendarUtility.setBaseColorToNode(mFooterPane, mDatePicker.getBaseColor());
		mFooterPane.setPrefWidth(mDatePicker.getBounds().getWidth());
		mFooterPane.setPrefHeight(32);
		mFooterPane.getStyleClass().add("fx-calendar-footer"); //$NON-NLS-1$
		NormalButton todayBtn = new FXCalendarControls().new NormalButton("Heute"); //$NON-NLS-1$

		/**
		 * Event triggering to set the current date of the system.
		 */
		todayBtn.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				Calendar today = FXCalendarUtility.getCurrentDateCalendar();
				mDatePicker.getFxCalendar().setSelectedDate(today.get(Calendar.DAY_OF_MONTH));
				mDatePicker.getFxCalendar().setSelectedMonth(today.get(Calendar.MONTH));
				mDatePicker.getFxCalendar().setSelectedYear(today.get(Calendar.YEAR));
				mDatePicker.getFxCalendar().hidePopup();
			}
		});

		mFooterPane.getChildren().add(todayBtn);
		mFooterPane.setTranslateY(mNavigatorPane.getPrefHeight() + mWeekPane.getPrefHeight()
				+ mDeskPane.getPrefHeight());
		getChildren().add(mFooterPane);
	}
}
