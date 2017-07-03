
package entity;

public class Elemento {

	private String nome;
	private double costo;
	private int numeroVendite;
	
	
	public Elemento(final String nome, final double costo, final int numeroVendite) {
		this.nome = nome;
		this.costo = costo;
		this.numeroVendite = numeroVendite;
	}
	
	public void incrementeVendite(){
		this.numeroVendite++;
	}
	
	public void decrementaVendite(){
		this.numeroVendite--;
	}
	
	// getters
	public double getCosto() {
		return this.costo;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public int getNumeroVendite() {
		return this.numeroVendite;
	}
}
