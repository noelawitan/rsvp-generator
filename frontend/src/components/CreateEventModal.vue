<template>
  <div class="modal fade" id="createEventModal" tabindex="-1" ref="myModal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Create Event</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="createEvent">
            <div class="mb-3">
              <label for="eventName" class="form-label">Event Name</label>
              <input type="text" class="form-control" id="eventName" v-model="event.name" required
                     @input="validateEventName">
              <div v-if="eventNameError" class="text-danger">{{ eventNameError }}</div>
            </div>
            <div class="mb-3">
              <label for="eventLocation" class="form-label">Location</label>
              <input type="text" class="form-control" id="eventLocation" v-model="event.location" required
                     @input="validateEventLocation">
              <div v-if="eventLocationError" class="text-danger">{{ eventLocationError }}</div>
            </div>
            <div class="mb-3">
              <label for="eventDate" class="form-label">Event Date</label>
              <input type="date" class="form-control" id="eventDate" v-model="event.date" required>
            </div>
            <div class="mb-3">
              <label for="startTime" class="form-label">Start Time</label>
              <input type="time" class="form-control" id="startTime" v-model="event.startTime" required>
            </div>
            <div class="mb-3">
              <label for="endTime" class="form-label">End Time (Optional)</label>
              <input type="time" class="form-control" id="endTime" v-model="event.endTime">
            </div>
            <div class="text-center">
              <button type="submit" class="btn btn-primary me-1">Create</button>
              <button type="button" class="btn btn-secondary ms-1" @click="hide">Cancel</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Modal from "bootstrap/js/dist/modal";
export default {
  data() {
    return {
      createEventModal: null,
      event: {
        name: '',
        location: '',
        date: '',
        startTime: '',
        endTime: ''
      },
      eventNameError: '',
      eventLocationError: ''
    };
  },
  mounted() {
    this.createEventModal = new Modal('#createEventModal');
  },
  methods: {
    createEvent() {
      this.$emit('submittedEvent', this.event);
      this.clearData();
    },
    validateEventName() {
      if (this.event.name.length < 5) {
        this.eventNameError = "Name must be at least 5 characters long.";
      } else if (this.event.name.length > 50) {
        this.eventNameError = "Name must be no more than 50 characters long.";
      } else {
        this.eventNameError = "";
      }
    },
    validateEventLocation() {
      if (this.event.location.length < 5) {
        this.eventLocationError = "Location must be at least 5 characters long.";
      } else if (this.event.location.length > 255) {
        this.eventLocationError = "Location must be no more than 255 characters long.";
      } else {
        this.eventLocationError = "";
      }
    },
    clearData() {
      this.event = {
        name: '',
        location: '',
        date: '',
        startTime: '',
        endTime: ''
      };
      this.eventNameError = '';
      this.eventLocationError = '';
    },
    show() {
      this.clearData();
      this.createEventModal.show();
    },
    hide() {
      this.createEventModal.hide();
    }
  }
};
</script>