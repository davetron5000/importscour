# ImportScour

This is my fork of [ImportScrubber](http://importscrubber.sourceforge.net) that will help me deal with my hatred of explicit imports, but the necessity I may have in not using star imports.  This can be used as a Git hook to "fix" imports so that star imports are not being used.

Ideally, this could go both ways, however converting a file to all star imports possibly breaks it without some more knowledge of the class, so this is just a one-way street.

# Install

## From Source

1. `ant package`
2. `build/importscour-0.9.0.tgz` has the Binary distro; proceed to the next section

## From non-existant Binary Distro

1. Extract .zip file somewhere
2. Make importscour.sh executable (e.g. chmod +x importscour.sh)
3. Make sure importscour.sh is in your path
4. importscour.sh path_to_classes path_to_sources path/relative/to/path_to_sources/File.java...

# Usage

`net.sourceforge.importscrubber.CLI` is the entry point to using this currently.  It takes at least three arguments:

1. Full path to the root of the classes directory
2. Full path to the root of the source directory
3. Either the word "ALL" or a list of files

## ALL

"ALL" indicates that every source file in the source root should be scoured.  

## List of Files

Here, the files are relative paths from the source root.  Each of them will be scoured in place.

## Ant

Ant should still work as it does for ImportScrubber and I don't think any of these options affect that (so you are better off just using the original).

# Configuration

There are four properties that control the behavior of ImportScour:

* `importscour.javalibshigh` - if "true", `java.*` import statements will precede all others.  If "false", everything is sorted by package name.  **Default is true**
* `importscour.breakstyle` - if "package", a line break is added between groups of import statements from the same package.  If "none", no line breaks are added.  **Default is "package"**
* `importscour.combinethreshold` - The threshold at which specific imports are abandoned for a package and a star import is used.  "0" means never use star imports.  This is the default.
* `importscour.threasholdstandard` - If "true", the `importscour.combinethreshold` setting only applies to <tt>java.*</tt> type classes.
* `importscour.ignoremissingclasses` - If "true", if a matching class file cannot be found for a source file, the source file is silently skipped.  If "false", such a condition will cause a fatal error (default "false").

How these get values is dependent on a few things.

First, you can override the defaults by specifying the above properties in a properties file (see below for where it's located).  **These** can be overridden by setting them as system properties.

## Locating the Properties File

By default, the code will look for a file named `.importscour.properties` in your home directory (as stored in the system property `user.home`).  If you set the environment variable IMPORTSCOUR_PROPERTIES to the location of the file, this will be used instead.

# As a Git Hook

`src/perl/pre-commit` is a Git hook you can use to fix imports on commit.  You must set three Git configuration options:

* `importscour.command` - full path to your ImportScour command
* `importscour.srcdir` - relative path to your java source root
* `importscour.classesdir` - relative path to your classes root

e.g.

    git-config --add importscour.srcdir src/java

Note that for each source file you check in, a class file must exist in the classes root.  If the class file is missing, ImportScour will (by default) throw an exception and abort your commit.  If you set `importscour.ignoremissingclasses` in your configuration, these sources would be skipped (bypassing the entire thing; just compile before you commit; it's a good practice anyway)

## Todo

* better test cases for better coverage
* Remove ImportStatement.DEBUG in favor of real logging
* Devise means by which certain files can be skipped
