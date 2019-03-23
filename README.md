# Program # 2
Name: Thomas Wilkinson 
Cosc 4735

Description:  Run on pixel 2, use FAB to create marker with picture inside. 

Anything that doesn't work:

# Graded: 48/50 #

* Markers are always placed on the map, even if no picture is taken. *(-2 points)*

If the user opens the camera, but then decides that they don't want to take a picture after all, a marker is still placed on the map after they back out of the camera. While this is a very minor issue, you really should only place a marker when a picture is actually taken. It does not make much sense for the user to want to view a blank marker on the map.

It may seem intuitive to always place a marker with whatever information is available (given that you would expect the user to want to take a picture whenever they open the camera), but you should always try to account for any strange app behavior a user may run into when using your app. Often times, these are the weird sort of things I will be testing for.

Besides this, everything runs great, so I figured I shouldn't take more than just a couple of points off for it.
