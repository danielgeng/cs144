#!/bin/bash

mysql CS144 < drop.sql
mysql CS144 < create.sql

ant
ant run-all

sort -u Item.dat > tmp.dat
rm Item.dat
mv tmp.dat Item.dat

sort -u User.dat > tmp.dat
rm User.dat
mv tmp.dat User.dat

sort -u ItemCategory.dat > tmp.dat
rm ItemCategory.dat
mv tmp.dat ItemCategory.dat

sort -u Bid.dat > tmp.dat
rm Bid.dat
mv tmp.dat Bid.dat

mysql CS144 < load.sql

rm -f *.dat
