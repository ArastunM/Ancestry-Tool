# Ancestry-Tool
Ancestry Tool is a Java window-based application created using [Swing](https://en.wikipedia.org/wiki/Swing_(Java)) framework.

The tool aims at making use of publicly available ancestry project calculator's datasheets similar to the likes of 
[Vahaduo](http://vahaduo.genetics.ovh/) and [Gedmatch](https://www.gedmatch.com/). It makes use of 3 ancestry sample types
(modern, ancient and average) based on 4 Gedmatch calculators (Eurogenes K13, Dodecad K12b, MDLP World, Ancient Eurasia K6).

The goal is achieved by implementing a local database interface, individual sample windows as well as distance 
and single calculators to be used for sample composition comparisons. The UI consists of light and dark mode layouts.


## Prerequisites
[Java version 15.0.1](https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html) along with
[JFreeChart](https://www.jfree.org/jfreechart/) library was used in development.


## Installation
The project mainly utilizes in-built Java package [Swing](https://en.wikipedia.org/wiki/Swing_(Java)) for 
the GUI; however, additional jcommon 1.0.23 and freechart 1.10.19 jar files of [JFreeChart](https://www.jfree.org/jfreechart/download.html) 
should be installed.
