/** VirtuallyRandomDie Project
 *  (c)2020 Stuart Smith and Sweet Tea software
*/
package com.sweetteasoftware.arduino

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
* ArduinoPlugin
*/
class ArduinoPlugin implements Plugin<Project> {

    static void addTask(Project project, Class type) {
        project.extensions.extraProperties.set(type.simpleName, type)
    }

    @Override
    void apply(Project project) {
        addTask(project, ArduinoVerify)
        addTask(project, ArduinoUpload)
    }

}
