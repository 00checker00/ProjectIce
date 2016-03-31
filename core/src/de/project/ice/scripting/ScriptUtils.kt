package de.project.ice.scripting

import de.project.ice.IceGame
import de.project.ice.Storage
import java.util.concurrent.atomic.AtomicInteger

fun runOnce(id: String, func: ()->Unit) {
    if(Storage.SAVESTATE.getBoolean("__ONLYONCE__${id}__")==false)
    {
        func.invoke()
        Storage.SAVESTATE.put("__ONLYONCE__${id}__",true)
    }
}

fun IceGame.blockInteraction(func: ()->Unit) {
    blockInteractionCounter.incrementAndGet()
    BlockInteraction = true
    func.invoke()
    if(blockInteractionCounter.decrementAndGet() == 0) {
        BlockInteraction = false
    }
}

fun IceGame.blockSaving(func: ()->Unit) {
    blockSavingCounter.incrementAndGet()
    BlockSaving = true
    func.invoke()
    if(blockSavingCounter.decrementAndGet() == 0) {
        BlockSaving = false
    }
}

internal val blockInteractionCounter = AtomicInteger(0)
internal val blockSavingCounter = AtomicInteger(0)