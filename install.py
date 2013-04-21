#!/usr/bin/python

import os
import sys

def listJars(dir):
	return [dir + "/" + f for f in os.listdir(dir) if f.lower().endswith(".jar")]

input = "install"
output = "lib"
files = []

if not os.path.isdir(input):
	print "No '" + input + "' directory found!"
	sys.exit()

print "Listing '" + input + "' directory..."

for (folder) in os.listdir(input):
	group = folder.split(" ")

	groupId = group[0]
	groupVersion = (group[-1] if len(group) > 1 else "")

	print "+> Found group: " + groupId + (" (Version " + groupVersion + ")" if groupVersion != "" else "")

	for(file) in listJars(input + "/" + folder):
		basename = os.path.basename(file)[:-4]

		if groupVersion == "":
			split = basename.split("-")
			name = "-".join(split[:-1])
			version = split[-1]
		else:
			name = basename
			version = groupVersion

		print " - Found jar: " + name + " (Version " + version + ")"

		files.append({
			"group": groupId,
			"version": version,
			"file": file,
			"name": name
		})

user = raw_input("Enter OK to continue, anything else to cancel: ")
if user.upper() != "OK":
	print "Aborting process..."
	sys.exit()

print "Installing jars into '" + output + "'..."

for(entry) in files:
	print ":> Installing jar '" + entry["name"] + "' (GroupId: " + entry["group"] + ", Version: " + entry["version"] + ")"
	
	os.system(
		"mvn install:install-file" + \
		" -Dfile=\"" + entry["file"] + "\"" + \
		" -DgroupId=\"" + entry["group"] + "\"" + \
		" -DartifactId=\"" + entry["name"] + "\"" + \
		" -Dversion=\"" + entry["version"] + "\"" + \
		" -Dpackaging=jar" + \
		" -DlocalRepositoryPath=" + output +  \
		" -DcreateChecksum=true"
	)

print "Done! Installed" + str(len(files)) + " jars into '" + output + "'!"
print "Maven dependencies:"

for(entry) in files:
	print "\n\
<dependency>\n\
	<groupId>" + entry["group"] + "</groupId>\n\
	<artifactId>" + entry["name"] + "</artifactId>\n\
	<version>" + entry["version"] + "</version>\n\
</dependency>\n"