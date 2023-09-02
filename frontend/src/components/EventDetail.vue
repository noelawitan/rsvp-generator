<template>
  <the-navbar/>
  <div class="container mt-5">
    <div v-if="event" class="row">
      <div class="card col-md-4">
        <div class="card-body">
          <div class="d-flex justify-content-between align-items-center">
            <h3>Event Detail</h3>
            <button type="button" class="btn btn-primary" @click="showUpdateEventModal">
              Edit
            </button>
          </div>
          <hr/>
          <div class="row">
            <div class="col-md-3">
              <label>Name</label>
            </div>
            <div class="col-md-9">
              <p>{{ event.name }}</p>
            </div>
          </div>
          <div class="row">
            <div class="col-md-3">
              <label>Location</label>
            </div>
            <div class="col-md-9">
              <p class="text-break"
                 :title="event.location">
                {{ event.location }}
              </p>
            </div>
          </div>
          <div class="row">
            <div class="col-md-3">
              <label>Date</label>
            </div>
            <div class="col-md-9">
              <p>{{ event.date }}</p>
            </div>
          </div>
          <div class="row">
            <div class="col-md-3">
              <label>Start Time</label>
            </div>
            <div class="col-md-9">
              <p>{{ event.startTime }}</p>
            </div>
          </div>
          <div v-if="event.endTime" class="row">
            <div class="col-md-3">
              <label>End Time</label>
            </div>
            <div class="col-md-9">
              <p>{{ event.endTime }}</p>
            </div>
          </div>
        </div>
      </div>
      <update-event-modal ref="updateEventModalRef" @submittedEvent="handleSubmittedEvent"
                          :event-obj="event"></update-event-modal>
    </div>
    <div v-else class="row">
      <h4>Event not found</h4>
    </div>
  </div>

</template>

<script>
import TheNavbar from "@/components/TheNavbar.vue";
import {EVENT_URL} from "@/config/config.js";
import UpdateEventModal from "@/components/UpdateEventModal.vue";

export default {
  components: {UpdateEventModal, TheNavbar},
  data() {
    return {
      event: null
    }
  },
  created() {
    const eventId = this.$route.params.id;
    const accessToken = localStorage.getItem('access_token');

    if (accessToken !== null) {
      fetch(`${EVENT_URL}/${eventId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${accessToken}`
        }
      })
          .then(response => {
            if (response.status === 403) {
              this.$router.push('/login');
              return;
            }

            return response.json();
          })
          .then(data => {
            this.event = data;
          })
          .catch((error) => {
            // TODO: Create a modal that says there's something wrong in the server
            console.error('Error:', error);
          });
    } else {
      this.$router.push('/login');
    }
  },
  methods: {
    async handleSubmittedEvent(newEvent) {
      const accessToken = localStorage.getItem('access_token');

      if (accessToken !== null) {
        await fetch(`${EVENT_URL}/${newEvent.id}`, {
          method: 'PUT',
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
                this.$refs.updateEventModalRef.hide();
                this.event = data;
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
    showUpdateEventModal() {
      this.$refs.updateEventModalRef.show();
    }
  }
}
</script>