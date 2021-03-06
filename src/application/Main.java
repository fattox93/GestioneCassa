package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import elementiGrafici.BottoneElemento;
import entity.Elemento;
import entity.ElementoScontrino;
import entity.Scontrino;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Main extends Application {

	//elementi grafici
	//gli elementi si possono togliere e usare semplicemente il flowpaneBottoni dal quale � possibile ottenere i bottoni i
	//quali al loro interno hanno un campo elemento (Da rivedere)
	public static ListView<ElementoScontrino> conteggio = new ListView<ElementoScontrino>();
	private static List<BottoneElemento> listaBottoniElementi = new ArrayList<>();
	private static FlowPane flowPaneBottoni = new FlowPane();
	public static Label totaleCosto = new Label("0.00");

	private static List<Elemento> elementi = new ArrayList<>();
	private static Scontrino scontrino = null;
	private static List<Scontrino> storicoScontrini = new ArrayList<>();

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			root.setCenter(flowPaneBottoni);
			flowPaneBottoni.setAlignment(Pos.CENTER);
			Scene scene = new Scene(root,1400,800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			impostaChiusura(primaryStage);
			inizializzaRiepilogo(root);
			inizializzaPaneAggiuntaElemento(root);
			inizializzaBottoniElementi(primaryStage);
			boolean riuscitaCaricamentoScontrini = caricaStoricoScontrini();
			
			if(!riuscitaCaricamentoScontrini){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Messaggio di ERRORE");
				alert.setHeaderText("Errore di caricamento dello storico degli scontrini");

				//Aggiungere il bottone inizializzaBottoni in caso di errore
				alert.setContentText("Errore nel caricare il file dello storico degli scontrini controlla la posizione e la sintassi!");
				alert.showAndWait();
			}
			
			
			FlowPane paneTest = new FlowPane();
			//Vedere poi dove metterlo magari in un menu in alto
			Button bottoneTest = new Button("Test");
			root.setBottom(paneTest);

			// Stampa lo storico di tutti gli scontrini
			bottoneTest.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					double ricavoTotale = 0.0;
					System.out.println("TEST stampa tutto");
					// ciclo tutto lo storico degli scontrini
					for (Scontrino tempScontrino : Main.storicoScontrini) {
						tempScontrino.stampaScontrino();
						ricavoTotale += tempScontrino.getTotaleScontrino();
					}

					System.out.println("Totali elementi:");
					for (Elemento tempElemento : Main.elementi) {
						System.out.println(tempElemento.getNome() + "\t nr totale vendite: " + tempElemento.getNumeroVendite());
					}
					System.out.println("Incasso totale scontrini:\t" + ricavoTotale);
				}
			});
			
			
			Button resetElementi = new Button("Inizia nuovo evento");
			
			//magari mettere la richiesta di una conferma
			resetElementi.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					//impostare a zero tutti i counter degli elementi e reinizializzare lo storico scontrini
					for (Elemento elem : elementi) {
						elem.setNumeroVendite(0);
					}
					
					//elimino lo scontrino in corso
					if(Main.scontrino != null){
						Main.totaleCosto.setText("0.00");
						Main.scontrino = null;
					}
					Main.storicoScontrini = new ArrayList<>();
				}
			});

			paneTest.getChildren().addAll(resetElementi,bottoneTest);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private boolean caricaElementi(){
		try {
			BufferedReader input = new BufferedReader(new FileReader("elementi.txt"));
			String riga;

			//ciclo per leggere tutte le righe del file degli elementi
			while ((riga = input.readLine()) != null) {
				System.out.println(riga);

				//ogni riga la scompongo estraendo le informazioni
				List<String> listaInformazioni= Arrays.asList(riga.split(","));

				Elemento elem = new Elemento(listaInformazioni.get(0), Double.parseDouble(listaInformazioni.get(1)), Integer.parseInt(listaInformazioni.get(2)));
				Main.elementi.add(elem);
			}

			input.close();

			return true;
		} catch (IOException ioException) {
			System.out.println("errore nella lettura del file");
			ioException.printStackTrace();
			return false;
		}
	}

	private boolean caricaStoricoScontrini(){
		try {
			BufferedReader input = new BufferedReader(new FileReader("storico.txt"));
			String riga;
			StringBuilder strb = new StringBuilder();
			//ciclo per leggere tutte le righe del file degli elementi
			while ((riga = input.readLine()) != null) {
				strb.append(riga);
			}
			
			if(strb.length() == 0){
				System.out.println("file vuoto");
				input.close();
				return true;
			}

			System.out.println("finito di leggere lo storico creo il jsonobject");
			JSONObject storicoScontrini = new JSONObject(strb.toString());
			JSONArray elementi = (JSONArray) storicoScontrini.get("elementi");

		
			for(int i=0; i<elementi.length(); i++){
				JSONObject tempScontrino = elementi.getJSONObject(i);
				System.out.println(tempScontrino.getString("data"));
				Scontrino s = new Scontrino();
				
				s.setDataCreazione(tempScontrino.getString("data"));
				s.setNumeroScontrino(tempScontrino.getInt("numero"));
				s.setTotaleScontrino(tempScontrino.getDouble("totale"));
				
				//carico gli elementi dello scontrino
				JSONArray elem = tempScontrino.getJSONArray("oggetti");
				ObservableList<ElementoScontrino> elementiScontrino = FXCollections.observableArrayList();
				
				for(int j=0; j<elem.length(); j++){
					JSONObject rigaScontrino = elem.getJSONObject(j);
					ElementoScontrino elementoScontrino = new ElementoScontrino(rigaScontrino.getString("nome"), rigaScontrino.getDouble("costo"));
					elementiScontrino.add(elementoScontrino);
				}
				s.setListaElementi(elementiScontrino);
				Main.storicoScontrini.add(s);
			}
			
			input.close();
			return true;
		} catch (IOException ioException) {
			System.out.println("errore nella lettura del file");
			ioException.printStackTrace();
			return false;
		}
	}

	private void inizializzaBottoniElementi(Stage primaryStage){

		// lettura da file degli elementi con relativi costi e quantit�
		boolean risultatoCarica = caricaElementi();

		if(!risultatoCarica){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Messaggio di ERRORE");
			alert.setHeaderText("Errore di lettura");

			//Aggiungere il bottone inizializzaBottoni in caso di errore
			alert.setContentText("Errore nel nome del file degli elementi controlla la posizione e la sintassi! Poi premere il bottone inizializza elementi");
			alert.showAndWait();
		}

		for (Elemento tempElemento : Main.elementi) {
			BottoneElemento bottoneElem = new BottoneElemento(tempElemento);
			listaBottoniElementi.add(bottoneElem);
		}

		for (BottoneElemento bottoneElemento : listaBottoniElementi) {

			flowPaneBottoni.getChildren().add(bottoneElemento);

			bottoneElemento.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					Elemento elemento = bottoneElemento.getElemento();

					if(scontrino == null){
						scontrino = new Scontrino(Main.storicoScontrini.size());
						scontrino.aggiungiElemento(elemento);
					}else{
						scontrino.aggiungiElemento(elemento);
					}
				}
			});

			bottoneElemento.setPrefSize(150, 80);
		}
	}	

	private void inizializzaRiepilogo(BorderPane root){

		BorderPane riepilogo = new BorderPane();
		riepilogo.setCenter(conteggio);
		// Riepilogo sulla destra
		conteggio.setCellFactory(lv -> new ListCell<ElementoScontrino>() {
			@Override
			public void updateItem(ElementoScontrino item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
				} else {
					String text = item.toString() ;
					setText(text);
				}
			}
		});
		root.setRight(riepilogo);

		FlowPane bottoniScontrino = new FlowPane();
		Button bottoneStampa = new Button("Stampa");
		bottoniScontrino.getChildren().addAll(bottoneStampa, totaleCosto);
		riepilogo.setBottom(bottoniScontrino);
		// Stampa lo scontrino
		bottoneStampa.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				if(Main.scontrino != null){
					Main.scontrino.stampaScontrino();
					Main.storicoScontrini.add(scontrino);
					Main.totaleCosto.setText("0.00");
					Main.scontrino = null;
				}
			}
		});
	}

	private void inizializzaPaneAggiuntaElemento(BorderPane root) {

		VBox nuovoBottonePane = new VBox();
		nuovoBottonePane.setAlignment(Pos.CENTER);
		Text labelNome = new Text("Nome Bottone");
		Text labelCosto = new Text("Costo");
		TextField textFieldNome = new TextField ();
		TextField textFieldCosto = new TextField ();

		Button salvaNuovoBottone = new Button("Salva");
		salvaNuovoBottone.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				//recupero i valori nome e costo del nuovo bottone
				String nome = textFieldNome.getText();

				//attenzione possibile errore di parsing (gestire exception)
				double costo = Double.parseDouble(textFieldCosto.getText());

				//creo nuovo elemento, bisogna salvarlo nel file in modo che resti salvato anche alla prossima apertura
				Elemento nuovoElemento = new Elemento(nome, costo, 0);
				Main.elementi.add(nuovoElemento);
				BottoneElemento nuovoBottone = new BottoneElemento(nuovoElemento);
				listaBottoniElementi.add(nuovoBottone);

				//aggiungo il listener
				nuovoBottone.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						Elemento elemento = nuovoBottone.getElemento();

						if(scontrino == null){
							scontrino = new Scontrino(Main.storicoScontrini.size());
							scontrino.aggiungiElemento(elemento);
						}else{
							scontrino.aggiungiElemento(elemento);
						}
					}
				});

				nuovoBottone.setPrefSize(150, 80);
				flowPaneBottoni.getChildren().add(nuovoBottone);

			}
		});

		nuovoBottonePane.getChildren().addAll(labelNome, textFieldNome, labelCosto, textFieldCosto, salvaNuovoBottone);

		root.setLeft(nuovoBottonePane);

	}
	
	private void impostaChiusura(Stage primaryStage){
		//operazioni da eseguire quando si chiude l'applicazione (salvare tutte le informazioni)
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override public void handle(WindowEvent t) {
				System.out.println("Save all information in file xyz.txt");
				try{
					PrintWriter writer = new PrintWriter("elementi.txt");
					for (Elemento elemento : Main.elementi) {
						writer.println(elemento.getNome() + "," + elemento.getCosto() + "," + elemento.getNumeroVendite());
					}
					writer.close();

					JSONObject storicoScontrini = new JSONObject();
					JSONArray elementi = new JSONArray();
					
					//ciclo tutti gli scontrini
					for (Scontrino scontrino : Main.storicoScontrini) {
						JSONObject tempScontrino = new JSONObject();
						tempScontrino.put("data", scontrino.getDataCreazione());
						tempScontrino.put("totale", scontrino.getTotaleScontrino());
						tempScontrino.put("numero", scontrino.getNumeroScontrino());

						JSONArray elem = new JSONArray();
						//ciclo tutti gli elementi dello scontrino per salvarli
						for (ElementoScontrino elemScontrino : scontrino.getListaElementi()) {
							JSONObject temp = new JSONObject();
							temp.put("costo", elemScontrino.getCosto());
							temp.put("nome", elemScontrino.getNome());
							elem.put(temp);
						}
						tempScontrino.put("oggetti", elem);
						elementi.put(tempScontrino);
					}

					storicoScontrini.put("elementi", elementi);
					writer = new PrintWriter("storico.txt");
					writer.print(storicoScontrini.toString());
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
