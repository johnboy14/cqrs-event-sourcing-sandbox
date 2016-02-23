(ns cqrs-event-sourcing-sandbox.events
  (:require [cqrs-event-sourcing-sandbox.domains :refer :all]
            [taoensso.carmine :as car :refer (wcar)])
  (:import (java.util Date)))

(def server1-conn {:pool {} :spec {:host "127.0.0.1" :port 6379}})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defprotocol EventStore
  (retrieve-event-stream [this aggregate-id] "Retrieve event stream for aggregate.")
  (append-events [this aggregate-id events] "Append events to aggregate event stream."))

(defrecord RedisEventStore [redis-conn]
  EventStore
  (retrieve-event-stream [_ aggregate-id]
    (wcar* (car/lrange (str "event::" aggregate-id "::stream") 0 -1)))
  (append-events [_ aggregate-id events]
    (wcar*
      (doseq [e events]
        (car/rpush (str "event::" aggregate-id "::stream") e)))))

(defrecord NewUserRegisteredEvent [aggregate_id first_name last_name dob gender])
(defrecord UserProfileImageUpdatedEvent [aggregate_id img_url])

(defmulti apply-event (fn [state event] (class event)))

(defmethod apply-event NewUserRegisteredEvent [_ event]
  (assoc (map->User event) :version 1 :created_at (.getTime (Date.))))

(defmethod apply-event UserProfileImageUpdatedEvent [state event]
  (-> (assoc state :img_url (:img_url event))
      (update-in [:version] inc)))