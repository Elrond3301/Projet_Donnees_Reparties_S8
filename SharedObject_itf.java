/* 
 * Interface SharedObject  
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 18/02/2023
 * Cette interface donne les fonctions que le client peut appeler sur un objet partagé
*/

public interface SharedObject_itf {
	/*
	 * Fonction Lock Read
	 * Cette fonction effectue un lock read sur un objet
	 */
	public void lock_read();

	/*
	 * Fonction Lock Write
	 * Cette fonction effectue un lock write sur un objet
	 */
	public void lock_write();

	/*
	 * Fonction Unlock
	 * Cette fonction effectue un unlock sur un objet
	 */
	public void unlock();
}