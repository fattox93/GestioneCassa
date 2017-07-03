package elementiGrafici;

import entity.Elemento;
import javafx.scene.control.Button;

public class BottoneElemento extends Button{
	private Elemento elemento;

	 public BottoneElemento(final Elemento elemento) {
		 super(elemento.getNome());
		 this.elemento = elemento;
	}
	 
	 public Elemento getElemento() {
		return this.elemento;
	}
}
