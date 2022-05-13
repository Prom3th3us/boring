
printf '{"id": "chat-C"},{ "userId": {"id": "miguel"}, "type":"AddUser"}
        {"id": "chat-C"},{ "userId": {"id": "anastasia"},  "type":"AddUser"}' \
      |  \
      kafka-console-producer \
      --topic chat-group-commands \
      --broker-list localhost:9092 \
      --property parse.key=true \
      --property key.separator=,

sleep 10

printf '{"id": "chat-C"},{ "text": "hello"}
        {"id": "chat-C"},{ "text": "hello to you too! I love you!"}
        {"id": "chat-C"},{ "text": "hello to you too! I love you!"}
        {"id": "chat-C"},{ "text": "hello to you too! I love you!"}
        {"id": "chat-C"},{ "text": "hello to you too! I love you!"}
        {"id": "chat-C"},{ "text": "hello to you too! I love you!"}
        {"id": "chat-C"},{ "text": "hello to you too! I love you!"}
        {"id": "chat-C"},{ "text": "hello to you too! I love you!"}
        {"id": "chat-C"},{ "text": "hello to you too! I love you!"}
        {"id": "chat-C"},{ "text": "hello to you too! I love you!"}
        {"id": "chat-C"},{ "text": "hello to you too! I love you!"}
        {"id": "chat-C"},{ "text": "hello to you too! I love you!"}
        {"id": "chat-C"},{ "text": "hello to you too! I love you!"}
        {"id": "chat-C"},{ "text": "hello to you too! I love you!"}' \
      | \
      kafka-console-producer \
      --topic messages \
      --broker-list localhost:9092 \
      --property parse.key=true \
      --property key.separator=,
