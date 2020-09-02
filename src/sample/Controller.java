package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {

    @FXML
    private AnchorPane principalePane;

    @FXML
    private ComboBox<String> listebases;

    @FXML
    private ComboBox<String> listealgos;

    @FXML
    private Button buttononto;

    @FXML
    private Button buttonstat;

    @FXML
    private Button buttonalgo;


    String[] listDB = new String[]{ "Yelp","MovieLens","RED" };
    String[] listalgo = new String[]{ "Filtrage social", "Test" };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        // populate the combo box with item choices.
        listebases.getItems().setAll(listDB);
        listealgos.getItems().setAll(listalgo);


        buttonstat.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    String check =listebases.getValue();
                    if (check==null){
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setHeaderText("Base de données non sélectionnée");
                        errorAlert.setContentText("Vous devez choisir une base de donnèes afin d'afficher ses statistiques");
                        errorAlert.showAndWait();
                    }
                    else{
                    LoadStats();}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        buttononto.setOnAction(new EventHandler<ActionEvent>() {
                               @Override public void handle(ActionEvent e) {
                                   afficherOnto();

                                   try {
                                       Process p = Runtime.getRuntime().exec("python -u C:\\Users\\lenovo\\Desktop\\App_Rec\\src\\sample\\ontos\\onto_Yelp.py");
                                       Scanner in = new Scanner(p.getInputStream());
                                       while (in.hasNextLine()) {
                                           String res='\n'+in.nextLine();
                                           System.out.println(res);
                                       }
                                       Scanner err = new Scanner(p.getErrorStream());
                                       while (err.hasNextLine()) {
                                           String res='\n'+err.nextLine();
                                           System.out.println(res);
                                       }

                                  } catch (IOException ex) {
                                               ex.printStackTrace();
                                           }





                               }
                           }
        );

        buttonalgo.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {

                try {
                    String check1 =listebases.getValue();
                    String check2 =listealgos.getValue();
                    if (check1==null || check2==null){
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setHeaderText("Base de données ou algorithme non sélectionné");
                        errorAlert.setContentText("Veuillez choisir un algorithme et une base de données");
                        errorAlert.showAndWait();
                    }
                    else {


                        LoadAlgo();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void afficherOnto() {
        String database=listebases.getValue();
        switch (database) {
            case "Yelp":
                try {
                    Process p = Runtime.getRuntime().exec("python -u C:\\Users\\lenovo\\Desktop\\App_Rec\\src\\sample\\ontos\\onto_Yelp.py");
                    Scanner in = new Scanner(p.getInputStream());
                    while (in.hasNextLine()) {
                        String res='\n'+in.nextLine();
                        System.out.println(res);
                    }
                    Scanner err = new Scanner(p.getErrorStream());
                    while (err.hasNextLine()) {
                        String res='\n'+err.nextLine();
                        System.out.println(res);
                    }
                    break;

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            case "RED":
                try {
                    Process p = Runtime.getRuntime().exec("python -u C:\\Users\\lenovo\\Desktop\\App_Rec\\src\\sample\\ontos\\onto_red.py");
                    Scanner in = new Scanner(p.getInputStream());
                    while (in.hasNextLine()) {
                        String res='\n'+in.nextLine();
                        System.out.println(res);
                    }
                    Scanner err = new Scanner(p.getErrorStream());
                    while (err.hasNextLine()) {
                        String res='\n'+err.nextLine();
                        System.out.println(res);
                    }
                    break;

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            case "MovieLens":
                Erreur();
                break;

        }


    }

    private void Erreur() {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Erreur");
        errorAlert.setContentText("Vu le peu de categories que MovieLens a il n'est pas interessant de lui construire une ontologie");
        errorAlert.showAndWait();
    }


    private void LoadAlgo() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("eval.fxml"));
        AnchorPane pane= loader.load();

        //2 lignes qui passent l'info à l'autre page
        Eval_controller eval = loader.getController();
        eval.setInfo(listebases.getValue(), listealgos.getValue());

        principalePane.getChildren().setAll(pane);
    }

    private void LoadStats() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("stats.fxml"));
        AnchorPane pane= loader.load();

        //2 lignes qui passent l'info à l'autre page
        Stats stat = loader.getController();
        stat.setDataBase(listebases.getValue());

        principalePane.getChildren().setAll(pane);
    }



}
