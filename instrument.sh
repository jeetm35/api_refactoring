#!/bin/bash

path="/mnt/c/Sneha/Studies/UCLA/Classes/Q3Spring2018/CS230/Project/github-repos_junit"
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
result="$(mvn test -f "${path}/${line}" | grep 'Tests run.*Time elapsed')"
#mvn test -f "${path}/${line}" | grep "Tests run.*Time elapsed" > testsExecuted.txt
#myuser="$(grep '^vivek' /etc/passwd)"
echo "$line=$result" >> "executions/${line}_tests.txt"
done < "${file}"

else
cd $path
for dir in *; do
if [[ ( -d $dir ) && ( $dir != "failedProjects" ) ]]; then
printf "****************************\nRunning tests of $dir....\n****************************\n\n"
testFile="testsExecuted"
mvn test -f "${path}/${dir}" | grep "Tests run.*Time elapsed" > "executions/${line}_tests.txt"

#count=$(($count + 1))
fi
done
#echo $count
fi
