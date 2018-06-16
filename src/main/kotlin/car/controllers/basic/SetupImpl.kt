package car.controllers.basic

import car.server.SetupSystem

object SetupImpl: Setup {
    override var handlingAssistanceState = SetupSystem.ASSISTANCE_NONE
}