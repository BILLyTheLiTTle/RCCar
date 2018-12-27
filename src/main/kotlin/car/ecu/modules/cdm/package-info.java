package car.ecu.modules.cdm;

/**
 * # Package car.ecu.modules.cdm
 *
 * The Collision Detection Module.
 * It is responsible to avoid any collision of the car with any item at the surroundings.
 *
 * It is activated when the car:
 * - is already in motion
 * - starts moving forward/backward from an idle position
 * - change direction (through the steering) even while it is moving or it is in idle
 */