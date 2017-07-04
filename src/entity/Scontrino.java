package entity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Scontrino {
	private String dataCreazione;
	private ObservableList<ElementoScontrino> listaElementi;
	private int numeroScontrino;
	private double totaleScontrino;
	
	// constructor
	public Scontrino(final int numeroScontrino) {
		Calendar calendario = new GregorianCalendar();
		Date data = calendario.getTime();
		
		this.dataCreazione = data.getDate() + "." + (data.getMonth()+1) + "." + (data.getYear()+1900) + " - " + data.getHours() + ":" +data.getMinutes() + ":" +data.getSeconds();
		System.out.println(dataCreazione);
		this.listaElementi = FXCollections.observableArrayList();
		this.numeroScontrino = numeroScontrino;
		this.totaleScontrino = 0.0;
		Main.conteggio.setItems(this.listaElementi);
	}
	
	public Scontrino(){
		
	}

	public boolean aggiungiElemento(final Elemento elemento){
				
		// creo un nuovo elementoScontrino con i dati di elemento
		ElementoScontrino elementoDaAggiungere = new ElementoScontrino(elemento.getNome(), elemento.getCosto());
		
		boolean aggiuntaRiuscita = this.listaElementi.add(elementoDaAggiungere);
		
		if(aggiuntaRiuscita){
			this.totaleScontrino += elementoDaAggiungere.getCosto();
			elemento.incrementeVendite();
			Main.totaleCosto.setText(Double.toString(this.totaleScontrino));
		}
		
		return aggiuntaRiuscita;
	}
	
	public boolean eliminaElemento(final ElementoScontrino elementoDaEliminare){
		
		boolean rimozioneRiuscita = true;
		//elimino elemento dalla lista todo
		return rimozioneRiuscita;
	}
	
	public boolean stampaScontrino(){
		//da usare quando ci sara l'interfacciamento alla stampante
		boolean stampaRiuscita = false;
		// fare la stampa e se si riesce mettere a true stampaRiuscita
		System.out.println("-------------------------------------------------------");
		System.out.println("Scontrino numero " + this.numeroScontrino + " Data: " + this.dataCreazione);
		for (ElementoScontrino elementoScontrino : listaElementi) {
			System.out.println(elementoScontrino.getNome() + "\t\t" + elementoScontrino.getCosto());
		}
		
		System.out.println("Costo totale: \t" + this.totaleScontrino);
		System.out.println("-------------------------------------------------------");
		
		return true;
	}
	
	// getters
	public ObservableList<ElementoScontrino> getListaElementi() {
		return this.listaElementi;
	}
	public String getDataCreazione() {
		return this.dataCreazione;
	}
	
	public double getTotaleScontrino() {
		return this.totaleScontrino;
	}
	
	public int getNumeroScontrino() {
		return this.numeroScontrino;
	}
	
	public void setDataCreazione(String dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	
	public void setListaElementi(ObservableList<ElementoScontrino> listaElementi) {
		this.listaElementi = listaElementi;
	}
	
	public void setNumeroScontrino(int numeroScontrino) {
		this.numeroScontrino = numeroScontrino;
	}
	
	public void setTotaleScontrino(double totaleScontrino) {
		this.totaleScontrino = totaleScontrino;
	}
	
}
