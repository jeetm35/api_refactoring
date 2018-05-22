#!/bin/bash

file=$1
#downloadPath=$2
downloadPath="/mnt/c/Sneha/Studies/UCLA/Classes/Q3Spring2018/CS230/Project/Repositories"

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
echo "Username=$username; Reponame=$reponame"


if [ ! -f "$path/${username}_${reponame}.tar.gz" ]; then
mkdir -p "${path}/${line}"
echo "Beginning to clone $line ..."
`git clone "https://github.com/$line" "$path/$line" > /dev/null 2>&1` 
printf "Successfully cloned $line!\n\n"
tar zcf "${path}/${username}_${reponame}.tar.gz" "${path}/${line}" --remove-files

if [ -z "$(ls -A $path/$username)" ]; then
rmdir "${path}/${username}"
else 
echo "Cannot delete directory!"
fi

else
echo "$line already cloned. Skipping..."
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
#if [ ! "$(ls -A $2)" ]; then
#printf "Repositories is empty...\n\n"
#download $1 $2
#fi

} 

checkDownloadLocation $file $downloadPath 
