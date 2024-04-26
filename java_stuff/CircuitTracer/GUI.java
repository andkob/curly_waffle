import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GUI extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label label = new Label("HI");
		
		Scene scene = new Scene(label, 300, 300);
		primaryStage.setTitle("JavaFX GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
}
