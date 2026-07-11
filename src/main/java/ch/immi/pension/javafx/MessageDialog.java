package ch.immi.pension.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * This class implements all message dialogs used in a application * showMessage * showWarning * showError * showConfirm
 * * showYesNo
 * 
 * @author M.Imhof
 */

/**
 * @author imfm
 *
 */
public class MessageDialog {
	private static String MORE_LABEL = ">>"; //$NON-NLS-1$

	private static String LESS_LABEL = "<<"; //$NON-NLS-1$

	public static String YES_LABEL = "Ja"; //$NON-NLS-1$

	public static String NO_LABEL = "Nein"; //$NON-NLS-1$

	public static String OK_LABEL = "Ok"; //$NON-NLS-1$

	private final Button lOkOrYesButton;

	private final Button lNoButton;

	private Boolean mReturnValue = null;

	private MessageDialog(final String okOrYesButtonLabel, final String lNoButtonLabel) {
		lOkOrYesButton = new Button(okOrYesButtonLabel);
		lNoButton = new Button(lNoButtonLabel);
	}

	/**
	 * Show error message dialog with stacktrace info. If pThrowable is not
	 * null, the dialog will show a "More" button wich opens a TextArea to show
	 * the stacktrace
	 * 
	 * @param pTitle
	 *            title
	 * @param pMessage
	 *            message
	 * @param pThrowable
	 *            If throwable is set, there will be a button to show the stack
	 *            trace
	 */
	public static void showError(final Stage pPrimaryStage, final String pTitle, final String pMessage,
			final Throwable pThrowable) {
		final MessageDialog lDialog = new MessageDialog(OK_LABEL, NO_LABEL);
		lDialog.lOkOrYesButton.setDefaultButton(true);
		lDialog.showMessage(pPrimaryStage, pTitle, pMessage, pThrowable, false, "error32.png"); //$NON-NLS-1$
	}

	/**
	 * Shows error message dialog with title "Error" If pThrowable is not null,
	 * the dialog will show a "More" button wich opens a TextArea to show the
	 * stacktrace
	 * 
	 * @param pMessage
	 *            message
	 */
	public static void showError(final Stage pPrimaryStage, final String pMessage, final Throwable pThrowable) {
		showError(pPrimaryStage, "Error", pMessage, pThrowable); //$NON-NLS-1$
	}

	/**
	 * Show error message dialog
	 * 
	 * @param pTitle
	 *            title
	 * @param pMessage
	 *            message
	 */
	public static void showError(final Stage pPrimaryStage, final String pTitle, final String pMessage) {
		final MessageDialog lDialog = new MessageDialog(OK_LABEL, NO_LABEL);
		lDialog.lOkOrYesButton.setDefaultButton(true);
		lDialog.showMessage(pPrimaryStage, pTitle, pMessage, null, false, "error32.png"); //$NON-NLS-1$
	}

	/**
	 * Shows error message dialog with title "Error"
	 * 
	 * @param pMessage
	 *            message
	 */
	public static void showError(final Stage pPrimaryStage, final String pMessage) {
		showError(pPrimaryStage, "Error", pMessage); //$NON-NLS-1$
	}

	/**
	 * Show warning message dialog
	 * 
	 * @param pTitle
	 *            title
	 * @param pMessage
	 *            message
	 */
	public static void showWarning(final Stage pPrimaryStage, final String pTitle, final String pMessage) {
		final MessageDialog lDialog = new MessageDialog(OK_LABEL, NO_LABEL);
		lDialog.lOkOrYesButton.setDefaultButton(true);
		lDialog.showMessage(pPrimaryStage, pTitle, pMessage, null, false, "warn32.png"); //$NON-NLS-1$
	}

	/**
	 * Shows warning message dialog with title "Warning"
	 * 
	 * @param pMessage
	 *            message
	 */
	public static void showWarning(final Stage pPrimaryStage, final String pMessage) {
		showWarning(pPrimaryStage, "Warning", pMessage); //$NON-NLS-1$
	}

	/**
	 * Show two option message dialog. First option button is default.
	 * 
	 * @param pTitle
	 *            title
	 * @param pMessage
	 *            message
	 * 
	 * @return true if Ok or Yes clicked, false if No clicked and null if
	 *         Cancelled or dialog closed
	 */
	public static Boolean showTwoOption(final Stage pPrimaryStage, final String pTitle, final String pMessage,
			final String okText, final String cancelText) {
		final MessageDialog lDialog = new MessageDialog(okText, cancelText);
		lDialog.lOkOrYesButton.setDefaultButton(true);
		return lDialog.showMessage(pPrimaryStage, pTitle, pMessage, null, true, "question32.png"); //$NON-NLS-1$
	}

