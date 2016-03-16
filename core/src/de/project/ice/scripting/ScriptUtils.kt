package de.project.ice.scripting

import de.project.ice.IceGame
import de.project.ice.Storage

fun runOnce(id: String, func: ()->Unit) {
    if(Storage.SAVESTATE.getBoolean("__ONLYONCE__${id}__")==false)
    {
        func.invoke()
        Storage.SAVESTATE.put("__ONLYONCE__${id}__",true)
    }
}

fun IceGame.blockInteraction(func: ()->Unit) {
    val blocked = BlockInteraction
    BlockInteraction = true
    func.invoke()
    BlockInteraction = blocked
}

fun IceGame.blockSaving(func: ()->Unit) {
    val blocked = BlockSaving
    BlockSaving = true
    func.invoke()
    BlockSaving = blocked
}