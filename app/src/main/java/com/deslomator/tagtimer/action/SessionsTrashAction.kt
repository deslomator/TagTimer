package com.deslomator.tagtimer.action

import com.deslomator.tagtimer.model.Session

sealed interface SessionsTrashAction {
    data class DeleteSessionClicked(val session: Session): SessionsTrashAction
}