	/**
	 * Show yes/no message dialog. Yes button is default.
	 * 
	 * @param pTitle
	 *            title
	 * @param pMessage
	 *            message
	 * 
	 * @return true if Ok or Yes clicked, false if No clicked and null if
	 *         Cancelled or dialog closed
	 */
	public static Boolean showYesNo(final Stage pPrimaryStage, final String pTitle, final String pMessage, final String defaultButton) {
		final MessageDialog lDialog = new MessageDialog(YES_LABEL, NO_LABEL);
		if (NO_LABEL.equals(defaultButton)) {
			lDialog.lNoButton.setDefaultButton(true);
		} else {
			lDialog.lOkOrYesButton.setDefaultButton(true);
		}
		return lDialog.showMessage(pPrimaryStage, pTitle, pMessage, null, true, "question32.png"); //$NON-NLS-1$
	}

	/**
	 * Shows question message dialog with title "Question"
	 * 
	 * @param pMessage
	 *            message
	 * 
	 * @return true if Ok or Yes clicked, false if No clicked and null if
	 *         Cancelled or dialog closed
	 */
	public static Boolean showYesNo(final Stage pPrimaryStage, final String pMessage) {
		return showYesNo(pPrimaryStage, "Question", pMessage, null); //$NON-NLS-1$
	}

	/**
	 * Show information message dialog
	 * 
	 * @param pTitle
	 *            title
	 * @param pMessage
	 *            message
	 */
	public static void showInfo(final Stage pPrimaryStage, final String pTitle, final String pMessage) {
		final MessageDialog lDialog = new MessageDialog(OK_LABEL, NO_LABEL);
		lDialog.lOkOrYesButton.setDefaultButton(true);
		lDialog.showMessage(pPrimaryStage, pTitle, pMessage, null, false, "info32.png"); //$NON-NLS-1$
	}

	/**
	 * Shows information message dialog with title "Information"
	 * 
	 * @param pMessage
	 *            message
	 */
	public static void showInfo(final Stage pPrimaryStage, final String pMessage) {
		showInfo(pPrimaryStage, "Information", pMessage); //$NON-NLS-1$
	}

