#!/bin/bash

path="/mnt/c/Sneha/Studies/UCLA/Classes/Q3Spring2018/CS230/Project/api-refactoring/api_refactoring/InstrumentationResults/XmlDump_googleGuava"
cd $path
for file in *
do
if [ ! -f "$file" ];
then
echo "Ignoring ..."
else
count="$(cat ${file} | grep -c '<functionCall>')"
fileName="$(cat $file | awk -F'XmlDump_junit/' '{print $2}')"
printf "${count}: ${file}\n\n"
echo "$file","${count}" >> "../calls_googleGuava.csv"
fi
done
