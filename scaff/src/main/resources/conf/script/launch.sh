#!/bin/bash

# Define variables
jarpath="/Users/priscafehiarisoadama/me/SCAFFOLDING/scaff/jar/scaff.jar"
path="Users/priscafehiarisoadama/RiderProjects/WebApplication5/WebApplication5/"
packageName="com.district.test"
view="viewTest"
viewPath="/Users/priscafehiarisoadama/IdeaProjects/"
url="http://localhost:8080/"
username="priscafehiarisoadama"
password=""
database="etudiantetu"
role="client"

# Prompt for user input
read -p "entrer le nom d utilisareur [$username]: " username
read -p "entrer le nom mot de passe [$password]: " password
read -p "entrer le nom de la base de donnees [$database]: " database
read -p "entrer l url [$url]: " url
read -p "entrer le role [$role]: " role
read -p "entrer le chemin du projet [$path]: " path
read -p "entrer le nom du package [$packageName]: " packageName
read -p "entrer le nom des view [$view]: " view
read -p "entrer le chemin des view [$viewPath]: " viewPath

# Call the jar
java -jar "$jarpath" "$path" "$packageName" "$view" "$viewPath" "$url" "$username" "$password" "$database" "$role"

# Pause (optional, as Unix-like systems don't typically pause at the end of scripts)
# read -p "Press enter to continue..."
