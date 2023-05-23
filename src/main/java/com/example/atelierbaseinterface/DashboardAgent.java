package com.example.atelierbaseinterface;

import com.example.atelierbaseinterface.Connexion.Connexion;
import com.example.atelierbaseinterface.entites.Agent;
import com.example.atelierbaseinterface.entites.Runner;
import com.example.atelierbaseinterface.entites.Sponsor;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardAgent implements Initializable {

    Statement smt;
    PreparedStatement ps;
    ResultSet result;
    Alert alert=null;
    @FXML
    private TableColumn<Agent,String> emailCol;

    @FXML
    private TableColumn<Agent,Integer> idagentCol;

    @FXML
    private TableColumn<Agent,String> roleCol;

    @FXML
    private TableView<Agent> tableAgent;

    @FXML
    private Stage stage;

    @FXML
    private TextField tcodeAgent;

    @FXML
    private TextField temailA;

    @FXML
    private TextField tpassword;

    @FXML
    private TextField trole;

    @FXML
    private TextField tusername;

    @FXML
    private TableColumn<Agent, String > usernameCol;

    ObservableList<Agent> dataAgent = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idagentCol.setCellValueFactory(new PropertyValueFactory<Agent,Integer>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<Agent,String>("username"));
        roleCol.setCellValueFactory(new PropertyValueFactory<Agent,String>("role"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Agent,String>("email"));

        tableAgent.setItems(dataAgent);

        // Charger les coureurs à partir de la base de données
        consulter(null);

    }

    private void consulter(Object o) {
        try {
            Connexion con = new Connexion();
            Connection c = con.connecter();
            smt=(Statement) c.createStatement();
            result = smt.executeQuery("select * from users where role='Agent'");

            while (result.next()){
                Integer id = result.getInt(1);
                String username= result.getString(2);
                String password = result.getString(3);
                String role = result.getString(4);
                String email= result.getString(5);
                Agent a = new Agent(id,username,password,role,email);

                dataAgent.add(a);
            }

            tableAgent.setItems(dataAgent);
        } catch (SQLException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Impossible de consulter la base");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void addAgent(ActionEvent actionEvent) {
        if (tcodeAgent.getText().isEmpty() && tusername.getText().isEmpty() && tpassword.getText().isEmpty() && trole.getText().isEmpty() && temailA.getText().isEmpty()) {
            // Display an alert indicating that fields should be filled
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur d'insertion");
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }
        try {
            Connexion con = new Connexion();
            Connection c = con.connecter();

            // Vérifier si l'ID de l'agent existe déjà
            PreparedStatement checkIdStmt = c.prepareStatement("SELECT id FROM users WHERE id = ?");
            checkIdStmt.setInt(1, Integer.parseInt(tcodeAgent.getText()));
            ResultSet rs = checkIdStmt.executeQuery();
            if (rs.next()) {
                // L'ID existe déjà, afficher une alerte d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur d'insertion");
                alert.setContentText("L'ID de l'agent existe déjà.");
                alert.showAndWait();
                return;
            }

            // Insérer l'agent dans la base de données
            PreparedStatement insertStmt = c.prepareStatement("INSERT INTO users (id, username, password, role, email) VALUES (?, ?, ?, ?, ?);");
            insertStmt.setInt(1, Integer.parseInt(tcodeAgent.getText()));
            insertStmt.setString(2, tusername.getText());
            insertStmt.setString(3, tpassword.getText());
            insertStmt.setString(4, trole.getText());
            insertStmt.setString(5, temailA.getText());

            int ligneInsere = insertStmt.executeUpdate();
            if (ligneInsere > 0) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Confirmation de l'insertion");
                successAlert.setContentText("Insertion effectuée dans la base.");
                successAlert.showAndWait();

                // Ajouter le nouvel agent à la liste existante
                Agent newAgent = new Agent(Integer.parseInt(tcodeAgent.getText()), tusername.getText(), tpassword.getText(), trole.getText(), temailA.getText());
                tableAgent.getItems().add(newAgent);
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur d'insertion");
                errorAlert.setContentText("Erreur lors de l'insertion dans la base de données.");
                errorAlert.showAndWait();
            }

            rs.close();
            checkIdStmt.close();
            insertStmt.close();
        } catch (SQLException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur d'insertion");
            errorAlert.setContentText("Impossible d'insérer l'agent.");
            errorAlert.showAndWait();
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

    public void btnListRunner(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboardCoureur.fxml"));
            Parent signUpPage = loader.load();
            Scene signUpScene = new Scene(signUpPage);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(signUpScene);
            stage.setTitle("Runner List"); // Ajoutez le titre souhaité
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnListSponsor(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboardSponsor.fxml"));
            Parent signUpPage = loader.load();
            Scene signUpScene = new Scene(signUpPage);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(signUpScene);
            stage.setTitle("Sponsor List"); // Ajoutez le titre souhaité
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnListMarathon(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboardMarathon.fxml"));
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

    public void minimizeAgent(ActionEvent actionEvent) {
        stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void closeAgent(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void updateAgent(ActionEvent actionEvent) {
        Agent selectedAgent = tableAgent.getSelectionModel().getSelectedItem();

        if (selectedAgent == null) {
            // Aucun coureur sélectionné
            return;
        }

        try {
            Connexion con = new Connexion();
            Connection c = con.connecter();
            PreparedStatement ps = c.prepareStatement("UPDATE users SET  username=?, password=?, role=?, email=? WHERE id=?");

            ps.setString(1, tusername.getText());
            ps.setString(2, tpassword.getText());
            ps.setString(3, trole.getText());
            ps.setString(4, temailA.getText());
            ps.setInt(5, selectedAgent.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Confirmation de la mise à jour");
                alert.setContentText("Mise à jour effectuée dans la base");
                alert.showAndWait();

                // Mettre à jour les champs du coureur sélectionné dans la table
                selectedAgent.setUsername(tusername.getText());
                selectedAgent.setPassword(tpassword.getText());
                selectedAgent.setRole(trole.getText());
                selectedAgent.setEmail(temailA.getText());

                // Rafraîchir la table
                tableAgent.refresh();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Form Error !");
                alert.setContentText("Erreur lors de la mise à jour");
                alert.showAndWait();
            }
            ps.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Error");
            alert.setContentText("Impossible de mettre à jour l'agent");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void deleteAgent(ActionEvent actionEvent) {
        Agent selectedAgent = tableAgent.getSelectionModel().getSelectedItem();
        if (selectedAgent == null) {
            // No sponsor selected, show an error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setContentText("Aucun agent sélectionné.");
            errorAlert.showAndWait();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Voulez-vous vraiment supprimer cet agent ?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Connexion con = new Connexion();
                Connection c = con.connecter();
                ps = c.prepareStatement("DELETE FROM users WHERE id = ? and role='Agent'");
                ps.setInt(1, selectedAgent.getId());

                int rowsDeleted = ps.executeUpdate();
                if (rowsDeleted > 0) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Confirmation");
                    successAlert.setContentText("L'agent a été supprimé avec succès.");
                    successAlert.showAndWait();

                    // Refresh the table view
                    ObservableList<Agent> agentList = tableAgent.getItems();
                    agentList.remove(selectedAgent);
                    tableAgent.refresh();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setContentText("Impossible de supprimer l'agent.");
                    errorAlert.showAndWait();
                }
            } catch (SQLException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setContentText("Une erreur s'est produite lors de la suppression de l'agent.");
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    public void btnRankingList(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("rankingList.fxml"));
            Parent signUpPage = loader.load();
            Scene signUpScene = new Scene(signUpPage);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(signUpScene);
            stage.setTitle("Ranking List"); // Ajoutez le titre souhaité
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
