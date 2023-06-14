package com.example.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController {



    @FXML
    private Button close_Button;

    @FXML
    void close_Application() {
        Stage stage = (Stage) close_Button.getScene().getWindow();
        stage.close();
    }


}


