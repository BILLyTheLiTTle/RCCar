package car.controllers.basic

import car.server.SetupSystem


interface Setup {
    var handlingAssistanceState: String

    fun reset(){
        handlingAssistanceState = SetupSystem.ASSISTANCE_NONE
    }
}