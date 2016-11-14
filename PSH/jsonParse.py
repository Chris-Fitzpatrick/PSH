#!/usr/bin/env python

# Written by William Luna
# Program parses relevant json data from Jump Off Campus
# For mobile dev class

import re

print ("Starting Python JSON Parsing Script")
oldJSON = open("jsondata.txt", "r")
newJSON = open("jsondataNew.txt", "w")

newJSON.write('{' + '\n')
newJSON.write('  "The_Houses" : {' + '\n')
first = True

for line in oldJSON:
	if "address" in line:
		if first == True:
			lastLine = re.sub(r'"address":', '', line)
			lastLine = re.sub(r',', '', lastLine)
			lastLine = re.sub(r'#', '', lastLine)
			lastLine = re.sub(r"\.", '', lastLine)
			lastLine = lastLine + "  : { "
			newJSON.write((lastLine) + '\n')
			first = False
		else:
			newJSON.write('   },' + '\n')
			lastLine = re.sub(r'"address":', '', line)
			lastLine = re.sub(r',', '', lastLine)
			lastLine = re.sub(r'#', '', lastLine)
			lastLine = re.sub(r"\.", '', lastLine)
			lastLine = lastLine + "  : { "
			newJSON.write((lastLine) + '\n')

	if (("price" in line) 
		and not ("max" in line) 
		and not ("person" in line)):
		newJSON.write((line) + '\n')
	if "bedrooms" in line:
		lastLine3 = re.sub(r'-', '', line)
		newJSON.write((lastLine3) + '\n')
	if "latitude" in line:
		newJSON.write((line) + '\n')
	if "longitude" in line:
		lastLine2 = re.sub(r',', '', line)
		newJSON.write((lastLine2) + '\n')

newJSON.write('}')
newJSON.write('}')
newJSON.write('}')
print("End of script")

oldJSON.close()
newJSON.close()
