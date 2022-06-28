package br.edu.femass.controller;

import br.edu.femass.dao.ViolaoDao;
import br.edu.femass.model.TipoCorda;
import br.edu.femass.model.Violao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;


import java.net.URL;
import java.util.*;

public class ViolaoController implements Initializable
{
    private final ViolaoDao violaoDao = new ViolaoDao();
    @FXML private Pane PnDados;

    @FXML private TableView<Violao> LstVioloes;

    @FXML private TableColumn<Violao, String> ClnIdViolao;

    @FXML private TableColumn<Violao, String> ClnMarcaViolao;

    @FXML private TableColumn<Violao, String> ClnTipoCorda;

    @FXML private TableColumn<Violao, String> ClnPrecoVenda;

    @FXML private TableColumn<Violao, String> ClnEstoque;

    @FXML private ImageView ImgViolao;

    @FXML private TextField TxtMarca;

    @FXML private TextField TxtPrecoVenda;

    @FXML private ComboBox<TipoCorda> CbxTipoCorda;

    @FXML private Button BtnIncluir;

    @FXML private Button BtnCancelar;

    @FXML private Button BtnAlterar;

    @FXML private Button BtnExcluir;

    @FXML private Button BtnGravar;


    @FXML
    private void BtnIncluir_Action()
    {
        limparTela();
        BtnIncluir.setVisible(false);
        BtnGravar.setVisible(true);
        PnDados.setVisible(true);
        TxtMarca.requestFocus();
        CbxTipoCorda.getItems().setAll(Arrays.asList(TipoCorda.values())); // https://stackoverflow.com/questions/27801119/populating-javafx-combobox-or-choicebox-from-enum
    }

    @FXML // Confirmar inclusão de um novo elemento no banco de dados
    private void BtnGravar_Action()
    {
        Violao violao = new Violao();
        violao.setMarca(TxtMarca.getText());
        violao.setTipoCorda(CbxTipoCorda.getValue());
        violao.setPrecoVenda(Float.valueOf(TxtPrecoVenda.getText()));

        if (Objects.equals(TxtMarca.getText(), "") || CbxTipoCorda.getValue() == null || Objects.equals(TxtPrecoVenda.getText(), ""))
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.show();
        } else
        {
            try
            {
                violaoDao.gravar(violao);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            atualizarLista();
        }

        limparTela();
        BtnIncluir.setVisible(true);
        PnDados.setVisible(false);
    }

    @FXML
    private void BtnCancelar_Action()
    {
        limparTela();
        PnDados.setVisible(false);
        BtnIncluir.setVisible(true);
        BtnAlterar.setVisible(false);
        BtnExcluir.setVisible(false);
    }

    @FXML
    private void BtnAlterar_Action()
    {
        Violao violao = LstVioloes.getSelectionModel().getSelectedItem();
        if (violao == null) return;

        violao.setMarca(TxtMarca.getText());
        violao.setTipoCorda(CbxTipoCorda.getValue());
        violao.setPrecoVenda(Float.valueOf(TxtPrecoVenda.getText()));

        if (Objects.equals(TxtMarca.getText(), "") || CbxTipoCorda.getValue() == null || Objects.equals(TxtPrecoVenda.getText(), ""))
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.show();
        }
        else
        {
            try
            {
                violaoDao.alterar(violao);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            limparTela();
            PnDados.setVisible(false);
            BtnIncluir.setVisible(true);
            atualizarLista();
        }
    }

    @FXML
    private void BtnExcluir_Action()
    {
        Violao violao = LstVioloes.getSelectionModel().getSelectedItem();
        if (violao == null) return;

        try
        {
            violaoDao.excluir(violao);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        limparTela();
        BtnIncluir.setVisible(true);
        BtnExcluir.setVisible(false);
        PnDados.setVisible(false);
        BtnAlterar.setVisible(false);
        atualizarLista();
    }

    @FXML // Caso algum item seja selecionado na lista(TableView) com o uso do mouse
    private void LstVioloes_MouseClicked(MouseEvent evento) { exibirViolao(); }

    @FXML // Caso algum item seja selecionado na lista(TableView) com o uso do teclado
    private void LstVioloes_KeyPressed(KeyEvent evento) { exibirViolao(); }

    private void exibirViolao()
    {
        Violao violao = LstVioloes.getSelectionModel().getSelectedItem(); // Retorna os valores do violão selecionado pelo usuário no TableView
        if (violao == null) return; // Caso não encontre nenhum retorna
        BtnIncluir.setVisible(false);
        BtnGravar.setVisible(false);
        PnDados.setVisible(true);
        BtnAlterar.setVisible(true);
        BtnExcluir.setVisible(true);

        TxtMarca.setText(String.valueOf(violao.getMarca()));
        CbxTipoCorda.getItems().setAll(Arrays.asList(TipoCorda.values()));
        CbxTipoCorda.setValue(violao.getTipoCorda());
        TxtPrecoVenda.setText(violao.getPrecoVenda().toString());
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
    }

    private void limparTela() // "Apaga" os valores exibidos para o usuário
    {
        TxtMarca.setText("");
        CbxTipoCorda.setValue(null);
        TxtPrecoVenda.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        atualizarLista();
    }
}
