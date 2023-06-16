package com.deslomator.tagtimer.action

sealed interface SessionsTrashAction {
    object BackClicked: SessionsTrashAction

}