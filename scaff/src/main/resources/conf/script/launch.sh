#!/bin/bash

# Define variables
jarpath="/Users/priscafehiarisoadama/me/SCAFFOLDING/scaff/jar/scaff.jar"
path="/Users/priscafehiarisoadama/RiderProjects/WebApplication5/WebApplication5/"
packageName="com.district.test"
view="viewTest"
viewPath="/Users/priscafehiarisoadama/IdeaProjects/learnIonicAngular/reciclica-app"
url="http://localhost:8080/"
username="priscafehiarisoadama"
password=""
database="etudiantitu"
role="client"

# Prompt for user input

read -p "entrer le nom d utilisareur [$username]: " username1
if [ -z "$username1" ]; then
    username1=$username
fi
read -p "entrer le nom mot de passe [$password]: " password1
if [ -z "$password1" ]; then
    password1=$password
fi
read -p "entrer le nom de la base de donnees [$database]: " database1
if [ -z "$database1" ]; then
    database1=$database
fi
read -p "entrer l url [$url]: " url1
if [ -z "$url1" ]; then
    url1=$url
fi
read -p "entrer le role [$role]: " role1
if [ -z "$role1" ]; then
    role1=$role
fi
read -p "entrer le chemin du projet [$path]: " path1
if [ -z "$path1" ]; then
    path1=$path
fi
read -p "entrer le nom du package [$packageName]: " packageName1
if [ -z "$packageName1" ]; then
    packageName1=$packageName
fi
read -p "entrer le nom des view [$view]: " view1
if [ -z "$view1" ]; then
    view1=$view
fi
read -p "entrer le chemin des view [$viewPath]: " viewPath1
if [ -z "$viewPath1" ]; then
    viewPath1=$viewPath
fi

# Call the jar
java -jar "$jarpath" "$path1" "$packageName1" "$view1" "$viewPath1" "$url1" "$username1" "$password1" "$database1" "$role1"

# Pause (optional, as Unix-like systems don't typically pause at the end of scripts)
# read -p "Press enter to continue..."
