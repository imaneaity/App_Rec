package sample;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Stats implements Initializable {



    String database;


    @FXML
    Label titlestat;
    @FXML
    Label intro;
    @FXML
    Label users;
    @FXML
    Label items;
    @FXML
    Label ratings;
    @FXML
    Label categories;
    @FXML
    Label amis;
    @FXML
    Label Density;

    @FXML
    private AnchorPane statsPane;

    @FXML
    private Button buttonretour;

    String[] Yelp = new String[]{ "7 456" ,"32 596","826 753" , "1 088", "156" , "0.34" };
    String[] MovieLens = new String[]{ "943" ,"1 682","100 000" , "19", "150" , "6.30" };
    String[] RED = new String[]{ "943" ,"1 682","100 000" , "19", "150" , "6.30" };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonretour.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    GridPane pane = FXMLLoader.load((getClass().getResource("sample.fxml")));
                    statsPane.getChildren().setAll(pane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setDataBase(String database) {

        this.database=database;
        ShowStats();
        ;
    }

    private void ShowStats() {
        switch(database) {
            case "Yelp":
                intro.setText("L'échantillon de Yelp a été pris avec un minimum de 60 évaluations par utilisateur et 50 par service" );
                SetInfos(Yelp);
                break;


            case "MovieLens":
                intro.setText("L'échantillon de MovieLens a été pris avec un minimum de 20 évaluations par utilisateur" );
                SetInfos(MovieLens);
                break;

            case "RED":
                intro.setText("L'échantillon de RED a été pris avec un minimum de 20 évaluations par utilisateur" );
                SetInfos(RED);
                break;
        }


        }

    private void SetInfos(String[] lis) {
        titlestat.setText("Statistiques sur :" + database );

        users.setText("Nombre d'utilisateurs:   " + lis[0] );
        items.setText("Nombre d'items:  " + lis[1]  );
        ratings.setText("Nombre d'évaluations:  " + lis[2]  );
        categories.setText("Nombre de catégories:   " + lis[3]  );
        amis.setText("Nombre d’amis moyen par utilisateur :    " + lis[4] );
        Density.setText("Densité :  " + lis[5]  + "%");

    }
}
