#!/bin/sh

nbclientsNormaux=2
nbclientsLazy=0


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

exit 0