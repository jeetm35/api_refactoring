#!/bin/bash

file=$1

sed -i 's/\\/\//g' $file
sed -i '/\/src\/test\//d' $file

printf "\n********************************************\nDone processing. Please check your file\n********************************************\n\n"
