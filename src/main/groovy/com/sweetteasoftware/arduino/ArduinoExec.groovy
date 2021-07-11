/** VirtuallyRandomDie Project
 *  (c)2020 Stuart Smith and Sweet Tea software
*/
package com.sweetteasoftware.arduino

import org.gradle.api.tasks.AbstractExecTask

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import jssc.SerialPort
import jssc.SerialPortList;
import jssc.SerialPortException

import groovy.transform.CompileDynamic

/**
* ArduinoExec
*/
@CompileDynamic
abstract class ArduinoExec extends AbstractExecTask {

    @Input
    @Optional
    String sketchName;
    @Input
    String vidPid;
    @Input
    String ideVersion;

    @Input
    Integer debugLevel;
    @Input
    boolean trace
    @Input
    boolean verbose
    @Input
    String warnings;
    @Input
    Map<String, String> prefs;
    @Input
    String[] libraries;

    protected final String activeSketch;

    protected final String fqBoardName;// make into a property
    protected final String sketchBookFolder;
    protected final String arduinoPath;
    protected final String arduinoConfigPath;
    protected final String arduinoExec;
    protected final String tempFolder;
    protected final String standardSrc;
    protected final String buildOutputPath;
    protected final String buildOutputCache;

    protected final String arduinoFlashExex;
    protected final String arduinoFlashExecPath;

    protected  ArduinoExec(Class<? extends ArduinoExec> taskType) {
        super(taskType)

        activeSketch = System.getProperty('arudino.activeSketch')

        fqBoardName = System.getProperty('arduino.fqBoardName')
        sketchBookFolder = System.getProperty('arduino.sketchbook.path')
        arduinoPath = System.getProperty('arduino.bin.path')
        arduinoConfigPath = System.getProperty('arduino.config.path')
        arduinoExec = System.getProperty('arduino.bin.executable')
        // for upload
        arduinoFlashExecPath = System.getProperty('arudino.flash.path')
        arduinoFlashExex = System.getProperty('arudino.flash.executable')

        String os = System.getProperty('os.name').toLowerCase()
        String bDir = project.buildDir

        if (os.contains('windows')) {
            tempFolder = 'C:\\temp\\'
            standardSrc = 'src\\main\\ino\\'
            buildOutputPath = bDir + File.separator + 'hex\\main\\release\\'
            buildOutputCache = bDir + File.separator + 'cache\\main\\release\\'
        }
        else if (os.contains('linux')) {
            tempFolder = '/tmp'
            standardSrc = 'src/main/ino/'
            buildOutputPath = bDir + File.separator + 'hex/main/release/'
            buildOutputCache = bDir + File.separator + 'cache/main/release/'
        }
        else {
            tempFolder = '/tmp'
            standardSrc = 'src/main/ino/'
            buildOutputPath = bDir + File.separator + 'hex/main/release/'
            buildOutputCache = bDir + File.separator + 'cache/main/release/'
        }

        // 1. not given - set to src/main/ino/{project.name}.ino
        // 2. given - use as is.
        if (!sketchName) {
            sketchName = standardSrc + project.name + '.ino'
        }
    }

    @Override
    @TaskAction
    protected void exec() {
        File bbpath = new File( buildOutputPath)
        if (!bbpath.mkdirs()) {
            println('error creating build folders')
        }

        File ccpath = new File( buildOutputCache)
        if (!ccpath.mkdirs()) {
            println('error creating build (cache) folders')
        }

        this.commandLine = makeCommandLine()
        if (debugLevel >= 0) {
            println(this.commandLine)
        }
        super.exec()
    }

    protected boolean touchForCDCReset(String iname) throws Exception {
        SerialPort serialPort = new SerialPort(iname)
        try {
            println('Attempting port reset for ' + iname)
            serialPort.openPort()
            serialPort.setParams(1200, 8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE)
            serialPort.setDTR(false)
            serialPort.closePort()
            return true
        } catch (SerialPortException e) {
            String error = "Error touching serial port " + iname;
            throw new Exception(error, e)
        } finally {
            if (serialPort.isOpened()) {
                try {
                    serialPort.closePort()
        } catch (SerialPortException e) {
                // noop
                }
            }
        }
    }

    protected void listSerialPorts() {
        String[] portNames = SerialPortList.getPortNames()
        if (portNames.length > 0)
            println("Known Ports... ")
        for (int i = 0; i < portNames.length; i++) {
            println(portNames[i])
        }
        if (portNames.length > 0)
            println("")
    }

    abstract protected List<String> makeCommandLine();

}
