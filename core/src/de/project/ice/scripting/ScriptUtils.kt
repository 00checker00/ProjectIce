package de.project.ice.scripting

import de.project.ice.Storage

fun runOnce(id: String, func: ()->Unit) {
    if(Storage.SAVESTATE.getBoolean("__ONLYONCE__${id}__")==false)
    {
        func.invoke()
        Storage.SAVESTATE.put("__ONLYONCE__${id}__",true)
    }
}