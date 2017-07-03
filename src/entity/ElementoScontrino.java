package entity;

public class ElementoScontrino {
	private String nome;
	private double costo;

	// constructor
	public ElementoScontrino(final String nome, final double costo) {
		this.costo = costo;
		this.nome = nome;
	}

	// getters
	public String getNome() {
		return this.nome;
	}
	
	public double getCosto() {
		return this.costo;
	}
	
	// setters
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setCosto(double costo) {
		this.costo = costo;
	}
	
	@Override
	public String toString() {
		return this.nome + "\t\t" + this.costo;
	}
}
