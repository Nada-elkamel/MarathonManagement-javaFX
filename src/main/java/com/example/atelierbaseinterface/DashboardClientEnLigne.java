package com.example.atelierbaseinterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.awt.Desktop;

public class DashboardClientEnLigne {
    public void btnMarathonList(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("espaceClient.fxml"));
            Parent signUpPage = loader.load();
            Scene signUpScene = new Scene(signUpPage);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(signUpScene);
            stage.setTitle("Marathon List"); // Ajoutez le titre souhaité
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(ActionEvent actionEvent) {
        // Fermer la fenêtre actuelle
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();

        try {
            // Charger la page de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène pour la page de connexion
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre pour la scène de connexion
            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewRankingList(ActionEvent actionEvent) {
        // Créer une instance de FileChooser
        FileChooser fileChooser = new FileChooser();

        // Définir le titre de la fenêtre de dialogue
        fileChooser.setTitle("Sélectionner le fichier de ranking list");

        // Définir le filtre de type de fichier (optionnel)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers texte (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la fenêtre de dialogue pour sélectionner le fichier de ranking list
        File file = fileChooser.showOpenDialog(((Node) actionEvent.getSource()).getScene().getWindow());
        if (file != null) {
            // Ouvrir le fichier de ranking list avec l'application par défaut
            try {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(file);
                } else {
                    // Afficher un message d'erreur si l'ouverture du fichier n'est pas supportée
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur d'ouverture du fichier");
                    alert.setContentText("L'ouverture du fichier n'est pas supportée sur ce système.");
                    alert.showAndWait();
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Afficher une boîte de dialogue d'erreur en cas d'échec d'ouverture du fichier
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur d'ouverture du fichier");
                alert.setContentText("Une erreur s'est produite lors de l'ouverture du fichier de ranking list.");
                alert.showAndWait();
            }
        }
    }

    public void btnRegister(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registerClient.fxml"));
            Parent signUpPage = loader.load();
            Scene signUpScene = new Scene(signUpPage);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(signUpScene);
            stage.setTitle("Register"); // Ajoutez le titre souhaité
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
