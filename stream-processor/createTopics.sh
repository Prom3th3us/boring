echo "Creating topic chat-group-commands"
docker exec -i broker \
kafka-topics \
  --bootstrap-server localhost:9092 \
  --topic chat-group-commands \
  --partitions 10 \
  --replication-factor 1 \
  --create \
  --config "cleanup.policy=compact" \
  --config "retention.ms=-1"
#  --config "cleanup.policy=delete" \
#  --config "retention.ms=160000"

echo "Creating topic chat-group-commands-zippedWithIndex"
docker exec -i broker \
kafka-topics \
  --bootstrap-server localhost:9092 \
  --topic chat-group-commands-zippedWithIndex \
  --partitions 10 \
  --replication-factor 1 \
  --create \
  --config "cleanup.policy=compact" \
  --config "retention.ms=-1"

#  --config "cleanup.policy=delete" \
#  --config "retention.ms=60000"


echo "Creating topic chat-group-events"
docker exec -i broker \
kafka-topics \
  --bootstrap-server localhost:9092 \
  --topic chat-group-events \
  --partitions 10 \
  --replication-factor 1 \
  --create \
  --config "cleanup.policy=compact" \
  --config "retention.ms=-1"

echo "Creating topic chat-group-snapshots"
docker exec -i broker \
kafka-topics \
  --bootstrap-server localhost:9092 \
  --topic chat-group-snapshots \
  --partitions 10 \
  --replication-factor 1 \
  --create \
  --config "cleanup.policy=compact" \
  --config "retention.ms=-1"


echo "Creating topic messages"
docker exec -i broker \
kafka-topics \
  --bootstrap-server localhost:9092 \
  --topic messages \
  --partitions 10 \
  --replication-factor 1 \
  --create \
  --config "cleanup.policy=compact" \
  --config "retention.ms=-1"


echo "Creating topic user-messages"
docker exec -i broker \
kafka-topics \
  --bootstrap-server localhost:9092 \
  --topic user-messages \
  --partitions 10 \
  --replication-factor 1 \
  --create \
  --config "cleanup.policy=compact" \
  --config "retention.ms=-1"