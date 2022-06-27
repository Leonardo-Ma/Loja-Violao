package br.edu.femass.controller;

import br.edu.femass.dao.CaixaDao;
import br.edu.femass.dao.ViolaoDao;
import br.edu.femass.model.Compra;
import br.edu.femass.model.TotalGeralCompraVenda;
import br.edu.femass.model.Venda;
import br.edu.femass.model.Violao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CaixaController implements Initializable
{
    private final CaixaDao caixaDao = new CaixaDao();

    @FXML Pane PnResultado;

    @FXML Pane PnConsulta;

    @FXML Pane PnDados;

    @FXML Button BtnConsultar;

    @FXML Button BtnConfirmar;

    @FXML Button BtnCancelar;

    @FXML Label LblInserirData;

    @FXML Label LblTotalCompra;

    @FXML Label LblTotalVenda;

    @FXML Label LblTotalGeral;

    @FXML TextField TxtDataEscolhida;

    @FXML private TableView<Compra> LstCompras;

    @FXML private TableColumn<Compra, String> ClnIdCompra;

    @FXML private TableColumn<Compra, String> ClnTotalCompra;

    @FXML private TableColumn<Compra, String> ClnDataCompra;

    @FXML private TableView<Venda> LstVendas;

    @FXML private TableColumn<Venda, String> ClnIdVenda;

    @FXML private TableColumn<Venda, String> ClnTotalVenda;

    @FXML private TableColumn<Venda, String> ClnDataVenda;

    @FXML
    private void BtnConsultar_Action()
    {
        PnDados.setVisible(true);
        PnConsulta.setVisible(false);
    }

    @FXML
    private void BtnConfirmar_Action()
    {
        mostrarTela(true);

        atualizarDados();
        TxtDataEscolhida.setText("");
    }

    @FXML
    private void BtnCancelar_Action()
    {
        mostrarTela(false);

        TxtDataEscolhida.setText("");
    }

    private void mostrarTela(Boolean visibilidade)
    {
        PnConsulta.setVisible(!visibilidade);
        PnDados.setVisible(visibilidade);
        PnResultado.setVisible(visibilidade);
        LstCompras.setVisible(visibilidade);
        LstVendas.setVisible(visibilidade);
    }
    private void atualizarDados()
    {
        Date dataEscolhida = Date.valueOf(TxtDataEscolhida.getText());

        List<Compra> compras;
        List<Venda> vendas;
        try
        {
            compras = caixaDao.listarCompra(dataEscolhida);
        } catch (Exception e)
        {
            compras = new ArrayList<>();
        }
        try
        {
            vendas = caixaDao.listarVendas(dataEscolhida);
        } catch (Exception e)
        {
            vendas = new ArrayList<>();
        }

        ObservableList<Compra> comprasOb = FXCollections.observableArrayList(compras);
        ClnIdCompra.setCellValueFactory(new PropertyValueFactory<Compra, String>("idCompra"));
        ClnTotalCompra.setCellValueFactory(new PropertyValueFactory<Compra, String>("totalCompra"));
        ClnDataCompra.setCellValueFactory(new PropertyValueFactory<Compra, String>("dataCompra"));
        LstCompras.setItems(comprasOb);

        ObservableList<Venda> vendasOb = FXCollections.observableArrayList(vendas);
        ClnIdVenda.setCellValueFactory(new PropertyValueFactory<Venda, String>("idVenda"));
        ClnTotalVenda.setCellValueFactory(new PropertyValueFactory<Venda, String>("totalVenda"));
        ClnDataVenda.setCellValueFactory(new PropertyValueFactory<Venda, String>("dataVenda"));
        LstVendas.setItems(vendasOb);


        TotalGeralCompraVenda totalGeralCompraVenda = new TotalGeralCompraVenda();
        LblInserirData.setText(String.valueOf(TxtDataEscolhida.getText()));
        LblTotalCompra.setText(String.valueOf(totalGeralCompraVenda.totalCompraGeral));
        LblTotalVenda.setText(String.valueOf(totalGeralCompraVenda.totalVendaGeral));
        Float totalGeral = totalGeralCompraVenda.totalVendaGeral - totalGeralCompraVenda.totalCompraGeral;

        if (totalGeral > 0)
            LblTotalGeral.setStyle("-fx-background-color: green; -fx-font-size: 20; -fx-font-weight: bold;");
        else
            LblTotalGeral.setStyle("-fx-background-color: red; -fx-font-size: 20; -fx-font-weight: bold;");
        LblTotalGeral.setText(String.valueOf(totalGeral));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }
}
