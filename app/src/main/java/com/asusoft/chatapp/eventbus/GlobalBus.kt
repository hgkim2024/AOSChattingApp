package com.asusoft.chatapp.eventbus

import org.greenrobot.eventbus.EventBus

object GlobalBus {
    var sBus: EventBus? = null

    private fun getBus(): EventBus {
        if (sBus == null) sBus = EventBus.getDefault()
        return sBus!!
    }

    fun register(any: Any) {
        if (!getBus().isRegistered(any)) {
            getBus().register(any)
        }
    }

    fun unregister(any: Any) {
        if (getBus().isRegistered(any)) {
            getBus().unregister(any)
        }
    }

    fun post(map: HashMap<String, Any>) {
        getBus().post(map)
    }
}