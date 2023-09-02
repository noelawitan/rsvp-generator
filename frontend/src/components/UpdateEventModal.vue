<template>
  <div class="modal fade" id="updateEventModal" tabindex="-1" ref="myModal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Edit Event</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <div v-if="event">
            <form @submit.prevent="updateEvent">
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
                <button type="submit" class="btn btn-primary me-1">Save</button>
                <button type="button" class="btn btn-secondary ms-1" @click="hide">Cancel</button>
              </div>
            </form>
          </div>
          <div v-else>
            <p>No Event Found</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Modal from "bootstrap/js/dist/modal";
export default {

  props: {
    eventObj: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      updateEventModal: null,
      event: null,
      eventNameError: '',
      eventLocationError: ''
    };
  },
  mounted() {
    this.updateEventModal = new Modal('#updateEventModal');
  },
  methods: {
    updateEvent() {
      this.$emit('submittedEvent', this.event);
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
    show() {
      this.event = JSON.parse(JSON.stringify(this.eventObj));
      this.updateEventModal.show();
    },
    hide() {
      this.updateEventModal.hide();
    }
  }
};
</script>