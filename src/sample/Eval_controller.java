package sample;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;


import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Eval_controller implements Initializable  {

    @FXML
    private Button buttonretour;

    @FXML
    private Button lancer;

    @FXML
    private AnchorPane evalPane;

    @FXML
    private TextArea trace;

    @FXML
    private TextField MAE;

    @FXML
    private TextField RMSE;

    String algo;
    String database;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trace.setEditable(false);
        buttonretour.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    GridPane pane = FXMLLoader.load((getClass().getResource("sample.fxml")));
                    evalPane.getChildren().setAll(pane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        lancer.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    SelectAlgo();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    Process p;
    String result="";
    private void LaunchAlgo(String script)  {

        Thread thread = new Thread(() -> {

            try {

                 p = Runtime.getRuntime().exec("python -u C:\\Users\\lenovo\\Desktop\\App_Rec\\src\\sample\\"+script);
                Scanner in = new Scanner(p.getInputStream());
                while (in.hasNextLine()) {
                    String res='\n'+in.nextLine();
                     result= result+res;
                    updateStatus(res);
                }
                Scanner err = new Scanner(p.getErrorStream());
                while (err.hasNextLine()) {
                    String res='\n'+err.nextLine();
                    result= result+res;
                    updateStatus(res);
                }
                DisplayMaeRmse(result);




            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();



    }

    private void DisplayMaeRmse(String result) {


        String mae="";
        String rmse="";
        String[] res = result.split("\n");
        for (int i = 0; i < res.length; i++) {

            String s1=res[i];
            if (s1.toLowerCase().contains("mae")){
                mae=s1.replaceAll("[[a-zA-Z][ :]' ' ]", "");
            }
            if (s1.toLowerCase().contains("rmse")){
                rmse=s1.replaceAll("[[a-zA-Z][ :]' ' ]", "");
            }
        }
        if (mae.length() >5 && rmse.length()>5){
        MAE.setText(mae.substring(0,5));
        RMSE.setText(rmse.substring(0,5));}

    }

    private void updateStatus(String message) {
        if (Platform.isFxApplicationThread()) {
            trace.appendText(message);
        } else {
            Platform.runLater(() -> trace.appendText(message));
        }
    }

    private void SelectAlgo() throws IOException, InterruptedException {
        switch (algo) {

            case "Filtrage social":
                if (database == "Yelp"){ LaunchAlgo("fSoc/fSoc_Yelp.py");}
                if (database == "MovieLens"){ LaunchAlgo("fSoc/fSoc_Ml.py");}
                if (database == "RED"){ AfficherErreur();}
                break;

            case "Test":
                LaunchAlgo("test.py");
                break;
        }
    }

    private void AfficherErreur() {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Erreur");
        errorAlert.setContentText(algo+" ne peut être executé avec la base de données "+ database);
        errorAlert.showAndWait();
    }

    public void setInfo(String database, String algo) {

        this.database=database;
        this.algo=algo;
        trace.setText("Algorithme: "+algo+"\nDataset: " +database);
    }
}
