package client;

/*Diese Klasse dient als Subklasse von Exception zur REST API Exception Handling, 
 * erhält die genaue Spezifizierung von der Superklasse.*/

public class VokabeltrainerException extends Exception {

	private static final long serialVersionUID = -8198947756408603500L;

	public VokabeltrainerException(String text) {
		super(text);
	}
}
