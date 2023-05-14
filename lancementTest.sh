#!/bin/sh

#### PENSEZ A CHANGER LE NOMBRE DES CLIENTS TOTAUX DANS Server.java ET LANCER LE SERVEUR ####

nbclientsNormaux=10
nbclientsLazy=0

echo "!== Server.java doit être lancé avant de lancer ce script ==!"
echo "!== Pensez à actualiser le nombre de clients totaux dans Server.java si il y a une erreur ==!"
echo "!== Pensez à faire un \$killall java pour tuer tous les processus en fond ==!"
echo "!== Pensez à modifier la probabilité de panne dans les clients ==!"
echo ""
echo "== LANCEMENT DU TEST AVEC $nbclientsNormaux CLIENTS NORMAUX ET $nbclientsLazy CLIENTS LAZY == "
echo ""
echo "==> Résultats en cours dans test.txt ... "


echo "" > test.txt

for i in `seq 1 $nbclientsNormaux` # on lance les clients normaux
do
    java ClientNormal ClientNormal$i &
done

for i in `seq 1 $nbclientsLazy` # on lance les clients lazy
do
    java ClientLazy ClientLazy$i &
done

wait

#### PENSEZ A FAIRE UN $killall java POUR TUER LES PROCESSUS JAVA EN FOND ####

exit 0