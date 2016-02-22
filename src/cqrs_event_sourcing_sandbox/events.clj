(ns cqrs-event-sourcing-sandbox.events
  (:require [cqrs-event-sourcing-sandbox.domains :refer :all])
  (:import (java.util Date)))

(defrecord NewUserRegisteredEvent [aggregate_id first_name last_name dob gender])

(defmulti apply-event (fn [state event] (class event)))

(defmethod apply-event NewUserRegisteredEvent [state event]
  (if (empty? state)
    (assoc (map->User event) :version 1 :created_at (.getTime (Date.)))
    (throw (Exception. "Cannot seed new user with existing state."))))