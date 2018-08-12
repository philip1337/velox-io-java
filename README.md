# Velox IO -  Fast VFS [(Virtual filesystem)]((https://en.wikipedia.org/wiki/Virtual_file_system))

This is a java port from the non public velox io virtual filesystem (written in c++).

Following features are implemented in the VFS:
* File compression using [lz4](https://github.com/lz4/lz4-java).
* File encryption - [XTEA algorithm](https://en.wikipedia.org/wiki/XTEA).

The archives do not own any file names so the paths are secured.  
It's not possible to access files in the archive without knowing the exact path.



