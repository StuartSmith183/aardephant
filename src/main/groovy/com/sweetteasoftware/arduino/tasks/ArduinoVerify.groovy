/** VirtuallyRandomDie Project
 *  (c)2020 Stuart Smith and Sweet Tea software
*/
package com.sweetteasoftware.arduino

import org.gradle.api.tasks.TaskAction
import javax.inject.Inject
import groovy.transform.CompileDynamic

@CompileDynamic
class ArduinoVerify extends ArduinoExec {

    @Inject
    ArduinoVerify() {
        super(ArduinoVerify)
    }

    @Override
    @TaskAction
    protected void exec() {
        println('Verify Sketch : ' + sketchName)
        super.exec()
    }

    protected List<String> makeCommandLine() {
        List<String> fullCommand = new ArrayList<String>()

        // This needs to handle any OS paths etc.
        fullCommand.add(arduinoPath + arduinoExec)

        if (verbose) {
            fullCommand.add('-verbose')
        }
        if (trace) {
            fullCommand.add('-trace')
        }
        if (debugLevel >= 0) {
            fullCommand.add('-debug-level')
            fullCommand.add(debugLevel)
        }

        fullCommand.add('-vid-pid=' + vidPid)
        fullCommand.add('-ide-version=' + ideVersion)

        fullCommand.add('-warnings=' + warnings)

        fullCommand.add('-fqbn=' + fqBoardName)

        fullCommand.add('-hardware')
        fullCommand.add(arduinoPath + File.separator + 'hardware')

        fullCommand.add('-hardware')
        fullCommand.add(arduinoConfigPath + File.separator + 'packages')

        fullCommand.add('-tools')
        fullCommand.add(arduinoPath + File.separator + 'tools-builder')

        fullCommand.add('-tools')
        fullCommand.add(arduinoPath + File.separator + 'hardware' + File.separator + 'tools' + File.separator + 'avr')

        fullCommand.add('-tools')
        fullCommand.add(arduinoConfigPath + File.separator + 'packages')

        fullCommand.add('-build-path')
        fullCommand.add(buildOutputPath)

        //File buildCacheDir = File.createTempDir(tempFolder + File.separator + 'arduino_cache_', '')
        fullCommand.add('-build-cache')
        fullCommand.add(buildOutputCache)

        prefs.each {  entry ->
            fullCommand.add('-prefs=' + entry.key + '=' + entry.value)
        }

        libraries.each { libEntry ->
            fullCommand.add('-libraries')
            fullCommand.add(libEntry)
        }

        fullCommand.add(sketchName)

        return fullCommand
    }

}
