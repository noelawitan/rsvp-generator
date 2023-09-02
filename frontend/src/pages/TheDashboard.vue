<template>
  <the-navbar/>
  <div class="container">
    <div id="eventSection" class="mt-5 p-10">
      <div class="d-flex justify-content-between align-items-center">
        <h3>Events</h3>
        <button type="button" class="btn btn-primary" @click="showCreateEventModal">
          <i class="fa fa-plus me-1"/>Create Event
        </button>
      </div>
      <hr/>
      <event-list ref="eventListRef"></event-list>
      <create-event-modal ref="createEventModalRef" @submittedEvent="handleSubmittedEvent"></create-event-modal>
    </div>
  </div>
</template>

<script>
import EventList from '@/components/EventList.vue';
import CreateEventModal from "@/components/CreateEventModal.vue";
import {EVENT_URL} from "@/config/config.js";
import TheNavbar from "@/components/TheNavbar.vue";

export default {
  components: {
    TheNavbar,
    EventList,
    CreateEventModal
  },
  methods: {
    async handleSubmittedEvent(newEvent) {
      const accessToken = localStorage.getItem('access_token');

      if (accessToken !== null) {
        await fetch(EVENT_URL, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
          },
          body: JSON.stringify(newEvent),
        })
            .then(response => {
              if (response.status === 403) {
                this.$router.push('/login');
                return;
              }

              return response.json();
            })
            .then(data => {
              if (data !== null) {
                this.$refs.createEventModalRef.hide();
                this.$refs.eventListRef.getAllUserEvents();
              }
            })
            .catch((error) => {
              // TODO: Create a modal that says there's something wrong in the server
              console.error('Error:', error);
            });
      } else {
        this.$router.push('/login');
      }
    },
    showCreateEventModal() {
      this.$refs.createEventModalRef.show();
    }
  }
}
</script>