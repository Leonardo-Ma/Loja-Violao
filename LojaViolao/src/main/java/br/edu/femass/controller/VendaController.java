package br.edu.femass.controller;

import br.edu.femass.dao.ViolaoDao;
import br.edu.femass.model.*;
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


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class VendaController implements Initializable
{
    private final ViolaoDao violaoDao = new ViolaoDao();
    @FXML private TableView<Violao> LstVioloes;

    @FXML private TableColumn<Violao, String> ClnIdViolao;

    @FXML private TableColumn<Violao, String> ClnMarcaViolao;

    @FXML private TableColumn<Violao, String> ClnTipoCorda;

    @FXML private TableColumn<Violao, String> ClnPrecoVenda;

    @FXML private TableColumn<Violao, String> ClnEstoque;

    @FXML private TableView<Venda> LstVendas;

    @FXML private TableColumn<Venda, String> ClnIdVenda;

    @FXML private TableColumn<Venda, String> ClnTotalVenda;

    @FXML private TableColumn<Venda, String> ClnDataVenda;

    @FXML private TextField TxtValorVenda;

    @FXML private TextField TxtQuantidade;

    @FXML private TextField TxtDataVenda;

    @FXML private Pane PnDetalhes;

    @FXML private Pane PnEscolha;

    @FXML private Button BtnVender;

    @FXML private Button BtnConsultar;

    @FXML private Button BtnConfirmar;

    @FXML private Label TextInformacao;

    @FXML
    private void BtnVender_Action(ActionEvent evento)
    {
        LstVioloes.setVisible(true);
        PnEscolha.setVisible(false);
        TextInformacao.setVisible(true);
        limparTela();
    }

    @FXML
    private void BtnConsultar_Action(ActionEvent evento)
    {
        LstVendas.setVisible(true);
    }

    @FXML
    private void BtnCancelar_Action(ActionEvent evento)
    {
        LstVioloes.setVisible(false);
        PnEscolha.setVisible(true);
        TextInformacao.setVisible(false);
        LstVendas.setVisible(false);
        PnDetalhes.setVisible(false);
        limparTela();
    }

    @FXML
    private void BtnConfirmar_Action(ActionEvent evento)
    {
        Violao violao = LstVioloes.getSelectionModel().getSelectedItem();
        if (violao == null) return;
        DetalhesVenda detalhesVenda = new DetalhesVenda();
        Venda venda = new Venda();

        float totalVenda = Float.valueOf(TxtValorVenda.getText());
        totalVenda *= Float.valueOf(TxtQuantidade.getText());
        venda.setTotalVenda(totalVenda); // totalVenda = valorVenda * quantidade
        venda.setDataVenda(TxtDataVenda.getText());

        detalhesVenda.setQuantidade(Integer.valueOf(TxtQuantidade.getText()));
        detalhesVenda.setPrecoUnitario(Float.valueOf(TxtValorVenda.getText()));

        try
        {
            violaoDao.vender(violao, venda, detalhesVenda);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        atualizarLista();
        limparTela();
        PnDetalhes.setVisible(false);
        PnEscolha.setVisible(true);
        LstVioloes.setVisible(false);
        LstVendas.setVisible(true);
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
        TxtValorVenda.requestFocus();
    }

    private void atualizarLista()
    {
        List<Violao> violoes;
        try
        {
            violoes = violaoDao.listarViolaoVenda();
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


        List<Venda> vendas;
        try
        {
            vendas = violaoDao.listarVendas();
        } catch (Exception e)
        {
            vendas = new ArrayList<>();
        }
        ObservableList<Venda> vendasOb = FXCollections.observableArrayList(vendas);
        ClnIdVenda.setCellValueFactory(new PropertyValueFactory<Venda, String>("idVenda"));
        ClnTotalVenda.setCellValueFactory(new PropertyValueFactory<Venda, String>("totalVenda"));
        ClnDataVenda.setCellValueFactory(new PropertyValueFactory<Venda, String>("dataVenda"));

        LstVendas.setItems(vendasOb);
    }

    private void limparTela()
    {
        TxtQuantidade.setText("");
        TxtValorVenda.setText("");
        TxtDataVenda.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        atualizarLista();
    }
}
