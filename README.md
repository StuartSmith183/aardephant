# Getting Started

## Getting Started - Using the Plugin

By including this plugin in your project you can have gradle start the firmware upload/flash to your arduino hardware.  You will need to setup a build file to properly handle the code and hardware.

### Example build.gradle

link to example gradle file. This is an example of how to use the plugin in your own project.
[docs/example.build.gradle](docs/example.build.gradle)

__Information on the different settings.__

## Getting Started - Development

1. requires java 10+
1. edit /home/user/.m2/settings.xml .. add localRepository (/home/user/.m2/repository)
1. gradle wrapper
1. ./gradlew publish

copy should now be in your .m2/repository and other projects can now use it.
