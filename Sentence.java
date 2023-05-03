/* 
 * Classe Sentence  
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 03/05/2023
 * La classe Sentence contient une donnée de type String
*/
public class Sentence implements java.io.Serializable {
	String 		data;
	public Sentence() {
		data = new String("");
	}
	
	/*
	 * Fonction write
	 * Parametres : text : texte à écrire
	 * Cette fonction permet d'écrire dans la donnée
	 */
	public void write(String text) {
		data = text;
	}

	/*
	 * Fonction read
	 * Retour : String : donnée
	 * Cette fonction permet de lire la donnée
	 */
	public String read() {
		return data;	
	}
	
}