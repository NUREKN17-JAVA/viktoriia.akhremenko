package ua.nure.kn.akhremenko.usermanagement.gui;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ua.nure.kn.akhremenko.usermanagement.agent.SearchAgent;
import ua.nure.kn.akhremenko.usermanagement.gui.controller.BrowseController;
import ua.nure.kn.akhremenko.usermanagement.util.UTF8Control;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SearchAgent sa = initSearchAgent();

        ResourceBundle bundle = ResourceBundle.getBundle("strings", Locale.getDefault(), new UTF8Control());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/browse.fxml"), bundle);

        BrowseController.setSearchAgent(sa);

        Parent root = loader.load();
        primaryStage.setTitle(bundle.getString("browse.title"));
        primaryStage.setScene(new Scene(root));

        primaryStage.show();
    }

    private SearchAgent initSearchAgent() throws StaleProxyException {
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        Profile profile = new ProfileImpl();
// profile.setParameter( ... );
        AgentContainer container = runtime.createMainContainer( profile );
        SearchAgent agent = new SearchAgent();
// agent.addBehaviour( ... );
        AgentController ac = container.acceptNewAgent( "searcher", agent);
        ac.start();
        return agent;
    }


    public static void main(String[] args) {
        launch(args);
    }

}
