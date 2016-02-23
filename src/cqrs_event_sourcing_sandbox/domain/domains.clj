(ns cqrs-event-sourcing-sandbox.domain.domains)

(defrecord User [aggregate_id version created_at first_name last_name dob gender])
