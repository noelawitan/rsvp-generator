<template>
  <the-navbar/>
  <div class="container">
    <div id="eventSection" class="mt-5 p-10">
      <div class="d-flex justify-content-between align-items-center">
        <h3>Events</h3>
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createEventModal">
          <i class="fa fa-plus me-1" />Create Event
        </button>
      </div>
      <hr/>
      <event-list ref="eventListRef"></event-list>
      <create-event-modal @submittedEvent="handleSubmittedEvent"></create-event-modal>
    </div>
  </div>
</template>
<script>
import EventList from '@/components/EventList.vue';
import CreateEventModal from "@/components/CreateEventModal.vue";
import TheNavbar from "@/components/TheNavbar.vue";
import Modal from 'bootstrap/js/dist/modal';
import {EVENT_URL} from "@/config/config.js";

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
                //Close the CreateEventModal
                const modalElement = document.getElementById('createEventModal');
                const modalBackdrops = document.getElementsByClassName('modal-backdrop');
                const modalInstance = Modal.getInstance(modalElement);

                if (modalInstance) {
                  this.$refs.eventListRef.getAllUserEvents();
                  modalInstance.hide();

                  while (modalBackdrops.length > 0) {
                    modalBackdrops[0].remove();
                  }
                }
              }
            })
            .catch((error) => {
              // TODO: Create a modal that says there's something wrong in the server
              console.error('Error:', error);
            });
      } else {
        this.$router.push('/login');
      }
    }
  }
}
</script>