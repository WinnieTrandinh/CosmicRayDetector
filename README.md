# CosmicRayDetector
Takes in a series of images and provides information about possible cosmic rays found in the image.
Multithreading used to optimize efficiency.

Blobs are created around the possible gamma ray pixels, and false blobs are eliminated through density and area checks. 
The gamma ray's direction and area is then calculated. Information is then stored into a txt file.

Runs on Eclipse IDE.
