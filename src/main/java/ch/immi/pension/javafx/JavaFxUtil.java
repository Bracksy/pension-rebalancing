package ch.immi.pension.javafx;

import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;

public class JavaFxUtil {

	/**
	 * Checks if position is in a cell and not a scrollbar
	 */
	public static boolean isInsideCell(TableView<?> tableView, MouseEvent mouseEvent) {
		Pane header = (Pane) tableView.lookup("TableHeaderRow"); //$NON-NLS-1$
		double headerHeight = 0;
		if (header != null) {
			headerHeight = header.getHeight();
		}

		boolean insideCell = true;
		for (Node n : tableView.lookupAll(".scroll-bar")) //$NON-NLS-1$
		{
			if (n instanceof ScrollBar) {
				ScrollBar bar = (ScrollBar) n;
				if (bar.isVisible()) {
					if (bar.getOrientation() == Orientation.HORIZONTAL) {
						if (mouseEvent.getY() > bar.getLayoutY() + headerHeight) {
							insideCell = false;
						}
					}
					if (bar.getOrientation() == Orientation.VERTICAL) {
						if (mouseEvent.getX() > bar.getLayoutX()) {
							insideCell = false;
						}
					}
				}
			}
		}
		return insideCell;
	}

	/**
	 * Centers a dialog on the screen (or stage)
	 *
	 * @param primaryStage parent stage
	 * @param dialogStage  dialog
	 */
	public static void centerDialog(Stage primaryStage, Stage dialogStage) {
		double width = 1024;
		if (Double.isNaN(dialogStage.getWidth())) {
			if (!Double.isNaN(primaryStage.getWidth())) {
				width = primaryStage.getWidth() / 2;
			}
		} else {
			width = dialogStage.getWidth();
		}

		double height = 768;
		if (Double.isNaN(dialogStage.getHeight())) {
			if (!Double.isNaN(primaryStage.getHeight())) {
				height = primaryStage.getHeight() / 2;
			}
		} else {
			height = dialogStage.getHeight();
		}
		centerDialog(dialogStage, width, height);
	}

	/**
	 * Centers a dialog on the screen (or stage)
	 *
	 * @param dialogStage  dialog
	 */
	private static void centerDialog(Stage dialogStage, double width, double height) {
		Screen screen = getCurrentScreen();
		double x = screen.getBounds().getMinX() - 8;
		if (width < screen.getBounds().getWidth()) {
			double centerX = screen.getBounds().getMinX() + screen.getBounds().getWidth() / 2;
			x = centerX - width / 2;
		}
		double y = screen.getBounds().getMinY() - 8;
		if (height < screen.getBounds().getHeight() - 50) {
			double centerY = screen.getBounds().getMinY() + screen.getBounds().getHeight() / 2;
			y = centerY - height / 2;
		}
		dialogStage.setX(x);
		dialogStage.setY(y);

		if (width > screen.getBounds().getWidth()) {
			dialogStage.setWidth(screen.getBounds().getWidth() + 16);
		}
		if (height > screen.getBounds().getHeight()) {
			dialogStage.setHeight(screen.getBounds().getHeight() - 32);
		}
	}

    /**
     * Returns current Screen (with mouse pointer on it)
     */
    private static Screen getCurrentScreen() {
		Screen screen;
		if (Screen.getScreens().size() == 1) {
			screen = Screen.getScreens().get(0);
		} else {
			// Handle multiple windows
	        Point p = MouseInfo.getPointerInfo().getLocation();
			screen = findScreen(p);
	        if (screen == null) {
				if (p.y < 0) {
					p.y += 100;
				} else {
					p.y -= 100;
				}
				screen = findScreen(p);
			}
			if (screen == null) {
				screen = Screen.getPrimary();
			}
    	}
        return screen;
    }

	private static Screen findScreen(Point p) {
		for(Screen screen: Screen.getScreens()) {
			Rectangle2D screenBounds = screen.getVisualBounds();
			if (screenBounds.contains(p.x, p.y)) {
				return screen;
			}
		}
		return null;
	}

	public static Image getImage(String imagePath) {
		URL resource = null;
		if (imagePath != null) {
			resource = JavaFxUtil.class.getResource(imagePath);
		}
		return resource != null? new Image(resource.toString()): null;
	}

	public static ImageView getImageView(String imagePath) {
		Image image = getImage(imagePath);
		return image != null? new ImageView(image): null;
	}
}
