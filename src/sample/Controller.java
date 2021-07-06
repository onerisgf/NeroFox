package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import sample.model.Favoritos;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    String http = "https://";

    @FXML  //CAMPO URL QUE SERA INSERIDO O SITE
    TextField url;

    @FXML
    private ComboBox<Favoritos> cbFavoritos;

    private ObservableList<Favoritos> obsFavoritos;

    public Button btn_go;

    public Button iconFav;

    @FXML  //CAMPO WEBVIEW ONDE APARECE OS CONTEUDOS
    WebView web;

    String endereco; //VARIAVEL GLOBAL PARA MANIPULAR AS URL

    ArrayList<String> historico = new ArrayList();  //VARIAVEL PARA SALVAR O HISTORICO DE NAVEGAÇÃO

    private List<Favoritos> favoritos = new ArrayList<>(); //LISTA DE FAVORITOS

    int navega = 0;  //VARIAVEL QUE ARMAZENA OS NUMEROS PARA AVANÇAR E VOLTAR

    WebEngine motor;  //WebEngine CARREGA A PAGINA E CRIA O MODELO DO DOCUMENTO

    Connection connect;

    //METODO AO CLICAR NO BOTÃO GO

    public void go(ActionEvent actionEvent) {

        endereco = url.getText().toString();
        motor.load(http+endereco);
        historico.add(endereco);
        navega++;
    }


    //METODO QUE RODA AO INICIAR O PROGRAMA

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            realizaConexao();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            carregaFavoritos();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        cbFavoritos.setPromptText("Favoritos");  //INDICE DO COMBOBOX

        motor = web.getEngine();  //INICIA O MOTOR DO NAVEGADOR
        endereco = "www.google.com";
        motor.load(http + endereco);  //MOTOR FAZ O CARREGAMENTO DA PAGINA INICIAL
        url.setText(endereco);
        historico.add(endereco);  //ADICIONO AO CAMPO 0 DO ARRAY O ENDEREÇO

        motor.isJavaScriptEnabled();

        motor.executeScript("document.documentElement.innerHTML");
        //motor.executeScript("navigator.mediaDevices.getUserMedia({audio: true})");

        System.out.println(motor.getUserAgent());  //RETORNA NO CONSOLE A VERSÃO DO NAVEGADOR QUE ESTOU UTILIZANDO


    }


    //METODO AO CLICAR NO BOTÃO VOLTAR

    public void volta(ActionEvent actionEvent) {

            if(navega > 0){

                navega--;
                motor.load(http + historico.get(navega));
                url.setText(historico.get(navega));

            }else{
                motor.load(http + historico.get(navega));
            }

    }

    //METODO AO CLICAR NO BOTÃO AVANÇAR

    public void avanca(ActionEvent actionEvent) {

        if(historico.size() > 1){
            navega++;
            motor.load((http + historico.get(navega)));
            url.setText(historico.get(navega));

        }else{
            motor.load(http + historico.get(navega));
        }

    }

    //EVENTO ACIONADO AO PRESSIONAR UMA TECLA NO CAMPO URL

    public void enter(KeyEvent keyEvent) {

        KeyCode botao = keyEvent.getCode(); //BOTAO RECEBE O COD DO EVENTO
        if(botao.toString() == "ENTER"){  //SE BOTÃO FOR ENTER ENTÃO CHAME O ACTION DO BOTÃO btn_go

            btn_go.fire();

        }

    }

    //EVENTO PARA VOLTAR A PAGINA INICIAL

    public void home(ActionEvent actionEvent) {
        navega++;
        endereco = "www.google.com";
        motor.load(http + endereco);
        url.setText(endereco);
        historico.add(endereco);

    }

    //METODOS BANCO DE DADOS

    public void realizaConexao() throws SQLException {

        //REALIZAR CONEXAO COM BANCO

        String caminho = "jdbc:sqlite:C:/Users/oneri/Documents/My Life/Faculdade/4-Semestre/Java/2-B/NeroFox/Favoritos.db";

        connect = DriverManager.getConnection(caminho);
        System.out.println("Conexão OK");

    }

    //METODO PARA CARREGAR OS FAVORITOS

    public void carregaFavoritos() throws SQLException {

        //LIMPA A LISTA DE FAVORITOS

        favoritos.clear();

        int aux = 0;

        //REALIZAR SELECT NO BD

        Statement stmt = connect.createStatement();
        ResultSet result = stmt.executeQuery("Select * from tblink");

        //PROCESSAR RESULTADOS

        while(result.next()){
            System.out.println(result.getString("colLink"));

            aux++;

            Favoritos favoritos1 = new Favoritos(aux, result.getString("colLink"));
            favoritos.add(favoritos1);
        }

        obsFavoritos = FXCollections.observableArrayList(favoritos);

        cbFavoritos.setItems(obsFavoritos);

        //FECHA A CONEXÃO

        result.close();
        stmt.close();
        connect.close();

    }

    //ADD FAVORITOS - INSERT

    public void addFav(ActionEvent actionEvent) throws SQLException {

        realizaConexao();

        String favorito =  url.getText();

        String comandoSQL = "insert into tbLink (colLink) values (?);";  //COMANDO PARA INSERT

        PreparedStatement inserir = connect.prepareStatement(comandoSQL);  //PREPARA A VARIAVEL PARA A CONEXÃO

        //PREENCHE OS VALORES NO (?)

        inserir.setString(1,favorito);

        //EXECUTAR COMANDO

        inserir.execute();

        carregaFavoritos();

    }

    //DELETE FAVORITOS

    public void btremove(ActionEvent actionEvent) throws SQLException {

        realizaConexao();

        String favorito =  url.getText();

        String comandoSQL = "delete from tbLink where colLink=(?);";    //COMANDO DELETE

        PreparedStatement excluir = connect.prepareStatement(comandoSQL);

        excluir.setString(1,favorito);

        excluir.execute();

        carregaFavoritos();

    }
}
