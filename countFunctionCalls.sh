#!/bin/bash

path="/mnt/c/Sneha/Studies/UCLA/Classes/Q3Spring2018/CS230/Project/api-refactoring/api_refactoring/InstrumentationResults/XmlDump"
cd $path
find -name "* *" -type f | rename 's/ /_/g'
for file in *
do
if [ ! -f "$file" ];
then
echo "Ignoring ..."
else
count="$(cat ${file} | grep -c '<functionCall>')"
printf "${count}: ${file}\n\n"
echo "$file","${count}" >> "../calls_junit_demo.csv"
fi
done
