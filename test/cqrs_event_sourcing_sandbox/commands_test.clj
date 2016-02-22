(ns cqrs-event-sourcing-sandbox.commands-test
  (:require [clojure.test :refer :all]
            [cqrs-event-sourcing-sandbox.commands :refer [->RegisterNewUserCommand process handle-command retrieve-user]]
            [cqrs-event-sourcing-sandbox.events :refer [->NewUserRegisteredEvent apply-event]])
  (:import (java.util UUID)))


(deftest register-user-commands
  (testing "Given a RegisterNewUserCommand, Then verify new event NewUserRegisteredEvent"
    (let [agg-id    (UUID/randomUUID)
          processed (process (->RegisterNewUserCommand agg-id "john" "ervine" "05/10/1984" "male") {})]
      (is (= (count processed) 1))))

  (testing "Given an event stream with one NewUserRegisteredEvent, Then verify "
    (let [agg-id (UUID/randomUUID)]
      (handle-command (->RegisterNewUserCommand agg-id "john" "ervine" "05/10/1984" "male"))
      (println (retrieve-user agg-id)))))
