(ns cqrs-event-sourcing-sandbox.domains)

(defrecord User [aggregate_id version created_at first_name last_name dob gender])
