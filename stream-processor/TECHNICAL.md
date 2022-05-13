
## The Implementation: Event Sourcing using Kafka
The core idea was to use the key-value store from Kafka, RocksDB, to be able to recover the state of any entity during stateful processing.

Doing so, in higher-level Kafka jargon, can be paraphrased as using a KTable.

For this tech talk I am going to need an example, a sufficient complex one.
Let's use the banks. The ATMs, that allow you to withdraw and deposit money.

What we want then is to have the commands be validated by bussiness logic processors, filtering out commands that for example withdraw more money than the bank account actually contains, which in terms of banking is referred as overdraft.

If a command, say, to widthdraw money, is valid, then an event is dispatched. Commands are not persisted, events are persisted forever.
The event then is applied to the current state of the bank account, and a new state is generated.

Other processors can listen to these changes made to the bank account state and trigger alarms, for example if a bank account contains less than 1000 units of money.

## The Caveats:
Because of eventual consistency we could argue that all events can be stored for processing on one side, and the actual processing can be idle, not processing at all.
But to validate commands, like for example deny a user a withdraw of money because the bank account does not have any, we need to read the latest version of the bank account state.
To fix this issue is necessary what ACID databases have: A lock.
We need to be able to say stop to the generation of events, because for them to be created we need validation, and for validation we need to know the actual state.

The solution that is currently accepted by the Kafka community needs to be implemented by us, in the application layer.

Is called Optimistic Concurrency Control.

We call it optimistic because yes, we are indeed blocking the entry for new events: No new events until the state is consistent again! -- but we do not actually lock, what we do is we try again later.

#### Optimistic Concurrency Control
If my place in queue is 1000, I should expect the state to have been updated by a command 1000 - 1, 999.
What was the last queue position that updated the state? 800? Then I need to wait for my turn. I will retry later.
What was the last queue position that updated the state? 999? This is my turn!

#### The act of retry
I remember a company that to perform this pattern retried from the UI. They incremented the requestId by 1 and tried again.
I personally went 100% Kafka on my solution, and made the command indexes be created and stored inside Kafka.
Also the retry system was just a matter of enqueue back again the command in the topic for later processing.

This could blow up Kafka. Or not. A careful configuration of retention times can achieve this pattern without endangering the memory/disk usage.
Commands are not persisted. And the retention time in memory is of 60 seconds. Events are persisted. Commands are a fire and forget pattern.

# Technical talk
![](https://videoapi-muybridge.vimeocdn.com/animated-thumbnails/image/9533e3cd-a743-4535-bb27-22f4b5e2f705.gif?ClientID=vimeo-core-prod&Date=1641400794&Signature=537a12d7e214d3d21969a921d4415b0de304df2f)
https://vimeo.com/662646376

# Stream Processor 
Now if we apply this architecture to our stream processor, the resulting 
topology goes as follows:

![](https://user-images.githubusercontent.com/9152392/168374986-60036f60-5037-4944-93d2-9b678d8f93e5.png)
