package br.edu.femass.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable
{

    @FXML private Button BtnCaixa;

    @FXML private Button BtnEstoque;

    @FXML private Button BtnCompra;

    @FXML private Button BtnVenda;

    @FXML private Button BtnViolao;

    @FXML private ImageView ImgDinheiroMeme;

    @FXML private ImageView ImgDinheiro;

    @FXML private ImageView ImgLupa;

    @FXML private ImageView ImgViolao;

    //                                   Botões de transição de tela
    @FXML
    private void BtnViolao_Action(ActionEvent evento)
    {
        abrirTela("Violao", "Gerenciamento dos Violões");
    }

    @FXML
    private void BtnCompra_Action(ActionEvent evento)
    {
        abrirTela("Compra", "Gerenciamento das Compras");
    }

    @FXML
    private void BtnVenda_Action(ActionEvent evento)
    {
        abrirTela("Venda", "Gerenciamento das Vendas");
    }

    @FXML
    private void BtnCaixa_Action(ActionEvent evento)
    {
        abrirTela("Caixa", "Consulta do Fechamento de Caixa");
    }
    //                                   ---------------------------

    @FXML
    private void EasterEgg_Action(MouseEvent evento)
    {
        ImgDinheiro.setVisible(true);
        ImgDinheiroMeme.setVisible(true);
        ImgLupa.setVisible(true);
        ImgViolao.setVisible(true);
    }

    private void abrirTela(String nome, String titulo)
    {
        try
        {
            Parent root = null;
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/" + nome + ".fxml")));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            scene.getRoot().setStyle("-fx-font-family: 'serif'");

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }
}
