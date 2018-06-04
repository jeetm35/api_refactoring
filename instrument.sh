#!/bin/bash

path="/mnt/c/Sneha/Studies/UCLA/Classes/Q3Spring2018/CS230/Project/github-repos"
file=""
if [ $# -eq 1 ]; then
file=$1
#echo $file
fi

#count=0
if [[ $file != "" ]]; then
#read the file
while read line
do
printf "****************************\nRunning tests of $line....\n****************************\n\n"
mvn test -f "${path}/${line}"
done < "${file}"

else
cd $path
for dir in *; do
if [[ ( -d $dir ) && ( $dir != "failedProjects" ) ]]; then
printf "****************************\nRunning tests of $dir....\n****************************\n\n"
mvn test -f "${path}/${dir}"
#count=$(($count + 1))
fi
done
#echo $count
fi
