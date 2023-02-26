#!/bin/sh

nbclientsNormaux=5
nbclientsLazy=2

echo "" > test.txt
echo "" > test_abonne.txt

for i in `seq 1 $nbclientsNormaux` # on lance les clients normaux
do
    java ClientNormal ClientNormal$i &
done

for i in `seq 1 $nbclientsLazy` # on lance les clients lazy
do
    java ClientLazy ClientLazy$i &
done

wait

exit 0