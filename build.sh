#!/bin/bash

downloadPath="/mnt/c/Sneha/Studies/UCLA/Classes/Q3Spring2018/CS230/Project/github-repos"
file=""

buildCheck() {
path=$1
project=$2
printf "****************************\nBuilding $project....\n**********************
******\n\n"
if mvn compile -f "${path}/${project}/pom.xml" | grep -q "BUILD SUCCESS";
then
return 1
else
return 0
fi
}

moveFailed() {
path=$1
line=$2
buildStatus=$3
username=`echo $line | awk -F'/' '{print $1}'`
reponame=`echo $line | awk -F'/' '{print $2}'`
project="${username}_${reponame}"

#Check build status
failedDir="failedProjects"
if [ $buildStatus == 0 ]; then
printf "****************************\nBuild FAILED for $line !!!\n*****************
***********\n\n"

#Check if failedDir exists
if [ ! -d "${path}/${failedDir}" ];
then
printf "The failed build directory does not exist. Creating...\n"
`mkdir "${path}/${failedDir}"`
fi

printf "Moving failed project to $failedDir directory...\n"
mv "${path}/${project}" "${path}/${failedDir}"

if [ ! -f "$path/failed.txt" ]; then
echo "$path/$line" > "$path/failed.txt"
else
echo "$path/$line" >> "$path/failed.txt"
fi

else
printf "****************************\nBuild SUCCEEDED for $line !!!\n**************
**************\n\n"
fi

}

if [ $# -eq 1 ]; then
file=$1
#echo $file
fi

if [[ $file != "" ]]; then
#read the file
while read line
do
line=`echo $line | awk -F'\r' '{print $1}'`
username=`echo $line | awk -F'/' '{print $1}'`
reponame=`echo $line | awk -F'/' '{print $2}'`
echo "Name=$username; Repo=$reponame"
project="${username}_${reponame}"

buildCheck $downloadPath $project
buildReturn=$?
moveFailed $downloadPath $line $buildReturn

done < "${file}"

else
cd $downloadPath
for dir in *; do
buildCheck $downloadPath $project
buildReturn=$?
moveFailed $downloadPath $line $buildReturn
#count=$(($count + 1))
done
#echo $count
fi