	/**
	 * Show message dialog
	 * 
	 * @param pPrimaryStage
	 *            Stage of parent of null if not available
	 * @param pTitle
	 * @param pMessage
	 * @param pThrowable
	 *            If throwable is set, there will be a button to show the stack
	 *            trace
	 * @param pIsYesNo
	 *            Should the buttons be Yes/No or Ok/Cancel
	 * @param pIconName
	 *            Name of icon
	 * 
	 * @return true if Ok or Yes clicked, false if No clicked and null if
	 *         Cancelled or dialog closed
	 */
	private Boolean showMessage(final Stage pPrimaryStage, final String pTitle, final String pMessage,
			final Throwable pThrowable, final boolean pIsYesNo, final String pIconName) {
		// Get icon image
		Image lIconImage = null;
		InputStream lIconStream = MessageDialog.class.getResourceAsStream(pIconName);
		if (lIconStream != null) {
			lIconImage = new Image(lIconStream);
		}

		final Stage lDialogStage = new Stage();
		if (lIconImage != null) {
			lDialogStage.getIcons().add(lIconImage);
		} else {
			if (pPrimaryStage != null) {
				lDialogStage.getIcons().addAll(pPrimaryStage.getIcons());
			}
		}

		final VBox lMainPane = new VBox();
		lMainPane.getStyleClass().add("message-dialog"); //$NON-NLS-1$
		lMainPane.setMinHeight(100);
		lMainPane.setMinWidth(300);

		// Message Pane
		final HBox lMessagePane = new HBox();
		VBox.setVgrow(lMessagePane, Priority.ALWAYS);
		VBox.setMargin(lMessagePane, new Insets(20, 20, 20, 20));
		lMessagePane.setAlignment(Pos.TOP_LEFT);

		ImageView lImageView = new ImageView();
		if (lIconImage != null) {
			lImageView = new ImageView(lIconImage);
		}

		String[] messageParts = { "null" };//$NON-NLS-1$
		if (pMessage != null) {
			messageParts = pMessage.split("\n"); //$NON-NLS-1$
		}
		String message = ""; //$NON-NLS-1$
		for (String msgPart : messageParts) {
			int lCrLnPos = 150;
			while (lCrLnPos < msgPart.length()) {
				msgPart = msgPart.substring(0, lCrLnPos) + "\n" + msgPart.substring(lCrLnPos); //$NON-NLS-1$
				lCrLnPos += 150;
			}
			message += msgPart + "\n"; //$NON-NLS-1$
		}
		Label lText = new Label();
		lText.setText(message);
		HBox.setMargin(lText, new Insets(0, 0, 0, 10));

		lMessagePane.getChildren().add(lImageView);
		lMessagePane.getChildren().add(lText);

		// Button pane
		HBox lButtonPane = new HBox();
		VBox.setMargin(lButtonPane, new Insets(10, 10, 10, 10));
		lButtonPane.setAlignment(Pos.BOTTOM_RIGHT);

		HBox lButtonBox = new HBox();

		lOkOrYesButton.setMinWidth(80);
		lOkOrYesButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				// Returns true
				mReturnValue = true;
				lDialogStage.close();
			}
		});
		lButtonBox.getChildren().add(lOkOrYesButton);

		if (pIsYesNo) {
			HBox.setMargin(lNoButton, new Insets(0, 0, 0, 10));
			lNoButton.setMinWidth(80);
			lNoButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent t) {
					mReturnValue = false;
					// Returns false
					lDialogStage.close();
				}
			});

			lButtonBox.getChildren().add(lNoButton);
		}
		lButtonPane.getChildren().add(lButtonBox);

		lMainPane.getChildren().add(lMessagePane);
		lMainPane.getChildren().add(lButtonPane);

		// If throwable then add detail button and detail pane
		if (pThrowable != null) {
			// Read stack trace
			String lOutput = ""; //$NON-NLS-1$
			Throwable lThrowable = pThrowable;
			Throwable lCause = null;
			while (lThrowable != null && lThrowable != lCause) {
				lOutput += lThrowable.getClass().getName() + "\n"; //$NON-NLS-1$
				lOutput += lThrowable.toString() + "\n"; //$NON-NLS-1$
				for (StackTraceElement lStackTraceElement : lThrowable.getStackTrace()) {
					lOutput += "   at " + lStackTraceElement.toString() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
				}
				lOutput += "\n"; //$NON-NLS-1$
				if (lThrowable instanceof InvocationTargetException) {
					lThrowable = ((InvocationTargetException) lThrowable).getTargetException();
					if (lThrowable != null) {
						lOutput += " target exception\n"; //$NON-NLS-1$
					}
				} else {
					lThrowable = lThrowable.getCause();
					if (lThrowable != null) {
						lOutput += "  caused by\n"; //$NON-NLS-1$
					}
				}
			}

			final TextArea lTextArea = new TextArea();
			lTextArea.setText(lOutput);
			lTextArea.setEditable(false);
			HBox.setHgrow(lTextArea, Priority.ALWAYS);
			lTextArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

			final HBox lStackTracePane = new HBox();
			VBox.setVgrow(lStackTracePane, Priority.ALWAYS);
			VBox.setMargin(lStackTracePane, new Insets(0, 10, 10, 10));
			lStackTracePane.getChildren().add(lTextArea);
			lStackTracePane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

			final Button lMoreOrLessButton = new Button(MORE_LABEL);
			HBox.setMargin(lMoreOrLessButton, new Insets(0, 0, 0, 10));
			lMoreOrLessButton.setMinWidth(80);
			lMoreOrLessButton.setPrefWidth(80);
			lMoreOrLessButton.setMaxWidth(80);

			lMoreOrLessButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent t) {
					if (lMainPane.getChildren().size() == 2) {
						lMainPane.getChildren().add(2, lStackTracePane);
						lDialogStage.setHeight(lDialogStage.getHeight() + 200);
						lMoreOrLessButton.setText(LESS_LABEL);
						VBox.setVgrow(lMessagePane, Priority.NEVER);

					} else {
						lMainPane.getChildren().remove(2);
						lDialogStage.setHeight(lDialogStage.getHeight() - 200);
						lMoreOrLessButton.setText(MORE_LABEL);
						VBox.setVgrow(lMessagePane, Priority.ALWAYS);
					}
				}
			});

			lButtonBox.getChildren().add(lMoreOrLessButton);
		}

		lDialogStage.initModality(Modality.APPLICATION_MODAL);
		Scene lScene = new Scene(lMainPane);
		lDialogStage.setScene(lScene);

		lDialogStage.setTitle(pTitle);
		
		lDialogStage.setOnShown(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				JavaFxUtil.centerDialog(pPrimaryStage, lDialogStage);
			}
		});

		lDialogStage.showAndWait();

		return mReturnValue;
	}
}
