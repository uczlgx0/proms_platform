#!/usr/bin/env bash
echo "Existing rabbitmq queues"
rabbitmqadmin list queues name
echo "Deleting promsapp queues"
rabbitmqadmin delete queue name='promsapp_queue'
rabbitmqadmin delete queue name='promsapp_bookings_queue'
rabbitmqadmin delete queue name='promsapp_plans_queue'
rabbitmqadmin delete queue name='promsapp_care_events_queue'
rabbitmqadmin delete queue name='promsapp_actions_queue'
echo "Deleted promsapp queues"
echo "Rabbitmq queues after delete"
rabbitmqadmin list queues name
