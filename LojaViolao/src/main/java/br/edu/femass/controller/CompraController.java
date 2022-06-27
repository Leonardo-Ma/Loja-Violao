package br.edu.femass.controller;

import br.edu.femass.dao.ViolaoDao;
import br.edu.femass.model.Compra;
import br.edu.femass.model.DetalhesCompra;
import br.edu.femass.model.Violao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CompraController implements Initializable
{
    private final ViolaoDao violaoDao = new ViolaoDao();
    @FXML private TableView<Violao> LstVioloes;

    @FXML private TableColumn<Violao, String> ClnIdViolao;

    @FXML private TableColumn<Violao, String> ClnMarcaViolao;

    @FXML private TableColumn<Violao, String> ClnTipoCorda;

    @FXML private TableColumn<Violao, String> ClnPrecoVenda;

    @FXML private TableColumn<Violao, String> ClnEstoque;

    @FXML private TableView<Compra> LstCompras;

    @FXML private TableColumn<Compra, String> ClnIdCompra;

    @FXML private TableColumn<Compra, String> ClnTotalCompra;

    @FXML private TableColumn<Compra, String> ClnDataCompra;

    @FXML private TextField TxtValorCompra;

    @FXML private TextField TxtQuantidade;

    @FXML private TextField TxtDataCompra;

    @FXML private Pane PnDetalhes;

    @FXML private Pane PnEscolha;

    @FXML private Button BtnComprar;

    @FXML private Button BtnConsultar;

    @FXML private Button BtnConfirmar;

    @FXML private Label TextInformacao;

    @FXML
    private void BtnComprar_Action(ActionEvent evento)
    {
        LstVioloes.setVisible(true);
        PnEscolha.setVisible(false);
        TextInformacao.setVisible(true);
        limparTela();
    }

    @FXML
    private void BtnConsultar_Action(ActionEvent evento)
    {
        LstCompras.setVisible(true);
    }

    @FXML
    private void BtnCancelar_Action(ActionEvent evento)
    {
        LstVioloes.setVisible(false);
        PnEscolha.setVisible(true);
        TextInformacao.setVisible(false);
        LstCompras.setVisible(false);
        PnDetalhes.setVisible(false);
        limparTela();
    }

    @FXML
    private void BtnConfirmar_Action(ActionEvent evento)
    {
        Violao violao = LstVioloes.getSelectionModel().getSelectedItem();
        if (violao == null) return;
        DetalhesCompra detalhescompra = new DetalhesCompra();
        Compra compra = new Compra();

        float totalCompra = Float.valueOf(TxtValorCompra.getText());
        totalCompra *= Float.valueOf(TxtQuantidade.getText());
        compra.setTotalCompra(totalCompra); // totalCompra = valorCompra * quantidade
        compra.setDataCompra(TxtDataCompra.getText());

        detalhescompra.setQuantidade(Integer.valueOf(TxtQuantidade.getText()));
        detalhescompra.setPrecoUnitario(Float.valueOf(TxtValorCompra.getText()));

        try
        {
            violaoDao.comprar(violao, compra, detalhescompra);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        atualizarLista();
        limparTela();
        PnDetalhes.setVisible(false);
        PnEscolha.setVisible(true);
        LstVioloes.setVisible(false);
        LstCompras.setVisible(true);
    }

    @FXML // Caso algum item seja selecionado na lista(TableView) com o uso do mouse
    private void LstVioloes_MouseClicked(MouseEvent evento) { buscarViolao(); }

    @FXML // Caso algum item seja selecionado na lista(TableView) com o uso do teclado
    private void LstVioloes_KeyPressed(KeyEvent evento) { buscarViolao(); }

    private void buscarViolao()
    {
        Violao violao = LstVioloes.getSelectionModel().getSelectedItem(); // Retorna os valores do violão selecionado pelo usuário no TableView
        if (violao == null) return; // Caso não encontre nenhum retorna
        limparTela();
        PnDetalhes.setVisible(true);
        TextInformacao.setVisible(false);
        TxtValorCompra.requestFocus();
    }

    private void atualizarLista()
    {
        List<Violao> violoes;
        try
        {
            violoes = violaoDao.listar();
        } catch (Exception e)
        {
            violoes = new ArrayList<>();
        }
        ObservableList<Violao> violoesOb = FXCollections.observableArrayList(violoes);
        // https://stackoverflow.com/questions/42176930/display-data-from-database-into-the-tableview
        // Valores da string recebida devem ser iguais aos que estão no BD.
        ClnIdViolao.setCellValueFactory(new PropertyValueFactory<Violao, String>("id"));
        ClnMarcaViolao.setCellValueFactory(new PropertyValueFactory<Violao, String>("marca"));
        ClnPrecoVenda.setCellValueFactory(new PropertyValueFactory<Violao, String>("precoVenda"));
        ClnTipoCorda.setCellValueFactory(new PropertyValueFactory<Violao, String>("tipoCorda"));
        ClnEstoque.setCellValueFactory(new PropertyValueFactory<Violao, String>("estoque"));
        //------------------------------------------------------------------------------------------
        LstVioloes.setItems(violoesOb);


        List<Compra> compras;
        try
        {
            compras = violaoDao.listarCompras();
        } catch (Exception e)
        {
            compras = new ArrayList<>();
        }
        ObservableList<Compra> comprasOb = FXCollections.observableArrayList(compras);
        ClnIdCompra.setCellValueFactory(new PropertyValueFactory<Compra, String>("idCompra"));
        ClnTotalCompra.setCellValueFactory(new PropertyValueFactory<Compra, String>("totalCompra"));
        ClnDataCompra.setCellValueFactory(new PropertyValueFactory<Compra, String>("dataCompra"));

        LstCompras.setItems(comprasOb);
    }

    private void limparTela()
    {
        TxtQuantidade.setText("");
        TxtValorCompra.setText("");
        TxtDataCompra.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        atualizarLista();
    }
}
