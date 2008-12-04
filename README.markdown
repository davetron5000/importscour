# ImportScrubber

This is my fork of [ImportScrubber](http://importscrubber.sourceforge.net) that will help me deal with, once and for all, the battle between star imports (my side) and explicit imports (which I cannot abide and think is generally retarded).

This should allow me to configure ImportScrubber to run as a Git hook with local configuration so I can put star imports in my code to get things working and then have git just convert that to the styleof the project/team in which I'm working.

## Todo

* Allow command line to take specific files/classes and not just recurse on everything
* Make options configurable via some .rc file and/or environment
* Roll this into an easy to use git hook
* Devise a way to allow the git hook to not do its thing on certain files, in case ImportScrubber actually makes things not work right.

