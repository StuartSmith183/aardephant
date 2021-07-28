# Getting Started

## Getting Started - Using the Plugin

By including this plugin in your project you can have gradle start the firmware upload/flash to your arduino hardware.  You will need to setup a build file to properly handle the code and hardware.

### Example build.gradle

This is an example of how to use the plugin in your own project.
[docs/example.build.gradle](docs/example.build.gradle)

_Information on the different settings._

## Getting Started - Development

1. Requires java 10+
1. The [Arduino IDE](https://www.arduino.cc/en/software) (minimum version 1.8)
1. Edit /home/user/.m2/settings.xml .. add localRepository (/home/user/.m2/repository)
1. gradle wrapper
1. ./gradlew publish

copy should now be in your .m2/repository and other projects can now use it.
