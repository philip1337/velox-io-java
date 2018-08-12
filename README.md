# Velox IO -  Fast VFS [(Virtual filesystem)]((https://en.wikipedia.org/wiki/Virtual_file_system))

Philip - is2.1 [Mini-Project - Antrag](PROJECT.md)

This is a java port from the non public velox io virtual filesystem (written in c++).

Following features are implemented in the VFS:
* File compression using [lz4](https://github.com/lz4/lz4-java).
* File encryption - [XTEA algorithm](https://en.wikipedia.org/wiki/XTEA) , [Library](https://github.com/xxtea/xxtea-java)

# Download:
* Releases contains example xml and precompiled binaries: [Releases](https://github.com/philip1337/velox-io-java/releases)

The archives do not own any file names so the paths are secured.  
It's not possible to access files in the archive without knowing the exact path.

# Project informations:
* The project can just be done alone or with a group of 2 people.
* The project has to be applied as a bluej project.
* You can just use the knowledge from classes.
  * Arrays, Strings, Functions
  * Surely forbidden: class, members, oop, hash tables
  
# Delivery:
* 13. August 2018

# Valuation:
* Code (75%)
  * Useability
  * Comments
  * Code guidelines
* Presentation (25%)

# Compile instructions:
* Open the pom.xml (the directory) with netbeans as maven project.
* Build it

# Useability:
* Command line:

	// Create: 
	java -jar velox-io-java-jar-with-dependencies.jar -pack output -create list.xml
	
	// Validate: 
	java -jar velox-io-java-jar-with-dependencies.jar -pack output -validate test_velox.png
