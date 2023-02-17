/* 
 * Enumération des différents états possibles d'un verrou  
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 * Date : 21/01/2023
*/

public enum Lock {
    NL, //no local lock
    RLC, //read lock cached
    WLC, //write lock cached
    RLT, //read lock taken
    WLT, //write lock taken
    RLT_WLC //read lock taken, write lock cached
}
