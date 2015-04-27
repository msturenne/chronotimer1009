general

	triger disabled channel
	toggle channel with no sensor
	off and on commands in the middle of a race
	reset command in the in the middle of a race
	endrun command in the in the middle of a race
	newrun command in the in the middle of a race
	change the event type in the in the midle of a race
	Test the printer. turn on after race start and turn of before it finishes

IND

PARIND
	try to trigger a start channel with no runners in the queue
	trigger a finish channel with no racers currently running

GRP
	add a competitor after race starts 

PARGPR

test1: command NEWRUN executed at the middle of the race.
	result: remaining racers tagged as DNF

test2: change de event type at the middle of a race
	result: nothing to export and crash on the print command.