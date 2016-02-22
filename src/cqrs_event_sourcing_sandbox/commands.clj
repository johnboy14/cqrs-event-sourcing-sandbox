(ns cqrs-event-sourcing-sandbox.commands
  (:require [cqrs-event-sourcing-sandbox.events :refer [map->NewUserRegisteredEvent]]))

(defrecord RegisterNewUserCommand [aggregate_id first_name last_name dob gender])

(defprotocol CommandHandler
  (coerce [command] "Validate command")
  (process [command state] "Process Command, returns vector of events"))

(extend-protocol CommandHandler
  RegisterNewUserCommand
  (coerce [command]
    command)
  (process [command _]
    [(map->NewUserRegisteredEvent command)]))
