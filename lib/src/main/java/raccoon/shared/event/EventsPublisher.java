package raccoon.shared.event;

import java.util.Collection;

public interface EventsPublisher {
    EventsPublisher publish(Event... events);

    default EventsPublisher publish(Collection<Event> events) {
        this.publish(events.toArray(new Event[0]));
        return this;
    }
}
