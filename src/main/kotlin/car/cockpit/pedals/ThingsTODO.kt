package car.cockpit.pedals

/*
The throttling classes should be something like this:
1. Electronic throttle class with a lot of filter functions.
A filter function is a function that modifies the throttle according to input data from sensors.
Sensors are divided according to importance for a crash possibility.
2. Manual throttle
3a. Raw Throttle class with 4 values as inputs each value is the pwm value for each motor
3b. In case I don't want the 3a solution the DCM module of the ECU could have as input the type of sensor importance
(High, Medium, Low) and this component could modify the values of the pwm pins

Sensors Importance (according to crash possibility):
1. High
    a. Collision Detection
    b. etc
2. Medium
    a. Electronic Stability -> if I cannot detect over/understeer individually
    b. Oversteer
    c. Understeer
    d. etc
3. Low
    a. Antispinning
    b. Antiblocking braking
    c. etc

Real life scenario:
At first the driver is braking so much that the car is sliding in straight,
then he/she turns the steering wheel and car start to understeer,
then the car approaches the wall.

Translate this real life scenario into the system decisions with:
1. Full handling assistance
The antiblocking module is activated as the lowest effort by the system,
then antiblocking module is deactivated (see Question 1) and the understeer detection module takes control,
then understeer detection module is deactivated and the collision detection module takes control

This means that if the high importance sensor/state is activated the function should control the pwm pins
without checking any lower importance sensors/states.

2. Warning or Manual Assistance
Let the driver do the driving by him/herself but in case of warning assistance just give him a feedback with the signals
in the controller!

Questions to myself:
1. If I deactivate the antiblocking braking, this means that the wheels will be blocked,
this means that I will not know the travel speed of the vehicle by reading wheel speed,
this means that I will have no feedback in order to fix the understeer! Should I redesign this functionality?
 */