(ns cqrs-event-sourcing-sandbox.commands
  (:require [cqrs-event-sourcing-sandbox.events :refer [map->NewUserRegisteredEvent map->RedisEventStore
                                                        retrieve-event-stream append-events apply-event]]))

(defrecord RegisterNewUserCommand [aggregate_id first_name last_name dob gender])

(defprotocol CommandHandler
  (coerce [command] "Validate command")
  (process [command state] "Process Command, returns vector of events"))

(extend-protocol CommandHandler
  RegisterNewUserCommand
  (coerce [command]
    command)
  (process [command state]
    (if (empty? state)
      [(map->NewUserRegisteredEvent command)]
      (throw (Exception. "Cannot seed new user with existing state.")))))

(defn handle-command [command]
  (let [event-store (map->RedisEventStore {})
        event-stream (retrieve-event-stream event-store (:aggregate_id command))
        current-state (reduce apply-event {} event-stream)
        new-events (process command current-state)]
    (append-events event-store (:aggregate_id command) new-events)))

(defn retrieve-user [user_id]
  (->> (retrieve-event-stream (map->RedisEventStore {}) user_id)
       (reduce apply-event {})))
