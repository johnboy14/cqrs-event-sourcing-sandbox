(ns cqrs-event-sourcing-sandbox.command.commands
  (:require [cqrs-event-sourcing-sandbox.event.events :refer [map->NewUserRegisteredEvent map->UserProfileImageUpdatedEvent
                                                        map->RedisEventStore retrieve-event-stream append-events apply-event]]))

(defrecord RegisterNewUserCommand [aggregate_id first_name last_name dob gender])
(defrecord UpdateUserProfileImageCommand [aggregate_id img_url])

(defprotocol CommandHandler
  (coerce [command] "Validate command")
  (process [command state] "Process Command, returns vector of events"))

(extend-protocol CommandHandler
  RegisterNewUserCommand
  (coerce [command] command)
  (process [command state]
    (when (empty? state)
      [(map->NewUserRegisteredEvent command)]))
  UpdateUserProfileImageCommand
  (coerce [command] command)
  (process [command state]
    (if-let [_ (seq state)]
      [(map->UserProfileImageUpdatedEvent command)]
      (throw (Exception. "Cannot update non existing user")))))

(defn handle-command [command]
  (let [event-store (map->RedisEventStore {})
        event-stream (retrieve-event-stream event-store (:aggregate_id command))
        current-state (reduce apply-event {} event-stream)
        new-events (process command current-state)]
    (append-events event-store (:aggregate_id command) new-events)))

(defn retrieve-user [user_id]
  (->> (retrieve-event-stream (map->RedisEventStore {}) user_id)
       (reduce apply-event {})))
