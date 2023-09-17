<template>
  <div class="row" v-if="events.length > 0">
    <event-card
        class="col-md-6 col-lg-4"
        v-for="event in events"
        :key="event.id"
        :id="event.id"
        :name="event.name"
        :date="event.date"
        :location="event.location"
        :start-time="event.startTime"
        :end-time="event.endTime">
    </event-card>
  </div>
  <div class="row text-center" v-else>
    <p>You don't have any events yet. You may create one by pressing the (+) icon.</p>
  </div>
</template>

<script>
import EventCard from "@/components/event/EventCard.vue";
import {EVENT_URL} from "@/js/config.js";

export default {
  components: {
    EventCard
  },
  created() {
    this.getAllUserEvents();
  },
  data() {
    return {
      events: []
    }
  },
  methods: {
    getAllUserEvents() {
      const accessToken = localStorage.getItem('access_token');
      this.$loader.isVisible = true;

      if (accessToken !== null) {
        fetch(EVENT_URL, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
          }
        })
            .then(response => {
              if (response.status === 403) {
                localStorage.clear();
                this.$router.push('/login');
              }

              return response.json();
            })
            .then(data => {
              if (Array.isArray(data)) {
                this.events = data;
              }
            })
            .catch((error) => {
              // TODO: Create a modal that says there's something wrong in the server
              console.error('Error:', error);
            })
            .finally(() => {
              this.$loader.isVisible = false;
            });
      } else {
        this.$router.push('/login');
      }
    }
  }
}
</script>