/** VirtuallyRandomDie Project
 *  (c)2020 Stuart Smith and Sweet Tea software
*/
package com.sweetteasoftware.arduino

import org.gradle.api.tasks.TaskAction
import javax.inject.Inject
import groovy.transform.CompileDynamic

import org.gradle.api.tasks.Input

@CompileDynamic
class ArduinoUpload extends ArduinoExec {

    @Input
    String portName;

    @Inject
    ArduinoUpload() {
        super(ArduinoUpload)
    }

    @Override
    @TaskAction
    protected void exec() {
        println('Upload Sketch : ' + sketchName)
        if (sketchName.contains(activeSketch)) {
            listSerialPorts()
            touchForCDCReset('/dev/ttyACM0')

            super.exec()
        }
        else {
            println('We are not the choosen one.')
        }
    }

    protected List<String> makeCommandLine() {
        String confFile = arduinoFlashExecPath + '..' + File.separator + 'etc' + File.separator + 'avrdude.conf'

        List<String> fullCommand = new ArrayList<String>()

        // This needs to handle any OS paths etc.
        fullCommand.add(arduinoFlashExecPath + arduinoFlashExex)
        fullCommand.add('-C' + confFile)

        // trace + verbose == -v -v (very verbose)
        if (verbose) {
            fullCommand.add('-v')
        }

        if (trace) {
            fullCommand.add('-v')
        }

        String avrDevice = 'atmega32u4' // make into a property
        fullCommand.add('-p' + avrDevice)

        String programmer = 'avr109' // make into a property
        fullCommand.add('-c' + programmer)

        fullCommand.add('-P' + portName)

        String baudRate = '57600' // make into a property
        fullCommand.add('-b' + baudRate)

        //Disable auto erase for flash memory (?)
        fullCommand.add('-D') // make into a property

/*
         -U <memtype>:r|w|v:<filename>[:format]
                             Memory operation specification.
                             Multiple -U options are allowed, each request
                             is performed in the order specified.
*/
        String memoryOpSpec = 'flash:w:' + buildOutputPath + project.name + '.ino.hex:i'
        fullCommand.add('-U' + memoryOpSpec)

        return fullCommand
    }

}
