#!/bin/bash

file=$1
#downloadPath=$2
downloadPath="/mnt/c/Sneha/Studies/UCLA/Classes/Q3Spring2018/CS230/Project/github-repos"

buildCheck() {
path=$1
project=$2
printf "****************************\nBuilding $project....\n****************************\n\n"
if mvn compile -f "${path}/${project}/pom.xml" | grep -q "BUILD SUCCESS";
then
return 1
else
return 0
fi
}

tarAndDelete() {
path=$1
line=$2
buildStatus=$3
username=`echo $line | awk -F'/' '{print $1}'`
reponame=`echo $line | awk -F'/' '{print $2}'`
project="${username}_${reponame}"
tarball="${project}.tar.gz"
tar zcf ${tarball} -C ${path} ${project}/ --remove-files
#tar zcf ${tarball} -C ${path} ${project}/
mv ${tarball} $path

#Check build status
failedDir="failedProjects"
if [ $buildStatus == 0 ]; then
printf "****************************\nBuild FAILED for $line !!!\n****************************\n\n"

#Check if failedDir exists
if [ ! -d "${path}/${failedDir}" ];
then
printf "The failed build directory does not exist. Creating...\n"
`mkdir "${path}/${failedDir}"`
fi

printf "Moving failed project to $failedDir directory...\n"
mv "${path}/${tarball}" "${path}/${failedDir}"

if [ ! -f "$path/failed.txt" ]; then
echo "$path/$line" > "$path/failed.txt"
else
echo "$path/$line" >> "$path/failed.txt"
fi

else
printf "****************************\nBuild SUCCEEDED for $line !!!\n****************************\n\n"
fi

}

download() {
file=$1
path=$2
printf "****************************\nCommencing download.\nStay calm, this may take a few minutes...\n****************************\n\n"

while read line
do
#echo "$line"
line=`echo $line | awk -F'\r' '{print $1}'`
username=`echo $line | awk -F'/' '{print $1}'`
reponame=`echo $line | awk -F'/' '{print $2}'`
echo "Name=$username; Repo=$reponame"
project="${username}_${reponame}"

#if [ ! -f "$path/${project}.tar.gz" ]; then
if [[ ( ! -d "$path/${project}" ) && ( ! -f "$path/failedProjects/${project}.tar.gz" ) && ( ! -d "$path/failedProjects/${project}" ) ]]; then
mkdir "${path}/${project}"
echo "Beginning to clone $line ..."
`git clone "https://github.com/${line}.git" "$path/${project}" > /dev/null 2>&1` 
printf "Successfully cloned $line!\n\n"

#buildCheck $path $project
#buildReturn=$?
#tarAndDelete $path $line $buildReturn

else
printf "$line already cloned. Skipping...\n\n"
fi

done < $file
}

checkDownloadLocation() {
printf "\n****************************\nChecking download location...\n****************************\n\n"
reposFile=$1
dirLocation=$2

if [ ! -d "$2" ]; then
printf "The download location does not exist. Creating...\n"
`mkdir $2`
printf "Created $2\n"
fi

download $1 $2

} 

checkDownloadLocation $file $downloadPath

#ToDo: init failed.txt
#ToDo: usage
#ToDo: find tar in failed or dir 
#ToDo: repos file starts with #
#ToDo: separate download and build script
