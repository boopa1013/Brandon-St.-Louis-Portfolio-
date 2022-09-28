// IMPORTS
// These are some classes that may be useful for completing the project.
// You may have to add others.
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.stage.Stage;
import javafx.concurrent.Worker.State;
import javafx.concurrent.Worker;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * The main class for Week8Program. Week8Program constructs the JavaFX window and
 * handles interactions with the dynamic components contained therein.
 */
public class Week14Program extends Application {
	// INSTANCE VARIABLES
	// These variables are included to get you started.
	private Stage stage = null;
	private BorderPane borderPane = null;
    private WebView view = null;
	private WebEngine webEngine = null;
	private TextField statusbar = null;
	private TextField webAddress = null;

	// HELPER METHODS
	/**
	 * Retrieves the value of a command line argument specified by the index.
	 *
	 * @param index - position of the argument in the args list.
	 * @return The value of the command line argument.
	 */
	private String getParameter( int index ) {
		Parameters params = getParameters();
		List<String> parameters = params.getRaw();
		return !parameters.isEmpty() ? parameters.get(index) : "";
	}

	/**
	 * Creates a WebView which handles mouse and some keyboard events, and
	 * manages scrolling automatically, so there's no need to put it into a ScrollPane.
	 * The associated WebEngine is created automatically at construction time.
	 *
	 * @return browser - a WebView container for the WebEngine.
	 */
	private WebView makeHtmlView( ) {
		view = new WebView();
		webEngine = view.getEngine();

		webEngine.getLoadWorker().stateProperty().addListener(
				( ov,  oldState,  newState)-> {
						if (newState == State.SUCCEEDED) {
							stage.setTitle(webEngine.getTitle());
							//stage.setTitle(webEngine.getLocation());

						}

				});

		webEngine.setOnStatusChanged(
				(ev)-> {
						statusbar.setText( ev.getData() );

				});

		return view;

	}

	/**
	 * Generates the status bar layout and text field.
	 *
	 * @return statusbarPane - the HBox layout that contains the statusbar.
	 */
	private HBox makeStatusBar( ) {
		HBox statusbarPane = new HBox();
		statusbarPane.setPadding(new Insets(5, 4, 5, 4));
		statusbarPane.setSpacing(10);
		statusbarPane.setStyle("-fx-background-color: #336699;");
		statusbar = new TextField(); //statusbar.setText to change text
		HBox.setHgrow(statusbar, Priority.ALWAYS);
		statusbarPane.getChildren().addAll(statusbar);
		return statusbarPane;
	}

	private HBox makeToolbar( ) {
		HBox toolbarpane = new HBox();
		toolbarpane.setPadding(new Insets(5, 4, 5, 4));
		toolbarpane.setSpacing(10);
		toolbarpane.setStyle("-fx-background-color: #336699;");
		Button backButton = new Button("<-");
		backButton.setOnAction( event -> {
			if(webEngine.getHistory().getCurrentIndex()!=0) {
				webEngine.getHistory().go(-1);
			}
		});

		Button nextButton = new Button("->");
		nextButton.setOnAction( event -> {
			if(webEngine.getHistory().getCurrentIndex()+1>webEngine.getHistory().getMaxSize()) {
				webEngine.getHistory().go(1);
			}
		});

		webAddress = new TextField(); //statusbar.setText to change text
		webAddress.setOnAction((event -> {
			String text = webAddress.getText();
			if(!text.startsWith("https")){
				text = "https://"+text;

			}
			webAddress.setText(text);


			webEngine.load(text);
		}));

		HBox.setHgrow(webAddress, Priority.ALWAYS);
		Button helpButton = new Button("?");
		helpButton.setOnAction( event -> {
			webEngine.load("file://mypage.html");
			webEngine.loadContent(
					"<HTML>" +
							"<HEAD>" +
							"<TITLE>CS1121 Web Browser Help </TITLE>" +
							"</HEAD" +
							"<BODY>" +
							"WebBrowser developed by Michael Riccobono" +
							"</BODY>" +
							"</HTML>"
			);
		});

		toolbarpane.getChildren().addAll(backButton, nextButton,webAddress, helpButton);
		return toolbarpane;
	}

	// REQUIRED METHODS
	/**
	 * The main entry point for all JavaFX applications. The start method is
	 * called after the init method has returned, and after the system is ready
	 * for the application to begin running.
	 *
	 * NOTE: This method is called on the JavaFX Application Thread.
	 *
	 * @param primaryStage - the primary stage for this application, onto which
	 * the application scene can be set.
	 */
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		borderPane = new BorderPane(  );
		Scene scene = new Scene( borderPane, 800, 600 ); //might need scene to be an instance variable

		borderPane.setCenter( makeHtmlView() );
		String commandLineArg = getParameter( 0 );
		webEngine.load( commandLineArg.isEmpty() ? "https://www.mtu.edu" : commandLineArg );

		borderPane.setBottom( makeStatusBar() );
		statusbar.setText("This is the statusbar");

		borderPane.setTop( makeToolbar() );
		webAddress.setText( commandLineArg.isEmpty() ? "https://www.mtu.edu" : commandLineArg );




		stage.setScene( scene );

		stage.show();

	}

	/**
	 * The main( ) method is ignored in JavaFX applications.
	 * main( ) serves only as fallback in case the application is launched
	 * as a regular Java application, e.g., in IDEs with limited FX
	 * support.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
