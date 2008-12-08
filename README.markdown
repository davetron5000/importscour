# ImportScour

This is my fork of [ImportScrubber](http://importscrubber.sourceforge.net) that will help me deal with, once and for all, the battle between star imports (my side) and explicit imports (which I find useless and retarded).

This should allow me to create a Git hook to fix the imports on checkin (so I can sanely use star imports while coding, but check in pedantic explicit imports for someone's OCD).

# Usage

`net.sourceforge.importscrubber.CLI` is the entry point to using this currently.  It takes at least three arguments:

1. Full path to the root of the classes directory
2. Full path to the root of the source directory
3. Either the word "ALL" or a list of files

## ALL

"ALL" indicates that every source file in the source root should be scoured.  

## List of Files

Here, the files are relative paths from the source root.  Each of them will be scoured in place.

# Configuration

There are four properties that control the behavior of ImportScour:

* `importscour.javalibshigh` - if "true", `java.*` import statements will precede all others.  If "false", everything is sorted by package name.  **Default is true**
* `importscour.breakstyle` - if "package", a line break is added between groups of import statements from the same package.  If "none", no line breaks are added.  **Default is "package"**
* `importscour.combinethreshold` - The threshold at which specific imports are abandoned for a package and a star import is used.  "0" means never use star imports.  This is the default.
* `importscour.threasholdstandard` - If "true", the `importscour.combinethreshold` setting only applies to <tt>java.*</tt> type classes.

How these get values is dependent on a few things.

First, you can override the defaults by specifying the above properties as system properties. **These** values can be overridden by a properties file (I realize this is somewhat backwards, but I'll fix it later).

## Locating the Properties File

By default, the code will look for a file named `.importscour.properties` in your home directory (as stored in the system property `user.home`).  If you set the environment variable IMPORTSCOUR_PROPERTIES to the location of the file, this will be used instead.

# As a Git Hook

`src/perl/pre-commit` is a Git hook you can use to fix imports on commit.  You must set three Git configuration options:

* `importscour.command` - full path to your ImportScour command
* `importscour.srcdir` - relative path to your java source root
* `importscour.classesdir` - relative path to your classes root

e.g.

    git-config --add importscour.srcdir src/java

Note that for each source file you check in, a class file must exist in the classes root, or ImportScour can't run.

## Todo

* better test cases for better coverage
* Remove ImportStatement.DEBUG in favor of real logging
* Allow system properties to override rc file
* Devise means by which certain files can be skipped
* Create option to silence output
